package com.elsauron.aitube.data

import android.content.Context
import android.util.Base64
import androidx.security.crypto.MasterKey
import androidx.security.crypto.EncryptedSharedPreferences

class SecureSettings(context: Context) {
    private val prefs = EncryptedSharedPreferences.create(
        context,
        "aitube_secure_settings",
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    fun put(key: String, value: String) { prefs.edit().putString(key, value).apply() }
    fun get(key: String): String = prefs.getString(key, "") ?: ""
    fun clear(key: String) { prefs.edit().remove(key).apply() }
}
