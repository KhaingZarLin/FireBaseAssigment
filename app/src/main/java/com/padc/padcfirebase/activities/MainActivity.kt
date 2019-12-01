package com.padc.padcfirebase.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View.NO_ID
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.padc.padcfirebase.R
import com.padc.padcfirebase.adapters.ArticlesAdapter
import com.padc.padcfirebase.data.vos.ArticleVO
import com.padc.padcfirebase.mvp.presenters.ArticlesPresenter
import com.padc.padcfirebase.mvp.views.ArticlesView

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ArticlesView {

    private lateinit var adapter: ArticlesAdapter
    private lateinit var presenter: ArticlesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        super.onCreate(savedInstanceState)
        checkIntentForDetailActivity()
        setContentView(R.layout.activity_main)
        setupPresenter()
        setSupportActionBar(toolbar)
        setupRecyclerView()
        setupListeners()
        checkGooglePlayService()
        presenter.onUIReady(this)
    }

    override fun onResume() {
        super.onResume()
        checkGooglePlayService()
    }


    private fun checkIntentForDetailActivity() {
        val detailId = intent.getStringExtra(EXTRA_ARTICLE_ID)
        if ( detailId != null) {
            navigateToDetail(detailId)
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun showLoginUser(user: FirebaseUser) {
            Glide.with(this)
                .load(user.photoUrl)
                .into(ivCurrentUser)
    }

    override fun navigateToGoogleSignInScreen(signInIntent: Intent, rcGoogleSign: Int) {
        startActivityForResult(signInIntent, rcGoogleSign)
    }

    override fun showGoogleLoginError(message: String) {
        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show()
    }

    override fun showGoogleLoginSuccess(user: FirebaseUser) {
        showLoginUser(user)
    }

    override fun showLogoutUser() {
        ivCurrentUser.setImageResource(R.drawable.ic_account_circle_black_24dp)
    }

    override fun navigateToDetail(id: String) {
        startActivity(DetailActivity.newIntent(this, id))
    }

    override fun showArticles(data: List<ArticleVO>) {
        Log.d("Firebase", data.toString())
        adapter.setNewData(data.toMutableList())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onActivityResult(requestCode, resultCode, data, this)
    }

    private fun setupRecyclerView(){
        recyclerArticles.setHasFixedSize(true)
        adapter = ArticlesAdapter(presenter)
        recyclerArticles.adapter = adapter
    }

    private fun setupPresenter(){
        presenter = ViewModelProviders.of(this).get(ArticlesPresenter::class.java).apply {
            initPresenter(this@MainActivity)
        }
    }

    private fun setupListeners() {
        ivCurrentUser.setOnClickListener {
            presenter.onUserProfileClicked(this)
        }
    }

    private fun checkGooglePlayService(){
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
            GoogleApiAvailability().makeGooglePlayServicesAvailable(this)
        }
    }

    companion object {
        const val EXTRA_ARTICLE_ID = "id"

        fun getNewIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
