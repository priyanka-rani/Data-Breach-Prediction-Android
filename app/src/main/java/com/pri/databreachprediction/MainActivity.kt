package com.pri.databreachprediction

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.observe
import com.pri.databreachprediction.databinding.ActivityMainBinding
import com.pri.databreachprediction.vo.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
                DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.btDetect.setOnClickListener {
            viewModel.callPredictionApi()
            hideKeyBoard(it)
        }
        viewModel.prediction.observe(this) {
            it?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        viewModel.breachType.value = getString(R.string.breach_type_placeholder, it.data?.joinToString())
                        binding.scrollView.post {
                            binding.scrollView.fullScroll(View.FOCUS_DOWN)
                        }
                    }
                    Status.ERROR -> viewModel.breachType.value = it.message
                            ?: getString(R.string.error_classification)
                    else -> {
                    }
                }
            }

        }
        viewModel.text.distinctUntilChanged().observe(this) {
            viewModel.breachType.value = null
        }
    }

    fun hideKeyBoard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}