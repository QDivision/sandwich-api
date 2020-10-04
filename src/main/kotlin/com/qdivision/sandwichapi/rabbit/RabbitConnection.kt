package com.qdivision.sandwichapi.rabbit

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.context.annotation.Bean

class RabbitConnection {

    @Bean
    fun queue(): Queue? {
        return Queue(queueName, false)
    }

    @Bean
    fun exchange(): TopicExchange? {
        return TopicExchange(topicExchangeName)
    }

    @Bean
    fun binding(queue: Queue?, exchange: TopicExchange?): Binding? {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey)
    }

    @Bean
    fun container(connectionFactory: ConnectionFactory?,
                  listenerAdapter: MessageListenerAdapter?): SimpleMessageListenerContainer? {
        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory!!
        container.setQueueNames(queueName)
        container.setMessageListener(listenerAdapter!!)
        return container
    }

    @Bean
    fun listenerAdapter(receiver: RabbitReceiver?): MessageListenerAdapter? {
        return MessageListenerAdapter(receiver, "receiveMessage")
    }

//    @Bean
//    fun queue() = Queue(queueName, false)
//
//    @Bean
//    fun exchange() = TopicExchange(topicExchangeName)
//
//    @Bean
//    fun binding(queue: Queue, exchange: TopicExchange) =
//        BindingBuilder.bind(queue).to(exchange).with(routingKey)
//
//    @Bean
//    fun container(
//        connectionFactory: ConnectionFactory,
//        listenerAdapter: MessageListenerAdapter
//    ): SimpleMessageListenerContainer {
//        val container = SimpleMessageListenerContainer()
//        container.connectionFactory = connectionFactory
//        container.setQueueNames(queueName)
//        container.setMessageListener(listenerAdapter)
//        return container
//    }
//
//    @Bean
//    fun listenerAdapter(receiver: RabbitReceiver) =
//        MessageListenerAdapter(receiver, "receiveMessage")

    companion object {
        const val topicExchangeName = "food-exchange"
        const val queueName = "food-queue"
        const val routingKey = "food.#"
    }

}