package cn.gmlee;

import cn.gmlee.stock.server.ConsoleServer;
import cn.gmlee.tools.ds.config.druid.DruidMonitorAutoConfiguration;
import cn.gmlee.tools.spring.util.IocUtil;
import cn.gmlee.util.ConsoleKit;
import cn.gmlee.util.SysKit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Scanner;

/**
 * The type App.
 */
@EnableAsync
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
                if ("clear".equals(content) || "cls".equals(content)) {
                    SysKit.clear();
                    continue;
                }
                long start = System.currentTimeMillis();
                ConsoleServer consoleServer = IocUtil.getBean(ConsoleServer.class);
                if (consoleServer != null) {
                    try {
                        ConsoleKit.put(new ConsoleKit.Obj(content));
                        consoleServer.handle(content);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        ConsoleKit.remove();
                    }
                }
                System.out.println(String.format("指令已处理: %s(ms)", System.currentTimeMillis() - start));
                System.out.print("请输入指令: ");
            }
            System.out.println("收到退出指令: 再见!");
        }).start();
    }
}
