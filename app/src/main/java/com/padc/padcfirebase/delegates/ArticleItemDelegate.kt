package com.padc.padcfirebase.delegates

import com.padc.padcfirebase.data.vos.ArticleVO

interface ArticleItemDelegate {

    fun onArticleItemClicked(data: ArticleVO)
}