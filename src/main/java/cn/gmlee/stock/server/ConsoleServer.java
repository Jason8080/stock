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
    /**
     * Handle.
     *
     * @param content the content
     */
    public void handle(String content) {
        log.info("检测到输入内容: {}", content);
    }
}
