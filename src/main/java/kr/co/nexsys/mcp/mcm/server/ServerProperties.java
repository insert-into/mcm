package kr.co.nexsys.mcp.mcm.server;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("mcm.server")
public class ServerProperties {
  private int port;
  private int bossThreadCount;
  private int workerThreadCount;
}
