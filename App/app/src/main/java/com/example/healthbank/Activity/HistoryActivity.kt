package com.example.healthbank.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.healthbank.Adapters.HistoryAdapter
import com.example.healthbank.DataAssets.historyInfo
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    lateinit var adapter: HistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)


        val reversedHistoryInfo = historyInfo.reversed().toMutableList()

        adapter =  reversedHistoryInfo?.let {
            HistoryAdapter(this, it as ArrayList<DataService.historyInfo>)
        }!!

        historyListView.adapter = adapter

        val layoutManager = GridLayoutManager(this,1)
        historyListView.layoutManager = layoutManager



    }



    fun returnMenuOnClicked(view: View){
        val returnMenuIntent = Intent(this, MenuActivity::class.java)
        startActivity(returnMenuIntent)

    }

}
