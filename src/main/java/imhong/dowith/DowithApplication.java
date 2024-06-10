package imhong.dowith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DowithApplication {

    public static void main(String[] args) {
        SpringApplication.run(DowithApplication.class, args);
    }
}
