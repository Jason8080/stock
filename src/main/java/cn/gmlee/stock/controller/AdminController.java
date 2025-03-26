package cn.gmlee.stock.controller;

import cn.gmlee.stock.server.StockServer;
import cn.gmlee.stock.server.StrategyServer;
import cn.gmlee.stock.service.Stock2024Service;
import cn.gmlee.tools.base.mod.R;
import cn.gmlee.tools.base.util.AssertUtil;
import cn.gmlee.util.ConsoleKit;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 管理接口.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {

    private final StockServer stockServer;

    private final StrategyServer strategyServer;

    private final Stock2024Service stock2024Service;

    /**
     * 最近交易日
     *
     * @return r
     */
    @GetMapping("lastDay")
    public R<String> lastDay() {
        return R.OK.newly(stock2024Service.lastDay());
    }

    /**
     * 最近交易日交易
     *
     * @return r
     */
    @GetMapping("lastDeal")
    public R<String> lastDeal() {
        stockServer.marketPull();
        if(LocalDateTime.now().getHour() > 15){
            return lastDay();
        }
        strategyServer.dealHandle();
        return lastDay();
    }

    /**
     * 重置策略交易
     *
     * @return r
     */
    @GetMapping("reDeal/{id}")
    public R<String> reDeal(@PathVariable String id, @RequestParam String password) {
        AssertUtil.eq(password, "123.", "密码错误");
        ConsoleKit.put(new ConsoleKit.Obj("deal ".concat(id)));
        try {
            strategyServer.dealHandle();
        } finally {
            ConsoleKit.remove();
        }
        return R.OK;
    }

}
