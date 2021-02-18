package com.example.healthbank.ConnectApi

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.healthbank.DataAssets.*
import com.example.healthbank.DataClass.CheckClass
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_menu.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

object ZKRP {
    @RequiresApi(Build.VERSION_CODES.O)
    fun findCorrespondDisease(eventData: JSONObject?){

                val diseaseToCheck_W_Data = (eventData?.getString("diseaseInfo"))

                val gson = Gson()
                val diseaseCheck_W_Type =
                    object : TypeToken<Array<CheckClass.diseaseToCheck_W>>() {}.type
                val diseaseCheck_W_Object: Array<CheckClass.diseaseToCheck_W> =
                    gson.fromJson(diseaseToCheck_W_Data, diseaseCheck_W_Type)

                val diseaseCode_W = diseaseCheck_W_Object.get(0).diseaseId
                val diseaseDays_W = diseaseCheck_W_Object.get(0).requireDays.toInt()
                val diseaseId_W = diseaseCheck_W_Object.get(0).diseaseId

                var lastDateWithDisease_W : Int? = null
                var lastDiseaseId:String? = null
                var lastRequiredDays_W: Int? = null


                for(item in westernMedicineInfo!!){
                    if (diseaseId_W in item.diseaseId){
                        lastDateWithDisease_W = item.medicalDate.toInt()
                        lastDiseaseId = item.diseaseId
                        break
                    }
                }

                val upBound = upBound
                val lowBound = getLowBound(diseaseDays_W)

                println("secret ${lastDateWithDisease_W}, lowBound ${lowBound} , upBound ${upBound}")
                sendProof(lowBound,upBound,lastDateWithDisease_W,lastDiseaseId)


    }

    fun sendProof(lowBound: Int?, upBound: Int, secret: Int?, diseaseId: String?){
        println("user name is ${userAccountName}")
        val getDetailUrl = "http://$currentIp:3000/?lowBound=${lowBound}&upBound=${upBound}&secret=${secret}&userName=${userAccountName}&diseaseId=${diseaseId}"
        val request = Request.Builder().url(getDetailUrl).get().build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val rawData = response?.body()!!.string()
                println("proof data status ${rawData}")
            }
            override fun onFailure(call: Call, e: IOException) {
                if (e != null) {
                    println(e.message)
                }
            }
        })

    }
}
