package com.example.justchatting.ui.chatting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.justchatting.Event
import com.example.justchatting.data.DTO.UserModel
import com.example.justchatting.repository.chatting.SelectGroupRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class SelectGroupViewModel : ViewModel() ,KoinComponent{

    private val selectGroupRepository : SelectGroupRepository by inject()
    val alreadyEnteredMember : HashMap<String, UserModel> = HashMap()

    fun loadFriends(){
        selectGroupRepository.loadFriends(alreadyEnteredMember)
    }
    fun getFriends(): LiveData<ArrayList<UserModel>> {
        return selectGroupRepository.getFriends()
    }

    fun loadGroupId(groupMembers : HashMap<String, UserModel?>){
        selectGroupRepository.loadGroupId(groupMembers)
    }
    fun getGroupId() : LiveData<Event<String>>{
        return selectGroupRepository.getGroupId()
    }

    fun addAlreadyEnteredMember(member: java.util.HashMap<String, UserModel>?) {
        member?: return
        alreadyEnteredMember.putAll( member)
    }

}