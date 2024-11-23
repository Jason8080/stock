package cn.gmlee.stock;

import cn.gmlee.stock.util.FeiShuKit;

/**
 * 股票接口.
 */
public class TestFsKit {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        String token = FeiShuKit.getToken();
        System.out.println(token);
    }
}
