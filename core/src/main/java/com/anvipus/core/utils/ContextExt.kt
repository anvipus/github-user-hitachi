package com.anvipus.core.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.anvipus.core.ui.AnimationBottomSheet
import com.anvipus.core.ui.TimeoutBottomSheet
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

fun Fragment.openBottomSheetWithAnimation(
    animationId: Int? = null,
    title: String? = null,
    message: String?,
    textButton: String? = null,
    isCancelable: Boolean = true,
    listener: (() -> Unit)? = null
): AnimationBottomSheet {
    var bottomSheet: BottomSheetDialogFragment? = null
    bottomSheet?.dismiss()
    bottomSheet = AnimationBottomSheet.newInstance(animationId, title, message.orEmpty(), textButton, isCancelable).apply {
        setBottomSheetListener(object : BottomSheetListener {
            override fun bottomSheetOnClickListener() {
                listener?.invoke()
            }

            override fun bottomSheetWithStringListener(value: String) {}

            override fun bottomSheetOnClickListenerButton2() {}
        })
    }
    bottomSheet.show(childFragmentManager, AnimationBottomSheet.TAG)
    return bottomSheet
}

fun FragmentActivity.openBottomSheetWithAnimation(
    animationId: Int? = null,
    title: String? = null,
    message: String?,
    textButton: String? = null,
    isCancelable: Boolean = true,
    listener: (() -> Unit)? = null
): AnimationBottomSheet {
    var bottomSheet: BottomSheetDialogFragment? = null
    bottomSheet?.dismiss()
    bottomSheet = AnimationBottomSheet.newInstance(animationId, title, message.orEmpty(), textButton, isCancelable).apply {
        setBottomSheetListener(object : BottomSheetListener {
            override fun bottomSheetOnClickListener() {
                listener?.invoke()
            }

            override fun bottomSheetWithStringListener(value: String) {}

            override fun bottomSheetOnClickListenerButton2() {}
        })
    }
    bottomSheet.show(supportFragmentManager, AnimationBottomSheet.TAG)
    return bottomSheet
}

fun Fragment.openTimeoutBottomSheet(
    isCancelable: Boolean = true,
    listener: () -> Unit
): TimeoutBottomSheet {
    var bottomSheet: BottomSheetDialogFragment? = null
    bottomSheet?.dismiss()
    bottomSheet = TimeoutBottomSheet.newInstance(isCancelable).apply {
        setBottomSheetListener(object : BottomSheetListener {
            override fun bottomSheetOnClickListener() {
                listener.invoke()
            }

            override fun bottomSheetOnClickListenerButton2() {

            }

            override fun bottomSheetWithStringListener(value: String) {}


        })
    }
    bottomSheet.show(childFragmentManager, TimeoutBottomSheet.TAG)
    return bottomSheet
}