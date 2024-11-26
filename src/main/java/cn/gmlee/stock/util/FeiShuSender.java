package cn.gmlee.stock.util;

import cn.gmlee.tools.base.builder.KvBuilder;
import cn.gmlee.tools.base.mod.HttpResult;
import cn.gmlee.tools.base.mod.Kv;
import cn.gmlee.tools.base.util.BoolUtil;
import cn.gmlee.tools.base.util.HttpUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Slf4j
public class FeiShuSender {

    private static String sendMessagesApi = "https://open.feishu.cn/open-apis/im/v1/messages?receive_id_type=user_id";

    @Data
    public static class Template {
        private String type = "template";
        private Variable data = new Variable();
    }

    @Data
    public static class Variable {
        private String template_id = "AAqjgLzYIBxuh";
        private Map template_variable = new HashMap();
    }

    /**
     * 发送消息
     */
    public static void send(Map... maps) {
        if (BoolUtil.isEmpty(maps)) {
            return;
        }
        String token = FeiShuKit.getToken();
        Kv<String, String>[] headers = KvBuilder.array("Authorization", "Bearer ".concat(token));
        Arrays.stream(maps).filter(BoolUtil::notEmpty).forEach(map -> {
            HttpResult httpResult = HttpUtil.post(sendMessagesApi, map, headers);
            log.info(httpResult.byteResponseBody2String());
        });
    }
}
