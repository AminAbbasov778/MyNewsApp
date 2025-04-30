package com.example.mynewsapp.domain.usecases.settingsusecases

import com.example.mynewsapp.domain.interfaces.RegisterRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(val registerRepository: RegisterRepository) {

    operator fun invoke(): Result<Boolean> = registerRepository.logout()

}