package com.fitnessclub.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitnessclub.android.adapters.MemberAdapter
import com.fitnessclub.android.databinding.ActivityMembersBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MembersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMembersBinding
    private lateinit var adapter: MemberAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupRecyclerView()
        loadMembers()
    }
    
    private fun setupRecyclerView() {
        adapter = MemberAdapter { member ->
            // Обработка клика по участнику
            val intent = Intent(this, MemberDetailActivity::class.java).apply {
                putExtra("MEMBER_ID", member.id)
            }
            startActivity(intent)
        }
        
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
    
    private fun loadMembers() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val members = ApiClient.apiService.getMembers()
                withContext(Dispatchers.Main) {
                    adapter.submitList(members)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}