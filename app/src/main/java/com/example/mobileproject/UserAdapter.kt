package com.example.mobileproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView

class UserAdapter (
    private val context: Context,
    private val dataset: List<UserModel>,
    private val frag: Fragment
    ) : RecyclerView.Adapter<UserAdapter.ItemViewHolder>() {


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Affirmation object.
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView = view.findViewById(R.id.item_user_name)
        val textViewScore: TextView = view.findViewById(R.id.item_user_score)
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_user, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        holder.textViewName.text = item.name
        holder.textViewScore.text = item.score.toString()
        holder.itemView.setOnClickListener(View.OnClickListener(){
            /*
            //val bundle = bundleOf("aic" to item.aic, "name" to item.name)
            if(frag.javaClass.name == "com.example.drpill.ui.medicines.MedicinesFragment")
                frag.findNavController().navigate(R.id.action_navigation_medicines_to_medicineDetailFragment,bundle)
            else if(frag.javaClass.name == "com.example.drpill.ui.therapies.TherapyDetailFragment")
                frag.findNavController().navigate(R.id.action_therapyDetailFragment_to_medicineDetailFragment,bundle)
            else
                frag.findNavController().navigate(R.id.action_addTherapyFragment_to_medicineDetailFragment,bundle)
             */
        })

    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = dataset.size
}