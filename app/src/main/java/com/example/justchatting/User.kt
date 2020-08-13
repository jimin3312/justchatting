package com.example.justchatting

class User(val uid: String, val username : String, val phoneNumber: String, val profileImageUrl : String?) {
    constructor(): this("","","","")
}