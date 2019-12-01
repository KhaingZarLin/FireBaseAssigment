package com.padc.padcfirebase.mvp.presenters

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.padc.padcfirebase.data.models.*
import com.padc.padcfirebase.data.vos.ArticleVO
import com.padc.padcfirebase.delegates.ArticleItemDelegate
import com.padc.padcfirebase.mvp.views.ArticlesView
import java.util.*

class ArticlesPresenter: BaseGoogleSignInPresenter<ArticlesView>(), ArticleItemDelegate {

    private val model : FirebaseModel = FireStoreModelImpl
    private val userModel : UserAuthenticationModel = UserAuthenticationModelImpl
    private val clearedLiveData = MutableLiveData<Unit>()

    fun onUIReady(owner: LifecycleOwner){
        model.getAllArticles(clearedLiveData).observe(owner, Observer {
            mView.showArticles(it)
        })
    }

    override fun onArticleItemClicked(data: ArticleVO) {
        mView.navigateToDetail(data.id)
    }

    override fun onCleared() {
        clearedLiveData.value = Unit
        super.onCleared()
    }

    fun onStart() {
        userModel.currentUser?.let {
            mView.showLoginUser(it)
        } ?: mView.showLogoutUser()
    }

    fun onUserProfileClicked(context: Context) {
        if (userModel.isLoginUser()){
            userModel.logOut()
            onStart()

        } else {
            googleSignIn(context)
        }
    }
}