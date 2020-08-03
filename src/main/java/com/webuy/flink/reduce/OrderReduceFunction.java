package com.webuy.flink.reduce;

import com.webuy.flink.dto.OrderDTO;
import org.apache.flink.api.common.functions.ReduceFunction;

import java.text.SimpleDateFormat;


public class OrderReduceFunction implements ReduceFunction<OrderDTO> {

    public static SimpleDateFormat yearMonthDayHourMinutesOfChinese = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

    @Override
    public OrderDTO reduce(OrderDTO t1, OrderDTO t2) throws Exception {

        return new OrderDTO(t1.getItemId(),t1.getPrice() + t2.getPrice(),t1.getTime());
    }
}
