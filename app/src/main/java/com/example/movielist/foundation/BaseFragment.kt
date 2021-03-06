package com.example.movielist.foundation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.movielist.utils.Status


/**
 * Base class for all fragments
 */
//abstract class BaseFragment: Fragment() {
abstract class BaseFragment(viewResId: Int): Fragment(viewResId) {

    /**
     *  Function hide all elements on screen,
     *  and depend on Status value invokes corresponding function
     */
    open fun <T> renderResult(root: ViewGroup,
                         status: Status<T>,
                         onLoading: () -> Unit,
                         onError: (Exception) -> Unit,
                         onSuccess: (T) -> Unit) {

        root.children.forEach { it.visibility = View.GONE }

        when (status) {
            is Status.InProgress -> { onLoading() }
            is Status.Error -> { onError(status.exception) }
            is Status.Success -> { onSuccess(status.data) }
        }
    }

    /**
     * Function responsible for setting up a @toolbar with navController,
     * and adding title in Toolbar
     */
    fun setupNavigation(toolbar: Toolbar, title: String) {
        val navController = findNavController()
        NavigationUI.setupWithNavController(toolbar, navController)
        toolbar.title = title
    }

    /**
     * Function responsible for setting up a Toolbar with navController,
     * and adding empty title in Toolbar
     */
    fun setupNavigation(toolbar: Toolbar) {
        val navController = findNavController()
        NavigationUI.setupWithNavController(toolbar, navController)
        toolbar.title = ""
    }

    /**
     * Function to populate toolbar with menu items
     */
    fun setupToolbarMenu(
        toolbar: Toolbar,
        menuId: Int,
        onClickListener: (item: MenuItem) -> Boolean
    ) {
        toolbar.inflateMenu(menuId)
        toolbar.setOnMenuItemClickListener(onClickListener)
    }


//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        Log.e("$this", "attach")
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.e("$this", "create")
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        Log.e("$this", "create view")
//        return super.onCreateView(inflater, container, savedInstanceState)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        Log.e("$this", "view created")
//    }
//
//    override fun onStart() {
//        super.onStart()
//        Log.e("$this", "start")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Log.e("$this", "resume")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.e("$this", "pause")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.e("$this", "stop")
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        Log.e("$this", "destroyView")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.e("$this", "destroy")
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        Log.e("$this", "detach")
//    }
}