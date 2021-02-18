package main

import (
	"github.com/HachimanHiki/zkrpApi/router"

	_ "github.com/HachimanHiki/zkrpApi/docs"
)

// @title Swagger Example API
// @version 1.0
// @description This is a sample server celler server.
// @termsOfService http://swagger.io/terms/

// @contact.name API Support
// @contact.url http://www.swagger.io/support
// @contact.email support@swagger.io

// @license.name Apache 2.0
// @license.url http://www.apache.org/licenses/LICENSE-2.0.html

// @host localhost:8080
// @BasePath
// @query.collection.format multi

// @x-extension-openapi {"example": "value on a json format"}

func main() {
	router.InitRouter()
}