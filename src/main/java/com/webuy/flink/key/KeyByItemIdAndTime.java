package com.webuy.flink.key;

import com.webuy.flink.dto.OrderDTO;
import org.apache.flink.api.java.functions.KeySelector;

public class KeyByItemIdAndTime implements KeySelector<OrderDTO, String> {

    @Override
    public String getKey(OrderDTO orderDTO) throws Exception {
        return orderDTO.getItemId() + "" + orderDTO.getTime();
    }
}
