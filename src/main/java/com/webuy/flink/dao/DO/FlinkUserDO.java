package com.webuy.flink.dao.DO;

import com.webuy.flink.dto.OrderDTO;
import lombok.Data;

@Data
public class FlinkUserDO {

    /**
     * 主键id
     */
	private Long id;
    /**
     * 商品id
     */
	private Integer itemId;
    /**
     * 价格
     */
	private Integer price;
    /**
     * 时间字符串
     */
	private String time;


	public static FlinkUserDO convert(OrderDTO orderDTO) {

	    FlinkUserDO flinkUserDO = new FlinkUserDO();
	    flinkUserDO.setPrice(orderDTO.getPrice());
	    flinkUserDO.setTime(orderDTO.getTime());
	    flinkUserDO.setItemId(orderDTO.getItemId());
	    return flinkUserDO;
    }
}

