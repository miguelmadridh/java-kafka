package kafka.devs4j.producers;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerKafka {


  private static final Logger LOGGER = LoggerFactory.getLogger(ProducerKafka.class);

  public static void main(String[] args) {
    Properties properties = new Properties();
    properties.put("bootstrap.servers", "localhost:9092");
    properties.put("acks", "1");
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    properties.put("linger.ms", "10");

    try (Producer<String, String> stringStringProducer = new KafkaProducer<>(properties)) {
      for (int i = 0; i <= 1000000; i++) {
        /*
        metodo assincrono

         */
        stringStringProducer
            .send(new ProducerRecord<String, String>("devs4j-topic", "devs4j-key",
                "devs4j-value"));

       /* stringStringProducer
            .send(new ProducerRecord<>("devs4j-topic", String.valueOf(i), "devs4j-value")).get();*/

      }
   /* } catch (InterruptedException | ExecutionException e) {
      LOGGER.error("Messaage producer error", e);
    }*/
    }
  }
}
