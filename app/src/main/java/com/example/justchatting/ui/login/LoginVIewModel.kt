package com.example.justchatting.ui.login

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.justchatting.User
import com.example.justchatting.repository.login.UserRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginVIewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val disposables: CompositeDisposable = CompositeDisposable()
    var email: String? = null
    var password: String? = null
    var name: String? = null
    var phoneNumber: String? = null
    var selectedPhotoUri: Uri? = null

    private val _successLogin = MutableLiveData<Boolean>()
    val successLogin: LiveData<Boolean>
        get() = _successLogin

    private val _selectingPhoto = MutableLiveData<Boolean>()
    val selectingPhoto: LiveData<Boolean>
        get() = _selectingPhoto

    private val _successSignUp = MutableLiveData<Boolean>()
    val successSignUp: LiveData<Boolean>
        get() = _successSignUp


    fun login() {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            //failure event here
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
                    // error event here
                })
        )
    }

    fun selectPhoto() {
        _selectingPhoto.value = true;
    }

    fun signUp() {
        if(email.isNullOrEmpty() || password.isNullOrEmpty() || phoneNumber.isNullOrEmpty())
        {
            //failure event here
            return
        }

        // loading event here
        val uploadImage: Completable = repository.uploadProfile(selectedPhotoUri)
        val signUpWithEmail: Completable = repository.signUpWithEmail(email!!, password!!)
        val saveUserToDB: Completable = repository.saveUser(name!!, phoneNumber!!, selectedPhotoUri)

        disposables.add(signUpWithEmail
            .andThen(uploadImage)
            .andThen(saveUserToDB)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _successSignUp.value = true
            },{
                // error event here
            }))
    }


    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}