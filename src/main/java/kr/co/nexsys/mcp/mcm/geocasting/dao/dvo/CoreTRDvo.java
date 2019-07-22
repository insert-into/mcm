package kr.co.nexsys.mcp.mcm.geocasting.dao.dvo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "core_t_r")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CoreTRDvo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "position_no")
  private Integer id;
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
