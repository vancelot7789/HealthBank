package com.example.healthbank.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.healthbank.DataAssets.historyInfo
import com.example.healthbank.DataAssets.personInfo
import com.example.healthbank.DataAssets.status
import com.example.healthbank.DataAssets.userAccountName
import com.example.healthbank.R
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)



        val userName = this.findViewById<TextView>(R.id.userName)
        userName?.text = personInfo?.get(0)?.Name

        userAccountName =  personInfo?.get(0)?.Name


        val userId = this.findViewById<TextView>(R.id.userId)
        userId?.text = (personInfo?.get(0)?.personId)?.substring(0,5)
        userId.append("XXXXX")


    }

    fun logoutOnClicked(view:View){
        val logoutIntent = Intent(this,MainActivity::class.java)
        status = 0
        historyInfo = ArrayList()
        startActivity(logoutIntent)
    }

    fun westernMedicineOnClicked(view:View){
        val westernMedicineIntent = Intent(this,WesternMedicineActivity::class.java)
        startActivity(westernMedicineIntent)

    }

    fun hospitalOnClicked(view: View){
        val hospitalIntent = Intent(this,HospitalActivity::class.java)
        startActivity(hospitalIntent)
    }

    fun medicineUsageOnClicked(view: View){
        val medicineUsageIntent = Intent(this,MedicineUsageActivity::class.java)
        startActivity(medicineUsageIntent)
    }

    fun historyButtonOnClicked(view: View){
        val historyIntent = Intent(this, HistoryActivity::class.java)
        startActivity(historyIntent)
    }

    fun thirdPartyOnClicked(view: View){
        val thirdPartyIntent = Intent(this, ThirdPartyActivity::class.java)
        startActivity(thirdPartyIntent)
    }

    fun examineOnClicked(view: View){
        val examineIntent = Intent(this, ExamineActivity::class.java)
        startActivity(examineIntent)
    }


}
