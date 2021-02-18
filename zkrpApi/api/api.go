package api

import (
	"bytes"
	"encoding/json"
	"encoding/base64"
	"fmt"
	"net/http"
	"strconv"
	"time"
	"io/ioutil"
	"image/png"

	"github.com/boombuler/barcode"
	"github.com/boombuler/barcode/qr"
	"github.com/HachimanHiki/zkrpApi/selftype"
	"github.com/HachimanHiki/zkrpApi/zsl"
	"github.com/HachimanHiki/zkrpApi/service"
	"github.com/gin-gonic/gin"
)

var (
	allEvent map[string]selftype.Event
	userHashRoot map[string]string
	resultStatus bool = false
	resultMessage string = ""
)

// NotFound godoc
func NotFound(c *gin.Context) {
    c.JSON(http.StatusNotFound, gin.H{
        "status": "fail",
        "error":  "Page not exists!",
    })
}

func GetIndex(c *gin.Context) {
	c.HTML(http.StatusOK, "new.html", nil)
}

func GetResult(c *gin.Context) {

	if(resultStatus == true){
		c.JSON(http.StatusOK, gin.H{
			"status": resultStatus,
			"message": resultMessage,
		})

		resultStatus = false

	}else{
		c.JSON(http.StatusBadRequest, gin.H{
			"status": "fail",
			"message": "Bad request!",
		})
	}
}

func PostMerkleTreeRoot(c *gin.Context) {
	merkleTreeRequire := selftype.MerkleTreeRequire{}

	if c.BindJSON(&merkleTreeRequire) == nil {
		var hashArray []string

		for _, medicineUsage := range merkleTreeRequire.MedicineUsages {
			hashArray = append(hashArray, service.GenerateHashFromStruct(medicineUsage))
		}

		if userHashRoot == nil {
			userHashRoot = make(map[string]string)
		}
		fmt.Println(hashArray)
		userHashRoot[merkleTreeRequire.UserName] = service.GenerateMerkleTreeRoot(hashArray)
		
		c.JSON(http.StatusOK, gin.H{
			"status": "success",
			"message": userHashRoot[merkleTreeRequire.UserName],
		})

	}else{
		c.JSON(http.StatusBadRequest, gin.H{
			"status": "fail",
			"message": "Bad request!",
		})
	}
}

func VerifyMerkleTreeRoot(c *gin.Context) {
	verifyMerkleTree := selftype.VerifyMerkleTree{}

	if c.BindJSON(&verifyMerkleTree) == nil {
		// for demo use
		if userHashRoot == nil {
			userHashRoot = make(map[string]string)
		}
		userHashRoot["\u738b\u6625\u5b0c"] = "dffb0a974110005d6fed4e17c8c29619cb50e9df0b3bc0a633cd80b317b9a36d"
		userHashRoot["\u674e\u5c0f\u8c6a"] = "22916c7ae2af50553a8cded598ca4f1917585d1d55ac2f265f7888f0054e5adf"
		userHashRoot["\u5f35\u5fd7\u660e"] = "2eb1eb753e41af3c3c19f0830834db24317fa30d521a7d70e86bb1086f0d628d"
		// 

		var hashArray []string

		for _, medicineUsage := range verifyMerkleTree.MedicineUsages {

			for len(hashArray) != (medicineUsage.ID - 1) {
				hashArray = append(hashArray, verifyMerkleTree.HashArray[0])
				verifyMerkleTree.HashArray = verifyMerkleTree.HashArray[1:]
			}
			hashArray = append(hashArray, service.GenerateHashFromStruct(medicineUsage))
		}

		for len(verifyMerkleTree.HashArray) != 0 {
			hashArray = append(hashArray, verifyMerkleTree.HashArray[0])
			verifyMerkleTree.HashArray = verifyMerkleTree.HashArray[1:]
		}

		if userHashRoot[verifyMerkleTree.UserName] == service.GenerateMerkleTreeRoot(hashArray) {
			c.JSON(http.StatusOK, gin.H{
				"status": "success",
				"message": "verify success with user name: " + verifyMerkleTree.UserName,
			})

		} else {
			c.JSON(http.StatusOK, gin.H{
				"status": "success",
				"message": "verify fail with user name: " + verifyMerkleTree.UserName,
			})
		}

	}else{
		c.JSON(http.StatusBadRequest, gin.H{
			"status": "fail",
			"message": "Bad request!",
		})
	}
}

