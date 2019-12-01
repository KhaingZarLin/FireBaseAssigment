package com.padc.padcfirebase.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.padc.padcfirebase.R
import com.padc.padcfirebase.data.vos.ArticleVO
import com.padc.padcfirebase.delegates.ArticleItemDelegate
import com.padc.padcfirebase.viewholders.ArticleViewHolder

class ArticlesAdapter(private val delegate: ArticleItemDelegate): BaseAdapter<ArticleViewHolder, ArticleVO>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_item_article, parent, false)
        return ArticleViewHolder(itemView, delegate)
    }

}