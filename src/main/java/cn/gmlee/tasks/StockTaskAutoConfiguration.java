package cn.gmlee.tasks;

import cn.gmlee.stock.server.StatsServer;
import cn.gmlee.stock.server.StockServer;
import cn.gmlee.stock.server.StrategyServer;
import cn.gmlee.tools.base.util.BoolUtil;
import cn.gmlee.tools.base.util.NullUtil;
import cn.gmlee.tools.ds.dynamic.DynamicDataSourceHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@Profile("prd")
@RequiredArgsConstructor
public class StockTaskAutoConfiguration {

    private final StockServer stockServer;
    private final StatsServer statsServer;
    private final StrategyServer strategyServer;

    private String db = System.getProperty("db");

    @PostConstruct
    public void init(){
        log.warn("当前持久化采用的是: {}", NullUtil.get(db, "mysql"));
    }

    /**
     * 更新股票列表
     */
    @Scheduled(cron = "0 0 0 * * 6")
    public void stockUpdate() {
        if (BoolUtil.notEmpty(db)) {
            log.warn("当前持久化采用的是: {}", db);
            DynamicDataSourceHolder.set(db);
        }
        stockServer.stockUpdate();
    }

    /**
     * 策略统计处理
     */
    @Scheduled(cron = "0 0 17 ? * 1-5")
    public void statsHandle() {
        if (BoolUtil.notEmpty(db)) {
            log.warn("当前持久化采用的是: {}", db);
            DynamicDataSourceHolder.set(db);
        }
        statsServer.statsHandle();
    }

    /**
     * 交易策略处理
     */
    @Scheduled(cron = "0 5 15 ? * 1-5")
    public void dealHandle() {
        if (BoolUtil.notEmpty(db)) {
            log.warn("当前持久化采用的是: {}", db);
            DynamicDataSourceHolder.set(db);
        }
        strategyServer.dealHandle();
    }

    /**
     * 行情数据保存
     */
    @Scheduled(cron = "30 0 15 ? * 1-5")
    public void marketPull() {
        if (BoolUtil.notEmpty(db)) {
            log.warn("当前持久化采用的是: {}", db);
            DynamicDataSourceHolder.set(db);
        }
        stockServer.marketPull();
    }

    /**
     * 订阅量化消息
     */
//    @Scheduled(cron = "0 55 14 ? * 1-5")
    public void userMessage() {
        if (BoolUtil.notEmpty(db)) {
            log.warn("当前持久化采用的是: {}", db);
            DynamicDataSourceHolder.set(db);
        }
        strategyServer.userMessage();
    }

    /**
     * 群发量化消息
     */
//    @Scheduled(cron = "30 0 15 ? * 1-5")
    @Scheduled(cron = "0 50,53,56 14 ? * 1-5")
    public void groupMessage() {
        if (BoolUtil.notEmpty(db)) {
            log.warn("当前持久化采用的是: {}", db);
            DynamicDataSourceHolder.set(db);
        }
        strategyServer.groupMessage();
    }
}