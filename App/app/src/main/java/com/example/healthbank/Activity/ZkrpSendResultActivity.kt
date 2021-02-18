package com.example.healthbank.Activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.example.healthbank.ConnectApi.ZKRP
import com.example.healthbank.DataAssets.*
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R

class ZkrpSendResultActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zkrp_send_result)

        val approveData : DataService.historyInfo = DataService.historyInfo(
            eventName, todayDate,"同意授權", zkrpEmail, zkrpUrl, zkrpPhoneNumber
        )
        historyInfo.add(approveData)
        ZKRP.findCorrespondDisease(eventData)
    }

    fun returnMenuOnClicked(view: View){
        val returnMenuIntent = Intent(this, MenuActivity::class.java)
        startActivity(returnMenuIntent)
    }
}
