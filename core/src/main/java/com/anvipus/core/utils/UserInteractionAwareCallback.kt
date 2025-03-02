package com.anvipus.core.utils

import android.app.Activity
import android.os.Build
import android.view.*
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi

/**
 * Created by devisevianus on 21/02/22
 */

class UserInteractionAwareCallback(
    private val originalCallback: Window.Callback,
    private val activity: Activity?
) : Window.Callback {

    override fun dispatchKeyEvent(p0: KeyEvent?): Boolean {
        return originalCallback.dispatchKeyEvent(p0)
    }

    override fun dispatchKeyShortcutEvent(p0: KeyEvent?): Boolean {
        return originalCallback.dispatchKeyShortcutEvent(p0)
    }

    override fun dispatchTouchEvent(p0: MotionEvent?): Boolean {
        when (p0?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP, MotionEvent.ACTION_MOVE -> {
                activity?.onUserInteraction()
            }
        }
        return originalCallback.dispatchTouchEvent(p0)
    }

    override fun dispatchTrackballEvent(p0: MotionEvent?): Boolean {
        return originalCallback.dispatchTrackballEvent(p0)
    }

    override fun dispatchGenericMotionEvent(p0: MotionEvent?): Boolean {
        return originalCallback.dispatchGenericMotionEvent(p0)
    }

    override fun dispatchPopulateAccessibilityEvent(p0: AccessibilityEvent?): Boolean {
        return originalCallback.dispatchPopulateAccessibilityEvent(p0)
    }

    override fun onCreatePanelView(p0: Int): View? {
        return originalCallback.onCreatePanelView(p0)
    }

    override fun onCreatePanelMenu(p0: Int, p1: Menu): Boolean {
        return originalCallback.onCreatePanelMenu(p0, p1)
    }

    override fun onPreparePanel(p0: Int, p1: View?, p2: Menu): Boolean {
        return originalCallback.onPreparePanel(p0, p1, p2)
    }

    override fun onMenuOpened(p0: Int, p1: Menu): Boolean {
        return originalCallback.onMenuOpened(p0, p1)
    }

    override fun onMenuItemSelected(p0: Int, p1: MenuItem): Boolean {
        return originalCallback.onMenuItemSelected(p0, p1)
    }

    override fun onWindowAttributesChanged(p0: WindowManager.LayoutParams?) {
        originalCallback.onWindowAttributesChanged(p0)
    }

    override fun onContentChanged() {
        originalCallback.onContentChanged()
    }

    override fun onWindowFocusChanged(p0: Boolean) {
        originalCallback.onWindowFocusChanged(p0)
    }

    override fun onAttachedToWindow() {
        originalCallback.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        originalCallback.onDetachedFromWindow()
    }

    override fun onPanelClosed(p0: Int, p1: Menu) {
        originalCallback.onPanelClosed(p0, p1)
    }

    override fun onSearchRequested(): Boolean {
        return originalCallback.onSearchRequested()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onSearchRequested(p0: SearchEvent?): Boolean {
        return originalCallback.onSearchRequested(p0)
    }

    override fun onWindowStartingActionMode(p0: ActionMode.Callback?): ActionMode? {
        return originalCallback.onWindowStartingActionMode(p0)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onWindowStartingActionMode(p0: ActionMode.Callback?, p1: Int): ActionMode? {
        return originalCallback.onWindowStartingActionMode(p0, p1)
    }

    override fun onActionModeStarted(p0: ActionMode?) {
        originalCallback.onActionModeStarted(p0)
    }

    override fun onActionModeFinished(p0: ActionMode?) {
        originalCallback.onActionModeFinished(p0)
    }
}