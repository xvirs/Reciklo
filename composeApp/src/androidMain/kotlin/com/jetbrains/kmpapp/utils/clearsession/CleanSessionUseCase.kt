package com.jetbrains.kmpapp.utils.clearsession

import android.util.Log
import java.io.File

class CleanSessionUseCase {
    fun invoke (file:File):Boolean{
        if (file.exists()) {
            if (file.delete()) {
                return true
            } else {
                return false
            }
        } else {
            Log.e("session","fallo")
            return true
        }
    }
}