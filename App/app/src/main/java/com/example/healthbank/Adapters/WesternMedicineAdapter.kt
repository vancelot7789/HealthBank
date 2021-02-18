package com.example.healthbank.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthbank.DataClass.DataService
import com.example.healthbank.R

class WesternMedicineAdapter(val context: Context,
                             var products: Array<DataService.WesternMedicine>
) : RecyclerView.Adapter<WesternMedicineAdapter.ProductHolder>(), Filterable {

    val westernMed: Array<DataService.WesternMedicine> = products

    inner class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val diseaseName = itemView?.findViewById<TextView>(R.id.diseaseName)
        val medicalDate = itemView?.findViewById<TextView>(R.id.medicalDate)
        val hospitalName = itemView?.findViewById<TextView>(R.id.hospitalName)


        fun bindProduct(product: DataService.WesternMedicine, context: Context){

//            diseaseName?.text = "疾病名稱： "
//            diseaseName.append(product.diseaseName)
            diseaseName?.text = product.diseaseName

//            medicalDate?.text = "就醫日期： "
//            medicalDate.append(product.medicalDate)
            medicalDate?.text = product.medicalDate

//            hospitalName?.text = "醫療院所： "
//            hospitalName.append(product.hospitalName)
            hospitalName?.text = product.hospitalName

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.western_medicine_list_item,parent,false)
        return ProductHolder(view)
    }

    override fun getItemCount(): Int {
        return products.count()
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder?.bindProduct(products[position],context)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                var constraint: CharSequence? = constraint
                constraint = constraint.toString()
                val result = FilterResults()
                if (constraint != null && constraint.toString().isNotEmpty()) {
                    val filteredItem = products.filter { it.diseaseName.contains(constraint) }
                    result.count = filteredItem.size
                    result.values = filteredItem.toTypedArray()
                } else {
                    result.count = westernMed.size
                    result.values = westernMed
                }
                return result
            }

            override fun publishResults(
                constraint: CharSequence,
                results: FilterResults
            ) {
                products = results.values as Array<DataService.WesternMedicine>
                if (results.count > 0) {
                    notifyDataSetChanged()
                }
            }
        }
    }
}