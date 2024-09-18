package com.munywele.sms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.munywele.sms.utils.DateUtils
import com.munywele.sms.database.entities.SmsEntity
import com.munywele.sms.databinding.ItemSmsBinding


class SmsAdapter(private val onItemClick: (SmsEntity) -> Unit) :
    ListAdapter<SmsEntity, SmsAdapter.SmsViewHolder>(SmsDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        val binding = ItemSmsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SmsViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        val sms = getItem(position)
        holder.bind(sms)
    }

    inner class SmsViewHolder(private val binding: ItemSmsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(sms: SmsEntity) {
            binding.apply {
                val dateString = DateUtils.formatDateFromTimestamp(sms.timestamp)
                smsSender.text = sms.sender
                smsDate.text = dateString
                smsBody.text = sms.body

                // Highlight messages with high amounts
                messageLayout.setBackgroundResource(
                    if (sms.amount > 1000) android.R.color.holo_blue_bright
                    else android.R.color.holo_orange_dark
                )
            }
        }
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