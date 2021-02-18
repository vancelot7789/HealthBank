package com.example.healthbank.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.healthbank.Adapters.SelectMedicineDisplayAdapter
import com.example.healthbank.DataAssets.medicineInfo

import com.example.healthbank.DataAssets.selectedMedicineInfo
import com.example.healthbank.DataAssets.unSelectedMedicineInfo
import com.example.healthbank.R
import kotlinx.android.synthetic.main.activity_self_sharing_display_medicine.*

@SuppressLint("Registered")
class SelfSharingDisplayMedicineActivity : AppCompatActivity() {
    var selectedId: ArrayList<Int>? = ArrayList()
    lateinit var adapter: SelectMedicineDisplayAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_sharing_display_medicine)

        val sortedSelectedMedicineInfo = selectedMedicineInfo?.sortedBy { it.id }?.reversed()

        for(selectedItem in sortedSelectedMedicineInfo!!){
            selectedId?.add(selectedItem.id)
        }


        for(item in medicineInfo!!){
            if (!(selectedId?.contains(item.id)!!)){
                unSelectedMedicineInfo?.add(item)
            }
        }

        adapter = sortedSelectedMedicineInfo?.let {
            SelectMedicineDisplayAdapter(this, it)
        }!!

        selectDisplayMedicineListView.adapter = adapter

        val layoutManager = GridLayoutManager(this,1)
        selectDisplayMedicineListView.layoutManager = layoutManager


    }
    fun transferButtonOnClicked(view: View){
        val Nextntent = Intent(this, ColumnSelectWestern::class.java)
        startActivity(Nextntent)
    }

    fun lastStepButtonOnClicked(view: View){
        val lastStepIntent = Intent(this, SelfSharingSelectMedicineActivity::class.java)
        startActivity(lastStepIntent)
    }

}

