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
     * 交易策略处理
     */
    @Scheduled(cron = "0 0 18 ? * 1-5")
    public void dealHandle() {
        stockServer.dealHandle();
    }

    /**
     * 交易策略通知
     */
    @Scheduled(cron = "0 55 14 ? * 1-5")
    public void dealInform() {
        stockServer.dealInform();
    }
}