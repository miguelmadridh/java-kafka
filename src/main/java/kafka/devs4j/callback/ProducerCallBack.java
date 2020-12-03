package kafka.devs4j.callback;

import java.util.Properties;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CallBackProd implements Callback{

  Logger logger = LoggerFactory.getLogger(CallBackProd.class);
  @Override
  public void onCompletion(RecordMetadata recordMetadata, Exception e) {
    if (e != null) {
      logger.error("We have an exception = {}", e);
    }

    logger.info("Offset ={}, Partition ={}, topic ={}", recordMetadata.offset(),
        recordMetadata.partition(), recordMetadata.topic());
  }
}

public class ProducerCallBack {

  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(ProducerCallBack.class);
    Properties properties = new Properties();
    properties.put("bootstrap.servers", "localhost:9092");
    properties.put("acks", "1");
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    properties.put("linger.ms", "10");
    try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(
        properties)) {
      for (int i = 0; i <= 100; i++) {
        kafkaProducer.send(
            new ProducerRecord<String, String>("devs4j-topic", i%2==0?"key-2.1":"key-3.1",String.valueOf(i) ),
      new CallBackProd()
      /*      (recordMetadata, e) -> {
              if (e != null) {
                logger.error("We have an exception = {}", e);
              }

              logger.info("Offset ={}, Partition ={}, topic ={}", recordMetadata.offset(),
                  recordMetadata.partition(), recordMetadata.topic());
            }

       */
           /* new Callback() {
              @Override
              public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e != null) {
                  logger.error("We have an exception = {}", e);
                }

                logger.info("Offset ={}, Partition ={}, topic ={}", recordMetadata.offset(),
                    recordMetadata.partition(), recordMetadata.topic());
              }
            }*/
        );
      }

    }
  }
}
