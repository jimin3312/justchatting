package com.example.justchatting.data.chattingRoom

import com.example.justchatting.data.DTO.UserModel
import com.google.firebase.auth.FirebaseAuth

class TokenParser {

    fun parser(groupMembers : HashMap<String, UserModel>) : List<String>{
        var tokens = mutableListOf<String>()
        groupMembers.forEach {
            if(it.key != FirebaseAuth.getInstance().uid)
                tokens.add(it.value.token)
        }
        return tokens
    }
}