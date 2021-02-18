package com.example.healthbank.DataClass

class DataService {
    data class personInfo(
        val Name: String,
        val personId: String
    )

    data class WesternMedicine(
        val id : Int,
        val diseaseId: String,
        val diseaseName: String,
        val medicalDate: String,
        val hospitalId: Int,
        val hospitalName: String
    ){
        var isSelected: Boolean = false
    }

    data class hospitalInfo(
        val id : Int,
        val diseaseId : String,
        val diseaseName: String,
        val operationId: String,
        val operationName: String,
        val inHospitalDate: String,
        val outHospitalDate: String,
        val hospitalId: Int,
        val hospitalName: String
    )

    data class medicineUsage(
        val id : Int,
        val medId: String,
        val medName: String,
        val amount: String,
        val days: String,
        val medicalDate: String
    ){
        var isSelected: Boolean = false
    }

    data class examineInfo(
        val id: Int,
        val diseaseId: String,
        val diseaseName: String,
        val examineName: String,
        val medicalDate: String,
        val result: String,
        val reference: String,
        val hospitalId: Int,
        val hospitalName: String
    )

    data class historyInfo(
        val agencyName: String,
        val verifyDate: String,
        val desireOption: String,
        val emailAddress: String,
        val urlAddress: String,
        val phoneNumber: String
    )
}