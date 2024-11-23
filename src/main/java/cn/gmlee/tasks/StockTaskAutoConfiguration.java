package cn.gmlee.tasks;

import cn.gmlee.stock.dao.entity.StockList;
import cn.gmlee.stock.service.StockListService;
import cn.gmlee.stock.util.ByKit;
import cn.gmlee.tools.base.util.LocalDateTimeUtil;
import cn.gmlee.tools.base.util.TimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StockTaskAutoConfiguration {

    private final StockListService stockListService;

    /**
     * 更新股票列表
     */
    @Scheduled(cron = "0 0 0 * * 6")
    public void stockUpdate(){
        List<StockList> list = stockListService.list(Wrappers.<StockList>lambdaQuery()
                .gt(StockList::getTimestamp, LocalDateTimeUtil.offsetCurrent(-1, ChronoUnit.MONTHS)));
        if(list.size() > 0){
            return;
        }
        List<StockList> entities = ByKit.getStockLists();
        stockListService.saveBatch(entities);
    }

    /**
     * 行情数据保存
     */
    @Scheduled(cron = "0 0 17 ? * 1-5")
    public void marketSave() {
        System.out.println("现在时间是：" + TimeUtil.getCurrentDatetime());
    }

    /**
     * 价格策略监控
     */
    @Scheduled(cron = "0 55 14 ? * 1-5")
    public void priceMonitor() {
        System.out.println("现在时间是：" + TimeUtil.getCurrentDatetime());
    }
}