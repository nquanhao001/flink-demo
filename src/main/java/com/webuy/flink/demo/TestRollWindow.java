package com.webuy.flink.demo;

import com.webuy.flink.key.KeyByItemId;
import com.webuy.flink.reduce.OrderReduceFunction;
import com.webuy.flink.sink.OrderSinkFunction;
import com.webuy.flink.source.SpringSourceFunction;
import com.webuy.flink.dto.OrderDTO;
import com.webuy.flink.key.KeyByItemIdAndTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * 这个例子，一秒钟一个订单产生，针对商品id进行汇总，5s一次数据库的操作，插入数据库,数据库配置可以从mybatis-conf.xml中取得
 * 通过观察控制台的日志，可以自己手动计算下是否正确
 * 如果1s有几千条订单消息，那么可以通过这个汇总大大减少mysql的操作
 */
@Slf4j
public class TestRollWindow {

    public static void main(String[] args) {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<OrderDTO> source = env.addSource(new SpringSourceFunction(false));
        source.keyBy(new KeyByItemId())
                .timeWindow(Time.seconds(5))
                .reduce(new OrderReduceFunction())
                .addSink(new OrderSinkFunction());
        try {
            env.execute("TestRollWindow");
        } catch (Exception e) {
            log.error("TestRollWindow-job运行异常",e);
        }
    }
}
