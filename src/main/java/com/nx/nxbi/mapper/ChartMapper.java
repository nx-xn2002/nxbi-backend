package com.nx.nxbi.mapper;

import com.nx.nxbi.model.entity.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

/**
 * chart mapper
 *
 * @author nx-xn2002
 * @date 2024-08-16
 */
public interface ChartMapper extends BaseMapper<Chart> {
    /**
     * 动态创建表
     *
     * @param params params
     * @author nx-xn2002
     */
    void createTable(Map<String, Object> params);

    /**
     * 插入数据
     *
     * @param params params
     * @author nx-xn2002
     */
    void insertData(Map<String, Object> params);

    /**
     * select all
     *
     * @param tableName table name
     * @return {@link LinkedHashMap }<{@link String }, {@link String }>
     * @author nx-xn2002
     */
    List<LinkedHashMap<String, String>> selectAll(String tableName);
}




