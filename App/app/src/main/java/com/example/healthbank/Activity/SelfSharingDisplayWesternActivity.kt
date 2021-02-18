package com.example.healthbank.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.healthbank.Adapters.SelectWesternDisplayAdapter
import com.example.healthbank.DataAssets.*
import com.example.healthbank.R
import kotlinx.android.synthetic.main.activity_self_sharing_display_western.*

class SelfSharingDisplayWesternActivity : AppCompatActivity() {
    var selectedId: ArrayList<Int>? = ArrayList()
    lateinit var adapter: SelectWesternDisplayAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_sharing_display_western)

        val sortedSelectedWesternInfo = selectedWesternInfo?.sortedBy { it.id }?.reversed()

        for(selectedItem in sortedSelectedWesternInfo!!){
            selectedId?.add(selectedItem.id)

        }

        for(item in westernMedicineInfo!!){
            if (!(selectedId?.contains(item.id)!!)){
                unSelectedWesternInfo?.add(item)
            }
        }

        adapter = sortedSelectedWesternInfo?.let {
           SelectWesternDisplayAdapter(this, it)
        }!!

        selectDisplayWesternListView.adapter = adapter

        val layoutManager = GridLayoutManager(this,1)
        selectDisplayWesternListView.layoutManager = layoutManager


    }

    fun transferButtonOnClicked(view: View){
        val allSelectedItemIntent = Intent(this, SelfSharingDisplayAllActivity::class.java)
        startActivity(allSelectedItemIntent)
    }

    fun lastStepButtonOnClicked(view: View){
        val lastIntent = Intent(this, SelfSharingSelectWesternActivity::class.java)
        startActivity(lastIntent)
    }
}
