package cn.gmlee.stock.server;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * The type Console server.
 */
@Component
@RequiredArgsConstructor
public class ConsoleServer {
    /**
     * Handle.
     *
     * @param content the content
     */
    public void handle(String content) {
        System.out.println("检测到输入内容: " + content);
    }
}
