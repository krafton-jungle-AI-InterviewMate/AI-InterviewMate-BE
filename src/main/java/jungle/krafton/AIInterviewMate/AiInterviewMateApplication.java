package jungle.krafton.AIInterviewMate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableJpaAuditing
@SpringBootApplication
public class AiInterviewMateApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiInterviewMateApplication.class, args);
    }

    @PostConstruct
    public void started() {
        // timezone seoul 셋팅
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
