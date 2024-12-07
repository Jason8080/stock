package cn.gmlee.stock.server;

import cn.gmlee.stock.dao.entity.StockStats;
import cn.gmlee.stock.service.StockStatsService;
import cn.gmlee.tools.base.util.CollectionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * The type Stock server.
 */
@Component
@RequiredArgsConstructor
public class StatsServer {
    private final StockStatsService stockStatsService;

    /**
     * Stats handle.
     *
     * @return
     */
    public boolean statsHandle() {
        List<StockStats> soldStats = stockStatsService.stats(null, null, true);
        List<StockStats> lockStats = stockStatsService.stats(null, null, false);
        Collection<StockStats> entities = CollectionUtil.merge(soldStats, lockStats);
        stockStatsService.insertOrUpdateBatch(entities);
        return true;
    }
}
