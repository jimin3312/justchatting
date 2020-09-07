package com.example.justchatting.repository.chattingRoom

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChattingRoomRepositoryImpl : ChattingRoomRepository {

    override fun alreadyHaveChattingRoom() {
        val ref = FirebaseDatabase.getInstance().getReference("/chatroom_members")
        ref.addListenerForSingleValueEvent(object  : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { chatRoomMembers->

                }
            }
        })
    }


}