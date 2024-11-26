package cn.gmlee.util;

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
     */
    public static void remove() {
        threadLocal.remove();
    }
}
