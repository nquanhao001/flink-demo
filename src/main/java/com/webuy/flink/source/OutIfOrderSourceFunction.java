package com.webuy.flink.source;

import com.alibaba.fastjson.JSON;
import com.webuy.flink.dto.OrderDTO;
import com.webuy.flink.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class OutIfOrderSourceFunction extends RichSourceFunction<OrderDTO> {

    private ApplicationContext applicationContext;

    private void init() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("classpath*:spring-config.xml");
        } catch (Exception e) {
            log.error("Spring上下文初始化失败",e);
        }
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        if (applicationContext == null) {
            init();
        }
    }

    @Override
    public void run(SourceContext<OrderDTO> sourceContext) {

        while(true) {
            for (int i = 0; i < 100; i++) {
                OrderDTO a1 = new OrderDTO(getItemId(), 1, getTime());
                log.info("发生订单" +JSON.toJSONString(Arrays.asList(a1)));
                if (i < 10){
                    //延迟10s再发送
                    Timer timer = new Timer();// 实例化Timer类
                    timer.schedule(new TimerTask() {
                        public void run() {
                            sourceContext.collect(a1);
                            this.cancel();
                        }
                    }, 10000);// 这里百毫秒
                }else {
                    sourceContext.collect(a1);
                }
                try {
                    // 假装1秒接收一次消息
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private Integer getItemId() {

        return 1;
    }

    private Integer getPrice() {

        return 1;
    }

    private String getTime() {

        return DateTimeUtil.dateToString(new Date());
    }

    @Override
    public void cancel() {

    }
}
