package com.munywele.sms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.munywele.sms.utils.DateUtils
import com.munywele.sms.database.entities.SmsEntity
import com.munywele.sms.databinding.ItemSmsBinding


class SmsAdapter : RecyclerView.Adapter<SmsAdapter.SmsViewHolder>() {

    private var smsList: List<SmsEntity> = listOf()

    fun updateSmsList(newList: List<SmsEntity>) {
        smsList = newList
        notifyDataSetChanged()  // Update UI with the new list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        val binding = ItemSmsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SmsViewHolder(binding = binding)
    }

    override fun getItemCount(): Int = smsList.size


    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        val sms = smsList[position]
        holder.bind(sms)
    }

    inner class SmsViewHolder(private val binding: ItemSmsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sms: SmsEntity) {
            val dateString = DateUtils.formatDateFromTimestamp(sms.timestamp)
            binding.smsSender.text = sms.sender
            binding.smsDate.text = dateString
            binding.smsBody.text = sms.body
        }
    }

}