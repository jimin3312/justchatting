package com.example.justchatting.repository.chattingRoom

import com.example.justchatting.UserModel
import com.google.firebase.auth.FirebaseAuth

class TokenParser {

    fun parser(groupMembers : HashMap<String,UserModel>?) : List<String>{
        var tokens = mutableListOf<String>()
        groupMembers!!.remove(FirebaseAuth.getInstance().uid)
        groupMembers!!.forEach {
                tokens.add(it.value.token)
        }
        return tokens
    }
}