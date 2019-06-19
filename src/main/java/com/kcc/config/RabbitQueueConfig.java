package com.kcc.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitQueueConfig {

  public static final String TEST_EXCHANGE = "TEST_EXCHANGE";
  public static final String TEST_ROUTING_KEY = "TEST_ROUTING_KEY";
  public static final String TEST_QUEUE = "TEST_QUEUE";

  @Bean("testExchange")
  public Exchange testExchange() {
    return ExchangeBuilder.directExchange(TEST_EXCHANGE).durable(true).build();
  }

  @Bean("testQueue")
  public Queue testQueue() {
    return new Queue(TEST_QUEUE, true);
  }

  @Bean
  public Binding testBinding(@Qualifier("testQueue") Queue queue,
      @Qualifier("testExchange") Exchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(TEST_ROUTING_KEY).noargs();
  }

}
