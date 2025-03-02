package com.anvipus.core.ui

import android.os.Bundle
import android.view.View
import com.anvipus.core.utils.BottomSheetListener
import com.anvipus.core.utils.UserInteractionAwareCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BaseBottomSheet : BottomSheetDialogFragment() {

    private var listener: BottomSheetListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val window = dialog?.window
        window?.let {
            it.callback = UserInteractionAwareCallback(it.callback, requireActivity())
        }
    }

    protected fun getBottomSheetListener() = listener

    fun setBottomSheetListener(bottomSheetListener: BottomSheetListener) {
        listener = bottomSheetListener
    }

}