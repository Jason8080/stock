package cn.gmlee.stock.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
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

    @IsKey
    private Integer id;

    @Column
    private String name;

    @Column
    private String author;

    @Column
    private Integer v;

    @Column
    private String remark;

    @Column
    private Integer status;

    @Column
    private Date createdAt;


}
