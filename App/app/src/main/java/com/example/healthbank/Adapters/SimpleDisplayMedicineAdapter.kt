package com.example.healthbank.Adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R

class SimpleDisplayMedicineAdapter(val context: Context, val products: List<DataService.medicineUsage>) : RecyclerView.Adapter<SimpleDisplayMedicineAdapter.ProductHolder>() {
    inner class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val medicalDate = itemView?.findViewById<TextView>(R.id.medicalDate)
        val medName = itemView?.findViewById<TextView>(R.id.medName)


        fun bindProduct(product: DataService.medicineUsage, context: Context){
            medicalDate?.text = product.medicalDate
            medName?.text = product.medName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.medicine_simple_item,parent,false)
        return ProductHolder(view)
    }

    override fun getItemCount(): Int {
        return products.count()
    }



    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder?.bindProduct(products[position],context)
    }
}
