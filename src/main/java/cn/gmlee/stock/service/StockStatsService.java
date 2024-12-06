package cn.gmlee.stock.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import cn.gmlee.stock.dao.entity.StockStats;
import cn.gmlee.stock.controller.vo.StockStatsVo;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jas°
 * @since 2024-12-06
 */
public interface StockStatsService extends IService<StockStats> {
    /**
    * 批量保存.
    *
    * @param list the list
    */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
    void saveBatch(List<StockStatsVo> list);

    /**
    * 新增/修改 (根据ID).
    *
    * @param stockStatsVo the stockStatsVo
    * @return the stockStats
    */
    StockStats modify(StockStatsVo stockStatsVo);

    /**
    * 批量修改 (根据ID).
    *
    * @param list the list
    * @return the int
    */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
    void updateBatch(List<StockStatsVo> list);

    /**
    * 逻辑删除 (根据ID).
    *
    * @param id the id
    */
    void logicDelById(Long id);

    /**
    * 批量逻辑删除 (根据ID).
    *
    * @param ids the ids
    */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
    void logicDelByIds(Collection<Long> ids);

    /**
    * 条件查询.
    *
    * @param stockStatsVo the stockStatsVo
    * @return the list
    */
    List<StockStats> listBy(StockStatsVo stockStatsVo);

    /**
    * 分页条件查询.
    *
    * @param page         the page
    * @param stockStatsVo the stockStatsVo
    * @return the list
    */
    IPage listPageBy(Page page, StockStatsVo stockStatsVo);
}
