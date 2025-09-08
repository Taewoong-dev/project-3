package com.boost.issueTracker.ui.issue

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.boost.issueTracker.ui.issue.viewmodel.IssueListViewModel

abstract class IssueBaseFragment<T : ViewBinding>(private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> T) :
    Fragment() {
    protected val viewmodel: IssueListViewModel by activityViewModels()
    private var _binding: T? = null
    val binding: T
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.e("${this::class.simpleName}", "onCreateView")
        _binding = bindingInflater(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.e("${this::class.simpleName}", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("${this::class.simpleName}", "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.e("${this::class.simpleName}", "onStop")
    }

    // 메모리 누수 방지
    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("${this::class.simpleName}", "onDestroyView")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("${this::class.simpleName}", "onDestroy")
    }
}