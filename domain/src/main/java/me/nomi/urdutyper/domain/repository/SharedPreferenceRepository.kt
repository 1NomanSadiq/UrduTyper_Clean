package me.nomi.urdutyper.domain.repository

interface SharedPreferenceRepository {
    var uid: String
    fun clear()
}