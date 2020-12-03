package kafka.devs4j.consumers;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerKafka {

  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(ConsumerKafka.class);
    Properties properties = new Properties();
    properties.put("bootstrap.servers", "localhost:9092");
    properties.put("group.id", "devs4j-group");
    properties.put("enable.auto.commit", "true");
    properties.put("auto.commit.intervals.ms", "1000");
    properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    properties
        .put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties)) {
      TopicPartition topicPartition = new TopicPartition("devs4j-topic", 4);
      //consumer.assign(Arrays.asList(topicPartition));
      //consumer.seek(topicPartition,50);
      consumer.subscribe(Arrays.asList("devs4j-topic"));
      while (true) {
        ConsumerRecords<String, String> poll = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord consumerRecord : poll) {
          logger.info("--> partition={}, offset={} , key={}, value={}", consumerRecord.partition(),
              consumerRecord.offset(), consumerRecord.key(), consumerRecord.value());
        }
      }
    }
  }
}
