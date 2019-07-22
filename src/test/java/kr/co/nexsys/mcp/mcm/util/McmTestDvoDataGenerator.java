package kr.co.nexsys.mcp.mcm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.nexsys.mcp.mcm.geocasting.dao.dvo.CoreTRDvo;

import java.time.LocalDateTime;

public class McmTestDvoDataGenerator {

  private static final Logger LOG = LoggerFactory.getLogger(McmTestDvoDataGenerator.class);

  public static CoreTRDvo coreTRDvo(){
    return CoreTRDvo.builder()
        .commNo(2)
        .positionDate(LocalDateTime.now())
        .mrn("urn:mrn:mcp:sc1")
        .mmsi("urn:mrn:mcp:sc1")
        .sIp("172.10.1.3")
        .gateway("172.10.1.1")
        .macAddress("255.255.255.0")
        .latitude(88.4888746165567)
        .longitude(158.14709104425)
        .portNumber("6443")
        .build();
  }

}
