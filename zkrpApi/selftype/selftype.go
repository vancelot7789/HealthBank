package selftype

type MedicineUsage struct {
	ID int `json:"id"`
	MedID string `json:"medId"`
	MedName string `json:"medName"`
	Amount string `json:"amount"`
	Days string  `json:"days"`
	MedicalDate string `json:"medicalDate"`
}

type MerkleTreeRequire struct {
	MedicineUsages []MedicineUsage
	UserName string `json:"personName" example:"\u738b\u6625\u5b0c"`
}

type VerifyMerkleTree struct {
	MerkleTreeRequire
	HashArray []string `json:"hashArray"`
}

type DiseaseInfo struct {
	DiseaseID string `json:"diseaseId" example:"R"`
	DiseaseName string `json:"diseaseName" example:"\u75f0\u7570\u5e38"`
	RequireDays int `json:"requireDays" example:"28"`
}

type OperationInfo struct {
	OperationID	string `json:"operationId" example:"8155"`
	OperationName string `json:"operationName" example:"\u819d\u95dc\u7bc0\u518d\u7f6e\u63db\u8853"`
	RequireDays int `json:"requireDays" example:"28"`
}

type WesternMedicine struct {
	ID int	`json:"id"`
	DiseaseID string `json:"diseaseId"`
	DiseaseName string `json:"diseaseName"`
	MedicalDate	string `json:"medicalDate"`
	HospitalID int `json:"hospitalId"`
	HospitalName string `json:"hospitalName"`
}

type HospitalInfo struct {
	ID int	`json:"id"`
	DiseaseID string `json:"diseaseId"`
	DiseaseName string `json:"diseaseName"`
	OperationID	string `json:"operationId"`
	OperationName string `json:"operationName"`
	InHospitalDate string `json:"inHospitalDate"`
	OutHospitalDate string `json:"outHospitalDate"`
	HospitalID int `json:"hospitalId"`
	HospitalName string `json:"hospitalName"`
}

type ProveRequired struct {
	WesternMedicine []WesternMedicine `json:"westernMedicine"`
	HospitalInfo []HospitalInfo `json:"hospitalInfo"`
}

type ProvePackage struct {
	Type string `json:"type" example:"Disease"`
	Code string `json:"diseaseid" example:"J0390"`
	Prove string `json:"proof"`
	Lowerbound int64 `json:"lowBound" example:"20200503"`
	Upperbound int64 `json:"upBound" example:"20200531"`
	Commitment string `json:"commitment" example:"0x1af2e641e834f6c503e0e6b9b593323342e7d45ad0aa299bc934110a4e13ae2f33c3a0fc695f397d51e8b4699b4d9f9397b3bc3e20fa42cb1a540ff8f19dc118"`
}

type Verify struct {
	//ProvePackages []ProvePackage `json:"provePackages"`
	ProvePackage
	UserName string `json:"personName" example:"\u738b\u6625\u5b0c"`
}

type Event struct {
	EventName string `json:"eventName" example:"marathon"`
	EventInfo string `json:"eventInfo" example:"強化主動防疫 我們需要您提供28天內住院與手術資料\n\n我們將從您的健康存摺加密處理後交給馬拉松中心進行第三方驗證 確定您是否符合馬拉松資格"`
	EventType string `json:"EventType" example:"zkrp"`
	DiseaseInfo []DiseaseInfo `json:"diseaseInfo"`
	OperationInfo []OperationInfo `json:"operationInfo"`
	VarifyURL string `json:"varifyURL" example:"http://localhost:8080/verify"`
}