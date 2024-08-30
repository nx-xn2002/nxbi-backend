package com.nx.nxbi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * data chart
 *
 * @author nx-xn2002
 * @date 2024-08-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataChart {

    private String tableName;
    private String tableId;
    private List<String> columns;
    private List<List<String>> data;

    /**
     * build data chart
     *
     * @param mapList   map list
     * @param tableName table name
     * @param tableId   table id
     * @return {@link DataChart }
     * @author nx-xn2002
     */
    public static DataChart buildDataChart(List<LinkedHashMap<String, String>> mapList, String tableName,
                                           String tableId) {
        if (mapList == null) {
            return null;
        }
        DataChart dataChart = new DataChart();
        List<List<String>> data = new ArrayList<>();

        LinkedHashMap<String, String> firstData = mapList.get(0);
        List<String> columns = new ArrayList<>(firstData.keySet());

        for (LinkedHashMap<String, String> map : mapList) {
            List<String> oneData = new ArrayList<>();
            map.keySet().forEach(key -> oneData.add(map.get(key)));
            data.add(new ArrayList<>(oneData));
        }

        dataChart.setTableName(tableName);
        dataChart.setTableId(tableId);
        dataChart.setColumns(columns);
        dataChart.setData(data);
        return dataChart;
    }

    public static DataChart buildDataChart(List<LinkedHashMap<String, String>> mapList) {
        return buildDataChart(mapList, null, null);
    }
}
