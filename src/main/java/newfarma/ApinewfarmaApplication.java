package newfarma;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class ApinewfarmaApplication {

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
	}

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
		SpringApplication.run(ApinewfarmaApplication.class, args);
	}

}
