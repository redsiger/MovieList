package com.example.movielist.foundation

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.movielist.R
import com.example.movielist.utils.Status


/**
 * Base class for all fragments
 */
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
    fun setupToolbarMenu(toolbar: Toolbar,
                         menuId: Int,
                         onClickListener: Toolbar.OnMenuItemClickListener
    ) {
        toolbar.inflateMenu(menuId)
        toolbar.setOnMenuItemClickListener(onClickListener)
    }
}