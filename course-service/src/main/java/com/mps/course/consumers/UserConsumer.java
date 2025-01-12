package com.mps.course.consumers;

import com.mps.course.dtos.UserEventDto;
import com.mps.course.enums.ActionType;
import com.mps.course.services.UserService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {

    @Autowired
    UserService userService;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${mps.broker.queue.userEventQueue.name}", durable = "true"),
            exchange = @Exchange(value = "${mps.broker.exchange.userEventExchange}", type = ExchangeTypes.FANOUT, ignoreDeclarationExceptions = "true"))
    )
    public void listenUserEvent(@Payload UserEventDto userEventDto){
        var userModel = userEventDto.convertToUserModel();

        switch (ActionType.valueOf(userEventDto.getActionType())){
            case CREATE:
            case UPDATE:
                userService.save(userModel);
                break;
            case DELETE:
                userService.delete(userEventDto.getUserId());
                break;
        }
    }
}
