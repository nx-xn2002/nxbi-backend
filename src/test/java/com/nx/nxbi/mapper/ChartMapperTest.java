package com.nx.nxbi.mapper;

import com.nx.nxbi.model.entity.DataChart;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestMethod;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@SpringBootTest
class ChartMapperTest {
    @Resource
    private ChartMapper chartMapper;

    @Test
    void createTable() {
        DataChart dataChart = new DataChart("users", Arrays.asList("id", "name", "email"),
                Arrays.asList(Arrays.asList("1", "John", "john@example.com"), Arrays.asList("2", "Bob", "bob@example" +
                        ".com")));
        chartMapper.createTable(getFakeChart());
    }

    @Test
    void insertData() {
        chartMapper.insertData(getFakeChart());
    }

    @Test
    void selectAll() {
        List<LinkedHashMap<String, String>> linkedHashMaps =
                chartMapper.selectAll((String) getFakeChart().getTableName());
        linkedHashMaps.forEach(System.out::println);
    }

    private static DataChart getFakeChart() {
        return new DataChart("users", Arrays.asList("id", "name", "email"),
                Arrays.asList(Arrays.asList("1", "John", "john@example.com"), Arrays.asList("2", "Bob", "bob@example" +
                        ".com")));
    }
}