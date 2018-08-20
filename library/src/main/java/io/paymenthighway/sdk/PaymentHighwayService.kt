package io.paymenthighway.sdk

import io.paymenthighway.sdk.model.*
import io.paymenthighway.sdk.util.Result


//internal data class TransactionKeyResult(val result: ApiResultInfo? = null, val key: String? = null) {
//    class Deserializer : ResponseDeserializable<TransactionKeyResult> {
//        override fun deserialize(content: String) = Gson().fromJson(content, TransactionKeyResult::class.java)
//    }
//}

//class TransactionKeyDeserializer : ResponseDeserializable<TransactionKey> {
//    inline fun <reified T> fromJson(content: String): T?  = Gson().fromJson(content, T::class.java)
//    override fun deserialize(content: String) = Gson().fromJson(content, TransactionKey::class.java)
//}
//

sealed class NetworkError {

}
class PaymentHighwayService(val merchantId: MerchantId, val accountId: AccountId) {

    fun transactionKey(transactionId: TransactionId, completion: (Result<TransactionKey, PaymentHighwayError>) -> Unit) {
//        println("transactionKey ----> start")
//        Fuel.request(PaymentHighwayEndpoint.transactionKey(merchantId, accountId, transactionId))
//             .responseObject(TransactionKeyDeserializer()) { _, _, result ->
//            println("transactionKey result---->$result")
//            completion(result)
//        }
    }

    fun tokenizeTransaction(
        transactionId: TransactionId,
        cardData: CardData,
        transactionKey: TransactionKey) {
    }
}
