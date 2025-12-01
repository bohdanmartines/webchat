package dto

import java.time.LocalDateTime

case class MessageWithUser(id: Long,
                           chatId: Long,
                           userId: Long,
                           username: String,
                           content: String,
                           createdAt: LocalDateTime)