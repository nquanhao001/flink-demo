package com.webuy.flink.demo;

import com.alibaba.fastjson.JSON;
import com.webuy.flink.reduce.OrderReduceFunction;
import com.webuy.flink.source.SpringSourceFunction;
import com.webuy.flink.dto.OrderDTO;
import com.webuy.flink.key.KeyByItemId;
import com.webuy.flink.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Comparator;
import java.util.Date;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 这个例子，一秒钟一个订单产生，针对商品id进行汇总，每5s计算一次前面10s的所有商品的销售额，并取出来top1
 * 通过观察控制台的日志，可以自己手动计算下是否正确
 */
@Slf4j
public class TopN {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<OrderDTO> source = env.addSource(new SpringSourceFunction(false));

        SingleOutputStreamOperator<OrderDTO> reduce = source.keyBy(new KeyByItemId())
                .timeWindow(Time.seconds(10), Time.seconds(5))
                .reduce(new OrderReduceFunction());

        //这个2s只要小于上面的5就行，因为上游是5秒输出一次
        reduce.windowAll(TumblingProcessingTimeWindows.of(Time.seconds(2)))
                .process(new TopNFunction(1))
                .print();//这个print可以替换为任何类型的sink，把结果给持久化


        try {
            env.execute("test");
        } catch (Exception e) {
            log.error("job运行异常", e);
        }
    }

    private static class TopNFunction extends ProcessAllWindowFunction<OrderDTO, String, TimeWindow> {

        private int topSize;

        public TopNFunction(int topSize) {
            this.topSize = topSize;
        }

        @Override
        public void process(ProcessAllWindowFunction<OrderDTO, String, TimeWindow>.Context context, Iterable<OrderDTO> elements, Collector<String> out) throws Exception {

            Date windowStart = new Date(context.window().getStart());
            Date windowend = new Date(context.window().getEnd());
            log.info("当前窗口的时间" + DateTimeUtil.dateToString(windowStart) + "-" +DateTimeUtil.dateToString(windowend));
            log.info(JSON.toJSONString(elements));
            Stream<OrderDTO> stream = StreamSupport.stream(elements.spliterator(), false);
            OrderDTO topUser = stream.sorted(Comparator.comparing(OrderDTO::getPrice).reversed()).findFirst().get();
            out.collect("恭喜" + topUser.getItemId() + "卖了" + topUser.getPrice());


        }
    }
}


