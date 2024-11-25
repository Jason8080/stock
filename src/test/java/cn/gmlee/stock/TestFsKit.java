package cn.gmlee.stock;

import cn.gmlee.stock.util.FeiShuKit;
import cn.gmlee.stock.util.FeiShuReader;
import cn.gmlee.tools.base.builder.KvBuilder;
import cn.gmlee.tools.base.mod.HttpResult;
import cn.gmlee.tools.base.util.HttpUtil;

import java.util.List;
import java.util.Map;

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
//        String url = "https://open.feishu.cn/open-apis/bitable/v1/apps/X1ZCbM4PBahKJhsQeWlczzFonzR/tables/tblRZ5sw6u4j1F7I/records/search?user_id_type=user_id";
//        String token = FeiShuKit.getToken();
//        HttpResult httpResult = HttpUtil.post(url, KvBuilder.array("authorization", "Bearer ".concat(token)));
//        System.out.println(httpResult.byteResponseBody2String());
//        String token = FeiShuKit.getToken();
//        System.out.println(token);
        Map<String, List<String>> subscribeMap = FeiShuReader.getSubscribeMap();
        System.out.println(subscribeMap);
    }
}
