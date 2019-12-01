package com.padc.padcfirebase.data.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

object UserAuthenticationModelImpl: UserAuthenticationModel {

    const val TAG = "UserAuthModel"

    private val auth = FirebaseAuth.getInstance()

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override fun isLoginUser(): Boolean {
        return currentUser != null
    }

    override fun firebaseAuthWithGoogle(
        account: GoogleSignInAccount,
        onError: (String) -> Unit
    ): LiveData<FirebaseUser> {
        val liveData = MutableLiveData<FirebaseUser>()
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.id!!)

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    liveData.value = user
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    onError(task.exception?.message ?: "")
                }

            }

        return liveData
    }

    override fun logOut() {
       auth.signOut()
    }

}