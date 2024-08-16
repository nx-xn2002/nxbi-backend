package com.nx.nxbi.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

@SpringBootTest
class ChartMapperTest {
    @Resource
    private ChartMapper chartMapper;

    @Test
    void createTable() {
        chartMapper.createTable(getFakeChart());
    }

    @Test
    void insertData() {
        chartMapper.insertData(getFakeChart());
    }

    @Test
    void selectAll() {
        chartMapper.selectAll((String) getFakeChart().get("tableName")).forEach(System.out::println);
    }

    private static HashMap<String, Object> getFakeChart() {
        HashMap<String, Object> fakeChart = new LinkedHashMap<>();
        fakeChart.put("tableName", "users");
        fakeChart.put("columns", Arrays.asList("id", "name", "email"));
        fakeChart.put("data", Arrays.asList(
                Arrays.asList("1", "John", "john@example.com"),
                Arrays.asList("2", "Bob", "bob@example.com")
        ));
        return fakeChart;
    }
}