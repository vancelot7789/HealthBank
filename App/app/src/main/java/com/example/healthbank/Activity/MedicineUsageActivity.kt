package com.example.healthbank.Activity

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.healthbank.Adapters.MedicineUsageAdapter
import com.example.healthbank.Adapters.WesternMedicineAdapter
import com.example.healthbank.DataAssets.medicineInfo
import com.example.healthbank.DataAssets.westernMedicineInfo
import com.example.healthbank.R
import kotlinx.android.synthetic.main.activity_medicine_usage.*
import kotlinx.android.synthetic.main.activity_western_medicine.*

class MedicineUsageActivity : AppCompatActivity() {

    lateinit var adapter: MedicineUsageAdapter
    lateinit var sv: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_usage)

        adapter = medicineInfo?.let {
            MedicineUsageAdapter(this, it)
        }!!

        sv = findViewById<SearchView>(R.id.searchView)
        sv.isIconifiedByDefault = false
        sv.isFocusable = true

        var spanCount = 2
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            spanCount = 3
        }
        MedicalUsageListView.adapter = adapter

        val layoutManager = GridLayoutManager(this,1)
        MedicalUsageListView.layoutManager = layoutManager
    }
    fun returnMenuOnClicked(view: View){
        val returnMenuIntent = Intent(this,MenuActivity::class.java)
        startActivity(returnMenuIntent)
    }
}
