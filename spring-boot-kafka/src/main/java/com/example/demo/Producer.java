package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;

public class Producer implements Runnable {
	
	public static void main(String[] args) {
		new Producer().run();
	}

	@Override
	public void run() {
		Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "SASL_PLAINTEXT://192.168.31.109:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 添加认证配置
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        props.put("sasl.jaas.config",
                "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"admin\" password=\"admin\";");
        // 创建producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        List<Future<RecordMetadata>> sends = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            ProducerRecord<String, String> producerRecord = new ProducerRecord("test", "aaa" + i);
            Future<RecordMetadata> send = producer.send(producerRecord, (m, e) -> {
                System.out.println(m.offset());
                if (e != null) {
                    System.out.println("send error: " + e);
                }
            });
            sends.add(send);
        }
        boolean exit = false;
        while (!exit) {
            for (Future<RecordMetadata> send : sends) {
                if (!send.isDone()) {
                    exit = false;
                    break;
                }
                exit = true;
            }
        }
		
	}

}
