package com.padc.padcfirebase.mvp.views

import android.content.Intent
import com.google.firebase.auth.FirebaseUser

interface BaseGoogleSignInView: BaseView {

    fun navigateToGoogleSignInScreen(signInIntent: Intent, rcGoogleSign: Int)
    fun showGoogleLoginError(message: String)
    fun showGoogleLoginSuccess(user: FirebaseUser)
}