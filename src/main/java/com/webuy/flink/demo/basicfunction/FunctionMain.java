package com.webuy.flink.demo.basicfunction;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * 主要是演练一下基础的 map，flatmap，filter等基础功能
 */
public class FunctionMain {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //获取数据源
        DataStreamSource<Item> items = env.addSource(new MyStreamingSource()).setParallelism(1);

        /*===========Map===============*/
        //Map
        SingleOutputStreamOperator<String> mapItems = items.map(item -> item.getName());
//        SingleOutputStreamOperator<String> mapItems = items.map(new MyMapFunction());
//
//        mapItems.printToErr().setParallelism(2);
        /*===========Map===============*/

        /*===========flatMap===============*/



        SingleOutputStreamOperator<String> flatMapItems = items.flatMap(new FlatMapFunction<Item, String>() {
            @Override
            public void flatMap(Item item, Collector<String> collector) throws Exception {
                String name = item.getName();
                collector.collect(name);
            }
        });

        /*===========flatMap===============*/

        /*===========fliter===============*/
        //filter
//        SingleOutputStreamOperator<Item> filterItems = items.filter( item -> item.getId() % 2 != 0);

        //打印结果
//        filterItems.print().setParallelism(1);
        /*===========fliter===============*/
        String jobName = "user defined streaming source";
        env.execute(jobName);
    }


    static class MyMapFunction extends RichMapFunction<Item,String> {

        @Override
        public String map(Item item) throws Exception {
            return item.getName();
        }
    }
}
