package com.nx.nxbi.utils;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.nx.nxbi.model.entity.DataChart;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel相关工具类
 *
 * @author nx
 */
@Slf4j
public class ExcelUtils {
    /**
     * excel to csv
     *
     * @param multipartFile multipart file
     * @return {@link String }
     * @author nx-xn2002
     */
    public static String excelToCsv(MultipartFile multipartFile) {
        //读取数据
        List<Map<Integer, String>> list = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String suffix = FileUtil.getSuffix(originalFilename);
            ExcelTypeEnum excelType = null;
            if ("xlsx".equals(suffix)) {
                excelType = ExcelTypeEnum.XLSX;
            } else {
                excelType = ExcelTypeEnum.XLS;
            }
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(excelType)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("表格处理错误:", e);
            throw new RuntimeException(e);
        }
        if (CollUtil.isEmpty(list)) {
            return "";
        }
        //转换CSV
        StringBuilder stringBuilder = new StringBuilder();
        //读取表头
        LinkedHashMap<Integer, String> headerMap = (LinkedHashMap<Integer, String>) list.get(0);
        List<String> headerList =
                headerMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
        stringBuilder.append(StringUtils.join(headerList, ",")).append("\\n");
        for (int i = 1; i < list.size(); i++) {
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap<Integer, String>) list.get(i);
            List<String> dataList =
                    dataMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
            stringBuilder.append(StringUtils.join(dataList, ",")).append("\\n");
        }
        return stringBuilder.toString();
    }

    /**
     * excel to data chart
     *
     * @param file file
     * @return {@link HashMap }<{@link String }, {@link Object }>
     * @author nx-xn2002
     */
    public static DataChart excelToDataChart(MultipartFile file) {
        DataChart dataChart = new DataChart();
        dataChart.setTableId("chart-" + IdUtil.fastSimpleUUID());
        String originalFilename = file.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);
        String tableName = suffix == null ? originalFilename : originalFilename.split(suffix)[0];
        dataChart.setTableName(tableName);

        return dataChart;
    }
}
