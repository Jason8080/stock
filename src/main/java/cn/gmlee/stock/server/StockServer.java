package cn.gmlee.stock.server;

import cn.gmlee.stock.service.Stock2024Service;
import cn.gmlee.stock.service.StockListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockServer {
    private final Stock2024Service stock2024Service;
    private final StockListService stockListService;
}
