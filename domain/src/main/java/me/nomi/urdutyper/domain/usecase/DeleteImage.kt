package me.nomi.urdutyper.domain.usecase

import me.nomi.urdutyper.domain.repository.FirebaseRepository

class DeleteImage(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(uid: String, fileName: String, url: String) = firebaseRepository.deleteImage(uid, fileName, url)
}