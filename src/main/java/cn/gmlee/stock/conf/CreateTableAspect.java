package cn.gmlee.stock.conf;

import cn.gmlee.stock.conf.dao.mapper.CustomCreateMysqlTablesMapper;
import com.gitee.sunchenbin.mybatis.actable.command.TableConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Map;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class CreateTableAspect {

    private final CustomCreateMysqlTablesMapper customCreateMysqlTablesMapper;

    @Around("execution(* com.gitee.sunchenbin.mybatis.actable.dao.system.CreateMysqlTablesMapper.createTable(..))")
    public void createTable(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Map<String, TableConfig> tableMap = (Map<String, TableConfig>) joinPoint.getArgs()[0];
            customCreateMysqlTablesMapper.createTable(tableMap);
        } catch (Exception e) {
            log.info("自定义扩展支持联合主键处理异常", e);
        }
        // joinPoint.proceed(joinPoint.getArgs());
    }
}
