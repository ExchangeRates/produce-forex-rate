package com.wcreators.produceforexrate.producer;

public interface ProducerService<T> {
    void produce(T model);
}
