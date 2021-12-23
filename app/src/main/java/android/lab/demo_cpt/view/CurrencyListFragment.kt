package android.lab.demo_cpt.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.lab.demo_cpt.adapter.CurrencyAdapter
import android.lab.demo_cpt.databinding.FragmentCurrencyListBinding
import android.lab.demo_cpt.viewmodel.CurrencyListViewModel
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CurrencyListFragment : Fragment() {
    private val viewModel: CurrencyListViewModel by activityViewModels()
    private var _binding: FragmentCurrencyListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view.context)
    }

    private fun initView(context: Context) {
        val adapter = CurrencyAdapter { viewModel.setSelectedItem(it) }
        binding.rvCurrencyList.adapter = adapter
        binding.rvCurrencyList.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: CurrencyAdapter) {
        lifecycleScope.launchWhenResumed {
            viewModel.currencyList.collect {
                adapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}