package com.dinkar.blescanner.ui.areaCard.detailSelector.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dinkar.blescanner.R
import com.dinkar.blescanner.databinding.FragmentAreaDetailSelectorBinding

class AreaDetailSelectorFragment : Fragment() {

    private lateinit var binding: FragmentAreaDetailSelectorBinding

    companion object {
        fun newInstance() = AreaDetailSelectorFragment()
    }

    private lateinit var viewModel: AreaDetailSelectorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AreaDetailSelectorViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAreaDetailSelectorBinding.inflate(inflater, container, false)

        val texts = arrayOf("abc ", "bcd", "cde", "def", "efg",
            "fgh", "ghi", "hij", "ijk", "jkl", "klm","lmn","mno","nop",
            "opq","pqr","qrs","rst","stu","tuv","uvw","vwx","wxy","xyz")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_single_choice, texts)
        binding.idListAreaDetailSelector.adapter = adapter
//        binding.listWifiDetail.adapter = ListWifiRecyclerViewAdapter()

        return binding.root
    }
}