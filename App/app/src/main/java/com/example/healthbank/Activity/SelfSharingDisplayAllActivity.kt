package com.example.healthbank.Activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.healthbank.Adapters.SelectMedicineDisplayAdapter
import com.example.healthbank.Adapters.SelectWesternDisplayAdapter
import com.example.healthbank.Adapters.SimpleDisplayMedicineAdapter
import com.example.healthbank.Adapters.SimpleDisplayWesternAdapter
import com.example.healthbank.DataAssets.*
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R
import kotlinx.android.synthetic.main.activity_self_sharing_display_all.*
import kotlinx.android.synthetic.main.activity_self_sharing_display_medicine.*
import kotlinx.android.synthetic.main.activity_self_sharing_display_western.*

class SelfSharingDisplayAllActivity : AppCompatActivity() {
    var selectedMedicineId: ArrayList<Int>? = ArrayList()
    var selectedWesternId: ArrayList<Int>? = ArrayList()
    lateinit var adapterMedicine: SimpleDisplayMedicineAdapter
    lateinit var adapterWestern: SimpleDisplayWesternAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_sharing_display_all)



        val data_in_2019_Med : ArrayList<DataService.medicineUsage> = ArrayList()

        for(item in medicineInfo!!){
            if ("2019" in item.medicalDate){
                data_in_2019_Med?.add(item)
            }
        }
        medicineCount.text = "(共" + data_in_2019_Med.size.toString() + "筆)"

        val sortedSelectedMedicineInfo = data_in_2019_Med?.sortedBy { it.id }?.reversed()

        for(selectedItem in sortedSelectedMedicineInfo!!){
            selectedMedicineId?.add(selectedItem.id)
        }
        for(item in medicineInfo!!){
            if (!(selectedMedicineId?.contains(item.id)!!)){
                unSelectedMedicineInfo?.add(item)
            }
        }
        adapterMedicine = sortedSelectedMedicineInfo?.let {
            SimpleDisplayMedicineAdapter(this, it)
        }!!

        medicineSimpleAllList.adapter =  adapterMedicine
        val layoutManagerMedicine = GridLayoutManager(this,1)
        medicineSimpleAllList.layoutManager = layoutManagerMedicine







        val data_in_2019_Wes : ArrayList<DataService.WesternMedicine> = ArrayList()
        westernCount.text = "(共" + data_in_2019_Wes.size.toString() + "筆)"

        for(item in westernMedicineInfo!!){
            if ("2019" in item.medicalDate){
                data_in_2019_Wes?.add(item)
            }
        }


        westernCount.text = "(共" + data_in_2019_Wes.size.toString() + "筆)"

        val sortedSelectedWesternInfo =  data_in_2019_Wes?.sortedBy { it.id }?.reversed()



        for(selectedItem in sortedSelectedWesternInfo!!){
            selectedWesternId?.add(selectedItem.id)

        }

        for(item in westernMedicineInfo!!){
            if (!( selectedWesternId?.contains(item.id)!!)){
                unSelectedWesternInfo?.add(item)
            }
        }

        adapterWestern = sortedSelectedWesternInfo?.let {
            SimpleDisplayWesternAdapter(this, it)
        }!!

        westernSimpleAllList.adapter =  adapterWestern
        val layoutManagerWestern = GridLayoutManager(this,1)
        westernSimpleAllList.layoutManager = layoutManagerWestern


    }
    fun lastStepButtonOnClicked(view: View){
        val lastStepIntent = Intent(this, SelfSharingDisplayWesternActivity::class.java)
        startActivity(lastStepIntent)
    }

    fun transferDataButtonOnClicked(view:View){
        val merkleTreeIntent = Intent(this, MerkleTreeSendActivity::class.java)
        val backToThirdParty = Intent(this, FindDetailActivity::class.java)
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)

        builder.setMessage("請問您是否同意傳送資料？")
        builder.setPositiveButton("同意",
            DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                val approveData : DataService.historyInfo = DataService.historyInfo(
                    eventName, todayDate,"同意授權", merkleTreeEmail, merkleTreeUrl, merkleTreePhoneNumber)
                historyInfo.add(approveData)
                setContentView(R.layout.data_transfer_loading)
                val welcomeThread: Thread = object : Thread() {
                    override fun run() {
                        try {
                            super.run()
                            sleep(6000)
                        } catch (e: Exception) {

                        } finally {
                            startActivity(merkleTreeIntent)
                            finish()
                        }
                    }
                }
                welcomeThread.start()
            })
        builder.setNegativeButton("不同意",
            DialogInterface.OnClickListener {  dialogInterface: DialogInterface, i: Int -> startActivity(backToThirdParty)})
        builder.create()?.show()
    }
    fun cancelDataButtonOnClicked(view: View){
        val backToThirdParty = Intent(this, FindDetailActivity::class.java)
        startActivity(backToThirdParty)
    }
}