func NewProve (c *gin.Context) {
	proveRequired := selftype.ProveRequired{}

	if c.BindJSON(&proveRequired) == nil {
		const layout = "20060102" // time format
		var ProvePackages []selftype.ProvePackage

		upperbound, _ := strconv.ParseInt(time.Now().Format(layout), 10, 64) // today

		for _, hospitalInfo := range proveRequired.HospitalInfo {
			t, _ := time.Parse(layout, time.Now().Format(layout))
			t = t.AddDate(0, 0, -28)
			lowerbound, _ := strconv.ParseInt(t.Format(layout), 10, 64) // "hospitalInfo.DuringTime" day before today

			number, _ := strconv.ParseInt(hospitalInfo.OutHospitalDate, 10, 64)

			commitmentPackage := zsl.Committer(int(number))

			ProvePackage := selftype.ProvePackage {
				Type: "DiseaseID",
				Code: hospitalInfo.DiseaseID,
				Prove: zsl.Prover(number, lowerbound, upperbound, commitmentPackage.Confounding),
				Lowerbound: lowerbound,
				Upperbound: upperbound,
				Commitment: commitmentPackage.Commitment,
			}
			
			ProvePackages = append(ProvePackages, ProvePackage)
		}

		for _, hospitalInfo := range proveRequired.HospitalInfo {
			t, _ := time.Parse(layout, time.Now().Format(layout))
			t = t.AddDate(0, 0, -28)
			lowerbound, _ := strconv.ParseInt(t.Format(layout), 10, 64) // "hospitalInfo.DuringTime" day before today

			number, _ := strconv.ParseInt(hospitalInfo.OutHospitalDate, 10, 64)

			commitmentPackage := zsl.Committer(int(number))

			ProvePackage := selftype.ProvePackage {
				Type: "OperationID",
				Code: hospitalInfo.OperationID,
				Prove: zsl.Prover(number, lowerbound, upperbound, commitmentPackage.Confounding),
				Lowerbound: lowerbound,
				Upperbound: upperbound,
				Commitment: commitmentPackage.Commitment,
			}
			
			ProvePackages = append(ProvePackages, ProvePackage)
		}

		for _, westernMedicine := range proveRequired.WesternMedicine {
			t, _ := time.Parse(layout, time.Now().Format(layout))
			t = t.AddDate(0, 0, -28)
			lowerbound, _ := strconv.ParseInt(t.Format(layout), 10, 64) // "procedure.DuringTime" day before today

			number, _ := strconv.ParseInt(westernMedicine.MedicalDate, 10, 64)

			commitmentPackage := zsl.Committer(int(number))

			ProvePackage := selftype.ProvePackage {
				Type: "DiseaseID",
				Code: westernMedicine.DiseaseID,
				Prove: zsl.Prover(number, lowerbound, upperbound, commitmentPackage.Confounding),
				Lowerbound: lowerbound,
				Upperbound: upperbound,
				Commitment: commitmentPackage.Commitment,
			}

			ProvePackages = append(ProvePackages, ProvePackage) 
		}

		fmt.Println(ProvePackages)

		jsonValue, _ := json.Marshal(ProvePackages)
		jsonString := `{"provePackages":` + string(jsonValue) + "}"
		jsonValue = []byte(jsonString)

		res, _ := http.Post("http://localhost:8080/verify", "application/json", bytes.NewBuffer(jsonValue))
		fmt.Println(res.Body)
		defer res.Body.Close()
		body, _ := ioutil.ReadAll(res.Body)

		c.JSON(http.StatusOK, gin.H{
			"status": "success",
			"data": string(body),
		})

	} else {
		c.JSON(http.StatusBadRequest, gin.H{
			"status": "fail",
			"message": "Bad request!",
		})
	}
}

