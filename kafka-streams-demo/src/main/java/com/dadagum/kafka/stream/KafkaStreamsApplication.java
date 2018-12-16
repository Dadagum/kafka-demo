package com.dadagum.kafka.stream;

import com.dadagum.kafka.stream.processor.WordCountProcessor;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

public class KafkaStreamsApplication {

    private static Properties props;

    private static final Logger logger = LoggerFactory.getLogger(KafkaStreamsApplication.class);

    static {
        props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 5 * 1000); // commit时间间隔
        // props.put(StreamsConfig.METRICS_SAMPLE_WINDOW_MS_CONFIG, 10 * 1000);

    }

    /**
     * 官方文档入门demo : word count
     * 使用的是DSL,更加方便快捷
     */
    private static void wordCountDSL() {
        // 配置kafka streams

        // 使用high-level DSL
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream("hongda-input");
        KTable<String, Long> wordCounts = textLines
                .flatMapValues(textLine -> Arrays.asList(textLine.toLowerCase().split("\\W+")))
                .groupBy((key, word) -> word)
                .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"));
        wordCounts.toStream().to("hongda-output", Produced.with(Serdes.String(), Serdes.Long()));

        // 使用DSL创建的拓扑结构并开启流
        Topology topology = builder.build();
        KafkaStreams streams = new KafkaStreams(topology, props);
        streams.start();

        // 抓取抛出的异常
        streams.setUncaughtExceptionHandler((Thread thread, Throwable throwable) -> {
            logger.info("An exception was caught in kafka streams app : " + throwable.getMessage());
        });

        // 优雅关闭流
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    /**
     * 更加低级更加灵活的api
     */
    private static void processorApi() {
        // 建立拓扑结构
        Topology topology = new Topology();
        topology.addSource("Source", new StringDeserializer(), new StringDeserializer(), "hongda-input")
                .addProcessor("WordCountProcessor", WordCountProcessor::new, "Source")
                .addStateStore(Stores.keyValueStoreBuilder(Stores.inMemoryKeyValueStore("Counts"), Serdes.String(), Serdes.Long()), "WordCountProcessor")
                .addSink("Sink", "hongda-output",  new StringSerializer(), new LongSerializer(), "WordCountProcessor");
        KafkaStreams streams = new KafkaStreams(topology, props);

        // 开启流
        streams.start();

        // 抓取抛出的异常
        streams.setUncaughtExceptionHandler((Thread thread, Throwable throwable) -> {
            logger.info("An exception was caught in kafka streams app : " + throwable.getMessage());
        });

        // 优雅关闭流
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }



    public static void main(String[] args) {
        // wordCountDSL();
        processorApi();
    }





}
