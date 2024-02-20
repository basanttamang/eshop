package com.basanttamang.e_shop.data


data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val imgPath: String = ""
){
    constructor():this("","","","")
}
