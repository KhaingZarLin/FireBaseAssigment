package com.padc.padcfirebase.viewholders

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.bumptech.glide.Glide
import com.padc.padcfirebase.data.vos.CommentVO
import com.padc.padcfirebase.utils.getPostedAgoTimeWithTimeStamp
import kotlinx.android.synthetic.main.viewholder_comment.view.*

class CommentViewHolder(itemView: View): BaseViewHolder<CommentVO>(itemView) {

    private val ivUser = itemView.ivCurrentUser
    private val tvUser = itemView.tvUserName
    private val tvAgo = itemView.tvAgo
    private val tvComment = itemView.tvComment
    private val ivComment = itemView.ivComment

    override fun bindData(data: CommentVO) {
        Glide.with(itemView.context)
            .load(data.user?.profile)
            .into(ivUser)
        tvUser.text = data.user?.name
        tvAgo.text = getPostedAgoTimeWithTimeStamp(data.id)
        tvComment.text = data.text

        if (data.img.isNotEmpty()){
            ivComment.visibility = VISIBLE
            Glide.with(itemView.context)
                .load(data.img)
                .into(ivComment)
        }else {
            ivComment.visibility = GONE
        }
    }
}