package com.example.healthbank.DataClass

class CheckClass(param: Any?) {


    data class diseaseToCheck_W(

        val diseaseId: String,
        val diseaseName: String,
        val requireDays : String

    )

    data class diseaseToCheck_H(

        val operationId : String,
        val operationName: String,
        val requireDays: String
    )
}