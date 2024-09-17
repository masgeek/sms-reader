package com.munywele.sms.reader.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemAnimator.ItemAnimatorFinishedListener
import com.munywele.sms.reader.DateUtils
import com.munywele.sms.reader.database.entities.SmsEntity
import com.munywele.sms.reader.databinding.ItemSmsBinding

class SmsAdapter(private var smsList: List<SmsEntity>) :
    RecyclerView.Adapter<SmsAdapter.SmsViewHolder>() {

    inner class SmsViewHolder(private val binding: ItemSmsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sms: SmsEntity) {
            val dateString = DateUtils.formatDateFromTimestamp(sms.date)
            binding.smsAddress.text = sms.address
            binding.smsDate.text = dateString
            binding.smsBody.text = sms.body
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsAdapter.SmsViewHolder {
        val binding = ItemSmsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SmsViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: SmsAdapter.SmsViewHolder, position: Int) {
        val sms = smsList[position]
        holder.bind(sms)
    }

    override fun getItemCount(): Int {
        return smsList.size
    }

    fun updateSmsList(newSmsList: List<SmsEntity>) {
        smsList = newSmsList
        notifyDataSetChanged()
    }
}