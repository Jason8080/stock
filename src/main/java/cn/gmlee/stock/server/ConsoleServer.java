package cn.gmlee.stock.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * The type Console server.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConsoleServer {

    private final StockServer stockServer;

    private final StrategyServer strategyServer;

    /**
     * Handle.
     *
     * @param content the content
     */
    public boolean handle(String content) {
        switch (content){
            case "":
            case "help": return help();
            case "list": return stockServer.stockUpdate();
            case "pull": return stockServer.marketPull();
            case "deal": return strategyServer.dealHandle();
            default: return other();
        }
    }

    private boolean help() {
        System.out.println("当前支持的指令如下:");
        System.out.println(String.format("[%s]: %s", "list", "初始化沪深市场列表"));
        System.out.println(String.format("[%s]: %s", "pull", "拉取当前沪深股票行情(基于list)"));
        System.out.println(String.format("[%s]: %s", "deal", "根据交易规则进行交易(基于pull、rule)"));
        return true;
    }

    private boolean other() {
        return true;
    }
}
