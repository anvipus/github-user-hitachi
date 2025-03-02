package com.anvipus.explore.ui.xml

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.anvipus.core.R
import com.anvipus.core.models.Status
import com.anvipus.core.models.Users
import com.anvipus.core.models.UsersListData
import com.anvipus.core.utils.OnLoadMoreListener
import com.anvipus.core.utils.SimpleDividerItemDecoration
import com.anvipus.core.utils.hide
import com.anvipus.core.utils.openBottomSheetWithAnimation
import com.anvipus.core.utils.openTimeoutBottomSheet
import com.anvipus.core.utils.show
import com.anvipus.core.utils.showIf
import com.anvipus.explore.base.BaseFragment
import com.anvipus.explore.databinding.MainFragmentBinding
import com.bumptech.glide.Glide
import com.codedisruptors.dabestofme.di.Injectable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

    private var userName: String? = null
    private var nextId = 0
    private val listUsersCopy: MutableList<Users> = ArrayList()
    private var mAdapter: ItemListAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        defaultLifecycleObserver = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                ownTitle(getString(R.string.app_name))
            }
        }
        lifecycle.addObserver(defaultLifecycleObserver)
    }

    override fun initView(view: View) {
        super.initView(view)
        mBinding = MainFragmentBinding.bind(view)
        ownIcon(null)
        ownTitle(getString(R.string.app_name))
        with(mBinding){
            with(viewModelMain){
                listUsersCopy.clear()
                rvUser.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                mAdapter = ItemListAdapter(requireContext(),rvUser, itemClickCallback = {data ->
                    userName = data.login!!
                    getDetailUsers(data.login!!)
                })
                val divider = SimpleDividerItemDecoration(requireContext())
                rvUser.apply {
                    adapter = mAdapter
                    setHasFixedSize(true)
                    addItemDecoration(divider)
                }
                getListUsers(0)
                ownMenu(com.anvipus.explore.R.menu.search_menu) {
                    if (it.itemId == com.anvipus.explore.R.id.searchItem) {
                        val userListData = UsersListData(userList = listUsersCopy)
                        navigate(MainFragmentDirections.actionToSearch(userListData))
                    }
                }
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        with(viewModelMain){
            mBinding.shimmerNotificationAndMessage.show()
            mBinding.shimmerNotificationAndMessage.startShimmer()
            nextId = 0
            detalUsersResult.observe(viewLifecycleOwner){
                showProgress(isShown = it?.status == Status.LOADING, isCancelable = false)
                if (it?.status == Status.SUCCESS) {
                    if (navController().currentDestination?.id == com.anvipus.explore.R.id.fragment_main) {
                        removeDetalUsersObserve()
                        navigate(MainFragmentDirections.actionToDetailCompose(it.data!!))
                    }

                }
                if (it?.status == Status.ERROR) {
                    openBottomSheetWithAnimation(
                        title = getString(com.anvipus.explore.R.string.lbl_perhatian),
                        message = it.msg.toString()
                    ) {}
                }
                if (it?.status == Status.TIMEOUT) {
                    openTimeoutBottomSheet {
                        getDetailUsers(userName!!)
                    }
                }
            }
            listUsersResult.observe(viewLifecycleOwner) {
                if (it?.status == Status.SUCCESS && it.data != null && it.data!!.isNotEmpty()) {
                    mBinding.shimmerNotificationAndMessage.stopShimmer()
                    mBinding.shimmerNotificationAndMessage.hide()
                    mBinding.layoutContent.show()
                    if (nextId == 0) {
                        listUsersCopy.clear()
                        mAdapter!!.notifyDataSetChanged()
                    }

                    listUsersCopy.addAll(it.data!!)
                    nextId = listUsersCopy.get(listUsersCopy.size-1).id!!.toInt()+1
                    mAdapter!!.submitList(listUsersCopy) {
                        showNoData(listUsersCopy.size == 0)
                        if (nextId > 1) {
                            mAdapter!!.notifyDataSetChanged()
                        }
                    }
                }
                if (it?.status == Status.SUCCESS  && it.data ==null) {
                    mBinding.shimmerNotificationAndMessage.stopShimmer()
                    mBinding.shimmerNotificationAndMessage.hide()
                    showNoData(listUsersCopy.size == 0)
                    mBinding.layoutContent.hide(1)
                }
                if (it?.status == Status.SUCCESS && it.data != null  && it.data!!.isEmpty()) {
                    mBinding.shimmerNotificationAndMessage.stopShimmer()
                    mBinding.shimmerNotificationAndMessage.hide()
                    showNoData(listUsersCopy.size == 0)
                    mBinding.layoutContent.hide(1)
                }
                if (it?.status == Status.ERROR) {
                    showNoData(listUsersCopy.size == 0)
                    mBinding.shimmerNotificationAndMessage.stopShimmer()
                    mBinding.shimmerNotificationAndMessage.hide()
                }
                if (it?.status == Status.TIMEOUT) {
                    mBinding.progressBar.visibility = View.GONE
                }
                if (it?.status != Status.LOADING) {
                    mBinding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun showNoData(isShown: Boolean) {
        with(mBinding) {
            if(isShown){
                layoutContent.hide(1)
            }
            layoutNoData.layoutParentNoData.showIf(isShown)
            layoutNoData.tvErrorNoDataTitle.text =
                getString(com.anvipus.explore.R.string.history_error_no_data_title)
            layoutNoData.tvErrorNoDataDescription.text =
                getString(com.anvipus.explore.R.string.message_no_data_default)
        }

    }

}