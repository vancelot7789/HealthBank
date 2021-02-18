package com.example.healthbank.Activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.healthbank.Adapters.WesternMedicineAdapter
import com.example.healthbank.DataAssets.westernMedicineInfo
import com.example.healthbank.R
import kotlinx.android.synthetic.main.activity_western_medicine.*

class WesternMedicineActivity : AppCompatActivity() {

    lateinit var adapter: WesternMedicineAdapter
    lateinit var sv: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_western_medicine)

        sv = findViewById<SearchView>(R.id.searchView)
        sv.isIconifiedByDefault = false
        sv.isFocusable = true
        sv.queryHint = "請輸入疾病名稱或醫療院所"
        searchFunction()

        adapter = westernMedicineInfo?.let {
            WesternMedicineAdapter(this, it)
        }!!

        var spanCount = 2
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            spanCount = 3
        }
        WesternMedicineListView.adapter = adapter

        val layoutManager = GridLayoutManager(this,1)
        WesternMedicineListView.layoutManager = layoutManager


    }

    fun returnMenuOnClicked(view: View){
        val returnMenuIntent = Intent(this,MenuActivity::class.java)
        startActivity(returnMenuIntent)
    }

    private fun searchFunction() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                println(newText)
                adapter.filter.filter(newText)
                return true
            }
        })
    }

}
