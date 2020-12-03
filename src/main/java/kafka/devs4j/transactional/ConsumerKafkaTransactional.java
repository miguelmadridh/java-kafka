package kafka.devs4j.transactional;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerKafkaTransactional {

  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(ConsumerKafkaTransactional.class);
    Properties properties = new Properties();
    properties.put("bootstrap.servers", "localhost:9092");
    properties.put("isolation.level", "read_committed");
    properties.put("group.id", "devs4j-group");
    properties.put("enable.auto.commit", "true");
    properties.put("auto.commit.intervals.ms", "1000");
    properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    properties
        .put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties)) {
      consumer.subscribe(Arrays.asList("devs4j-topic"));
      while (true) {
        ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
          if (Integer.parseInt(consumerRecord.key()) % 10000 == 0) {
            logger
                .info("--> partition={}, offset={} , key={}, value={}", consumerRecord.partition(),
                    consumerRecord.offset(), consumerRecord.key(), consumerRecord.value());
          }
        }
      }
    }
  }
}
