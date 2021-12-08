package com.example.movielist.foundation

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import com.example.movielist.utils.Status

/**
 * Base class for all fragments with motionLayout root
 */
//abstract class BaseMotionFragment: BaseFragment() {
abstract class BaseMotionFragment(viewResId: Int): BaseFragment(viewResId) {

    open var _transitionState: Bundle? = null
    open var _motionLayout: MotionLayout? = null
    open val TRANSITION_STATE = "$this + TRANSITION_STATE"

    /**
     * Added functionality to restore layout's transition state
     * when fragment's view creating
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.getBundle(TRANSITION_STATE).let { bundle ->
            bundle?.let {
                saveMotionState(it)
            }
        }
        exposeMotionState()
    }

    /**
     * Added functionality to save layout's transition state
     * when fragment's view destroying
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _motionLayout?.let {
            saveMotionState(it.transitionState)
        }
    }

    /**
     * Added functionality to save layout's transition state
     * when fragment's activity destroying
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(TRANSITION_STATE, _transitionState)
    }

    /**
     * Function save motionLayout's transition state.
     * It invokes before fragment's view layout has to be destroyed
     */
    private fun saveMotionState(state: Bundle) {
        _transitionState = state
    }

    /**
     * Function restore motionLayout's transition state.
     * It invokes when fragment's view layout are creating
     */
    private fun exposeMotionState() {
        _transitionState?.let { state ->
            _motionLayout?.let { motionLayout ->
                motionLayout.transitionState = state
            }
        }
    }

    /**
     *  Function make layout hideable and
     *  hide all elements on screen by invoking parent constructor
     */
    override fun <T> renderResult(root: ViewGroup,
                                  status: Status<T>,
                                  onLoading: () -> Unit,
                                  onError: (Exception) -> Unit,
                                  onSuccess: (T) -> Unit) {
        hideMotionLayout(root as MotionLayout)
        super.renderResult(root, status, onLoading, onError, onSuccess)
    }

    /**
     * Function make motionLayout hideable
     */
    private fun hideMotionLayout(root: MotionLayout) {
        val childrenIds = mutableSetOf<Int>()
        root.children.forEach { view ->
            childrenIds.add(view.id)
            if (view is ViewGroup) {
                view.children.forEach { viewItem ->
                    childrenIds.add(viewItem.id)
                }
            }
        }
        root.constraintSetIds.forEach { setId ->
            childrenIds.forEach { viewId ->
                root.getConstraintSet(setId).setVisibilityMode(viewId, ConstraintSet.VISIBILITY_MODE_IGNORE)
            }
        }
    }
}