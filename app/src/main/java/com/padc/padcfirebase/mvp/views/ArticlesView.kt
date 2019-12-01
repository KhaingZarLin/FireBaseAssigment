package com.padc.padcfirebase.mvp.views

import com.google.firebase.auth.FirebaseUser
import com.padc.padcfirebase.data.vos.ArticleVO

interface ArticlesView: BaseGoogleSignInView {

    fun navigateToDetail(id: String)
    fun showArticles(data: List<ArticleVO>)
    fun showLoginUser(user: FirebaseUser)
    fun showLogoutUser()
}