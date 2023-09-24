package me.nomi.urdutyper.domain.usecase

import me.nomi.urdutyper.data.repository.FirebaseRepositoryImpl
import me.nomi.urdutyper.utils.extensions.Result

open class Logout(private val firebaseRepository: FirebaseRepositoryImpl) {
    suspend operator fun invoke(): Result<Unit> {
        return firebaseRepository.logOut()
    }
}
