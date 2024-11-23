package cn.gmlee.stock.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Jas°
 * @since 2024-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stock_list")
public class StockList implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String name;

    private String alias;

    private String market;

    private Date timestamp;


}
