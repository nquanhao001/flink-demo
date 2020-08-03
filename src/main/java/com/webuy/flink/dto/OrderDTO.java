package com.webuy.flink.dto;

import lombok.Data;


@Data
public class OrderDTO {

    /**
     * 用户ID
     */
    private Integer itemId;
    /**
     * 价格
     */
    private Integer price;
    /**
     * 时间
     */
    private String time;

    public OrderDTO(Integer itemId, Integer price) {
        this.itemId = itemId;
        this.price = price;
    }

    public OrderDTO(Integer itemId, Integer price, String time) {
        this.itemId = itemId;
        this.price = price;
        this.time = time;
    }

    public OrderDTO() {
    }


}
