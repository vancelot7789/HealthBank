package selftype

type JSONResponse struct {
    Status string	`json:"status" example:"success"`
    Message string	`json:"message"`
}

type ProvePackagesResponse struct {
	JSONResponse
	Data []ProvePackage `json:"data"`
}

type EventResponse struct {
	JSONResponse
	Data Event `json:"data"`
}