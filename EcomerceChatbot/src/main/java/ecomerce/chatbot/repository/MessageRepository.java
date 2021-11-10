package ecomerce.chatbot.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import ecomerce.chatbot.model.Message;

public interface MessageRepository extends MongoRepository<Message,String >{
	Message findByCategory(String title);
	
}
