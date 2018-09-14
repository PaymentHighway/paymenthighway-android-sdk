package io.paymenthighway.sdk.util

import android.util.Base64

import com.google.gson.Gson
import io.paymenthighway.sdk.model.CardData
import io.paymenthighway.sdk.model.TokenizeData
import io.paymenthighway.sdk.model.TokenizeDataKey
import io.paymenthighway.sdk.model.EncryptionKey
import java.io.ByteArrayInputStream
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


private val encryptionAlgorithm = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding"
private val aesCbcPkcs7paddingAlgorithm = "AES/CBC/PKCS7Padding"

private data class CardDataCrypt(val expiry_month: String, val expiry_year: String, val cvc: String, val pan: String)

internal fun tokenizeCardData(cardData: CardData, encryptionKey: EncryptionKey): Result<TokenizeData, Exception> {
    try {
        val aesCipher = Cipher.getInstance(aesCbcPkcs7paddingAlgorithm)
        val secureRandom = SecureRandom()
        val iv = ByteArray(aesCipher.blockSize)
        secureRandom.nextBytes(iv)
        val keyBytes = ByteArray(aesCipher.blockSize)
        secureRandom.nextBytes(keyBytes)
        val keySpec = SecretKeySpec(keyBytes, "AES")
        val ivSpec = IvParameterSpec(iv)
        aesCipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

        val cardDataCrypt = CardDataCrypt(cardData.expiryDate.month, cardData.expiryDate.year, cardData.cvc, cardData.pan)
        val json = Gson().toJson(cardDataCrypt)
        val encryptedData = aesCipher.doFinal(json.toByteArray(charset("UTF-8")))
        val fac = CertificateFactory.getInstance("X509")
        val keyBase64 = Base64.decode(encryptionKey.key, Base64.DEFAULT)
        val inputStream = ByteArrayInputStream(keyBase64)
        val cert = fac.generateCertificate(inputStream) as X509Certificate

        val rsaCipher = Cipher.getInstance(encryptionAlgorithm)
        rsaCipher.init(Cipher.ENCRYPT_MODE, cert.publicKey)
        val encryptedKey = rsaCipher.doFinal(keyBytes)

        val encryptedBase64Data = Base64.encodeToString(encryptedData, Base64.NO_WRAP)
        val encryptedBase64Key = Base64.encodeToString(encryptedKey, Base64.NO_WRAP)
        val encryptedBase64Iv = Base64.encodeToString(iv, Base64.NO_WRAP)

        return Result.success(TokenizeData(encryptedBase64Data, TokenizeDataKey(encryptedBase64Key, encryptedBase64Iv)))
    } catch (exception: Exception) {
        println("tokenizeCardData exception: $exception")
        return Result.failure(exception)
    }
}
