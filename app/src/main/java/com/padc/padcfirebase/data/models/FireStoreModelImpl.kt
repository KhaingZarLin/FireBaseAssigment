package com.padc.padcfirebase.data.models

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.storage.FirebaseStorage
import com.padc.padcfirebase.data.vos.ArticleVO
import com.padc.padcfirebase.data.vos.CommentVO
import com.padc.padcfirebase.data.vos.UserVO
import com.padc.padcfirebase.utils.REF_KEY_CLAP_COUNT
import com.padc.padcfirebase.utils.REF_KEY_COMMENTS
import com.padc.padcfirebase.utils.REF_PATH_ARTICLES
import com.padc.padcfirebase.utils.STORAGE_FOLDER_PATH
import java.util.*

object FireStoreModelImpl:FirebaseModel {

    const val TAG = "FirebaseModel"

    private val firestore = FirebaseFirestore.getInstance()


    override fun getAllArticles(cleared: LiveData<Unit>): LiveData<List<ArticleVO>> {

        val liveData = MutableLiveData<List<ArticleVO>>()
        val articlesRef = firestore.collection(REF_PATH_ARTICLES)

       val realtimelistener=object:EventListener<QuerySnapshot>
       {
           override fun onEvent(documentSnap: QuerySnapshot?, e: FirebaseFirestoreException?) {
               if(e!=null)
               {
                   Log.w(TAG, "Failed to read value.", e)
                   return
               }
               val articles = ArrayList<ArticleVO>()
               for (snapshot in documentSnap?.documents!!) {
                   val article = snapshot.toObject(ArticleVO::class.java)
                   article?.let {
                       articles.add(article)
                   }
               }
               liveData.value=articles
           }
       }
        val listenerRegister=articlesRef.addSnapshotListener(realtimelistener)

        cleared.observeForever(object : Observer<Unit>{
            override fun onChanged(unit: Unit?) {
                unit?.let {
                    listenerRegister.remove()
                    cleared.removeObserver(this)
                }
            }
        })

        return liveData
    }

    override fun getArticleById(id: String, cleared: LiveData<Unit>): LiveData<ArticleVO> {
        val liveData = MutableLiveData<ArticleVO>()

        val articleRef =firestore.collection(REF_PATH_ARTICLES).document(id)

        // Start real-time data observing
        val listenerRegister = articleRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.d(TAG, "fail", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "data: ${snapshot.data}")
                val article = snapshot.toObject(ArticleVO::class.java)
                article?.also {
                    liveData.value = it
                }

            } else {
                Log.d(TAG, "data=null")
            }
        }


        // Stop real-time data observing when Presenter's onCleared() was called
        cleared.observeForever(object : Observer<Unit>{
            override fun onChanged(unit: Unit?) {
                unit?.let {
                    listenerRegister.remove()
                    cleared.removeObserver(this)
                }
            }
        })

        return liveData
    }

    override fun updateClapCount(count: Int, article: ArticleVO) {
        val articleRef = firestore.collection(REF_PATH_ARTICLES).document(article.id)
       val data= hashMapOf(REF_KEY_CLAP_COUNT to count+article.claps)
        articleRef.set(data, SetOptions.merge())
    }

    override fun addComment(comment: String, pickedImage: Uri?, article: ArticleVO) {
        if (pickedImage != null) {
           uploadImageAndAddComment(comment, pickedImage, article)

        } else {
            val currentUser = UserAuthenticationModelImpl.currentUser!!
            val newComment = CommentVO(
                System.currentTimeMillis().toString(), "", comment, UserVO(
                    currentUser.providerId,
                    currentUser.displayName ?: "",
                    currentUser.photoUrl.toString())
            )
            addComment(newComment, article)
        }
    }

    private fun uploadImageAndAddComment(comment: String, pickedImage: Uri, article: ArticleVO) {
        val storageRef =FirebaseStorage.getInstance().reference
        val imagesFolderRef = storageRef.child(STORAGE_FOLDER_PATH)


        val imageRef = imagesFolderRef.child(
            pickedImage.lastPathSegment ?: System.currentTimeMillis().toString()
        )

        val uploadTask = imageRef.putFile(pickedImage)

        uploadTask.addOnFailureListener{
            Log.e(TAG, it.localizedMessage)
        }
            .addOnSuccessListener {
                // get comment image's url

                imageRef.downloadUrl.addOnCompleteListener {
                    Log.d(TAG, "Image Uploaded ${it.result.toString()}")

                    val currentUser = UserAuthenticationModelImpl.currentUser!!
                    val newComment = CommentVO(
                        System.currentTimeMillis().toString(), it.result.toString(), comment,
                        UserVO(
                            currentUser.providerId,
                            currentUser.displayName ?: "",
                            currentUser.photoUrl.toString())
                    )

                    addComment(newComment, article)
                }

            }
    }

    private fun addComment(comment: CommentVO, article: ArticleVO){
        val commentsRef = firestore.collection(REF_PATH_ARTICLES).document(article.id)

        val key = comment.id

        val addcomment=article.comments.toMutableMap()
        addcomment[key]=comment

        val dataWrap= mapOf(REF_KEY_COMMENTS to addcomment)

        commentsRef.update(dataWrap)
            .addOnSuccessListener {
                Log.d(TAG, "Add Comment")
            }
            .addOnFailureListener {
                Log.e(TAG, "Add Comment error ${it.localizedMessage}")
            }

    }
}