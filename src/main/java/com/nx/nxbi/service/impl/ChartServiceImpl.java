package com.nx.nxbi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nx.nxbi.model.entity.Chart;
import com.nx.nxbi.service.ChartService;
import com.nx.nxbi.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author 18702
* @description 针对表【chart(图表信息)】的数据库操作Service实现
* @createDate 2023-11-20 00:10:19
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

}




