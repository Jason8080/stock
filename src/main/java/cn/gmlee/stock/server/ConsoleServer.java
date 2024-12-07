package cn.gmlee.stock.server;

import cn.gmlee.tools.base.util.BoolUtil;
import cn.gmlee.util.ConsoleKit;
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

    private final StatsServer statsServer;

    private final StrategyServer strategyServer;

    /**
     * Handle.
     *
     * @param content the content
     */
    public boolean handle(String content) {
        if (BoolUtil.isEmpty(content) || content.startsWith("help")) {
            return help(content);
        }
        if (content.startsWith("list")) {
            return stockServer.stockUpdate();
        }
        if (content.startsWith("pull")) {
            return stockServer.marketPull();
        }
        if (content.startsWith("deal")) {
            return strategyServer.dealHandle();
        }
        if (content.startsWith("user")) {
            return strategyServer.userMessage();
        }
        if (content.startsWith("group")) {
            return strategyServer.groupMessage();
        }
        if (content.startsWith("stats")) {
            return statsServer.statsHandle();
        }
        if (content.startsWith("?")) {
            return strategyServer.userMessage();
        }
        return other(content);
    }

    private boolean help(String content) {
        System.out.println("当前支持的指令如下:");
        System.out.println(String.format("[%s]: %s", "list", "初始化沪深市场列表"));
        System.out.println(String.format("[%s]: %s", "pull", "拉取当前沪深股票行情(基于list)"));
        System.out.println(String.format("[%s]: %s", "deal", "根据交易规则进行交易(基于pull)"));
        System.out.println(String.format("[%s]: %s", "user", "根据订阅内容推送消息(基于rule)"));
        return true;
    }

    private boolean other(String content) {
        if (ConsoleKit.isStock(content)) {
            return strategyServer.userMessage();
        }
        if (BoolUtil.isDigit(content)) {
            System.out.println("你想查股票吗?请输入正确的股票代码!");
            return false;
        }
        return true;
    }
}
