package by.coolproj.bots

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember


@Component
class AleshaBot(
    @Value("\${TG_BOT_USERNAME}") private val tgBotUserName: String,
    @Value("\${TG_BOT_TOKEN}") private val tgBotToken: String,
) : TelegramLongPollingBot(tgBotToken) {
    override fun getBotUsername(): String {
        return tgBotUserName
    }

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            val messageText: String = update.message.text

            if (messageText == "/all") {
                try {
                    val chatId = update.message.chatId
                    val admins = getChatAdministrators(chatId) // Получаем список админов
                    val mentions = admins.filterNot { it.user.isBot }.joinToString(", ") { admin ->
                        val user = admin.user
                        "<a href=\"tg://user?id=${user.id}\">${user.firstName}</a>"
                    }
                    sendTextMessage(chatId, mentions, parseMode = "HTML")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    // Функция для получения списка администраторов
    private fun getChatAdministrators(chatId: Long): List<ChatMember> {
        return try {
            val response = execute(GetChatAdministrators().apply {
                this.chatId = chatId.toString()
            })
            response
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Упрощенная отправка сообщения
    private fun sendTextMessage(chatId: Long, text: String, parseMode: String? = null) {
        execute(SendMessage(chatId.toString(), text).apply {
            this.parseMode = parseMode
        })
    }
}