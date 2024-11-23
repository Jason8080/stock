package cn.gmlee.stock.server;

import cn.gmlee.stock.dao.entity.StockStrategy;
import cn.gmlee.stock.dao.entity.StockStrategyRule;
import cn.gmlee.stock.service.StockStrategyDealService;
import cn.gmlee.stock.service.StockStrategyRuleService;
import cn.gmlee.stock.service.StockStrategyService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Strategy server.
 */
@Component
@RequiredArgsConstructor
public class StrategyServer {

    private final StockStrategyService stockStrategyService;
    private final StockStrategyRuleService stockStrategyRuleService;
    private final StockStrategyDealService stockStrategyDealService;

    /**
     * Deal handle.
     */
    public void dealHandle() {
        List<StockStrategy> list = stockStrategyService.list(Wrappers.<StockStrategy>lambdaUpdate()
                .eq(StockStrategy::getStatus, 1)
        );
    }

    /**
     * Deal inform.
     */
    public void dealInform() {

    }
}
