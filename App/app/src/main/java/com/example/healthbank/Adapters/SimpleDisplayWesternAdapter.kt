package com.example.healthbank.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R

class SimpleDisplayWesternAdapter(val context: Context, val products: List<DataService.WesternMedicine>) : RecyclerView.Adapter<SimpleDisplayWesternAdapter.ProductHolder>() {
    inner class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val diseaseName = itemView?.findViewById<TextView>(R.id.diseaseName)
        val medicalDate = itemView?.findViewById<TextView>(R.id.medicalDate)



        fun bindProduct(product: DataService.WesternMedicine, context: Context){

            diseaseName?.text = product.diseaseName
            medicalDate?.text = product.medicalDate

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.western_simple_item,parent,false)
        return ProductHolder(view)
    }

    override fun getItemCount(): Int {
        return products.count()
    }



    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder?.bindProduct(products[position],context)
    }
}