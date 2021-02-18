package com.example.healthbank.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.example.healthbank.ConnectApi.ZKRP
import com.example.healthbank.DataAssets.*
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R
import kotlinx.android.synthetic.main.activity_zkrp_send.*

class ZkrpSendActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zkrp_send)

        resultFromThirdParty.setTextColor(Color.WHITE)
        if (userAccountName == "李小豪"){
            resultFromThirdParty.text = "不適合參賽"
            resultFromThirdParty.setBackgroundColor(Color.RED)


        }else{
            resultFromThirdParty.text = "適合參賽"
            resultFromThirdParty.setBackgroundColor(Color.rgb(0,128,0))

        }



    }
    fun sendProofButtonOnClicked(view: View){
        val sendProofIntent = Intent(this,ZkrpSendResultActivity::class.java)
        startActivity(sendProofIntent)

    }

    fun returnMenuOnClicked(view : View){
        val returnMenuIntent = Intent(this, MenuActivity::class.java)
        startActivity(returnMenuIntent)
    }
}
