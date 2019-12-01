package com.padc.padcfirebase.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.nfc.NfcAdapter.EXTRA_ID
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.padc.padcfirebase.R
import com.padc.padcfirebase.adapters.CommentsAdapter
import com.padc.padcfirebase.data.vos.ArticleVO
import com.padc.padcfirebase.mvp.presenters.ArticleDetailPresenter
import com.padc.padcfirebase.mvp.views.ArticleDetailView
import com.padc.padcfirebase.utils.ScreenUtils
import com.wajahatkarim3.clapfab.ClapFAB
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity(), ArticleDetailView {

    private lateinit var presenter: ArticleDetailPresenter
    private lateinit var commentsAdapter: CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupPresenter()
        setupListener()
        setupRecyclerView()
        val id = intent.getStringExtra(EXTRA_ID)!!
        presenter.onUIReady(this,id)
    }

    override fun navigateToGoogleSignInScreen(signInIntent: Intent, rcGoogleSign: Int) {
        startActivityForResult(signInIntent, rcGoogleSign)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != RC_PICK_IMAGE) {
            presenter.onActivityResult(requestCode, resultCode, data, this)
        }else {

            if (resultCode == RESULT_OK && data != null && data.data != null) {
                val uri = data.data

                presenter.onImagePicked(uri!!)
            }
        }

    }

    override fun showPickedImage(uri: Uri) {
        ivImagePick.setImageURI(uri)
    }

    override fun showCommentInputView() {
        btnComment.visibility = View.GONE
        etComment.requestFocus()
    }

    override fun showGoogleLoginError(message: String) {
        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show()
        etComment.clearFocus()
    }

    override fun showGoogleLoginSuccess(user: FirebaseUser) {
       showCommentInputView()
    }

    override fun showArticle(data: ArticleVO) {
        container.visibility = VISIBLE

        Glide.with(this)
            .load(data.img)
            .into(ivArticle)
        tvTitle.text = data.title

        if (data.claps > 0) {
            tvClaps.text = data.claps.toString()
        } else {
            tvClaps.text = ""
        }

        if (data.comments_count > 0) {
            tvComments.text = data.comments_count.toString()
        } else {
            tvComments.text = ""
        }

        tvDate.text = data.date
        tvBody.text = data.body

        commentsAdapter.setNewData(data.comments.values.sortedBy { it.id }.toMutableList())

        etComment.text = null
        etComment.clearFocus()
        ivImagePick.setImageResource(R.drawable.ic_image_black_24dp)

        ScreenUtils.hideSoftKeyboard(this, etComment)
    }



    private fun setupPresenter() {
        presenter = ViewModelProviders.of(this).get(ArticleDetailPresenter::class.java)
        presenter.initPresenter(this)
    }

    private fun setupListener() {
        fab.clapListener = object : ClapFAB.OnClapListener {
            override fun onFabClapped(clapFab: ClapFAB, count: Int, isMaxReached: Boolean) {
                presenter.onClapped(count)
            }
        }

        btnComment.setOnClickListener {
            presenter.onCommentClicked(this)
        }

        ivSend.setOnClickListener {
            presenter.onCommentSendClicked(etComment.text.toString())
        }

        ivImagePick.setOnClickListener {
            val intent = Intent()
            // Show only images, no videos or anything else
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            // Always show the chooser (if there are multiple options available)
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                RC_PICK_IMAGE
            )

        }
    }

    private fun setupRecyclerView() {
        commentsAdapter = CommentsAdapter()
        rvComments.adapter = commentsAdapter
        rvComments.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
    }

    companion object {

        private const val EXTRA_ID = "article_id"
        private const val RC_PICK_IMAGE = 100

        fun newIntent(context: Context, id: String): Intent =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(EXTRA_ID, id)
            }
    }

}
