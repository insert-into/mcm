package kr.co.nexsys.mcp.mcm;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import kr.co.nexsys.mcp.mcm.server.McmServer;
import kr.co.nexsys.mcp.mcm.server.ServerProperties;


@RequiredArgsConstructor
@SpringBootApplication
@EnableConfigurationProperties( {ServerProperties.class})
public class McmApplication {

    private final McmServer mcmServer;

    public static void main(String[] args) {
        SpringApplication.run(McmApplication.class, args);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> readyEventApplicationListener() {
        return applicationReadyEvent ->  {
            mcmServer.start();
        };
    }
}
