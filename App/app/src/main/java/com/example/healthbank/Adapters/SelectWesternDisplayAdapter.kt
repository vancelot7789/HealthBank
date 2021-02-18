package com.example.healthbank.Adapters



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R

class SelectWesternDisplayAdapter(val context: Context, val products: List<DataService.WesternMedicine>) : RecyclerView.Adapter<SelectWesternDisplayAdapter.ProductHolder>() {
    inner class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val diseaseName = itemView?.findViewById<TextView>(R.id.diseaseName)
        val medicalDate = itemView?.findViewById<TextView>(R.id.medicalDate)



        fun bindProduct(product: DataService.WesternMedicine, context: Context){

//            diseaseName?.text = "疾病名稱： "
//            diseaseName.append(product.diseaseName)
            diseaseName?.text = product.diseaseName

//            medicalDate?.text = "就醫日期： "
//            medicalDate.append(product.medicalDate)
            medicalDate?.text = product.medicalDate


//            hospitalName?.text = "醫療院所： "
//            hospitalName.append(product.hospitalName)



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.western_selected_list,parent,false)
        return ProductHolder(view)
    }

    override fun getItemCount(): Int {
        return products.count()
    }



    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder?.bindProduct(products[position],context)
    }
}