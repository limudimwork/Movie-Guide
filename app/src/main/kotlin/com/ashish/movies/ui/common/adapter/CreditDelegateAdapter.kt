package com.ashish.movies.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ashish.movies.R
import com.ashish.movies.data.models.Credit
import com.ashish.movies.ui.base.recyclerview.BaseContentHolder
import com.ashish.movies.utils.extensions.applyText
import com.ashish.movies.utils.extensions.getOriginalImageUrl
import com.ashish.movies.utils.extensions.getPosterUrl
import com.ashish.movies.utils.extensions.isNotNullOrEmpty

/**
 * Created by Ashish on Jan 03.
 */
class CreditDelegateAdapter(private val layoutId: Int = R.layout.list_item_content_alt,
                            private var onItemClickListener: OnItemClickListener?)
    : ViewTypeDelegateAdapter, RemoveListener {

    override fun onCreateViewHolder(parent: ViewGroup) = CreditHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        (holder as CreditHolder).bindData(item as Credit)
    }

    override fun removeListener() {
        onItemClickListener = null
    }

    inner class CreditHolder(parent: ViewGroup) : BaseContentHolder<Credit>(parent, layoutId) {

        override fun bindData(item: Credit) = with(item) {
            contentTitle.applyText(if (title.isNotNullOrEmpty()) title else name)
            contentSubtitle.applyText(if (job.isNotNullOrEmpty()) job else character)
            itemView.setOnClickListener { onItemClickListener?.onItemClick(adapterPosition, it) }
            super.bindData(item)
        }

        override fun getImageUrl(item: Credit): String? {
            with(item) {
                if (profilePath.isNotNullOrEmpty()) {
                    return profilePath.getOriginalImageUrl()
                } else {
                    return posterPath.getPosterUrl()
                }
            }
        }
    }
}