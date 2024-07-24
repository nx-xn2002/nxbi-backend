package com.nx.nxbi.manager;

import cn.hutool.json.JSONObject;
import com.nx.nxbi.common.ErrorCode;
import com.nx.nxbi.exception.BusinessException;
import com.nx.nxbi.exception.ThrowUtils;
import com.squareup.okhttp.*;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class WenXinManager {
    @Value("${wenxin.api-key}")
    public String apiKey;
    @Value("${wenxin.secret-key}")
    public String secretKey;

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    public String chat(String message) {
        HTTP_CLIENT.setConnectTimeout(120, TimeUnit.SECONDS);
        HTTP_CLIENT.setReadTimeout(120, TimeUnit.SECONDS);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "{\"messages\":[{\"role\":\"user\",\"content\":\"" + message + "\"}],\"temperature\":0.2," +
                        "\"system\":\"你是一个数据分析师和前端开发专家,接下来我按照以下固定格式给你提供内容: 分析需求: {数据分析的需求或者目标} 原始数据: {CSV格式的原始数据,用," +
                        "\\\" +                         \\\"作为分隔符} 根据这两部分内容,按照以下格式生成内容(不要输出任何多余的开头、结尾、注释) {前端Echarts " +
                        "V5的option配置对象json代码,合理将数据进行可视化,\\\" +                         \\\"不要生成多余内容,如注释} {明确的数据分析结论," +
                        "越详细越好,不要生成多余的注释} 比如当我输入: 分析需求:{网站用户趋势} \\\" +                         \\\"原始数据:{日期,\\\" +   " +
                        "                      \\\"用户数\\\" +                         \\\" \\\" +                     " +
                        "    \\\"1号,\\\" +                         \\\"10 2号,20 \\\" + \\\"3号,30 4号,25 5号,0 } 请输出: " +
                        "```JSON {     \\\\\\\\\\\\\\\"xAxis\\\\\\\\\\\\\\\": {         " +
                        "\\\\\\\\\\\\\\\"type\\\\\\\\\\\\\\\": \\\" + \\\"\\\\\\\\\\\\\\\"category\\\\\\\\\\\\\\\",  " +
                        "       \\\\\\\\\\\\\\\"data\\\\\\\\\\\\\\\": [\\\\\\\\\\\\\\\"1号\\\\\\\\\\\\\\\"," +
                        "\\\\\\\\\\\\\\\"2号\\\\\\\\\\\\\\\",\\\\\\\\\\\\\\\"3号\\\\\\\\\\\\\\\"," +
                        "\\\\\\\\\\\\\\\"4号\\\\\\\\\\\\\\\",\\\" + \\\"\\\\\\\\\\\\\\\"5号\\\\\\\\\\\\\\\"]     },    " +
                        " \\\\\\\\\\\\\\\"yAxis\\\\\\\\\\\\\\\": {         \\\\\\\\\\\\\\\"type\\\\\\\\\\\\\\\": " +
                        "\\\\\\\\\\\\\\\"value\\\\\\\\\\\\\\\"     },     \\\" + " +
                        "\\\"\\\\\\\\\\\\\\\"series\\\\\\\\\\\\\\\": [{         \\\\\\\\\\\\\\\"data\\\\\\\\\\\\\\\":" +
                        " [10,20,30,25,0],         \\\\\\\\\\\\\\\"type\\\\\\\\\\\\\\\": " +
                        "\\\\\\\\\\\\\\\"line\\\\\\\\\\\\\\\"\\\" + \\\"     }] }; ``` 数据分析结论：\\\\n\\\" +            " +
                        "             \\\"\\\\n\\\" +                         " +
                        "\\\"从提供的网站用户趋势数据中，我们可以看到用户数的波动情况。\\\\n\\\" +                         \\\"\\\\n\\\" +        " +
                        "                 \\\"1. 在1号到10号期间，用户数呈现一个显著的上升趋势，从10号用户数的10人增长到10号用户数的300人，增长了近30" +
                        "倍，说明网站在这段时间内可能进行了有效的推广或活动，吸引了大量新用户。\\\\n\\\" +                         \\\"\\\\n\\\" +      " +
                        "                   \\\"2. 11号到15号期间，用户数出现了明显的下降，从300人下降到13人，这可能是由于活动结束或推广效果减弱导致的。\\\\n\\\" +" +
                        "                         \\\"\\\\n\\\" +                         \\\"3. " +
                        "16号到19号，用户数再次上升，从40人增长到200人，这可能意味着网站进行了新的推广或活动，或者用户开始意识到网站的价值并持续访问。\\\\n\\\" +              " +
                        "           \\\"\\\\n\\\" +                         \\\"4. " +
                        "20号用户数达到一个高峰，为350人，这是整个月份中的最高点，说明网站在这一天可能进行了特别的活动或推广，吸引了大量用户。\\\\n\\\" +                    " +
                        "     \\\"\\\\n\\\" +                         \\\"5. " +
                        "21号到25号，用户数再次下降，但下降速度较之前慢，这可能表明网站仍有一定的用户粘性，但需要进一步推广或优化以维持用户增长。\\\\n\\\" +                   " +
                        "      \\\"\\\\n\\\" +                         \\\"6. " +
                        "26号到30号，用户数在波动中上升，但整体增长幅度不大，说明网站需要寻找新的增长点或优化策略来进一步提升用户数量。\\\\n\\\" +                        " +
                        " \\\"\\\\n\\\" +                         " +
                        "\\\"综上所述，网站在推广和活动方面取得了一定的效果，但用户数的波动较大，需要进一步优化推广策略和用户留存机制，以维持和增长用户数量。\"," +
                        "\"disable_search\":false,\"enable_citation\":false,\"response_format\":\"text\"}");
        Request request = null;
        try {
            request = new Request.Builder().url("https://aip.baidubce.com/rpc/2" +
                    ".0/ai_custom/v1/wenxinworkshop/chat/ernie-3.5-128k?access_token=" + getAccessToken()).method(
                    "POST", body).addHeader(
                    "Content-Type", "application/json").build();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "ai 请求异常");
        }
        Response response = null;
        try {
            response = HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "ai 响应超时");
        }
        String json = null;
        try {
            json = response.body().string();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "ai 响应异常");
        }
        log.info(json);
        String result = new JSONObject(json).getStr("result");
        log.info("分析结果:" + result);
        ThrowUtils.throwIf(result == null, new BusinessException(ErrorCode.SYSTEM_ERROR, "ai 响应异常"));
        return result;
    }

    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder().url("https://aip.baidubce.com/oauth/2.0/token?client_id=" + apiKey +
                "&client_secret=" + secretKey + "&grant_type=client_credentials").method("POST", body).addHeader(
                "Content-Type", "application/json").addHeader("Accept", "application/json").build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String accessToken = new JSONObject(response.body().string()).getStr("access_token");
        log.info("access_token: " + accessToken);
        return accessToken;
    }
}
