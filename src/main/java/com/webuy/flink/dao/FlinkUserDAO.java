package com.webuy.flink.dao;

import com.webuy.flink.dao.DO.FlinkUserDO;
import org.springframework.stereotype.Repository;


@Repository
public interface FlinkUserDAO {

    /**
    * 选择性插入
    * @param insertDO
    */
    void insertSelective(FlinkUserDO insertDO);
}
