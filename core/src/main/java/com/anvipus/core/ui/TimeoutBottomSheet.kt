package com.anvipus.core.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.anvipus.core.R
import com.anvipus.core.databinding.BottomSheetRetryBinding
import com.anvipus.core.utils.showIf

class TimeoutBottomSheet : BaseBottomSheet() {

    companion object {
        private const val EXTRA_CANCELABLE = "EXTRA_CANCELABLE"
        private const val EXTRA_ENABLE_BACK_BUTTON = "EXTRA_ENABLE_BACK_BUTTON"
        const val TAG: String = "TimeoutBottomSheet"

        fun newInstance(
            isCancelable: Boolean,
            isEnableBackButton: Boolean = false
        ): TimeoutBottomSheet {
            return TimeoutBottomSheet().apply {
                arguments = bundleOf().apply {
                    putBoolean(EXTRA_CANCELABLE, isCancelable)
                    putBoolean(EXTRA_ENABLE_BACK_BUTTON, isEnableBackButton)
                }
            }
        }
    }

    private lateinit var mDataBinding: BottomSheetRetryBinding

    private var possibleToCancel: Boolean = true
    private var isEnableBackButton: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.bottom_sheet_retry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDataBinding = BottomSheetRetryBinding.bind(view)
        arguments?.let {
            possibleToCancel = it.getBoolean(EXTRA_CANCELABLE)
            isEnableBackButton = it.getBoolean(EXTRA_ENABLE_BACK_BUTTON)
        }
        initView()
        initViewAction()
    }

    override fun onStart() {
        super.onStart()
        dialog!!.setCancelable(possibleToCancel)
    }

    private fun initView() {
        with(mDataBinding) {
            ivSwipe.showIf(possibleToCancel)
            btnBack.showIf(isEnableBackButton)
        }
    }

    private fun initViewAction() {
        with(mDataBinding) {
            btnRetry.setOnClickListener {
                getBottomSheetListener()?.bottomSheetOnClickListener()
                dismiss()
            }
            btnBack.setOnClickListener {
                getBottomSheetListener()?.bottomSheetWithStringListener("")
                dismiss()
            }
        }
    }

    /*fun setBottomSheetListener(bottomSheetListener: BottomSheetListener) {
        listener = bottomSheetListener
    }*/

}