package com.example.hiltmvvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hiltmvvm.adapter.FilterCategoryRecyclerAdapter
import com.example.hiltmvvm.databinding.FragmentHomeBinding
import com.example.hiltmvvm.model.FilterCategoryObject

class HomeFragment : Fragment(), View.OnClickListener,
    FilterCategoryRecyclerAdapter.FilterCategoryInteraction {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var filterRecyclerAdapter: FilterCategoryRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        /*binding.rvFilterCategory.apply {
            if (!this@HomeFragment::filterRecyclerAdapter.isInitialized) {
                filterRecyclerAdapter = FilterCategoryRecyclerAdapter(this@HomeFragment)
            }
            adapter = filterRecyclerAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }*/
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.tvSearch.id -> {
                TODO("Not yet implemented")
            }
        }
    }

    override fun onFilterClicked(position: Int, item: FilterCategoryObject) {
        TODO("Not yet implemented")
    }
}