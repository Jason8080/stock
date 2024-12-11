package cn.gmlee.stock.controller;

import cn.gmlee.stock.controller.vo.StrategyDealVo;
import cn.gmlee.stock.dao.entity.Stock2024;
import cn.gmlee.stock.dao.entity.StockStrategy;
import cn.gmlee.stock.service.Stock2024Service;
import cn.gmlee.stock.service.StockStatsService;
import cn.gmlee.stock.service.StockStrategyService;
import cn.gmlee.tools.base.entity.Key;
import cn.gmlee.tools.base.mod.PageRequest;
import cn.gmlee.tools.base.mod.PageResponse;
import cn.gmlee.tools.base.mod.R;
import cn.gmlee.tools.base.util.BoolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 股票接口.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("stock")
public class StockController {

    private final StockStatsService stockStatsService;

    private final Stock2024Service stock2024Service;

    private final StockStrategyService stockStrategyService;

    /**
     * 股票列表.
     *
     * @param pr  the pr
     * @param key the key
     * @return the r
     */
    @GetMapping("list")
    public R<PageResponse> list(PageRequest pr, Key key) {
        IPage page = new Page(pr.current, pr.size);
        Stock2024 date = stock2024Service.getOne(Wrappers.<Stock2024>lambdaQuery()
                .select(Stock2024::getDate)
                .orderByDesc(Stock2024::getDate), false);
        IPage<Stock2024> iPage = stock2024Service.page(page, Wrappers.<Stock2024>lambdaQuery()
                .and(BoolUtil.notEmpty(key.uniqueKey), wrapper -> wrapper
                        .like(Stock2024::getName, key.uniqueKey)
                        .or()
                        .like(Stock2024::getCode, key.uniqueKey)
                )
                .eq(Stock2024::getDate, date.getDate())
        );
        return R.OK.newly(PageResponse.of(pr, iPage.getTotal(), iPage.getRecords()));
    }

    /**
     * 交易列表.
     *
     * @param pr  the pr
     * @param key the key
     * @return the r
     */
    @GetMapping("deal")
    public R<PageResponse> deal(PageRequest pr, Key key, StrategyDealVo vo) {
        IPage page = new Page(pr.current, pr.size);
        IPage<StockStrategy> iPage = stockStrategyService.page(page, Wrappers.<StockStrategy>lambdaQuery()
                .and(BoolUtil.notEmpty(key.uniqueKey), wrapper -> wrapper
                        .like(StockStrategy::getName, key.uniqueKey)
                        .or()
                        .like(StockStrategy::getAuthor, key.uniqueKey)
                )
                .eq(StockStrategy::getStatus, 1)
        );
        List<StrategyDealVo> vos = iPage.getRecords().stream().map(x -> toStockDeal(x, vo)).collect(Collectors.toList());
        return R.OK.newly(PageResponse.of(pr, iPage.getTotal(), vos));
    }

    private StrategyDealVo toStockDeal(StockStrategy strategy, StrategyDealVo vo) {
        return null;
    }

}
