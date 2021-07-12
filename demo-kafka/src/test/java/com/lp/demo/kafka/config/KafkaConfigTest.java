package com.lp.demo.kafka.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lp
 * @create 2021/5/3 23:51
 * @description
 **/
class KafkaConfigTest {
    @Autowired
    private KafkaProperties properties;
    @Test
    public void KafkaConfigTest(){
        AdminClient client = AdminClient.create(properties.buildAdminProperties());
        if(client !=null){
            try {
                Collection<NewTopic> newTopics = new ArrayList<>(1);
                newTopics.add(new NewTopic("topic-kl",1,(short) 1));
                client.createTopics(newTopics);
            }catch (Throwable e){
                e.printStackTrace();
            }finally {
                client.close();
            }
        }
    }
}

