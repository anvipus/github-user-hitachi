package com.anvipus.core.ui

import android.animation.ValueAnimator
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.anvipus.core.R
import com.anvipus.core.databinding.BottomSheetWithAnimationBinding
import com.anvipus.core.utils.fromHtml
import com.anvipus.core.utils.showIf

class AnimationBottomSheet : BaseBottomSheet() {

    companion object {
        private const val EXTRA_TITLE = "title"
        private const val EXTRA_MESSAGE = "message"
        private const val EXTRA_TEXT_BUTTON = "text_button"

        const val TAG: String = "ErrorBottomSheet"

        var isDialogShown = false

        fun newInstance(
            animationId: Int?,
            title: String?,
            message: String,
            textButton: String?,
            isCancelable: Boolean
        ): AnimationBottomSheet {
            return AnimationBottomSheet().apply {
                arguments = bundleOf().apply {
                    putString(EXTRA_MESSAGE, message)
                    putString(EXTRA_TITLE, title)
                    putString(EXTRA_TEXT_BUTTON, textButton)
                }
                this.animationId = animationId
                this.possibleToCancel = isCancelable
            }
        }
    }

    private lateinit var mDataBinding: BottomSheetWithAnimationBinding

    private var animationId: Int? = null
    private var possibleToCancel: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.bottom_sheet_with_animation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDataBinding = BottomSheetWithAnimationBinding.bind(view)
        initView()
        initViewAction()
    }

    override fun onStart() {
        super.onStart()
        dialog!!.setCancelable(possibleToCancel)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        isDialogShown = false
    }

    private fun initView() {
        with(mDataBinding) {
            animationId?.let { id ->
                animationView.setAnimation(id)
                animationView.repeatCount = if (id == R.raw.waiting) {
                    ValueAnimator.INFINITE
                } else {
                    0
                }
            }
            ivSwipe.showIf(possibleToCancel)
            tvFailedDesc.text = arguments?.getString(EXTRA_TITLE) ?: getString(R.string.label_error_warning)
            tvFailedDesc2.text = arguments?.getString(EXTRA_MESSAGE)?.fromHtml()
            btnTutup.text = arguments?.getString(EXTRA_TEXT_BUTTON) ?: getString(R.string.lbl_cards_17)
        }
    }

    private fun initViewAction() {
        mDataBinding.btnTutup.setOnClickListener {
            try {
                getBottomSheetListener()?.bottomSheetOnClickListener()
                dismissAllowingStateLoss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}