package com.devalz.toturials.rabbitmq.publisherconfirmsspringrabbit.service;

public interface ReceiverService {

    void onReceive(String message);

}
