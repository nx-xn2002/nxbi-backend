package com.nx.nxbi.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WenXinManagerTest {
    @Resource
    private WenXinManager wenXinManager;
    @Test
    void chat() throws IOException {
        System.out.println(wenXinManager.chat("分析需求:{网站用户趋势}\n" +
                "原始数据:{日期,用户数\n" +
                "1号,10\n" +
                "2号,20\n" +
                "3号,30\n" +
                "4号,25\n" +
                "5号,0\n" +
                "}"));
    }
}