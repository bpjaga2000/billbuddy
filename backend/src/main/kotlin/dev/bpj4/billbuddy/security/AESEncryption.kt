package dev.bpj4.billbuddy.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.spec.RSAKeyGenParameterSpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESEncryption(
//        @Value("\${bpj4.securityKey}")
        key: String = "SECURITYSECURITYSECURITYSECURITY",
//        @Value("\${bpj4.securityIv}")
        iv: String = "SECURITY",
) {

    private val algorithm = "AES/CBC/PKCS5Padding"
    var keySpec: SecretKeySpec = SecretKeySpec(
            key.toByteArray(),
            "AES"
    )
    var ivSpec: IvParameterSpec = IvParameterSpec(iv.toByteArray())

    fun decrypt(cipherText: String?): CharArray {
        return if (!cipherText.isNullOrEmpty()) {
            val cipher = Cipher.getInstance(algorithm).apply {
                init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            }
            cipher.doFinal(Base64.getDecoder().decode(cipherText.toByteArray())).toCharArray()
        } else CharArray(0)
    }

    fun decryptToString(cipherText: String?): String {
        return if (!cipherText.isNullOrEmpty()) {
            val cipher = Cipher.getInstance(algorithm).apply {
                init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            }
            String(cipher.doFinal(Base64.getDecoder().decode(cipherText.toByteArray())))
        } else ""
    }

    fun encrypt(inputText: String): String {
        val cipher = Cipher.getInstance(algorithm).apply {
            init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        }
        return Base64.getEncoder().encodeToString(cipher.doFinal(inputText.toByteArray()))
    }

    fun encrypt(inputText: CharArray): String {
        val cipher = Cipher.getInstance(algorithm).apply {
            init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        }
        return Base64.getEncoder().encodeToString(cipher.doFinal(inputText.toByteArray()))
    }

    companion object {
        fun keyGen(): Key {
            val keyGen = KeyGenerator.getInstance("AES")
            keyGen.init(256)
            return SecretKeySpec(keyGen.generateKey().encoded, "AES")
        }


        fun generateRandomBytes(size: Int): ByteArray =
                ByteArray(size).apply {
                    SecureRandom.getInstanceStrong().nextBytes(this)
                }

        fun CharArray.toByteArray(): ByteArray {
            val b = ByteArray(this.size)
            for (i in this.indices)
                b[i] = this[i].code.toByte()
            return b
        }

        fun ByteArray.toCharArray(): CharArray {
            val c = CharArray(this.size)
            for (i in this.indices)
                c[i] = this[i].toInt().toChar()
            return c
        }
    }
}