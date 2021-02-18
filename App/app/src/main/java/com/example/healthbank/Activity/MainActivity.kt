package com.example.healthbank.Activity


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthbank.DataAssets.*
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

data class Person(val personId: String, val name: String)

class MainActivity : AppCompatActivity() {

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun loginButtonOnClicked(view:View) {
        val insertPersonId = (findViewById(R.id.personId) as TextView).getText().toString()
        val insertPassword = (findViewById(R.id.password) as TextView).getText().toString()


        if (insertPersonId.isBlank()){
            Toast.makeText(this,"請輸入身分證號",Toast.LENGTH_SHORT).show()
        }

        if (insertPassword.isBlank()){
            Toast.makeText(this,"請輸入密碼",Toast.LENGTH_SHORT).show()
        }

        if (!insertPersonId.isBlank() and !insertPassword.isBlank()) {
            fetchJson(insertPersonId, insertPassword)
        }
    }


    fun fetchJson(
        insertPersonId: String,
        insertPassword: String
    ){

        val url = "http://${currentIp}:8000/users/${insertPersonId}"
        val request = Request.Builder().url(url).get().build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call?, response: Response?) {

                val rawData = response?.body()!!.string()

                println("now status $status")

                if (rawData == "No such user"){
                    status = 1

                } else {
                    val jsonData = JSONObject(
                        rawData.substring(
                            rawData.indexOf("{"),
                            rawData.lastIndexOf("}") + 1
                        )
                    ).getJSONObject("data")

                    val personId = jsonData.getString("personId")
                    val password = jsonData.getString("password")

                    if (password != insertPassword){
                        status = 2
                    } else {

                        status = 3

                        val personData = jsonData.getString("personalInfo")
                        val westernData = jsonData.getString("westernMedicine")
                        val hospitalData = jsonData.getString("hospitalInfo")
                        val medicineData = jsonData.getString("medicineUsage")
                        val examineData = jsonData.getString("examineInfo")


                        val gson = Gson()
                        val arrayPersonInfoType =
                            object : TypeToken<Array<DataService.personInfo>>() {}.type
                        val personDataObject: Array<DataService.personInfo> =
                            gson.fromJson(personData, arrayPersonInfoType)
                        personInfo = personDataObject

                        val arrayWesternMedicineType =
                            object : TypeToken<Array<DataService.WesternMedicine>>() {}.type
                        val westernDataObject: Array<DataService.WesternMedicine> =
                            gson.fromJson(westernData, arrayWesternMedicineType)
                        westernMedicineInfo = westernDataObject!!.reversedArray()

                        val arrayHospitalType =
                            object : TypeToken<Array<DataService.hospitalInfo>>() {}.type
                        val hospitalDataObject: Array<DataService.hospitalInfo> =
                            gson.fromJson(hospitalData, arrayHospitalType)
                        hospitalInfo = hospitalDataObject!!.reversedArray()

                        val arrayMedicineUsageType =
                            object : TypeToken<Array<DataService.medicineUsage>>() {}.type
                        val medicineDataObject: Array<DataService.medicineUsage> =
                            gson.fromJson(medicineData, arrayMedicineUsageType)
                        medicineInfo = medicineDataObject!!.reversedArray()

                        val arrayExamineType =
                            object : TypeToken<Array<DataService.examineInfo>>() {}.type
                        val examineDataObject: Array<DataService.examineInfo> =
                            gson.fromJson(examineData, arrayExamineType)
                        examineInfo = examineDataObject!!.reversedArray()

                        this@MainActivity.runOnUiThread {
                            Runnable{
                                if (status == 1){
                                    Toast.makeText(this@MainActivity,"查無此使用者",Toast.LENGTH_SHORT).show()
                                }

                                if (status == 2){
                                    Toast.makeText(this@MainActivity,"密碼錯誤",Toast.LENGTH_SHORT).show()
                                }

                                if (status == 3) {
                                    setContentView(R.layout.activity_loading)
                                    Toast.makeText(this@MainActivity,"成功登入",Toast.LENGTH_SHORT).show()

                                    val menuIntent = Intent(this@MainActivity, MenuActivity::class.java)
                                    val initialData : DataService.historyInfo = DataService.historyInfo("指南捐血中心","20190715","同意授權",
                                        "123@gmail.com","https://bloodtest.com.tw","(02)-XXXX-XXXX")

                                    historyInfo.add(initialData)
                                    val welcomeThread: Thread = object : Thread() {
                                        override fun run() {
                                            try {
                                                super.run()
                                                sleep(5000)
                                            } catch (e: Exception) {

                                            } finally {
                                                startActivity(menuIntent)
                                                finish()
                                            }
                                        }
                                    }
                                    welcomeThread.start()
                                }
                            }.run()
                        }
                    }
                }

            }

            override fun onFailure(call: Call?, e: IOException?){
                if (e != null) {
                    status = 4
                    println(e.message)
                }
            }
        })

    }

}



