package com.example.justchatting.ui.login

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.justchatting.repository.login.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val disposables: CompositeDisposable = CompositeDisposable()
    var email: String? = null
    var password: String? = null
    var name: String? = null
    var phoneNumber: String? = null
    var selectedPhotoUri : Uri? = null

    private val _successLogin = MutableLiveData<Boolean>()
    val successLogin: LiveData<Boolean>
        get() = _successLogin

    private val _selectingPhoto = MutableLiveData<Boolean>()
    val selectingPhoto: LiveData<Boolean>
        get() = _selectingPhoto

    private val _successSignUp = MutableLiveData<Boolean>()
    val successSignUp: LiveData<Boolean>
        get() = _successSignUp


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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _successLogin.value = true
                }, {
                    _errorToastMessage.value = "Authentication failed."
                })
        )
    }

    fun selectPhoto() {
        _selectingPhoto.value = true;
    }

    fun signUp() {
        if (name.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty() || phoneNumber.isNullOrEmpty()) {
            _errorToastMessage.value = "please enter your username, email, password, phone number"
            return
        }

        val uploadImage: Single<String> = repository.uploadProfile(selectedPhotoUri)
        val signUpWithEmail: Completable = repository.signUpWithEmail(email!!, password!!)

        disposables.add(signUpWithEmail
            .andThen(uploadImage
                .flatMap { imagePath ->
                    repository.saveUser(name!!, phoneNumber!!, imagePath, email!!)
                })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _successSignUp.value = true
            }, {
                _errorToastMessage.value = it.message
            })
        )
    }


    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}