package com.padc.padcfirebase.mvp.presenters

import androidx.lifecycle.ViewModel
import com.padc.padcfirebase.mvp.views.BaseView

/**
 * Created by Ye Pyae Sone Tun
 * on 2019-09-29.
 */

abstract class BasePresenter<T: BaseView> : ViewModel() {

    protected lateinit var mView: T

    open fun initPresenter(view: T){
        this.mView  = view
    }
}