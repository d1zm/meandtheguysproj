package by.coolproj.bots

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
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
            update.message.from

            if (messageText == "/all") {
                try {
                    val chatId = update.message.chatId
                    val admins = getChatAdministrators(chatId) // Получаем список админов
                    val me = getChatMember(chatId, update.message.from)
                    val mentions = admins.filterNot { it.user.isBot}.filterNot { it == me }.joinToString(", ") { admin ->
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

    // Получаем ChatMemberAdministrator из User
    private fun getChatMember(chatId: Long, user: User): ChatMember? {
        val getChatMember = GetChatMember().apply {
            this.chatId = chatId.toString()
            this.userId = user.id
        }

        return try {
            return execute(getChatMember)
        } catch (e: Exception) {
            null
        }
    }
}