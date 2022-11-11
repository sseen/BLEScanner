package com.dinkar.blescanner.ui.area.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dinkar.blescanner.R
import com.dinkar.blescanner.databinding.FragmentAreaBinding
import com.dinkar.blescanner.ui.area.ListAreaRVAdapter

class AreaFragment : Fragment() {

    private lateinit var binding: FragmentAreaBinding

    companion object {
        fun newInstance() = AreaFragment()
    }

    private lateinit var viewModel: AreaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AreaViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAreaBinding.inflate(inflater, container, false)
        binding.listAreasRecycle.layoutManager = LinearLayoutManager(requireContext())
        binding.listAreasRecycle.adapter = ListAreaRVAdapter()

        return binding.root
    }

}