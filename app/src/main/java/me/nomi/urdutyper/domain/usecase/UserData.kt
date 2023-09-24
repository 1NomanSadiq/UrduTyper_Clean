package me.nomi.urdutyper.domain.usecase

import me.nomi.urdutyper.data.repository.FirebaseRepositoryImpl
import me.nomi.urdutyper.domain.entity.User
import me.nomi.urdutyper.utils.extensions.Result

open class UserData(private val firebaseRepository: FirebaseRepositoryImpl) {
    suspend operator fun invoke(): Result<User> {
        return firebaseRepository.getUserData()
    }
}