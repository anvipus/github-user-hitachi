package com.anvipus.explore.ui.xml

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.anvipus.core.R
import com.anvipus.core.models.Status
import com.anvipus.core.utils.SimpleDividerItemDecoration
import com.anvipus.core.utils.closeKeyboard
import com.anvipus.core.utils.hide
import com.anvipus.core.utils.openBottomSheetWithAnimation
import com.anvipus.core.utils.openTimeoutBottomSheet
import com.anvipus.core.utils.show
import com.anvipus.core.utils.showIf
import com.anvipus.explore.base.BaseFragment
import com.anvipus.explore.databinding.MainFragmentBinding
import com.anvipus.explore.databinding.SearchListFragmentBinding
import com.codedisruptors.dabestofme.di.Injectable
import javax.inject.Inject

class SearchListFragment : BaseFragment(), Injectable {

    companion object {
        fun newInstance() = SearchListFragment()
    }

    override val layoutResource: Int
        get() = com.anvipus.explore.R.layout.search_list_fragment

    override val statusBarColor: Int
        get() = R.color.colorAccent

    override val headTitle: Int
        get() = R.string.app_name


    override val showToolbar: Boolean get() = false
    private lateinit var mBinding: SearchListFragmentBinding
    private val params by navArgs<SearchListFragmentArgs>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelMain: MainViewModel by activityViewModels { viewModelFactory }
    private var userName: String? = null

    private var mAdapter: ItemListAdapter? = null

    override fun initView(view: View) {
        super.initView(view)
        mBinding = SearchListFragmentBinding.bind(view)
        with(mBinding) {
            with(viewModelMain){
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
                mBinding.shimmerNotificationAndMessage.stopShimmer()
                mBinding.shimmerNotificationAndMessage.hide()
                mBinding.layoutContent.show()
                mAdapter!!.submitList(params.data.userList)
            }
        }
    }

    override fun setupListener() {
        super.setupListener()
        with(mBinding){
            with(viewModelMain){
                layoutToolbarChild.etSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        with(viewModelMain){
                            if(s.toString().isEmpty().not()){
                                searchUsers(s.toString()).observe(viewLifecycleOwner){
                                    if(it?.status == Status.SUCCESS) {
                                        mBinding.shimmerNotificationAndMessage.stopShimmer()
                                        mBinding.shimmerNotificationAndMessage.hide()
                                        mBinding.layoutContent.show()
                                        mAdapter!!.submitList(it.data?.items)
                                        showNoData(it.data?.items?.size == 0)
                                    }
                                }
                            }
                        }

                    }
                })
                layoutToolbarChild.toolbar.setNavigationOnClickListener {
                    navController().navigateUp()
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

    override fun initObserver() {
        super.initObserver()
        with(viewModelMain) {
            detalUsersResult.observe(viewLifecycleOwner){
                showProgress(isShown = it?.status == Status.LOADING, isCancelable = false)
                if (it?.status == Status.SUCCESS) {
                    if (navController().currentDestination?.id == com.anvipus.explore.R.id.fragment_search) {
                        removeDetalUsersObserve()
                        navigate(SearchListFragmentDirections.actionToDetailCompose(it.data!!))
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
        }
    }

}