package com.nx.nxbi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nx.nxbi.annotation.AuthCheck;
import com.nx.nxbi.common.BaseResponse;
import com.nx.nxbi.common.DeleteRequest;
import com.nx.nxbi.common.ErrorCode;
import com.nx.nxbi.common.ResultUtils;
import com.nx.nxbi.constant.CommonConstant;
import com.nx.nxbi.constant.UserConstant;
import com.nx.nxbi.exception.BusinessException;
import com.nx.nxbi.exception.ThrowUtils;
import com.nx.nxbi.manager.WenXinManager;
import com.nx.nxbi.model.dto.chart.*;
import com.nx.nxbi.model.entity.Chart;
import com.nx.nxbi.model.entity.User;
import com.nx.nxbi.model.vo.BiResponse;
import com.nx.nxbi.service.ChartService;
import com.nx.nxbi.service.UserService;
import com.nx.nxbi.utils.ExcelUtils;
import com.nx.nxbi.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 图表信息接口
 *
 * @author nx
 */
@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Resource
    private ChartService chartService;

    @Resource
    private UserService userService;
    @Resource
    private WenXinManager wenXinManager;

    // region 增删改查

    /**
     * 创建
     *
     * @return {@link BaseResponse }<{@link Long }>
     * @author nx
     */
    @PostMapping("/add")
    public BaseResponse<Long> addChart(@RequestBody ChartAddRequest chartAddRequest, HttpServletRequest request) {
        if (chartAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartAddRequest, chart);
        User loginUser = userService.getLoginUser(request);
        chart.setUserId(loginUser.getId());
        boolean result = chartService.save(chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newChartId = chart.getId();
        return ResultUtils.success(newChartId);
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteChart(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldChart.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = chartService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @return {@link BaseResponse }<{@link Boolean }>
     * @author nx
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateChart(@RequestBody ChartUpdateRequest chartUpdateRequest) {
        if (chartUpdateRequest == null || chartUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartUpdateRequest, chart);
        long id = chartUpdateRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     */
    @GetMapping("/get")
    public BaseResponse<Chart> getChartById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = chartService.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(chart);
    }

    /**
     * 分页获取列表（封装类）
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Chart>> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest) {
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartPage);
    }

    /**
     * 分页获取当前用户创建的资源列表
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<Chart>> listMyChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
                                                       HttpServletRequest request) {
        if (chartQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        chartQueryRequest.setUserId(loginUser.getId());
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartPage);
    }

    /**
     * 编辑（用户）
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editChart(@RequestBody ChartEditRequest chartEditRequest, HttpServletRequest request) {
        if (chartEditRequest == null || chartEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartEditRequest, chart);
        User loginUser = userService.getLoginUser(request);
        long id = chartEditRequest.getId();
        // 判断是否存在
        Chart oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldChart.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }

    /**
     * 获取查询包装类
     *
     * @return {@link QueryWrapper }<{@link Chart }>
     * @author nx
     */
    public QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest) {
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        if (chartQueryRequest == null) {
            return queryWrapper;
        }
        Long id = chartQueryRequest.getId();
        Long userId = chartQueryRequest.getUserId();
        String goal = chartQueryRequest.getGoal();
        String chartType = chartQueryRequest.getChartType();
        String name = chartQueryRequest.getName();


        String sortField = chartQueryRequest.getSortField();
        String sortOrder = chartQueryRequest.getSortOrder();

        queryWrapper.eq(ObjectUtils.isNotEmpty(id) && id > 0, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(goal), "goal", goal);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chartType", chartType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);

        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 智能分析
     *
     * @return {@link BaseResponse }<{@link String }>
     * @author nx
     */
    @PostMapping("/gen")
    public BaseResponse<BiResponse> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                                 GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        //用户输入
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String originGoal = goal;
        String chartType = genChartByAiRequest.getChartType();
        String originChartType = null;
        //拼接目标
        if (StringUtils.isNotBlank(chartType)) {
            originChartType = chartType;
            goal = goal + ",请使用" + chartType;
        } else {
            originChartType = "默认";
        }
        //校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");

        //用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析目标:{").append(goal).append("}\\n");
        //压缩后的数据
        String data = ExcelUtils.excelToCsv(multipartFile);
        userInput.append("原始数据:{").append(data).append("}\\n");
        String chat = wenXinManager.chat(userInput.toString());
        String[] strings = handlerBiResult(chat);

        BiResponse biResponse = new BiResponse();
        biResponse.setGenChart(strings[0]);
        biResponse.setGenResult(strings[1]);
        User loginUser = userService.getLoginUser(request);
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(originGoal);
        chart.setChartData(data);
        chart.setChartType(originChartType);
        chart.setGenChart(strings[0]);
        chart.setGenResult(strings[1]);
        chart.setUserId(loginUser.getId());
        boolean saveResult = chartService.save(chart);
        biResponse.setChartId(chart.getId());
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "图表保存失败");
        return ResultUtils.success(biResponse);
    }

    public String[] handlerBiResult(String chat) {
        String genChart = null, genResult = null;

        int begin = -1, end = -1;
        for (int i = 0; i < chat.length(); i++) {
            if (chat.charAt(i) == '{') {
                begin = i;
                break;
            }
        }
        for (int i = chat.length() - 1; i >= 0; i--) {
            if (chat.charAt(i) == '}') {
                end = i + 1;
                break;
            }
        }
        if (begin != -1 && end != -1) {
            genChart = chat.substring(begin, end);
        }

        String regex2 = "数据分析结论：(.*)";
        Pattern pattern2 = Pattern.compile(regex2);
        Matcher matcher2 = pattern2.matcher(chat);
        if (matcher2.find()) {
            genResult = matcher2.group();
        }
        if (genResult != null) {
            genResult = genResult.replaceAll("数据分析结论：", "");
            genResult = genResult.replaceAll("根据提供的原始数据，我们创建了一个Echarts的option配置对象，将数据进行了可视化。", "");
        }
        if (genChart == null && genResult == null) {
            log.info(chat);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "ai 响应异常");
        }
        return new String[]{genChart, genResult};
    }
}
