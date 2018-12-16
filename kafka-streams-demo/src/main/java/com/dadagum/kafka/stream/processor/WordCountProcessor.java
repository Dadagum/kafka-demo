package com.dadagum.kafka.stream.processor;


import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 使用更加低级的api需要继承Processor实现自己的拓扑节点
 * 官方文档说明:
 * You can also use this context instance to schedule a punctuation function (via ProcessorContext#schedule()),
 * to forward a new record as a key-value pair to the downstream processors (via ProcessorContext#forward()),
 * and to commit the current processing progress (via ProcessorContext#commit()).
 * Any resources you set up in init() can be cleaned up in the close() method.
 * Note that Kafka Streams may re-use a single Processor object by calling init() on it again after close()
 */
public class WordCountProcessor implements Processor<String, String> {

    // 可以通过context获取当前处理数据的元头部
    private ProcessorContext context;
    private KeyValueStore<String, Long> kvStore;

    @Override
    public void init(ProcessorContext context) {
        // keep the processor context locally because we need it in punctuate() and commit()
        this.context = context;

        // retrieve the key-value store named "Counts"
        kvStore = (KeyValueStore) context.getStateStore("Counts");

        // 新的版本取消了punctuate方法,直接在这里定义window
        // schedule a punctuate() method every second based on stream-time
        this.context.schedule(10 * 1000, PunctuationType.STREAM_TIME, (timestamp) -> {
            KeyValueIterator<String, Long> iter = this.kvStore.all();
            while (iter.hasNext()) {
                KeyValue<String, Long> entry = iter.next();
                context.forward(entry.key, entry.value); // 传给下一个processor
            }
            iter.close();

            // commit the current processing progress
            context.commit();
        });
    }

    /**
     * 对每一条记录都生效, key -> value 即为记录的键值对
     */
    @Override
    public void process(String key, String value) {
        Stream.of(value.toLowerCase().split("\\W+")).forEach(word -> {
            Long count = Optional.ofNullable(kvStore.get(word))
                    .map(wordCount -> wordCount + 1)
                    .orElse(1L);
            kvStore.put(word, count);
        });
    }

    @Override
    public void close() {
    }


}