// PostVerify godoc
// @Tags ZKRP 
// @Summary Verify ZKRP
// @Description Verify a prove of ZKRP
// @Accept  application/json
// @Produce  application/json
// @Param provePackages body []selftype.ProvePackage true "[]ProvePackage"
// @Param userName body string true "userName"
// @Success 200 {object} selftype.JSONResponse
// @Failure 400 {object} selftype.JSONResponse
// @Router /verify [post]
func PostVerify(c *gin.Context) {
	verify := selftype.Verify{}
	verify.Commitment = "0x100b92ff311c1f05dfbe31dbcd8f416245bb0ab7bd800afe7ecc402a5c2480fc4ee9f329d7d7ddb8ddab7f99fb25e3f439144046782d95795bf8c0726a24c3ec"

	if c.BindJSON(&verify) == nil {
		if verify.UserName == "\u738b\u6625\u5b0c" {
			verify.Commitment = "0x19be17213c2ee781defeef4abc2d6964f1418177f8d54418c55412e198eebf2107c6e8cc1a43e9be4e7f331f7b729e53a2e14e37aa874383628cf89be3cd0ef6"

		} else if verify.UserName == "\u674e\u5c0f\u8c6a" {
			verify.Commitment = "0x159e049899d7b4e444459feb5e832c1d589b97f348ff7a37bd84d2cdc4b2bb5e653c3163346c4055f17d9d27c01016869a64bb6bb3a91e2558b8b3183d3f4720"

		} else {
			verify.Commitment = "0x1edbe504d08a8da27424f7815dc6bfb0ff18746fabc38f0b7ec05915da0d9b302cf345a6f4523163ac3254155d45329f1f5c8ac835b2fd558db2f5bd543aefb8"
		}

		const layout = "20060102" // time format

		if zsl.Verifier(verify.Commitment, verify.Lowerbound, verify.Upperbound, []byte(verify.Prove)) {
			resultStatus = true
			resultMessage = "Verify fail with user name: " + verify.UserName

			c.JSON(http.StatusOK, gin.H{
				"status": "success",
				"message": "Verify fail with user name: " + verify.UserName,
			})

		}else {
			resultStatus = true
			resultMessage = "Verify success with user name: " + verify.UserName

			c.JSON(http.StatusOK, gin.H{
				"status": "success",
				"message": "Verify success with user name: " + verify.UserName,
			})
		}
/*
		result := make(chan bool, len(verify.ProvePackages))

		for _, provePackage := range verify.ProvePackages {
			go func(provePackage selftype.ProvePackage) {
				result <- zsl.Verifier(provePackage.Commitment, provePackage.Lowerbound, provePackage.Upperbound, []byte(provePackage.Prove))
			} (provePackage)
		}

		for i := 0; i < len(verify.ProvePackages); i++ {
			if ! <-result {
				c.JSON(http.StatusOK, gin.H{
					"status": "success",
					//"message": "Verify fail with user name: " + verify.UserName,
					"message": "false",
				})
				return
			}
		}*/
/*
		c.HTML(http.StatusOK, "new.tmpl", gin.H{
			"title": "IT HOME again",
		})*/
/*
		c.JSON(http.StatusOK, gin.H{
			"status": "success",
			//"message": "Verify success with user name: " + verify.UserName,
			"message": "true",
		})
*/
	} else {
		c.JSON(http.StatusBadRequest, gin.H{
			"status": "fail",
			"message": "Bad request!",
		})
	}
}

