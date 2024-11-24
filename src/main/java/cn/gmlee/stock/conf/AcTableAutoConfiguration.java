package cn.gmlee.stock.conf;


import cn.gmlee.stock.conf.dao.mapper.CustomCreateMysqlTablesMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@MapperScan({"com.gitee.sunchenbin.mybatis.actable.dao.*", "**.dao.mapper.**"})
@ComponentScan(basePackages = {"com.gitee.sunchenbin.mybatis.actable.manager.*"})
public class AcTableAutoConfiguration {

    @Bean
    public CreateTableAspect createTableAspect(CustomCreateMysqlTablesMapper customCreateMysqlTablesMapper){
        return new CreateTableAspect(customCreateMysqlTablesMapper);
    }

}
