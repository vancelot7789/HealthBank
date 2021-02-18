package com.example.healthbank.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthbank.DataAssets.*
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R
import com.example.healthbank.Utilities.Sha256
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

@SuppressLint("Registered")
class MerkleTreeSendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merkle_tree_send)


        var merkleObject = JSONObject()
        merkleObject.put("personName", userAccountName)
        merkleObject.put("Med",getMedicineHash())
        merkleObject.put("Wes",getWesternHash())


        val merkleTreeString = merkleObject.toString()


        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),merkleTreeString)


        val postDetailUrl = "http://${currentIp}:8080/merkletree"
        val request = Request.Builder().url(postDetailUrl).post(body)
            .addHeader("Content-Type","application/json").build()

        val clientPost = OkHttpClient()
        clientPost.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val rawData = response?.body()!!.string()
                println("response from merkle tree is ${rawData}")
            }

            override fun onFailure(call: Call, e: IOException) {
                println("error message ${e.message}")
            }
        })




    }
    fun getMedicineHash():JSONObject{

        val hashArray = JSONArray()


        for(item in unSelectedMedicineInfo!!){
            var tempObject = JSONObject()
            tempObject.put("id",item.id)
            tempObject.put("medId",item.medId)
            tempObject.put("medName",item.medName)
            tempObject.put("amount",item.amount)
            tempObject.put("days",item.days)
            tempObject.put("medicalDate",item.medicalDate)

            hashArray.put(Sha256.hash(tempObject.toString()))
        }



        val selectedMedicineObject = JSONArray()
        val data_in_2019_Med : ArrayList<DataService.medicineUsage> = ArrayList()

        for(item in medicineInfo!!){
            if ("2019" in item.medicalDate){
                data_in_2019_Med?.add(item)
            }
        }

        val sortedSelectedMedicineInfo = data_in_2019_Med?.sortedBy { it.id }?.reversed()
        for (item in sortedSelectedMedicineInfo!!){
            var tempObject = JSONObject()
            tempObject.put("id",item.id)
            tempObject.put("medId",item.medId)
            tempObject.put("medName",item.medName)
            tempObject.put("amount",item.amount)
            tempObject.put("days",item.days)
            tempObject.put("medicalDate",item.medicalDate)

            selectedMedicineObject.put(tempObject)
        }

        selectedMedicineInfo = ArrayList()
        unSelectedMedicineInfo = ArrayList()


        val merkleTreeObject = JSONObject()
        merkleTreeObject.put("medicineUsages",selectedMedicineObject)
        merkleTreeObject.put("hashArrayMed",hashArray)

        println("merkleTree Medicine Hash ${merkleTreeObject}")

        return merkleTreeObject

    }

    fun getWesternHash():JSONObject{
        val hashArray = JSONArray()


        for(item in unSelectedWesternInfo!!){
            var tempObject = JSONObject()
            tempObject.put("id",item.id)
            tempObject.put("diseaseId",item.diseaseId)
            tempObject.put("diseaseName",item.diseaseName)
            tempObject.put("medicalDate",item.medicalDate)
            tempObject.put("hospitalId",item.hospitalId)
            tempObject.put("hospitalName",item.hospitalName)



            hashArray.put(Sha256.hash(tempObject.toString()))
        }


        val data_in_2019_Wes : ArrayList<DataService.WesternMedicine> = ArrayList()

        for(item in westernMedicineInfo!!){
            if ("2019" in item.medicalDate){
                data_in_2019_Wes?.add(item)
            }
        }



        val selectedWesternObject = JSONArray()
        val sortedSelectedMedicineInfo = data_in_2019_Wes?.sortedBy { it.id }?.reversed()
        for (item in sortedSelectedMedicineInfo!!){
            var tempObject = JSONObject()
            tempObject.put("id",item.id)
            tempObject.put("diseaseId",item.diseaseId)
            tempObject.put("diseaseName",item.diseaseName)
            tempObject.put("medicalDate",item.medicalDate)
            tempObject.put("hospitalId",item.hospitalId)
            tempObject.put("hospitalName",item.hospitalName)
            selectedWesternObject.put(tempObject)
        }
        println("western unselect hash array ${hashArray}")
        println("western select ${selectedWesternObject}")

        selectedWesternInfo = ArrayList()
        unSelectedWesternInfo = ArrayList()


        val merkleTreeObject = JSONObject()
        merkleTreeObject.put("westernInfo",selectedWesternObject)
        merkleTreeObject.put("hashArrayWes",hashArray)

        println("merkleTree Western Hash ${merkleTreeObject}")
        return merkleTreeObject


    }

    fun returnMenuOnClicked(view: View){
        val returnMenuIntent = Intent(this,MenuActivity::class.java)
        startActivity(returnMenuIntent)
    }
}
