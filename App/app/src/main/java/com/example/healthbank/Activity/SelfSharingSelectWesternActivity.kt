package com.example.healthbank.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbank.Adapters.SelectWesternAdapter
import com.example.healthbank.DataAssets.selectedWesternInfo
import com.example.healthbank.DataAssets.westernMedicineInfo
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R

class SelfSharingSelectWesternActivity : AppCompatActivity() {
    internal lateinit var adapter: SelectWesternAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_sharing_select_western)
        val data_in_2019 : ArrayList<DataService.WesternMedicine> = ArrayList()

        for(item in westernMedicineInfo!!){
            if ("2019" in item.medicalDate){
                data_in_2019?.add(item)
            }
        }
        adapter = data_in_2019?.let { SelectWesternAdapter(this, it) }!!

//        adapter = westernMedicineInfo?.let { SelectWesternAdapter(this, it) }!!

        val rv = findViewById<RecyclerView>(R.id.westernCheckBoxList) as RecyclerView
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
        selectedWesternInfo = adapter.selectedWestern

        val selfSharingIntent2 = Intent(this, SelfSharingDisplayAllActivity::class.java)
        startActivity(selfSharingIntent2)

    }
}
