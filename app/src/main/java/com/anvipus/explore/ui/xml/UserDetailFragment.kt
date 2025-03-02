package com.anvipus.explore.ui.xml

import android.content.Context
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.anvipus.core.utils.state.AccountManager
import com.anvipus.explore.R
import com.anvipus.explore.base.BaseFragment
import com.anvipus.explore.databinding.UserDetailFragmentBinding
import com.codedisruptors.dabestofme.di.Injectable
import javax.inject.Inject

class UserDetailFragment : BaseFragment(), Injectable {

    companion object{
        const val TAG:String = "UserDetailFragment"
    }

    override val layoutResource: Int
        get() = R.layout.user_detail_fragment

    override val showToolbar: Boolean
        get() = true

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var mBinding: UserDetailFragmentBinding
    private val params by navArgs<UserDetailFragmentArgs>()

    @Inject
    lateinit var accountManager: AccountManager

    override fun initView(view: View) {
        super.initView(view)
        mBinding = UserDetailFragmentBinding.bind(view)
        with(mBinding){
            data = params.data
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        defaultLifecycleObserver = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                ownTitle(params.data.login!!)
            }
        }
        lifecycle.addObserver(defaultLifecycleObserver)
    }

}