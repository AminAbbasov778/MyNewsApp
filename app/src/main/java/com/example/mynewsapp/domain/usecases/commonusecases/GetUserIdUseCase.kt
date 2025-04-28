package com.example.mynewsapp.domain.usecases.commonusecases

import com.example.mynewsapp.domain.interfaces.UserRepository
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(val userRepository: UserRepository) {
    operator fun  invoke(): String? = userRepository.getUserId()
}