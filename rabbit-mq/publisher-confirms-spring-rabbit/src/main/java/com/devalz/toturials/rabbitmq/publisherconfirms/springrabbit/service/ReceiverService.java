package com.devalz.toturials.rabbitmq.publisherconfirms.springrabbit.service;

public interface ReceiverService {

    void onReceive(String message);

}
