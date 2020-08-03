package com.webuy.flink.sink;

import com.webuy.flink.dao.DO.FlinkUserDO;
import com.webuy.flink.dao.FlinkUserDAO;
import com.webuy.flink.dto.OrderDTO;
import com.webuy.flink.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;


@Slf4j
public class OrderSinkFunction extends RichSinkFunction<OrderDTO> {

    private FlinkUserDAO flinkUserDAO;

    @Override
    public void invoke(OrderDTO value, Context context) throws Exception {

        if (flinkUserDAO == null) {
            flinkUserDAO = SpringUtil.getBean("flinkUserDAO", FlinkUserDAO.class);
        }
        log.info("插入数据库的订单数据:" + value);
        this.flinkUserDAO.insertSelective(FlinkUserDO.convert(value));
    }
}
