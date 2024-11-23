package cn.gmlee.stock.util;

import cn.gmlee.stock.dao.entity.StockList;
import cn.gmlee.tools.base.mod.HttpResult;
import cn.gmlee.tools.base.util.AssertUtil;
import cn.gmlee.tools.base.util.HttpUtil;
import cn.gmlee.tools.base.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * http://api.biyingapi.com/hslt/list/E523612B-7255-418C-B8AF-7A4BFB62FE07
 */
public class ByKit {

    private static final String getHsStockApi = "http://api.biyingapi.com/hslt/list/E523612B-7255-418C-B8AF-7A4BFB62FE07";

    @Data
    static class Stock {
        private String dm;
        private String mc;
        private String jys;
        public StockList toStockList(){
            StockList entity = new StockList();
            entity.setCode(dm);
            entity.setName(mc);
            entity.setMarket(jys);
            return entity;
        }
    }

    /**
     * Get stock lists list.
     *
     * @return the list
     */
    public static List<StockList> getStockLists(){
        HttpResult httpResult = HttpUtil.get(getHsStockApi);
        List<Stock> stocks = httpResult.jsonResponseBody2bean(new TypeReference<List<Stock>>() {
        });
        return stocks.stream().map(Stock::toStockList).collect(Collectors.toList());
    }
}
