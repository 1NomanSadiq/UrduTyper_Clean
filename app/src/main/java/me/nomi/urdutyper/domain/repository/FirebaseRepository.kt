package me.nomi.urdutyper.domain.repository

import com.google.firebase.auth.FirebaseUser
import me.nomi.urdutyper.domain.entity.Image
import me.nomi.urdutyper.domain.entity.User
import me.nomi.urdutyper.utils.extensions.Result

interface FirebaseRepository {
    suspend fun register(email: String, password: String, user: User): Result<FirebaseUser>
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun logOut(): Result<Unit>
    suspend fun getUserData(): Result<User>
    suspend fun loadImages(uid: String): Result<List<Image>>
}