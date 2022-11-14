package com.dinkar.blescanner.ui.areaCard.detail.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dinkar.blescanner.R

class AreaDetailFragment : Fragment() {

    companion object {
        fun newInstance() = AreaDetailFragment()
    }

    private lateinit var viewModel: AreaDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AreaDetailViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_area_detail, container, false)
    }

}