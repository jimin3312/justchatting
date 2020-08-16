package com.example.justchatting.ui.login

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.justchatting.repository.login.UserRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginVIewModel(
    private val repository: UserRepository
) : ViewModel() {

    var email: String? = null
    var password: String? = null
    private val _successLogin = MutableLiveData<Boolean>()
    val successLogin: LiveData<Boolean>
        get() = _successLogin

    lateinit var disposable:Disposable

    fun login() {
        if(email.isNullOrEmpty() || password.isNullOrEmpty()){
            //pop up event for checking email text or password text
            return
        }

        // loading action here
        disposable = repository.loginWithEmail(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _successLogin.value = true
            }, {
                // error event here
            })

    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}