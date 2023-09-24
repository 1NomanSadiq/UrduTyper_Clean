package me.nomi.urdutyper.utils.extensions.repository

import me.nomi.urdutyper.utils.extensions.Result

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return try {
        val result = apiCall()
        Result.Success(result)
    } catch (e: Exception) {
        Result.Error(e)
    }
}