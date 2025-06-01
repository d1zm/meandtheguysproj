package by.coolproj.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController {

    @GetMapping("/hello")
    fun hello(): String {
        return "HellO!"
    }
}