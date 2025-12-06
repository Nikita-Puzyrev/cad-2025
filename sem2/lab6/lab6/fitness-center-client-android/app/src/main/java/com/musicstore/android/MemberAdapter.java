package com.fitnessclub.android.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitnessclub.android.databinding.ItemMemberBinding
import com.fitnessclub.android.models.Member

class MemberAdapter(
    private val onItemClick: (Member) -> Unit
) : RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {
    
    private var members = emptyList<Member>()
    
    inner class MemberViewHolder(
        private val binding: ItemMemberBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(member: Member) {
            binding.tvMemberName.text = "${member.firstName} ${member.lastName}"
            binding.tvMemberEmail.text = member.email
            binding.tvMemberPhone.text = member.phoneNumber
            
            binding.root.setOnClickListener {
                onItemClick(member)
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val binding = ItemMemberBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MemberViewHolder(bating)
    }
    
    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(members[position])
    }
    
    override fun getItemCount(): Int = members.size
    
    fun submitList(newList: List<Member>) {
        members = newList
        notifyDataSetChanged()
    }
}