package com.example.justchatting.ui.chatting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.justchatting.User
import com.example.justchatting.repository.chatting.SelectGroupRepository
import com.example.justchatting.repository.chatting.SelectGroupRepositoryImpl
import org.koin.core.KoinComponent
import org.koin.core.inject

class SelectGroupViewModel : ViewModel() ,KoinComponent{
    private val selectGroupRepository : SelectGroupRepository by inject()

    fun load(){
        selectGroupRepository.loadFriends()
    }

    fun getFriends(): LiveData<ArrayList<User>> {
        return (selectGroupRepository as SelectGroupRepositoryImpl).friends
    }
}