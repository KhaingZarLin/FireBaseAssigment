package com.padc.padcfirebase.data.models

import android.net.Uri
import androidx.lifecycle.LiveData
import com.padc.padcfirebase.data.vos.ArticleVO

interface FirebaseModel {

    fun getAllArticles(cleared: LiveData<Unit>): LiveData<List<ArticleVO>>

    fun getArticleById(id: String, cleared: LiveData<Unit>): LiveData<ArticleVO>

    fun updateClapCount(count: Int, article: ArticleVO)

    fun addComment(comment: String, pickedImage: Uri?, article: ArticleVO)
}