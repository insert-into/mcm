package kr.co.nexsys.mcp.mcm.geocasting.service.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoreTR {
  private Integer positionNo;
  private Integer commNo;
  private LocalDateTime positionDate;
  private String mrn;
  private String mmsi;
  private String sIp;
  private String gateway;
  private String macAddress;
  private Double latitude;
  private Double longitude;
  private String portNumber;
}
