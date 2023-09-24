package me.nomi.urdutyper.domain.usecase

import com.google.firebase.auth.FirebaseUser
import me.nomi.urdutyper.data.repository.FirebaseRepositoryImpl
import me.nomi.urdutyper.domain.entity.User
import me.nomi.urdutyper.utils.extensions.Result

open class Register(private val firebaseRepository: FirebaseRepositoryImpl) {
    suspend operator fun invoke(email: String, password: String, user: User): Result<FirebaseUser> {
        return firebaseRepository.register(email = email, password = password, user = user)
    }
}
