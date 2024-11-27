package cn.gmlee.stock.service.impl;

import cn.gmlee.stock.dao.entity.Stock2024;
import cn.gmlee.stock.dao.mapper.Stock2024Mapper;
import cn.gmlee.stock.service.Stock2024Service;
import cn.gmlee.tools.base.util.BoolUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Jas°
 * @since 2024-11-23
 */
@Service
public class Stock2024ServiceImpl extends ServiceImpl<Stock2024Mapper, Stock2024> implements Stock2024Service {
    @Override
    public void insertOrUpdateBatch(List<Stock2024> entities) {
        if (BoolUtil.isEmpty(entities)) {
            return;
        }
        baseMapper.insertOrUpdateBatch(entities);
    }
}
