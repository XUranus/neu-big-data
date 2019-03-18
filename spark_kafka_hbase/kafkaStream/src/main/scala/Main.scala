
object Main {
  def main(args: Array[String]): Unit = {
    //new StormingThread().start() //开启监听 接受

    new KafkaProducerThread().start();
    new KafkaStreamingConsumer().start()

    //Thread.sleep(3000);
    //new EchoSimulatorThread().start() //开启模拟发送

    //new KafkaProducerThread().start()
    //new KafkaConsumerThread().start()
  }
}