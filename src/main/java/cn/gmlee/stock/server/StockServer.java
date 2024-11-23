package cn.gmlee.stock.server;

import cn.gmlee.stock.dao.entity.Stock2024;
import cn.gmlee.stock.dao.entity.StockList;
import cn.gmlee.stock.mod.Stock;
import cn.gmlee.stock.mod.StockToStockYear;
import cn.gmlee.stock.service.Stock2024Service;
import cn.gmlee.stock.service.StockListService;
import cn.gmlee.stock.util.ByKit;
import cn.gmlee.stock.util.TencentKit;
import cn.gmlee.tools.base.enums.Function;
import cn.gmlee.tools.base.util.LocalDateTimeUtil;
import cn.gmlee.tools.base.util.QuickUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Stock server.
 */
@Component
@RequiredArgsConstructor
public class StockServer {
    private final Stock2024Service stock2024Service;
    private final StockListService stockListService;
    private final StockToStockYear stockToStockYear;

    /**
     * Stock update.
     */
    public void stockUpdate() {
        List<StockList> list = stockListService.list(Wrappers.<StockList>lambdaQuery()
                .gt(StockList::getTimestamp, LocalDateTimeUtil.offsetCurrent(-1, ChronoUnit.MONTHS)));
        if(list.size() > 0){
            return;
        }
        List<StockList> entities = ByKit.getStockLists();
        stockListService.saveBatch(entities);
    }

    /**
     * Market pull.
     */
    public void marketPull() {
        List<StockList> all = stockListService.list();
        List<List<Stock>> lists = QuickUtil.batch(all, 100, (Function.P2r<List<StockList>, List<Stock>>) TencentKit::getStocks);
        List<Stock2024> entities = lists.stream().flatMap(List::stream).map(stockToStockYear::toEntity).collect(Collectors.toList());
        stock2024Service.saveOrUpdateBatch(entities);
    }
}
