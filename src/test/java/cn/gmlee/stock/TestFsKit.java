package cn.gmlee.stock;

import cn.gmlee.stock.mod.Stock;
import cn.gmlee.stock.util.FeiShuKit;
import cn.gmlee.stock.util.TencentStockParser;
import cn.gmlee.tools.base.mod.HttpResult;
import cn.gmlee.tools.base.util.ClassUtil;
import cn.gmlee.tools.base.util.HttpUtil;

import java.util.List;

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
