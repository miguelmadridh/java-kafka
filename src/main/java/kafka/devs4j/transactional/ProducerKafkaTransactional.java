package kafka.devs4j.transactional;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerKafkaTransactional {

  Logger logger = LoggerFactory.getLogger(ProducerKafkaTransactional.class);

  public static void main(String[] args) {

    Properties properties = new Properties();
    properties.put("bootstrap.servers", "localhost:9092");
    properties.put("acks", "all");
    properties.put("transactional.id", "devs4j-producer");
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    properties.put("linger.ms", "10");

    try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(
        properties)) {
      try {
        kafkaProducer.initTransactions();
        kafkaProducer.beginTransaction();
        for (int i = 0; i <= 100000; i++) {
          kafkaProducer.send(new ProducerRecord<String, String>("devs4j-topic", String.valueOf(i),
              "devs4j-value" + i));
          if(i == 50000 ){
            throw new Exception("Unexpeccted exception");
          }
        }
        kafkaProducer.commitTransaction();
        kafkaProducer.flush();
      } catch (Exception e) {
        kafkaProducer.abortTransaction();
      }
    }
  }

}
