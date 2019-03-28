package cn.itxsl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ItxslApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItxslApplication.class, args);
    }


}

