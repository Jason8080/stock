package cn.gmlee.tasks;

import cn.gmlee.stock.server.StockServer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockTaskAutoConfiguration {

    private final StockServer stockServer;

    /**
     * 更新股票列表
     */
    @Scheduled(cron = "0 0 0 * * 6")
    public void stockUpdate(){
        stockServer.stockUpdate();
    }

    /**
     * 行情数据保存
     */
    @Scheduled(cron = "0 0 17 ? * 1-5")
    public void marketPull() {
        stockServer.marketPull();
    }

    /**
     * 价格策略监控
     */
    @Scheduled(cron = "0 55 14 ? * 1-5")
    public void priceMonitor() {
    }
}