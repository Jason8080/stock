package cn.gmlee.util;

import java.io.IOException;

/**
 * The type Sys kit.
 */
public class SysKit {
    /**
     * Clear.
     */
    public static void clear() {
        try {
            // 判断操作系统类型并选择相应命令
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
