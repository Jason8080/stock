package cn.gmlee.stock.server;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * The type Lock server.
 */
@Component
@RequiredArgsConstructor
public class LockServer {
    /**
     * Lock stats boolean.
     *
     * @return the boolean
     */
    public boolean lockStats() {
        return true;
    }
}
