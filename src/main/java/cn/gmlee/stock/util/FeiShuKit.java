package cn.gmlee.stock.util;

import cn.gmlee.tools.base.builder.KvBuilder;
import cn.gmlee.tools.base.mod.HttpResult;
import cn.gmlee.tools.base.util.AssertUtil;
import cn.gmlee.tools.base.util.HttpUtil;

import java.util.Map;

/**
 * The type Fei shu kit.
 */
public class FeiShuKit {

    /**
     * {    "app_id": "机器人ID",    "app_secret": "机器人秘钥"}
     */
    private static final String getBootTokenApi = "https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal";

    /**
     * Get token string.
     *
     * @return the string
     */
    public static String getToken(){
        HttpResult httpResult = HttpUtil.post(getBootTokenApi, KvBuilder.map("app_id", "cli_a7b3d3679b3b100b", "app_secret", "yMZ1cse5zVz04vk86YDfDh1jNo6BQ4VF"));
        Map result = httpResult.jsonResponseBody2bean(Map.class);
        AssertUtil.eq(result.get("code"), 0, String.format("飞书机器人获取令牌异常: %s",result.get("msg")));
        return (String) result.get("tenant_access_token");
    }
}
