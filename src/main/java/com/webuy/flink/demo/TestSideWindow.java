package com.webuy.flink.demo;

import com.webuy.flink.dto.OrderDTO;
import com.webuy.flink.key.KeyByItemId;
import com.webuy.flink.reduce.OrderReduceFunction;
import com.webuy.flink.sink.OrderSinkFunction;
import com.webuy.flink.source.SpringSourceFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * 这个例子，一秒钟一个订单产生，针对商品id进行汇总，每5s计算一次前面10s的所有商品的销售额，并打印出来
 * 通过观察控制台的日志，可以自己手动计算下是否正确
 */
@Slf4j
public class TestSideWindow {

    public static void main(String[] args) {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<OrderDTO> source = env.addSource(new SpringSourceFunction());
        source.keyBy(new KeyByItemId())
                .timeWindow(Time.seconds(10),Time.seconds(5))
                .reduce(new OrderReduceFunction())
                .printToErr();
        try {
            env.execute("TestSideWindow");
        } catch (Exception e) {
            log.error("TestSideWindow-job运行异常",e);
        }
    }
}
