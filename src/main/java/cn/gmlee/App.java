package cn.gmlee;

import cn.gmlee.tools.ds.config.druid.DruidMonitorAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The type App.
 */
@EnableScheduling
@SpringBootApplication(exclude = {
        DruidMonitorAutoConfiguration.class
})
public class App {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
