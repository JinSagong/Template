package com.jin.template.fcm

import com.jin.template.TemplateController.fcmKey
import com.jin.template.util.Debug
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection

@Suppress("UNUSED")
class FCMSender private constructor(
    private val msgJson: JSONObject,
    private val androidJson: JSONObject,
    private val topic: String? = null,
    private val tokenJson: JSONArray? = null
) {
    class Builder {
        private var senderId: String? = null
        private var senderToken: String? = null
        private var type: String? = null

        private var sendToMyself = false
        private var notification = false

        private var topic: String? = null
        private var tokens: JSONArray? = null

        private var title: String? = null
        private var content: String? = null
        private var contentResource = -1
        private var stringExtra1: String? = null
        private var stringExtra2: String? = null
        private var directBootOk = false

        fun setSenderId(senderId: String?) = apply { this.senderId = senderId }
        fun setSenderToken(senderToken: String?) = apply { this.senderToken = senderToken }
        fun setType(type: String) = apply { this.type = type }

        fun setSendToMyself(sendToMyself: Boolean) = apply { this.sendToMyself = sendToMyself }
        fun setNotification(notification: Boolean) = apply { this.notification = notification }

        fun setTopic(topic: String) = apply { this.topic = topic }
        fun setReceiverTokens(tokenList: List<String>) =
            apply { this.tokens = convertToJsonArray(tokenList) }

        fun setReceiverToken(token: String) =
            apply { this.tokens = convertToJsonArray(listOf(token)) }

        fun setTitle(title: String?) = apply { this.title = title }
        fun setContent(content: String?) = apply { this.content = content }
        fun setContentResource(resource: Int) = apply { this.contentResource = resource }

        fun setDirectBoot(directBootOk: Boolean) = apply { this.directBootOk = directBootOk }

        fun setExtra(extra: String) = apply { this.stringExtra1 = extra }
        fun setExtra2(extra: String) = apply { this.stringExtra2 = extra }

        private inline fun <reified T> convertToJsonArray(userList: List<T>) =
            when (T::class.java) {
                String::class.java -> JSONArray().apply { userList.forEach { put(it as String) } }
                else -> JSONArray()
            }

        fun build() {
            if (senderId == null || senderToken == null || type == null) return

            val msgObject = JSONObject().apply {
                put(FCM_SENDER_ID, senderId)
                put(FCM_SENDER_TOKEN, senderToken)
                put(FCM_TYPE, type)
                put(FCM_SEND_TO_MYSELF, sendToMyself)
                put(FCM_NOTIFICATION, notification)
                title?.let { put(FCM_TITLE, it) }
                content?.let { put(FCM_CONTENT, it) }
                if (contentResource != -1) put(FCM_CONTENT_RESOURCE, contentResource)
                stringExtra1?.let { put(FCM_STRING_EXTRA1, it) }
                stringExtra2?.let { put(FCM_STRING_EXTRA2, it) }
            }
            val androidObject = JSONObject().apply { put("priority", "high") }

            FCMSender(msgObject, androidObject, topic, tokens).send()
        }
    }

    private fun send() {
        Thread {
            val root = JSONObject().apply {
                topic?.let { put("to", "/topics/$it") }
                tokenJson?.let { put("registration_ids", it) }
                put("data", msgJson)
                put("android", androidJson)
            }.toString()

            val url = URL("https://fcm.googleapis.com/fcm/send")
            val conn = (url.openConnection() as HttpsURLConnection).apply {
                requestMethod = "POST"
                doOutput = true
                doInput = true
                addRequestProperty("Authorization", "key=$fcmKey")
                setRequestProperty("Accept", "application/json")
                setRequestProperty("content-type", "application/json")
            }

            val os = conn.outputStream
            os.use {
                it.write(root.toByteArray(StandardCharsets.UTF_8))
                it.flush()
            }

            val responseCode = conn.responseCode
            val responseMsg = conn.responseMessage
            Debug.i("pushParams=$root\nresponseCode=$responseCode\nresponseMsg=$responseMsg")
        }.start()
    }

    companion object {
        const val FCM_SENDER_ID = "senderId"
        const val FCM_SENDER_TOKEN = "senderToken"
        const val FCM_TYPE = "messageType"

        const val FCM_SEND_TO_MYSELF = "sendToMyself"
        const val FCM_NOTIFICATION = "notification"

        const val FCM_TITLE = "title"
        const val FCM_CONTENT = "content"
        const val FCM_CONTENT_RESOURCE = "contentResource"

        const val FCM_STRING_EXTRA1 = "stringExtra1"
        const val FCM_STRING_EXTRA2 = "stringExtra2"

        const val FCM_AND = "&#^&##@%$"
    }
}