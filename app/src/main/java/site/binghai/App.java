package site.binghai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import site.binghai.lib.config.IceConfig;

@ComponentScan(value = {"site.binghai.lib", "site.binghai.biz"})
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

        for (String arg : args) {
            if (arg.startsWith("-") && arg.contains("=")) {
                arg = arg.substring(1);
                String[] kv = arg.split("=");
                IceConfig.addSetupParam(kv[0], kv[1]);
            }
        }
    }
}