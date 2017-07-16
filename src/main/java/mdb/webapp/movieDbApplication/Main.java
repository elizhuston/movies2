package mdb.webapp.movieDbApplication;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import io.swagger.models.Contact;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableSwagger2
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	  
	@Bean
	public Docket swaggerSettings() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.pathMapping("/")
				.apiInfo(metaData());
	}

	
	   private ApiInfo metaData() {
	        ApiInfo apiInfo = new ApiInfo(
	        		 "Movie DB REST API",
	                  "REST API for Movie DB ",
	                 "1.0",
	                 "Terms of service",
	                  "Elizabeth and Jeb https://imdb.com lmig@lmig.com",
	                "Apache License Version 2.0",
	                 "https://www.apache.org/licenses/LICENSE-2.0"
	        );
	        return apiInfo;
	    }
	   
	   
	   public static final List<String> DEFAULT_INCLUDE_PATTERNS = Arrays.asList("/news/.*");
	   public static final String SWAGGER_GROUP = "mobile-api";
	 
	    /*@Value("${app.docs}")
	    private String docsLocation;*/
	 
	    	 
	  

//	@Override
//    public void run(String... strings) throws Exception {
//        System.out.printf("The database contains %s movie.\n", movieRepository.count());
//    }
}
