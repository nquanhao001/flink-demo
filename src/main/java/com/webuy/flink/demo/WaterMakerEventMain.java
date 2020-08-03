package com.webuy.flink.demo;

import com.webuy.flink.reduce.OrderReduceFunction;
import com.webuy.flink.source.OutIfOrderSourceFunction;
import com.webuy.flink.dto.OrderDTO;
import com.webuy.flink.key.KeyByItemId;
import com.webuy.flink.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;

import javax.annotation.Nullable;
import java.util.Date;

/**
 * 这个例子，一秒钟一个订单产生，汇总逻辑是按照商品id进行汇总销售额，每5s输出一次结果
 * 但是前面10s的订单因为系统原因，延迟了， 但是我们会设置使用eventtime，并且允许最大延迟20s，那么会发现每个窗口都是输出5
 */
@Slf4j
public class WaterMakerEventMain {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        SingleOutputStreamOperator<OrderDTO> source = env.addSource(new OutIfOrderSourceFunction())
                .assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<OrderDTO>(){

                    // 当前时间戳
                    long currentTimeStamp = 0L;
                    // 允许的迟到数据
                    long maxDelayAllowed = 20000L;
                    // 当前水位线
                    long currentWaterMark;
                    @Override
                    public long extractTimestamp(OrderDTO element, long recordTimestamp) {
                        Date eventTime = DateTimeUtil.strToDate(element.getTime());
                        currentTimeStamp = Math.max(eventTime.getTime(), currentTimeStamp);
                        System.out.println( "EventTime:" + eventTime + ",水位线:" + currentWaterMark);

                        return eventTime.getTime();
                    }

                    @Nullable
                    @Override
                    public Watermark getCurrentWatermark() {
                        currentWaterMark = currentTimeStamp - maxDelayAllowed;
                        //System.out.println("当前水位线:" + currentWaterMark);
                        return new Watermark(currentWaterMark);
                    }
                });

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
