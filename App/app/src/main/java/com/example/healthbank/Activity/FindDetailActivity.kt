package com.example.healthbank.Activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ExpandableListView
import com.example.healthbank.Adapters.ExpandableListViewAdapter
import com.example.healthbank.DataAssets.*
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R
import kotlinx.android.synthetic.main.activity_find_detail.*

class FindDetailActivity : AppCompatActivity() {

    @SuppressLint("WrongViewCast", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_detail)

        eventTitle.text = eventName
        eventView.text = eventInfo


        if(eventType == "merkletree"){
            linkButton.text = "http://www1.cde.org.tw/ct_taiwan/archivemore.html"
        }else{
            linkButton.text = ""
        }

        getListView()


    }

    fun getListView(){
        val listView = findViewById<ExpandableListView>(R.id.listView)

        var requiredEvent = listOf<String>()
        requiredEvent = if ("," in eventRequired) {
            eventRequired.replace("[", "").replace("]", "")
                .split(",").toList()
        }else{
            arrayOf(eventRequired.replace("[", "").replace("]", "")).toList()
        }
        var requiredEventArray = ArrayList<String>()
        for(i in requiredEvent){
            requiredEventArray.add(i.drop(1).dropLast(1))
        }




        var requiredDetailEvent  = listOf<String>()
        requiredDetailEvent = if ("," in eventRequiredDetail) {
            eventRequiredDetail.replace("[", "").replace("]", "")
                .split(",").toList()

        }else{
            arrayOf(eventRequiredDetail.replace("[", "").replace("]", "")).toList()
        }
        var requiredDetailEventArray = ArrayList<String>()
        for(i in requiredDetailEvent){
            requiredDetailEventArray.add(i.drop(1).dropLast(1))
        }


        var dataRangeQuestion:String

        var declaration: String


        var dataUsageDelcaration: String


        var others: String
        var othersDetail : String

        if(eventType == "zkrp"){
            dataRangeQuestion = "需要的資料驗證範圍"
            declaration = zkrpRiskDeclaration

            dataUsageDelcaration = dataUsageZkrpDeclaration


            others = "如果不參加驗證會如何？"
            othersDetail = otherDetailZkrp

        }else{
            dataRangeQuestion = "需要的資料範圍"
            declaration = merkleTreeRiskDeclaration

            dataUsageDelcaration = dataUsageMerkleTreeDeclaration


            others = "進度追蹤"
            othersDetail = otherDetailMerkleTree

        }





        val departments = listOf("需要的資料類別",dataRangeQuestion,"是否有任何風險？", "我的資料會被如何使用？",others)

        val classes = listOf(
            requiredEventArray,
            requiredDetailEventArray,
            listOf(declaration),
            listOf(dataUsageDelcaration),
            listOf(othersDetail)
        )

        val adapter = ExpandableListViewAdapter(this, departments, classes)
        listView.setAdapter(adapter)

    }
    @SuppressLint("WrongViewCast")
    fun approveSendButtonOnClicked(view: View) {
        val checkbox: CheckBox = findViewById(R.id.checkBoxForEvent)
        if (checkbox.isChecked) {

            if (eventType == "zkrp") {
                println("going to zkrp")
                val zkrpIntent = Intent(this, ZkrpSendActivity::class.java)
                val backIntent = Intent(this,FindDetailActivity::class.java)
                val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)

                builder.setMessage("請問您是否同意傳送驗證資料？")
                builder.setPositiveButton("同意",
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->

                        setContentView(R.layout.info_transfer_loading)
                        val welcomeThread: Thread = object : Thread() {
                            override fun run() {
                                try {
                                    super.run()
                                    sleep(6000)
                                } catch (e: Exception) {

                                } finally {

                                    startActivity(zkrpIntent)
                                    finish()
                                }
                            }
                        }
                        welcomeThread.start()
                    })
                builder.setNegativeButton("不同意",
                    DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int -> startActivity(backIntent)}
                )

                builder.create()?.show()
            }
            else if(eventType == "merkletree"){
                val ColumnSelectMedicineIntent = Intent(this,ColumnSelectMedicine::class.java)
                startActivity(ColumnSelectMedicineIntent)

             }
        }
    }
    fun declineSendButtonOnClicked(view: View){
        val email:String
        val phoneNumber :String
        val url : String

        if(eventType == "merkletree"){
            email = merkleTreeEmail
            phoneNumber = merkleTreePhoneNumber
            url = merkleTreeUrl
        }else{
            email = zkrpEmail
            phoneNumber = zkrpPhoneNumber
            url = zkrpUrl

        }
        val declineData : DataService.historyInfo = DataService.historyInfo(eventName,
            todayDate,"不同意授權" , email, url, phoneNumber)

        historyInfo.add(declineData)
        val backIntent = Intent(this,ThirdPartyActivity::class.java)
        startActivity(backIntent)
    }

}
