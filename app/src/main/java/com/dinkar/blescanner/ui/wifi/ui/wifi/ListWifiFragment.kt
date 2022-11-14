package com.dinkar.blescanner.ui.wifi.ui.wifi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dinkar.blescanner.databinding.FragmentListWifiBinding
import com.dinkar.blescanner.ui.wifi.ListWifiRecyclerViewAdapter


class ListWifiFragment : Fragment() {
    private lateinit var binding: FragmentListWifiBinding

    companion object {
        fun newInstance() = ListWifiFragment()
    }

    private lateinit var viewModel: ListWifiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListWifiViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListWifiBinding.inflate(inflater, container, false)
//        binding.listWifiDetail.layoutManager = LinearLayoutManager(requireContext())

        val texts = arrayOf("abc ", "bcd", "cde", "def", "efg",
            "fgh", "ghi", "hij", "ijk", "jkl", "klm","lmn","mno","nop",
            "opq","pqr","qrs","rst","stu","tuv","uvw","vwx","wxy","xyz")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_multiple_choice, texts)
        binding.listWifiDetail.adapter = adapter
//        binding.listWifiDetail.adapter = ListWifiRecyclerViewAdapter()

        return binding.root
    }

}