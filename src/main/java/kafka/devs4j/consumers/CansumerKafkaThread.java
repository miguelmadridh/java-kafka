package kafka.devs4j.consumers;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CansumerKafkaThread extends Thread {

  private final KafkaConsumer<String, String> consumer;
  private final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
  Logger logger = LoggerFactory.getLogger(CansumerKafkaThread.class);

  public CansumerKafkaThread(KafkaConsumer<String, String> consumer) {
    this.consumer = consumer;
  }

  @Override
  public void run() {
    consumer.subscribe(Arrays.asList("devs4j-topic"));

    try {
      while (!atomicBoolean.get()) {
        ConsumerRecords<String, String> poll = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> consumerRecord : poll) {
          logger
              .info("--> partition={}, offset={} , key={}, value={}", consumerRecord.partition(),
                  consumerRecord.offset(), consumerRecord.key(), consumerRecord.value());
/*          if ((Integer.parseInt(consumerRecord.key())) % 100000 == 0) {
            logger
                .info("--> partition={}, offset={} , key={}, value={}", consumerRecord.partition(),
                    consumerRecord.offset(), consumerRecord.key(), consumerRecord.value());
          }*/
        }
      }
    } catch (WakeupException e) {
      if (!atomicBoolean.get()) {
        throw e;
      }
    } finally {
      consumer.close();
    }
  }

  public void shutDown() {
    atomicBoolean.set(true);
    consumer.wakeup();
  }
}
