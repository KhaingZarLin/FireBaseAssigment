package com.padc.padcfirebase.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.padc.padcfirebase.R
import com.padc.padcfirebase.data.vos.CommentVO
import com.padc.padcfirebase.viewholders.CommentViewHolder

class CommentsAdapter: BaseAdapter<CommentViewHolder, CommentVO>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.viewholder_comment, parent, false
        )

        return CommentViewHolder(view)
    }
}