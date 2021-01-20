package com.example.justchatting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.justchatting.di.loginModule
import com.example.justchatting.di.viewModelModule
import com.example.justchatting.repository.auth.AuthRepository
import com.example.justchatting.ui.login.RegisterViewModel
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.mockito.Mockito
import org.mockito.Mockito.mock

class RegisterViewModelTest : KoinTest {

    val registerViewModel: RegisterViewModel by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(viewModelModule, loginModule)
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mock(clazz.java)
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun 회원가입_성공() {
        registerViewModel.email = "tom@gamil.com"
        registerViewModel.name = "tom"
        registerViewModel.password = "123123"
        registerViewModel.phoneNumber = "010"

        val mockRepository = mock(AuthRepository::class.java)

        Mockito.`when`(
            mockRepository.signUpWithEmail(
                registerViewModel.email!!,
                registerViewModel.password!!
            )
        ).thenReturn(Completable.complete())

        Mockito.`when`(
            mockRepository.uploadProfile(
                registerViewModel.selectedPhotoUri
            )
        ).thenReturn(Single.just(""))

        Mockito.`when`(
            mockRepository.saveUser(
                registerViewModel.name!!,
                registerViewModel.phoneNumber!!,
                "",
                registerViewModel.email!!
            )
        ).thenReturn(Single.just(true))

        mockRepository.signUpWithEmail(
            registerViewModel.email!!,
            registerViewModel.password!!
        ).andThen(mockRepository. uploadProfile(
            registerViewModel.selectedPhotoUri
        )).flatMap { imagePath ->
            mockRepository.saveUser(
                registerViewModel.name!!,
                registerViewModel.phoneNumber!!,
                imagePath,
                registerViewModel.email!!
            )
        }.doOnSuccess { _ ->
            registerViewModel.successSignUp.value = true
        }.test()

        Assert.assertEquals(registerViewModel.successSignUp.value, true)
    }
}

//@Test
//fun 회원가입_실패() {
//
//}
