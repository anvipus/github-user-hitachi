package com.anvipus.explore.ui.xml

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anvipus.core.models.Users
import com.anvipus.core.utils.OnLoadMoreListener
import com.anvipus.explore.R
import com.anvipus.explore.databinding.ItemListBinding

class ItemListAdapter (
    private val mContext: Context, recyclerView: RecyclerView,
    private val itemClickCallback: (Users) -> Unit
) : ListAdapter<Users, RecyclerView.ViewHolder>(COMPARATOR) {
    private var mOnLoadMoreListener: OnLoadMoreListener? = null
    private var isLoading: Boolean = false
    private val visibleThreshold = 1
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var previousTotalItemCount = 0
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    init {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayoutManager.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()

                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener!!.onLoadMore()
                    }
                    isLoading = true
                }

            }
        })
    }

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener?) {
        this.mOnLoadMoreListener = mOnLoadMoreListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val binding = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list, parent, false)
                ViewHolder(ItemListBinding.bind(binding))
            }
            VIEW_TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_loading_item, parent, false)
                LoadingViewHolder(view)
            }
            else -> {
                val binding = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list, parent, false)
                ViewHolder(ItemListBinding.bind(binding))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val item = getItem(position)
            holder.binding.run {
                data = item
            }
            holder.itemView.setOnClickListener {
                itemClickCallback.invoke(item)
            }
        } else if (holder is LoadingViewHolder) {
            holder.progressBar.isIndeterminate = true
        }
    }

    class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

    class LoadingViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var progressBar: ProgressBar = v.findViewById(R.id.progressBar1)
    }

    fun setLoaded() {
        isLoading = false
    }

    companion object {
        const val TAG: String = "LoyaltyHistoryAdapter"

        val COMPARATOR = object : DiffUtil.ItemCallback<Users>() {
            override fun areItemsTheSame(
                oldItem: Users,
                newItem: Users
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Users,
                newItem: Users
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}