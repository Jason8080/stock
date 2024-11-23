package cn.gmlee.stock.service.impl;

import cn.gmlee.stock.dao.entity.StockList;
import cn.gmlee.stock.dao.mapper.StockListMapper;
import cn.gmlee.stock.service.StockListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jas°
 * @since 2024-11-23
 */
@Service
public class StockListServiceImpl extends ServiceImpl<StockListMapper, StockList> implements StockListService {
}
