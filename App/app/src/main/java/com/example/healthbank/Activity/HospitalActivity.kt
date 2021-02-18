package com.example.healthbank.Activity

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.healthbank.Adapters.HospitalAdapter
import com.example.healthbank.Adapters.WesternMedicineAdapter
import com.example.healthbank.DataAssets.hospitalInfo
import com.example.healthbank.DataAssets.westernMedicineInfo
import com.example.healthbank.R
import kotlinx.android.synthetic.main.activity_hospital.*
import kotlinx.android.synthetic.main.activity_western_medicine.*

class HospitalActivity : AppCompatActivity() {

    lateinit var adapter: HospitalAdapter
    lateinit var sv: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital)

        adapter = hospitalInfo?.let {
           HospitalAdapter(this, it)
        }!!

        sv = findViewById<SearchView>(R.id.searchView)
        sv.isIconifiedByDefault = false
        sv.isFocusable = true

        var spanCount = 2
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            spanCount = 3
        }
        hospitalListView.adapter = adapter

        val layoutManager = GridLayoutManager(this,1)
        hospitalListView.layoutManager = layoutManager
    }

    fun returnMenuOnClicked(view: View){
        val returnMenuIntent = Intent(this,MenuActivity::class.java)
        startActivity(returnMenuIntent)
    }
}
