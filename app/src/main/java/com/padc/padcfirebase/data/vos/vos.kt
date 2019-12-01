package com.padc.padcfirebase.data.vos

data class ArticleVO(
    var id: String = "",
    var img: String = "",
    var title: String= "",
    var body: String= "",
    var claps: Int = 0,
    var comments: Map<String,CommentVO> = emptyMap(),
    var comments_count: Int = 0,
    var date: String= ""
)

data class CommentVO(
    var id: String= "",
    var img: String= "",
    var text: String= "",
    var user: UserVO? = null
)

data class UserVO(
    var id: String= "",
    var name: String= "",
    var profile: String= ""
)