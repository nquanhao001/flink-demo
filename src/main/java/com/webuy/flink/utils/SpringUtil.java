package com.webuy.flink.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware {

    private final static Logger logger = LoggerFactory.getLogger(SpringUtil.class);

    public static AbstractApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = (AbstractApplicationContext) applicationContext;
        if (logger.isInfoEnabled()) {
            logger.info("shopkeeperSaleStatistics setApplicationContext.");
        }
    }

    public static AbstractApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(AbstractApplicationContext applicationContext) {
        SpringUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }
}
