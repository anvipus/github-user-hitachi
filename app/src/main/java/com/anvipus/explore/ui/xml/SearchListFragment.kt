package com.anvipus.explore.ui.xml

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.anvipus.core.R
import com.anvipus.core.models.Status
import com.anvipus.core.utils.closeKeyboard
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

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelMain: MainViewModel by activityViewModels { viewModelFactory }

    override fun initView(view: View) {
        super.initView(view)
        mBinding = SearchListFragmentBinding.bind(view)
        /*with(mBinding) {
            with(viewModelMain){
                with(rvUser){
                    layoutManager =  GridLayoutManager(context, 2)
                    //adapter = mAdapter2
                    clipToPadding = false
                }
            }
        }*/
    }

    override fun setupListener() {
        super.setupListener()
        with(mBinding){

            /*layoutToolbarChild.etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    with(viewModelMain){
                        if(s.toString().isEmpty().not()){

                        }else{

                        }
                    }

                }
            })
            layoutToolbarChild.toolbar.setNavigationOnClickListener {
                navController().navigateUp()
            }*/
        }
    }

}