// PostEvent godoc
// @Tags Event 
// @Summary Add/Update event
// @Description null
// @Accept  application/json
// @Produce  application/json
// @Param eventName body string true "EventName"
// @Param deagnosis body []selftype.Deagnosis true "[]Deagnosis" 
// @Param procedure body []selftype.Procedure true "[]Procedure" 
// @Success 200 {object} selftype.EventResponse
// @Failure 400 {object} selftype.JSONResponse
// @Router /event [post]
func PostEvent (c *gin.Context) {
	event := selftype.Event{}
	const verifyURLPrefix = "http://localhost:8080/verify"
	event.VarifyURL = verifyURLPrefix

	if c.BindJSON(&event) == nil {
		if allEvent == nil {
			allEvent = make(map[string]selftype.Event)
		}

		allEvent[event.EventName] = event

		c.JSON(http.StatusOK, gin.H{
			"status": "success",
			"data": allEvent[event.EventName],
		})

	} else {
		c.JSON(http.StatusBadRequest, gin.H{
			"status": "fail",
			"message": "Bad request!",
		})
	}
}

// GetEvent godoc
// @Tags Event 
// @Summary Get an event
// @Description Get an event from string
// @Accept  json
// @Produce  json
// @Param eventName query string true "event search by eventName"
// @Success 200 {object} selftype.EventResponse
// @Failure 400 {object} selftype.JSONResponse
// @Router /event/{eventName} [get]
func GetEvent (c *gin.Context) {
	eventName := c.Query("eventName")
	
	if len(eventName) > 0 {
		if _, ok := allEvent[eventName]; ok {
			c.JSON(http.StatusOK, gin.H{
				"status": "success",
				"data": allEvent[eventName],
			})
			
		} else {
			c.JSON(http.StatusNotFound, gin.H{
				"status": "fail",
				"message": "Cannot find this event",
			})
		}

	} else {
		c.JSON(http.StatusBadRequest, gin.H{
			"status": "fail",
			"message": "Bad request!",
		})
	}
}

// DelEvent godoc
// @Tags Event
// @Summary Delete an event
// @Accept  json
// @Produce  json
// @Param eventName query string true "event delete by eventName"
// @Success 200 {object} selftype.JSONResponse
// @Failure 400 {object} selftype.JSONResponse
// @Failure 404 {object} selftype.JSONResponse
// @Router /event/{eventName} [delete]
func DelEvent (c *gin.Context) {
	eventName := c.Query("eventName")

	if len(eventName) > 0 {
		if _, ok := allEvent[eventName]; ok {
			delete(allEvent, eventName)

			c.JSON(http.StatusOK, gin.H{
				"status": "success",
			})
		} else {
			c.JSON(http.StatusNotFound, gin.H{
				"status": "fail",
				"message": "Cannot find this event",
			})
		}

	} else {
		c.JSON(http.StatusBadRequest, gin.H{
			"status": "fail",
			"message": "Bad request!",
		})
	}
}

// GetQRcode godoc
// @Tags Event 
// @Summary Get an qrcode
// @Description Return a qrcode which encodes event info
// @Accept  json
// @Produce  json
// @Param eventName query string true "event search by eventName"
// @Success 200 {object} selftype.EventResponse
// @Failure 400 {object} selftype.JSONResponse
// @Router /event_qr/{eventName} [get]
func GetQRcode (c *gin.Context) {
	eventName := c.Query("eventName")
	if len(eventName) > 0 {
		if _, ok := allEvent[eventName]; ok {
			
			qrCode, _ := qr.Encode("http://localhost:8080/event?eventName=" + eventName, qr.M, qr.Auto)

			// Scale the barcode to 200x200 pixels
			qrCode, _ = barcode.Scale(qrCode, 200, 200)

			var buf bytes.Buffer
			err :=png.Encode(&buf, qrCode)
			if err != nil {
				panic(err)
			}

			c.JSON(http.StatusOK, gin.H{
				"status": "success",
				"data": allEvent[eventName],
				"qrcode": base64.StdEncoding.EncodeToString(buf.Bytes()),
			})

		} else {
			c.JSON(http.StatusNotFound, gin.H{
				"status": "fail",
				"message": "Cannot find this event",
			})
		}

	} else {
		c.JSON(http.StatusBadRequest, gin.H{
			"status": "fail",
			"message": "Bad request!",
		})
	}
}