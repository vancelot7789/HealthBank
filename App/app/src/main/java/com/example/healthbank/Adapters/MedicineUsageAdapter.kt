package com.example.healthbank.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R

class MedicineUsageAdapter(val context: Context, val products: Array<DataService.medicineUsage>) : RecyclerView.Adapter<MedicineUsageAdapter.ProductHolder>() {
    inner class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val medicalDate = itemView?.findViewById<TextView>(R.id.medicalDate)
        val medName = itemView?.findViewById<TextView>(R.id.medName)
        val amount = itemView?.findViewById<TextView>(R.id.amount)
        val days = itemView?.findViewById<TextView>(R.id.days)

        fun bindProduct(product: DataService.medicineUsage, context: Context){
//            medicalDate?.text = "拿藥日期： "
//            medicalDate.append(product.medicalDate)
            medicalDate?.text = product.medicalDate

//            medName?.text = "藥物名稱： "
//            medName.append(product.medName)
            medName?.text = product.medName

//            amount?.text = "醫囑總量： "
//            amount.append(product.amount)
            amount?.text = product.amount

//            days?.text = "給藥日數： "
//            days.append(product.days)
            days?.text = product.days

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.medicine_usage_list_item,parent,false)
        return ProductHolder(view)
    }


    override fun getItemCount(): Int {
        return products.count()
    }



    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder?.bindProduct(products[position],context)
    }
}
