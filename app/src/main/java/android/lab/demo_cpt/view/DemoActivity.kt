package android.lab.demo_cpt.view

import android.lab.demo_cpt.BlankFragment
import android.lab.demo_cpt.R
import android.lab.demo_cpt.databinding.ActivityDemoBinding
import android.lab.demo_cpt.viewmodel.CurrencyListViewModel
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DemoActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDemoBinding
    private val viewModel: CurrencyListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_demo)
        initView()
        viewModel.getCurrencyListFromDB()
    }

    private fun initView() {
        binding.btnShowList.setOnClickListener(this)
        binding.btnSort.setOnClickListener(this)
        subscribeUi()
    }

    private fun subscribeUi() {
        lifecycleScope.launchWhenResumed {
            viewModel.selectedItem.collect {
                Toast.makeText(this@DemoActivity, it.name, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnShowList -> {
                if (getForegroundFragment() is BlankFragment) findNavController(R.id.nav_host).navigate(
                    R.id.navigate_to_list_fragment
                )
            }
            R.id.btnSort -> {
                viewModel.sortListByName()
            }
            else -> {
            }
        }
    }

    private fun getForegroundFragment(): Fragment? {
        val navHostFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host)
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }
}