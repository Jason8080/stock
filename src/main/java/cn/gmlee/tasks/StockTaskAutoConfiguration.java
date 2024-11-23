package cn.gmlee.tasks;

import cn.gmlee.tools.base.util.TimeUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StockTaskAutoConfiguration {
 
    @Scheduled(cron = "0 55 14 ? * 1/5")
    public void priceMonitor() {
        System.out.println("现在时间是：" + TimeUtil.getCurrentDatetime());
    }
}