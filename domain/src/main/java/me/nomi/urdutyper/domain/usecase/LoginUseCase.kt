package me.nomi.urdutyper.domain.usecase

import me.nomi.urdutyper.domain.repository.FirebaseRepository

class LoginUseCase(private val firebaseRepository: FirebaseRepository)  {
    suspend fun invoke(email: String, password: String) = firebaseRepository.login(email = email, password = password)
}