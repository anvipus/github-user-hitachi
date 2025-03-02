package com.anvipus.explore.ui.xml

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.anvipus.explore.databinding.MainActivityBinding
import com.anvipus.explore.ui.xml.MainFragment
import com.anvipus.explore.ui.xml.MainViewModel
import com.anvipus.core.utils.closeKeyboard
import com.anvipus.core.utils.hide
import com.anvipus.core.utils.openBottomSheetWithAnimation
import com.anvipus.core.utils.show
import com.anvipus.core.utils.resDrawable
import com.anvipus.explore.R
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import java.util.Arrays
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {

    companion object {
        private const val REQUEST_CODE_ASK_PERMISSIONS = 1

        private val REQUIRED_SDK_PERMISSIONS = arrayOf(
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE
        )

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private val REQUIRED_SDK_PERMISSIONS_33 = arrayOf(
            Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.ACCESS_NETWORK_STATE
        )
    }

    private lateinit var mDataBinding: MainActivityBinding
    // Required for Dagger AndroidInject
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mainViewModel: MainViewModel by viewModels { viewModelFactory }

    lateinit var layoutToolbar: ConstraintLayout
    lateinit var toolbar: Toolbar
    lateinit var toolbarTitle: TextView
    lateinit var toolbarLogo: ImageView
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var alertShown: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        layoutToolbar = mDataBinding.layoutToolbar.toolbarParent
        toolbar = mDataBinding.layoutToolbar.toolbar
        toolbarTitle = mDataBinding.layoutToolbar.tvTitle
        toolbarLogo = mDataBinding.layoutToolbar.ivLogoSpin

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        toolbar.setNavigationOnClickListener {
            closeKeyboard()
            navController.navigateUp() || super.onSupportNavigateUp()
        }
        navController.addOnDestinationChangedListener { _, _, _ ->
            toolbar.navigationIcon = resDrawable(com.anvipus.core.R.drawable.ic_back_arrow_dark)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermissions33()
        } else {
            checkPermissions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissions33() {
        try {
            val missingPermissions = ArrayList<String>()
            // check all required dynamic permissions
            for (permission in REQUIRED_SDK_PERMISSIONS_33) {
                val result = ContextCompat.checkSelfPermission(this, permission)
                if (result != PackageManager.PERMISSION_GRANTED) {
                    missingPermissions.add(permission)
                }
            }

            if (missingPermissions.isNotEmpty()) {
                // request all missing permissions
                if (alertShown) {
                    val permissions = missingPermissions.toTypedArray()
                    ActivityCompat.requestPermissions(
                        this, permissions, REQUEST_CODE_ASK_PERMISSIONS
                    )
                } else {
                    openBottomSheetWithAnimation(
                        animationId = com.anvipus.core.R.raw.pending,
                        title = getString(R.string.label_warning),
                        message = getString(R.string.label_setting_permission),
                        textButton = getString(R.string.action_ok_bottom_sheet),
                        isCancelable = false
                    ) {}
                    alertShown = false
                }
            } else {
                val grantResults = IntArray(REQUIRED_SDK_PERMISSIONS_33.size)
                Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED)
                onRequestPermissionsResult(
                    REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS_33, grantResults
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkPermissions() {
        try {
            val missingPermissions = ArrayList<String>()
            // check all required dynamic permissions
            for (permission in REQUIRED_SDK_PERMISSIONS) {
                val result = ContextCompat.checkSelfPermission(this, permission)
                if (result != PackageManager.PERMISSION_GRANTED) {
                    missingPermissions.add(permission)
                }
            }

            if (missingPermissions.isNotEmpty()) {
                // request all missing permissions
                if (alertShown) {
                    val permissions = missingPermissions.toTypedArray()
                    ActivityCompat.requestPermissions(
                        this, permissions, REQUEST_CODE_ASK_PERMISSIONS
                    )
                } else {
                    openBottomSheetWithAnimation(
                        animationId = com.anvipus.core.R.raw.pending,
                        title = getString(R.string.label_warning),
                        message = getString(R.string.label_setting_permission),
                        textButton = getString(R.string.action_ok_bottom_sheet),
                        isCancelable = false
                    ) {}
                    alertShown = false
                }
            } else {
                val grantResults = IntArray(REQUIRED_SDK_PERMISSIONS.size)
                Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED)
                onRequestPermissionsResult(
                    REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS, grantResults
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setTitleToolbar(title: String) {
        with(mDataBinding){
            layoutToolbar.tvTitle.show()
            layoutToolbar.ivLogoSpin.hide()
            layoutToolbar.lineToolbar.show()
            layoutToolbar.tvTitle.text = title
        }
    }

    fun hideTitleToolbar() {
        with(mDataBinding){
            layoutToolbar.tvTitle.hide()
            layoutToolbar.ivLogoSpin.show()
            layoutToolbar.lineToolbar.hide()

        }

    }


}