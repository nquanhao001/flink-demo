package com.webuy.flink.sink;

import com.webuy.flink.demo.helloword.WordWithCount;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

public class PrintSinkFunction extends RichSinkFunction<WordWithCount> {

    @Override
    public void invoke(WordWithCount value, Context context) throws Exception {

        System.out.println("我的结果哈哈哈" + value);
    }
}
