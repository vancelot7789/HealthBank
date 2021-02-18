package com.example.healthbank.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.healthbank.R

class ExpandableListViewAdapter(
    private val context: Context,
    private val questions: List<String>,
    private val answers: List<List<String>>)
    : BaseExpandableListAdapter() {
    override fun getGroup(groupPosition: Int): Any {
        return questions[groupPosition]
    }
    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return answers[groupPosition][childPosition]
    }
    override fun getGroupCount(): Int {
        return questions.size
    }
    override fun getChildrenCount(groupPosition: Int): Int {
        return answers[groupPosition].size
    }
    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }
    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return (groupPosition * 100 + childPosition).toLong()
    }
    override fun hasStableIds(): Boolean {
        return true
    }
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.expandablelist_question, null)
        val textView = view.findViewById<TextView>(R.id.txtDepartmentName)
        textView.text = questions[groupPosition]

        return view
    }
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.expandablelist_answer, null)
        val textView = view.findViewById<TextView>(R.id.txtClassName)
        textView.text = answers[groupPosition][childPosition]

        return view
    }
}