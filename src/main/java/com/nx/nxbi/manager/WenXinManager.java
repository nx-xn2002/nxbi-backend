package com.nx.nxbi.manager;

import cn.hutool.json.JSONObject;
import com.nx.nxbi.common.ErrorCode;
import com.nx.nxbi.exception.BusinessException;
import com.squareup.okhttp.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 调用文心一言api
 *
 * @author 18702
 */
@Service
public class WenXinManager {
    @Value("${wenxin.api-key}")
    public static String API_KEY;
    @Value("${wenxin.secret-key}")
    public static String SECRET_KEY;

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    public String chat(String message) {
        HTTP_CLIENT.setConnectTimeout(20, TimeUnit.SECONDS);
        HTTP_CLIENT.setReadTimeout(20, TimeUnit.SECONDS);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "{\"messages\":[{\"role\":\"user\",\"content\":\"" + message + "\"}],\"temperature\":0.2," +
                        "\"system\":\"你是一个数据分析师和前端开发专家,接下来我按照以下固定格式给你提供内容: 分析需求: {数据分析的需求或者目标} 原始数据: {CSV格式的原始数据,用," + "作为分隔符} 请根据这两部分内容,按照以下格式生成内容(此外不要输出任何多余的开头、结尾、注释) {前端Echarts V5的option配置对象json代码,合理地将数据进行可视化," + "不要生成任何多余内容,比如注释} {明确的数据分析结论,越详细越好，不要生成多余的注释} 比如当我输入: 分析需求:{网站用户趋势} 原始数据:{日期,用户数 1号,10 2号,20 " + "3号,30 4号,25 5号,0 } 请输出: ```JSON {     \\\"xAxis\\\": {         \\\"type\\\": " + "\\\"category\\\",         \\\"data\\\": [\\\"1号\\\",\\\"2号\\\",\\\"3号\\\",\\\"4号\\\"," + "\\\"5号\\\"]     },     \\\"yAxis\\\": {         \\\"type\\\": \\\"value\\\"     },     " + "\\\"series\\\": [{         \\\"data\\\": [10,20,30,25,0],         \\\"type\\\": \\\"line\\\"" + "     }] }; ``` 数据分析结论：根据提供的原始数据，我们创建了一个Echarts的option配置对象，将数据进行了可视化。从数据中可以看出，用户数在1号到4" + "号呈上升趋势，5号用户数为0。\"}");
        Request request = null;
        try {
            request = new Request.Builder().url("https://aip.baidubce.com/rpc/2" +
                    ".0/ai_custom/v1/wenxinworkshop/chat/completions?access_token" + "=" + getAccessToken()).method(
                    "POST", body).addHeader("Content-Type", "application/json").build();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "ai 响应异常");
        }
        Response response = null;
        try {
            response = HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "ai 响应异常");
        }
        String json = null;
        try {
            json = response.body().string();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "ai 响应异常");
        }
        return new JSONObject(json).getStr("result");
    }

    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY +
                "&client_secret=" + SECRET_KEY);
        Request request =
                new Request.Builder().url("https://aip.baidubce.com/oauth/2.0/token").method("POST", body).addHeader(
                        "Content-Type", "application/x-www-form-urlencoded").build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getStr("access_token");
    }
}
