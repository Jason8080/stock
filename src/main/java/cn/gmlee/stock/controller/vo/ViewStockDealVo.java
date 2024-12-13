package cn.gmlee.stock.controller.vo;

import cn.gmlee.stock.mod.Deal;
import cn.gmlee.stock.mod.Stock;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ViewStockDealVo extends Stock implements Serializable {
    private List<Deal> buy = new ArrayList<>();
    private List<Deal> look = new ArrayList<>();
    private List<Deal> sell = new ArrayList<>();
}
