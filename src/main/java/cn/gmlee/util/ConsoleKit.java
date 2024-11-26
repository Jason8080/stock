package cn.gmlee.util;

import cn.gmlee.tools.base.util.BoolUtil;
import lombok.Data;

/**
 * The type Console kit.
 */
public class ConsoleKit {

    /**
     * The type Obj.
     */
    @Data
    public static class Obj {
        private String cmd;
        private String date;
        private String code;
        private String strategy;

        /**
         * Instantiates a new Obj.
         *
         * @param content the content
         */
        public Obj(String content) {
            String[] arr = content.split(" ");
            for (String a : arr) {
                if (BoolUtil.isDigit(a)) {
                    if (a.length() == 1) {
                        this.strategy = a;
                    }
                    if (a.length() == 8) {
                        this.date = a;
                    }
                    if (a.length() == 6) {
                        this.code = a;
                    }
                } else {
                    this.cmd = a;
                }
            }
        }

        /**
         * Instantiates a new Obj.
         *
         * @param cmd      the cmd
         * @param date     the date
         * @param code     the code
         * @param strategy the strategy
         */
        public Obj(String cmd, String date, String code, String strategy) {
            this.cmd = cmd;
            this.date = date;
            this.code = code;
            this.strategy = strategy;
        }
    }

    private static ThreadLocal<Obj> threadLocal = new InheritableThreadLocal();

    /**
     * Put.
     *
     * @param obj the obj
     */
    public static void put(Obj obj) {
        if (obj == null) {
            return;
        }
        threadLocal.set(obj);
    }

    /**
     * Remove.
     *
     * @return the boolean
     */
    public static boolean remove() {
        threadLocal.remove();
        return true;
    }

    /**
     * Gets strategy id.
     *
     * @return the strategy id
     */
    public static String getStrategyId() {
        Obj obj = threadLocal.get();
        if (obj == null) {
            return null;
        }
        return obj.getStrategy();
    }

    /**
     * Gets cmd.
     *
     * @return the cmd
     */
    public static String getCmd() {
        Obj obj = threadLocal.get();
        if (obj == null) {
            return null;
        }
        return obj.getCmd();
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public static String getDate() {
        Obj obj = threadLocal.get();
        if (obj == null) {
            return null;
        }
        return obj.getDate();
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public static String getCode() {
        Obj obj = threadLocal.get();
        if (obj == null) {
            return null;
        }
        return obj.getCode();
    }
}
