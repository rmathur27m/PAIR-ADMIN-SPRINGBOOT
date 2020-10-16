package gov.uspto.patent.privatePair.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class SecurityConfiguration implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        // Modifies CORS configuration to allow connecting from the Angular frontend.
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("OPTIONS", "POST", "PUT", "GET", "DELETE", "PATCH")
                        .allowedOrigins("http://localhost:4200",
                                "https://ppair-q320-webdm-tomcat-0.sit.uspto.gov:8443",
                                "https://ppair-q320-svcsgs-eap-0.sit.uspto.gov:8443",
                                "https://ppair-q420-webdm-tomcat-0.dev.uspto.gov:8443",
                                "https://ppair-q420-webdm-tomcat-1.dev.uspto.gov:8443",
                                "https://ppair-q420-webgs-tomcat-0.dev.uspto.gov:8443",
                                "https://ppair-q420-webgs-tomcat-1.dev.uspto.gov:8443",
                                "https://ppair-q420-webdm-tomcat-0.sit.uspto.gov:8443",
                                "https://ppair-q420-webdm-tomcat-1.sit.uspto.gov:8443",
                                "https://ppair-q420-webgs-tomcat-0.sit.uspto.gov:8443",
                                "https://ppair-q420-webgs-tomcat-1.sit.uspto.gov:8443",
                                "https://ppair-q420-webdm-tomcat-0.fqt.uspto.gov:8443",
                                "https://ppair-q420-webdm-tomcat-1.fqt.uspto.gov:8443",
                                "https://ppair-q420-webgs-tomcat-0.fqt.uspto.gov:8443",
                                "https://ppair-q420-webgs-tomcat-1.fqt.uspto.gov:8443",
                                "https://ppair-q420-webdm-tomcat-0.pvt.uspto.gov:8443",
                                "https://ppair-q420-webdm-tomcat-1.pvt.uspto.gov:8443",
                                "https://ppair-q420-webgs-tomcat-0.pvt.uspto.gov:8443",
                                "https://ppair-q420-webgs-tomcat-1.pvt.uspto.gov:8443",
                                "https://ppair-int.sit.uspto.gov",
                                "https://ppair-int.fqt.uspto.gov",
                                "https://ppair-general.fqt.uspto.gov",
                                "https://ppair-my-rbac-sit.etc.uspto.gov:1443",
                                "https://ppair-my-rbac-fqt.etc.uspto.gov:1443",
                                "https://ppair-my-pvt.etc.uspto.gov")
                        .allowCredentials(true).maxAge(3600);
            }
        };
    }

}

