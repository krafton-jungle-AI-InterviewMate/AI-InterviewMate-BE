package jungle.krafton.AIInterviewMate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AiInterviewMateApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiInterviewMateApplication.class, args);
	}

}
