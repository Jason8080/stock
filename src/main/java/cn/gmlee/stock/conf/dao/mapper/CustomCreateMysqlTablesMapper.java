package cn.gmlee.stock.conf.dao.mapper;

import com.gitee.sunchenbin.mybatis.actable.command.TableConfig;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface CustomCreateMysqlTablesMapper {
    /**
     * 根据结构注解解析出来的信息创建表
     * @param tableMap 表结构的map
     */
    void createTable(@Param("tableMap") Map<String, TableConfig> tableMap);
}
