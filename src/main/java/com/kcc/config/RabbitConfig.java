package com.kcc.config;

import java.text.SimpleDateFormat;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitConfig {

  @Bean
  public MessageConverter messageConverter() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    objectMapper.setSerializationInclusion(Include.NON_NULL);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    objectMapper.setDateFormat(dateFormat);
    objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.setTimeZone(dateFormat.getTimeZone());
    return new Jackson2JsonMessageConverter(objectMapper);
  }

  @Bean
  public Queue repliesQueue() {
    return new AnonymousQueue();
  }

  @Bean
  @Primary
  public SimpleMessageListenerContainer replyContainer(ConnectionFactory connectionFactory) {
    SimpleMessageListenerContainer container =
        new SimpleMessageListenerContainer(connectionFactory);
    container.setQueueNames(repliesQueue().getName());
    return container;
  }

  @Bean
  public AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate template,
      SimpleMessageListenerContainer container) {
    return new AsyncRabbitTemplate(template, container);
  }

}
