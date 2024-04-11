package repository;

import java.util.List;


import org.springframework.data.mongodb.repository.MongoRepository;

import model.ChatMessage;
import model.MessageStatus;

public interface ChatMessageRepository
extends MongoRepository<ChatMessage, String> {

long countBySenderIdAndRecipientIdAndStatus(
    String senderId, String recipientId, MessageStatus status);

List<ChatMessage> findByChatId(String chatId);
}
