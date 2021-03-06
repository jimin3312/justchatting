package com.example.justchatting.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.repository.auth.AuthRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(
    private val repository: AuthRepository, application: Application
) : AndroidViewModel(application) {

    private val disposables: CompositeDisposable = CompositeDisposable()
    var email: String? = null
    var password: String? = null

    private val _successLogin = MutableLiveData<Boolean>()
    val successLogin: LiveData<Boolean>
        get() = _successLogin

    private val _errorToastMessage = MutableLiveData<String>()
    val errorToastMessage: LiveData<String>
        get() = _errorToastMessage

    fun login() {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            _errorToastMessage.value = "please enter your email and password"
            return
        }
        // loading event here
        disposables.add(
            repository.loginWithEmail(email!!, password!!)
                .andThen(Completable.mergeArray(repository.updateToken(), repository.saveProfileImageToCache(getApplication<Application>().applicationContext)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _successLogin.value = true
                }, {
                    _errorToastMessage.value = "Authentication failed."
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}