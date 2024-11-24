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
@TableName("stock_list")
public class StockList implements Serializable {

    private static final long serialVersionUID = 1L;

    @IsKey
    private String code;

    @Column
    private String name;

    @Column
    private String alias;

    @Column
    private String market;

    @Column
    private Date timestamp;

}
