package com.example.healthbank.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbank.Adapters.SelectMedicineAdapter
import com.example.healthbank.DataAssets.medicineInfo
import com.example.healthbank.DataAssets.selectedMedicineInfo
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R

@SuppressLint("Registered")
class SelfSharingSelectMedicineActivity : AppCompatActivity() {

    internal lateinit var adapter: SelectMedicineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_sharing_select_medicine)

        val data_in_2019 : ArrayList<DataService.medicineUsage> = ArrayList()

        for(item in medicineInfo!!){
            if ("2019" in item.medicalDate){
                data_in_2019?.add(item)
            }
        }
        adapter = data_in_2019?.let { SelectMedicineAdapter(this, it) }!!
//        adapter = medicineInfo?.let { SelectMedicineAdapter(this, it) }!!

        val rv = findViewById<RecyclerView>(R.id.medicineCheckBoxlist) as RecyclerView
        rv.layoutManager = LinearLayoutManager(this)
        rv.itemAnimator = DefaultItemAnimator()
        rv.adapter = adapter
        adapter.checkAll()
    }

    fun allCheckOnClicked(view: View){
        adapter.checkAll()
    }

    fun cancelButtonOnClicked(view: View) {
        val cancelIntent = Intent(this, FindDetailActivity::class.java)
        startActivity(cancelIntent)
    }

    fun testbuttonOnClicked(view: View) {
        selectedMedicineInfo = adapter.selectedMedicine


        val selfSharingIntent = Intent(this, ColumnSelectWestern::class.java)
        startActivity(selfSharingIntent)

    }

}


