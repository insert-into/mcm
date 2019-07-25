package kr.co.nexsys.mcp.mcm;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import kr.co.nexsys.mcp.mcm.server.McmServer;
import kr.co.nexsys.mcp.mcm.server.ServerProperties;


@RequiredArgsConstructor
@SpringBootApplication
@EnableConfigurationProperties( {ServerProperties.class})
public class McmApplication {

    private final McmServer mcmServer;

    public static void main(String[] args) {
        ///SpringApplication.run(McmApplication.class, args);
        SpringApplicationBuilder builder = new SpringApplicationBuilder(McmApplication.class);
        
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> readyEventApplicationListener() {
        return applicationReadyEvent ->  {
            mcmServer.start();
        };
    }
}
