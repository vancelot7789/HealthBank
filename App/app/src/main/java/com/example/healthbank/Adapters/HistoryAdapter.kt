package com.example.healthbank.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R

class HistoryAdapter(
    val context: Context, var products: ArrayList<DataService.historyInfo>
) : RecyclerView.Adapter<HistoryAdapter.ProductHolder>() {



    inner class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val agencyName = itemView?.findViewById<TextView>(R.id.agencyName)
        val verifyDate = itemView?.findViewById<TextView>(R.id.verfiyDate)
        val desireOption = itemView?.findViewById<TextView>(R.id.desireOption)
        val email =  itemView?.findViewById<TextView>(R.id.emailAddress)

        val phoneNumber = itemView?.findViewById<TextView>(R.id.phoneNumber)
        val urlAddress = itemView?.findViewById<TextView>(R.id.urlAddress)




        @SuppressLint("ResourceAsColor")
        fun bindProduct(product: DataService.historyInfo, context: Context){

            agencyName?.text = product.agencyName
            verifyDate?.text = product.verifyDate

            desireOption?.text = product.desireOption
            desireOption.setTextColor(Color.WHITE)

            email?.text = product.emailAddress

            phoneNumber?.text = product.phoneNumber
            urlAddress?.text = product.urlAddress


            if(product.desireOption == "不同意授權") {
                desireOption.setBackgroundColor(Color.RED)
            }else{

                desireOption.setBackgroundColor(Color.rgb(0,128,0))

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.history_list_item,parent,false)
        return ProductHolder(view)
    }

    override fun getItemCount(): Int {
        return products.count()
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder?.bindProduct(products[position],context)
    }

}