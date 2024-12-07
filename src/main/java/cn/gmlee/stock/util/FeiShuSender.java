package cn.gmlee.stock.util;

import cn.gmlee.tools.base.builder.KvBuilder;
import cn.gmlee.tools.base.mod.HttpResult;
import cn.gmlee.tools.base.mod.Kv;
import cn.gmlee.tools.base.util.BoolUtil;
import cn.gmlee.tools.base.util.HttpUtil;
import cn.gmlee.tools.base.util.JsonUtil;
import cn.gmlee.tools.base.util.PageUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 *
 */
@Slf4j
public class FeiShuSender {

    private static String sendMessagesApi = "https://open.feishu.cn/open-apis/im/v1/messages?receive_id_type=%s";

    @Data
    public static class Body {
        private String receive_id;
        private String msg_type = "interactive";
        private String content;
    }


    @Data
    public static class Template {
        private String type = "template";
        private Variable data = new Variable();
    }

    @Data
    public static class Variable {
        private String template_id;
        private Map template_variable = new HashMap();
    }

    /**
     * 发送消息
     */
    public static void sendUser(String uid, Map... maps) {
        if (BoolUtil.isEmpty(maps)) {
            return;
        }
        String token = FeiShuKit.getToken();
        Kv<String, String>[] headers = KvBuilder.array("Authorization", "Bearer ".concat(token));
        Arrays.stream(maps).filter(BoolUtil::notEmpty).forEach(map -> {
            Body body = new Body();
            body.setReceive_id(uid);
            Template template = new Template();
            Variable variable = template.getData();
            variable.setTemplate_id("AAqjgLzYIBxuh");//单聊模版
            variable.setTemplate_variable(map);
            body.setContent(JsonUtil.toJson(template));
            HttpResult httpResult = HttpUtil.post(String.format(sendMessagesApi, "user_id"), body, headers);
            log.info(httpResult.byteResponseBody2String());
        });
    }

    private static final Map<Integer, String> vMap = KvBuilder.map(
            1, "oc_8e22f38a15b9e892bd4f9e07ccbac416",
            2, "oc_891a01a7afb5a037fca08ff8844a8078"
    );

    /**
     * 发送消息
     */
    public static void sendGroup(Integer v, Map map, List<Map> maps) {
        String chatId = vMap.get(v);
        if (BoolUtil.isEmpty(maps) || BoolUtil.isEmpty(chatId)) {
            return;
        }
        String token = FeiShuKit.getToken();
        Kv<String, String>[] headers = KvBuilder.array("Authorization", "Bearer ".concat(token));
        Body body = new Body();
        body.setReceive_id(chatId);
        Template template = new Template();
        Variable variable = template.getData();
        variable.setTemplate_id("AAqjjVphSBXwR");//群聊模版
        List<List<Map>> rows = PageUtil.splitSize(maps, 4);
        List<Map> list = new ArrayList<>();
        map.put("list", list);
        for (int i = 0; i < rows.size(); i++) {
            Map row = new HashMap();
            List<Map> cols = rows.get(i);
            for (int j = 1; j <= cols.size(); j++) {
                Map col = cols.get(j - 1);
                Object code = col.get("code");
                Object name = col.get("name");
                Object color = col.get("color");
                Object url = col.get("url");
                col.clear();
                row.put("code" + j, code);
                row.put("name" + j, name);
                row.put("color" + j, color);
                row.put("url" + j, url);
            }
            list.add(row);
        }
        variable.setTemplate_variable(map);
        body.setContent(JsonUtil.toJson(template));
        HttpResult httpResult = HttpUtil.post(String.format(sendMessagesApi, "chat_id"), body, headers);
        log.info(httpResult.byteResponseBody2String());
    }
}
