package cn.gmlee;

import cn.gmlee.stock.server.ConsoleServer;
import cn.gmlee.tools.base.util.ExceptionUtil;
import cn.gmlee.tools.ds.config.druid.DruidMonitorAutoConfiguration;
import cn.gmlee.tools.spring.util.IocUtil;
import cn.gmlee.util.SysKit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Scanner;

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
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String content = "应用启动成功: 你好!";
            System.out.println(content);
            while (!"exit".equals(content = scanner.nextLine())) {
                if("clear".equals(content) || "cls".equals(content)){
                    SysKit.clear();
                    continue;
                }
                ConsoleServer consoleServer = IocUtil.getBean(ConsoleServer.class);
                if (consoleServer != null) {
                    try {
                        consoleServer.handle(content);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.print("请输入指令: ");
            }
            System.out.println("收到退出指令: 再见!");
        }).start();
    }
}
