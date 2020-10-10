package com.qdivision.sandwichapi.rabbit

import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class RabbitConnection {

    @Bean
    fun responseQueue() = Queue(responseQueueName, false)

    @Bean
    fun requestQueue() = Queue(requestQueueName, false)

    @Bean
    fun exchange() = TopicExchange(topicExchangeName)

    @Bean
    fun responseQueueBinding(exchange: TopicExchange) =
        BindingBuilder.bind(responseQueue()).to(exchange).with(responseRoutingKey)

    @Bean
    fun requestQueueBinding(exchange: TopicExchange) =
        BindingBuilder.bind(requestQueue()).to(exchange).with(requestRoutingKey)

    @Bean
    fun incomingContainer(
        connectionFactory: ConnectionFactory,
        listenerAdapter: MessageListenerAdapter
    ): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.setQueueNames(responseQueueName)
        container.setMessageListener(listenerAdapter)
        return container
    }

    @Bean
    fun listenerAdapter(receiver: RabbitReceiver) =
        MessageListenerAdapter(receiver, "receiveMessage")

    companion object {
        const val topicExchangeName = "food-exchange"
        const val responseQueueName = "response-food-queue"
        const val requestQueueName = "request-food-queue"
        const val requestRoutingKey = "food.ingredient.exists.request"
        const val responseRoutingKey = "food.ingredient.exists.response"
    }

}