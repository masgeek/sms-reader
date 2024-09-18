package com.munywele.sms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.munywele.sms.utils.DateUtils
import com.munywele.sms.database.entities.SmsEntity
import com.munywele.sms.databinding.ItemSmsBinding


class SmsAdapter : ListAdapter<SmsEntity, SmsAdapter.SmsViewHolder>(SmsDiffCallback()) {

    inner class SmsViewHolder(private val binding: ItemSmsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sms: SmsEntity) {
            val dateString = DateUtils.formatDateFromTimestamp(sms.timestamp)
            binding.smsAddress.text = sms.sender
            binding.smsDate.text = dateString
            binding.smsBody.text = sms.body
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        val binding = ItemSmsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SmsViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        val sms = getItem(position)
        holder.bind(sms)
    }


    class SmsDiffCallback : DiffUtil.ItemCallback<SmsEntity>() {
        override fun areItemsTheSame(oldItem: SmsEntity, newItem: SmsEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SmsEntity, newItem: SmsEntity): Boolean {
            return oldItem == newItem
        }
    }
}