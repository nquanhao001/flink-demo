package com.webuy.flink.demo;

import com.webuy.flink.reduce.OrderReduceFunction;
import com.webuy.flink.source.OutIfOrderSourceFunction;
import com.webuy.flink.dto.OrderDTO;
import com.webuy.flink.key.KeyByItemId;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * 这个例子，一秒钟一个订单产生，汇总逻辑是按照商品id进行汇总销售额，每5s输出一次结果
 * 但是前面10s的订单因为系统原因，延迟了， 我们会发现，延迟的前10s的数据被算作后面的数据了，经过15s之后  ，就稳定了
 *
 * 出现这个的原因是 flink默认就是按照开始处理stream流中data的时间来当做窗口的。
 */
@Slf4j
public class WaterMain {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<OrderDTO> source = env.addSource(new OutIfOrderSourceFunction());

        source.keyBy(new KeyByItemId())
                .timeWindow(Time.seconds(5))
                .reduce(new OrderReduceFunction())
                .printToErr();

        try {
            env.execute("test");
        } catch (Exception e) {
            log.error("job运行异常",e);
        }
    }
}
