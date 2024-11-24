package cn.gmlee.stock.server;

import cn.gmlee.tools.base.util.BoolUtil;
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

    private final StrategyServer strategyServer;

    /**
     * Handle.
     *
     * @param content the content
     */
    public boolean handle(String content) {
        if (BoolUtil.isEmpty(content)) {
            return false;
        }
        log.warn("检测到输入内容: {}", content);
        switch (content){
            case "deal": return strategyServer.dealHandle();
        }
        return false;
    }
}
