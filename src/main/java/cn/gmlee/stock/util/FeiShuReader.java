package cn.gmlee.stock.util;

import cn.gmlee.tools.base.builder.KvBuilder;
import cn.gmlee.tools.base.mod.HttpResult;
import cn.gmlee.tools.base.mod.Kv;
import cn.gmlee.tools.base.util.AssertUtil;
import cn.gmlee.tools.base.util.HttpUtil;
import cn.gmlee.tools.base.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Fei shu reader.
 */
public class FeiShuReader {

    private static String getMultiTableApi = "https://open.feishu.cn/open-apis/bitable/v1/apps/%s/tables/%s/records/search?user_id_type=user_id";

    /**
     * Get subscribe map map.
     * <p>
     * {  "view_id": "viewId",  "field_names": [    "股票代码",    "提交人"  ],  "filter": {    "conjunction": "and",    "conditions": [      {        "field_name": "是否缴费",        "operator": "is",        "value": [          "true"        ]      }    ]  },  "automatic_fields": false}
     *
     * @return the map
     */
    public static Map<String, List<String>> getSubscribeMap() {
        String url = String.format(getMultiTableApi, "X1ZCbM4PBahKJhsQeWlczzFonzR", "tblRZ5sw6u4j1F7I");
        String body = "{  \"view_id\": \"%s\",  \"field_names\": [    \"股票代码\",    \"提交人\"  ],  \"filter\": {    \"conjunction\": \"and\",    \"conditions\": [      {        \"field_name\": \"是否缴费\",        \"operator\": \"is\",        \"value\": [          \"true\"        ]      }    ]  },  \"automatic_fields\": false}";
        Map map = JsonUtil.toBean(String.format(body, "vewECJ0Fv2"), Map.class);
        Kv<String, String>[] headers = KvBuilder.array("Authorization", FeiShuKit.getToken());
        HttpResult httpResult = HttpUtil.post(url, map, headers);
        AssertUtil.isTrue(httpResult.isOk(), String.format("获取用户订阅异常: %s", httpResult.byteResponseBody2String()));
        Map resultMap = httpResult.jsonResponseBody2bean(Map.class);
        Object code = resultMap.get("code");
        AssertUtil.eq(code, 0, "飞书异常响应码");
        Map<String, List<String>> ret = new HashMap<>();
        Map data = (Map) resultMap.get("data");
        List<Map> items = (List<Map>) data.get("items");
        for (Map item : items) {
            Map fields = (Map) item.get("fields");
            List submit = (List) fields.get("提交人");
            Map firstSubmit = (Map) submit.get(0);
            String uid = (String) firstSubmit.get("id");
            List codes = (List) fields.get("股票代码");
            Map firstCodes = (Map) codes.get(0);
            String stock = (String) firstCodes.get("text");
            List<String> list = ret.get(uid);
            if(list == null){
                list = new ArrayList<>();
                ret.put(uid, list);
            }
            list.add(stock);
        }
        return ret;
    }
}
