package by.coolproj.bots

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException


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
            val chatId: Long = update.message.chatId

            val message = SendMessage()
            message.chatId = chatId.toString()
            message.text = "Вы написали: $messageText"

            try {
                execute(message)
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }
        }
    }
}