package cn.gmlee.stock.service.impl;

import cn.gmlee.stock.dao.entity.Stock;
import cn.gmlee.stock.dao.mapper.StockMapper;
import cn.gmlee.stock.service.StockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jas°
 * @since 2024-11-23
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {

    @Resource
    StockMapper stockMapper;
}
