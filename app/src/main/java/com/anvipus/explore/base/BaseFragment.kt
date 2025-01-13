package com.anvipus.explore.base

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.anvipus.core.utils.ProgressDialog
import com.anvipus.explore.R
import com.anvipus.core.utils.hide
import com.anvipus.core.utils.resColor
import com.anvipus.core.utils.resDrawable
import com.anvipus.core.utils.show
import com.anvipus.core.utils.showIf
import com.anvipus.explore.ui.xml.MainActivity

abstract class BaseFragment: Fragment() {

    private var mProgressDialog: ProgressDialog? = null
    protected abstract val layoutResource: Int

    protected open val showBottomNav: Boolean = false
    protected open val showToolbar: Boolean = true
    protected open val headTitle: Int = 0
    protected open val statusBarColor: Int? = null
    protected open val showToolbarLogo: Boolean = false

    protected lateinit var defaultLifecycleObserver: DefaultLifecycleObserver

    var rootView: View? = null

    private val main: MainActivity? by lazy {
        requireActivity() as? MainActivity
    }

    override fun onStart() {
        super.onStart()
        initStatusBar(statusBarColor)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView != null) {
//            rootView.removeSelf()
        }
        rootView = inflater.inflate(layoutResource, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        main?.apply {
            if(showToolbarLogo.not()){
                main?.hideTitleToolbar()
            }
            layoutToolbar.let{
                it.showIf(showToolbar)
                if(it.isVisible){
                    toolbarTitle.text = if (headTitle != 0) {
                        toolbarLogo.hide()
                        toolbarTitle.show()
                        getString(headTitle)
                    } else {
                        ""
                    }
                }else {
                    it.hide()
                }
                toolbar.menu.clear()
            }
        }

        setupArgs()

        initView(view)
        initObserver()
        setupListener()

        initAPI()
        onViewCreated()
    }

    override fun onResume() {
        super.onResume()
        initStatusBar(statusBarColor)
    }

    protected fun setupSpannable(
        fullText: String,
        actionText: String,
        color: Int? = null,
        fontId: Int? = null,
        action: () -> Unit = {}
    ): SpannableStringBuilder {
        val spannableStringBuilder = SpannableStringBuilder(fullText)
        val stringAction = object : ClickableSpan() {
            override fun onClick(widget: View) {
                action.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.typeface = ResourcesCompat.getFont(requireContext(), fontId ?: com.anvipus.core.R.font.inter_heavy)
                ds.color = resColor(color ?: com.anvipus.core.R.color.colorDimGrayAlt)
            }
        }
        val endText = fullText.indexOf(actionText) + actionText.length
        spannableStringBuilder.setSpan(
            stringAction,
            fullText.indexOf(actionText),
            endText,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableStringBuilder
    }

    @SuppressLint("InlinedApi")
    @Suppress("DEPRECATION")
    protected fun initStatusBar(color: Int?) {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = requireActivity().window

            window.statusBarColor = if (color != null) {
                setStatusBarAppearance(window, isLightStatusBar = true)
                requireContext().resColor(color)
            } else {
                setStatusBarAppearance(window, isLightStatusBar = false)
                Color.TRANSPARENT
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setWindowFlag(
        bits: Int = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
        on: Boolean = false
    ) {
        val win = activity?.window
        val winParams = win?.attributes
        if (on) {
            winParams?.flags = winParams!!.flags or bits
        } else {
            winParams?.flags = winParams!!.flags and bits.inv()
        }
        win?.attributes = winParams
    }




    private fun setStatusBarAppearance(window: Window, isLightStatusBar: Boolean) {
        val wic = WindowInsetsControllerCompat(window, window.decorView)
        wic.isAppearanceLightStatusBars = isLightStatusBar
    }

    protected fun navigate(destination: NavDirections) = lifecycleScope.launchWhenResumed {
        with(navController()) {
            currentDestination?.getAction(destination.actionId)?.let {
                navigate(destination)
            }
        }
    }

    fun navController() = findNavController()

    protected open fun initView(view: View) {}
    protected open fun initObserver() {}
    protected open fun setupListener() {}
    protected open fun setupArgs() {}
    protected open fun onViewCreated() {}
    protected open fun initAPI() {}

    protected fun ownTitle(title: String){
        main?.toolbar?.title = ""
        main?.setTitleToolbar(title)
    }

    protected fun showLogo(){
        main?.hideTitleToolbar()
    }

    protected fun ownIcon(res: Int?){
        main?.toolbar?.navigationIcon = if(res == null) null else context?.resDrawable(res)
    }

    protected fun ownMenu(menu: Int, menuCallback: (MenuItem)-> Unit){
        main?.toolbar?.apply {
            inflateMenu(menu)
            setOnMenuItemClickListener {
                menuCallback.invoke(it)
                true
            }
        }
    }

    fun isFragmentInBackStack(destinationId: Int) = try {
        navController().getBackStackEntry(destinationId)
        true
    } catch (e: Exception) {
        false
    }

    protected fun showProgress(isShown: Boolean, isCancelable: Boolean = true, isTransaction: Boolean = false) {
        if (isShown) {
            startProgress(isCancelable, isTransaction)
        } else {
            stopProgress()
        }
    }

    private fun startProgress(isCancelable: Boolean, isTransaction: Boolean) {
        try {
            if (mProgressDialog != null) {
                mProgressDialog?.dismiss()
                mProgressDialog = null
            }
            mProgressDialog = ProgressDialog.newInstance(
                isCancelable = isCancelable, hasNavController = true, isTransaction = isTransaction
            )
            mProgressDialog!!.show(childFragmentManager, ProgressDialog.TAG)
            mProgressDialog!!.isCancelable = false
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun stopProgress() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog!!.dismiss()
                mProgressDialog = null
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}