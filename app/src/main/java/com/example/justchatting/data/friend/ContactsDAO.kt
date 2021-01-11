package com.example.justchatting.data.friend

import android.app.Application
import android.database.Cursor
import android.provider.ContactsContract
import com.example.justchatting.data.DTO.Friend
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ContactsDAO {

    fun makeFriendRelationships(application: Application) {
        var uid = FirebaseAuth.getInstance().uid

        val contactList = getContacts(application)
        contactList.forEach { number ->

            FirebaseDatabase.getInstance().getReference("/phone/$number")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {}
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val friendId = snapshot.getValue(String::class.java) ?: return

                        FirebaseDatabase.getInstance().getReference("/friends/${uid}/${friendId}")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {
                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val friend = snapshot.getValue(Friend::class.java)
                                    if (friend == null) {
                                        val fromRef = FirebaseDatabase.getInstance()
                                            .getReference("/friends/${uid}/${friendId}")
                                        fromRef.setValue(
                                            Friend(
                                                true,
                                                ""
                                            )
                                        )

                                        val toRef = FirebaseDatabase.getInstance()
                                            .getReference("/friends/${friendId}/$uid")
                                        toRef.setValue(
                                            Friend(
                                                true,
                                                ""
                                            )
                                        )
                                    }
                                }
                            })
                    }
                })
        }
    }

    private fun getContacts(application: Application): ArrayList<String> {
        val contactList = ArrayList<String>()
        val cr = application.contentResolver
        val cur: Cursor? = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if ((cur?.count ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id: String = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur: Cursor? = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (pCur!!.moveToNext()) {
                        val temp: String =
                            pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        var phoneNo: String = ""
                        temp.forEach {
                            if (it.isDigit())
                                phoneNo += it
                        }
                        contactList.add(phoneNo)
                    }
                    pCur.close()
                }
            }
        }
        cur?.close()
        return contactList
    }
}