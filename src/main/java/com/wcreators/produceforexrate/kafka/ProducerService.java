package com.wcreators.produceforexrate.kafka;

public interface ProducerService<T> {
    String topicName(T model);

    void produce(T model);
}
