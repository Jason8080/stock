package cn.gmlee.stock.mod;

import cn.gmlee.stock.dao.entity.StockStats;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Jas°
 * @since 2024-11-23
 */
@Data
public class Deal implements Serializable {
    private Integer id;
    private String name;
    private String author;
    private Integer v;
    private Boolean sold;
    private String color = "gray";
    private String soldCn = "观望"; // 买入、卖出
    private Stock stock;
    private StockStats soldStats;// 卖出统计
    private StockStats lockStats;// 持仓统计
}
