package kafka.devs4j.consumers;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerKafkaMuliThread {

  public static void main(String[] args) {

    Logger logger = LoggerFactory.getLogger(ConsumerKafkaMuliThread.class);
    Properties properties = new Properties();
    properties.put("bootstrap.servers", "localhost:9092");
    properties.put("group.id", "devs4j-group");
    properties.put("enable.auto.commit", "true");
    properties.put("auto.commit.intervals.ms", "1000");
    properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    properties
        .put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

    ExecutorService executorService = Executors.newFixedThreadPool(5);
    for (int i = 0; i < 5; i++) {
      CansumerKafkaThread cansumerKafkaThread = new CansumerKafkaThread(
          new KafkaConsumer<String, String>(properties));
      executorService.execute(cansumerKafkaThread);
    }
    while(!executorService.isTerminated());
  }
}
