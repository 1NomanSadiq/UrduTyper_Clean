package me.nomi.urdutyper.domain.usecase

import me.nomi.urdutyper.domain.entity.Image
import me.nomi.urdutyper.domain.repository.FirebaseRepository

class DeleteImage(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(uid: String, file: Image) = firebaseRepository.deleteImage(uid, file)
}