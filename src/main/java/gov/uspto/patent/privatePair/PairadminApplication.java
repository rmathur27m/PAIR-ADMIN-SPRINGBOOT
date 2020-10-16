package gov.uspto.patent.privatePair;

import gov.uspto.patent.privatePair.admin.domain.ViewSaveCompleteJson;
import gov.uspto.utils.Statistics;
import oracle.jdbc.pool.OracleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.Executor;

@EnableAutoConfiguration(exclude = {HibernateJpaAutoConfiguration.class, DataSourceAutoConfiguration.class})
@SpringBootApplication(scanBasePackages = {"gov.uspto.patent.privatePair"})
@EnableAsync
public class PairadminApplication extends SpringBootServletInitializer {

    @Value("${spring.datasource.password}")
    String dbPassword;
    @Value("${spring.datasource.url}")
    String dbUrl;
    @Value("${spring.datasource.username}")
    String dbUserName;
    @Value("${spring.datasource.driver-class-name}")
    String driverClassName;

    @Value("${spring.datasource.primary.jndi-name}")
    String pairJNDI;

    private JndiDataSourceLookup lookup = new JndiDataSourceLookup();

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PairadminApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
        app.run(args);
        

    }
    
    @GetMapping(value = "/getThreadName")
    public String getThreadName(){
    	
        return Thread.currentThread().getName();
    }

    /* DataSource bean , make use of this bean for the DBConnections, it will be able to handle all the transactional properties*/
    @Bean
    public DataSource dataSource() throws SQLException {
        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setUser(dbUserName);
        dataSource.setDriverType(driverClassName);
        dataSource.setPassword(dbPassword);
        dataSource.setURL(dbUrl);
        dataSource.setDriverType(driverClassName); 
        dataSource.setImplicitCachingEnabled(true);
        dataSource.setFastConnectionFailoverEnabled(true);
        return dataSource;
        //return lookup.getDataSource(pairJNDI);
    }

    /*This annotation acheives a cleaner way of doing LazyInitialization of a bean*/
    @Lazy
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.setConnectTimeout(Duration.ofMillis(180000))
                .setReadTimeout(Duration.ofMillis(180000)).build();
    }
    
    @Bean
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(10000);
        executor.initialize();
        return executor;
    }

//    @Bean
//    public Statistics statistics() {
//        return new Statistics();
//    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PairadminApplication.class);
    }
}


