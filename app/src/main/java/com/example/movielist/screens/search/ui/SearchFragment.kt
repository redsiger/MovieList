package com.example.movielist.screens.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.movielist.R
import com.example.movielist.databinding.FragmentSearchBinding
import com.example.movielist.foundation.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
//class SearchFragment: BaseFragment() {
class SearchFragment: BaseFragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    val mBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        setupNavigation(mBinding.searchToolbar)
        context?.let {
            initSearchInput(mBinding.searchInput, mBinding.searchClear, mBinding.searchFind, navigateToSearchResults, it)
        }
    }

    val navigateToSearchResults = { searchQuery: String ->
        val bundle = Bundle()
        bundle.putString("searchQuery", searchQuery)
        findNavController().navigate(R.id.action_searchFragment_to_searchResultsFragment, bundle)
    }

    /**
     * Function to handle ACTION_DONE event on search editText
     */
    private fun initSearchInput(searchEditText: EditText, clearBtn: View, searchBtn: View, react: (String) -> Unit, context: Context) {
        searchEditText.requestFocus()
        showSoftKeyboard(searchEditText, context)
        initDoneBtn(searchEditText, react)
        searchBtn.setOnClickListener {
            if (searchEditText.text.isNotEmpty()) {
                react(searchEditText.text.toString())
            }
        }
        initClearBtn(searchEditText, clearBtn)
    }

    /**
     * Function to make input clearable
     */
    private fun initClearBtn(searchEditText: EditText, clearBtn: View) {
        searchEditText.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                Log.e("TEXT CHANGED", text.toString())
            }
            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(text: Editable?) {
                clearBtn.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        })

        clearBtn.setOnClickListener {
            searchEditText.text.clear()
        }
    }

    /**
     * Function to make input reactable to DONE btn
     */
    private fun initDoneBtn(searchEditText: EditText, react: (String) -> Unit) {
        searchEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(textView: TextView, actionId: Int, keyEvent: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE && textView.text.isNotEmpty()) {
                    Log.e("ENTER", "CLICKED + ${textView.text}")
                    react(textView.text.toString())
                }
                return true
            }
        })
    }

    /**
     * Function to show keyboard, when view is on focus
     */
    private fun showSoftKeyboard(view: View, context: Context) {
        if (view.requestFocus()) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}