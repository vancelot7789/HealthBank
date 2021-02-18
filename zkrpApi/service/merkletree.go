package service 

import (
	"crypto/sha256"
	"fmt"

	"github.com/HachimanHiki/zkrpApi/selftype"
)

func GenerateHashFromStruct(medicineUsage selftype.MedicineUsage) string {
	h := sha256.New()
	h.Write([]byte(fmt.Sprintf("%v", medicineUsage)))

	return fmt.Sprintf("%x", h.Sum(nil))
	//return h.Sum(nil)
}

func GenerateHashFromHash(a, b string) string {
	h := sha256.New()
	h.Write(append([]byte(a), []byte(b)...))

	return fmt.Sprintf("%x", h.Sum(nil))
}

func GenerateMerkleTreeRoot(hashArray []string) string {

	if len(hashArray) == 1 {
		return hashArray[0]
	}
	
	if len(hashArray) % 2 != 0 {
		hashArray = append(hashArray, hashArray[len(hashArray)-1])
	}

	var newArray []string
	
	for i := 0 ; i < len(hashArray) ; i += 2 {
		newArray = append(newArray, GenerateHashFromHash(hashArray[i], hashArray[i+1]))
	}

	return GenerateMerkleTreeRoot(newArray)
	
}