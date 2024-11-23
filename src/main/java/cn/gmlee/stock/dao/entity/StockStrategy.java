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
@TableName("stock_strategy")
public class StockStrategy implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String author;

    private Integer v;

    private String remark;

    private Integer status;

    private Date createdAt;


}
