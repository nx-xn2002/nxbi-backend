package com.nx.nxbi.mapper;

import com.nx.nxbi.model.entity.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nx.nxbi.model.entity.DataChart;

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
     * create table by data chart
     *
     * @param dataChart data chart
     * @author nx-xn2002
     */
    void createTable(DataChart dataChart);

    /**
     * 插入数据
     *
     * @param dataChart data chart
     * @author nx-xn2002
     */
    void insertData(DataChart dataChart);

    /**
     * select all
     *
     * @param tableName table name
     * @return {@link LinkedHashMap }<{@link String }, {@link String }>
     * @author nx-xn2002
     */
    List<LinkedHashMap<String, String>> selectAll(String tableName);
}




