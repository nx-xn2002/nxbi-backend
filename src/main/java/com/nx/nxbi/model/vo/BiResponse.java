package com.nx.nxbi.model.vo;

import lombok.Data;

/**
 * bi返回结果
 *
 * @author nx
 */
@Data
public class BiResponse {
    /**
     * 生成图表
     */
    private String genChart;
    /**
     * 分析结果
     */
    private String genResult;
    /**
     * 分析图表Id
     */
    private Long chartId;
}
