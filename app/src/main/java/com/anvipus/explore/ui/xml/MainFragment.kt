package com.anvipus.explore.ui.xml

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.anvipus.core.R
import com.anvipus.explore.base.BaseFragment
import com.anvipus.explore.databinding.MainFragmentBinding
import com.bumptech.glide.Glide
import com.codedisruptors.dabestofme.di.Injectable
import javax.inject.Inject

class MainFragment : BaseFragment(), Injectable {

    companion object {
        fun newInstance() = MainFragment()
    }

    override val layoutResource: Int
        get() = com.anvipus.explore.R.layout.main_fragment

    override val statusBarColor: Int
        get() = R.color.colorAccent

    override val headTitle: Int
        get() = R.string.app_name

    override val showToolbar: Boolean get() = true
    private lateinit var mBinding: MainFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelMain: MainViewModel by activityViewModels { viewModelFactory }

    override fun initView(view: View) {
        super.initView(view)
        mBinding = MainFragmentBinding.bind(view)
        ownIcon(null)
        ownTitle(getString(R.string.app_name))
        with(mBinding) {
            with(viewModelMain){
                val linearLayoutManager = LinearLayoutManager(
                    requireContext(), LinearLayoutManager.HORIZONTAL, false
                )
                with(rvUser){
                    layoutManager = linearLayoutManager

                    clipToPadding = false
                }

            }
            ownMenu(com.anvipus.explore.R.menu.search_menu) {
                if (it.itemId == com.anvipus.explore.R.id.searchItem) {
                    //navigate(MainFragmentDirections.actionToSearch())
                }
            }
        }
    }


}