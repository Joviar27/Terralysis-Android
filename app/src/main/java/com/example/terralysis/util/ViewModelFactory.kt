package com.example.terralysis.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.terralysis.data.di.Injection
import com.example.terralysis.data.repository.AuthRepository
import com.example.terralysis.data.repository.ScanRepository
import com.example.terralysis.ui.camera.CameraViewModel
import com.example.terralysis.ui.history.HistoryViewModel
import com.example.terralysis.ui.home.HomeViewModel
import com.example.terralysis.ui.profile.ProfileViewModel
import com.example.terralysis.ui.authorization.signin.SignInViewModel
import com.example.terralysis.ui.authorization.signup.SignUpViewModel
import com.example.terralysis.ui.authorization.splashscreen.SplashScreenViewModel

class ViewModelFactory (
    private val scanRepository: ScanRepository,
    private val authRepository: AuthRepository
):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> SignInViewModel(authRepository) as T
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> SignUpViewModel(authRepository) as T
            modelClass.isAssignableFrom(SplashScreenViewModel::class.java) -> SplashScreenViewModel(authRepository) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(authRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(authRepository) as T
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> HistoryViewModel(scanRepository, authRepository) as T
            modelClass.isAssignableFrom(CameraViewModel::class.java) -> CameraViewModel(scanRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(Injection.provideScanRepository(context), Injection.provideAuthRepository(context))
            }
        }
    }
}