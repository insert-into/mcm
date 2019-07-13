package com.neonex.mcp.mcm.geocasting.service;

import com.neonex.mcp.mcm.geocasting.dao.CoreTRDao;
import com.neonex.mcp.mcm.geocasting.dao.dvo.CoreTRDvo;
import com.neonex.mcp.mcm.geocasting.service.vo.CoreTR;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
public class GeocastCircleService {

  private final CoreTRDao coreTRDao;

  public GeocastCircleService(CoreTRDao coreTRDao){
    this.coreTRDao = coreTRDao;
  }

  @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
  public Optional<CoreTR> findCoreTR(int positionNo){
   return coreTRDao.findById(positionNo).map(GeocastCircleService::valueOf);
  }

  public void updateTestData(List<Integer> positionNos, String testData){
    for(int positionNo : positionNos){
      CoreTRDvo dvo =  coreTRDao.findById(positionNo).get();
      dvo.setMrn(testData);
      coreTRDao.saveAndFlush(dvo);
    }
  }

  public static CoreTR valueOf(CoreTRDvo dvo) {
    return CoreTR.builder()
        .commNo(dvo.getCommNo())
        .gateway(dvo.getGateway())
        .latitude(dvo.getLatitude())
        .longitude(dvo.getLongitude())
        .macAddress(dvo.getMacAddress())
        .mmsi(dvo.getMmsi())
        .mrn(dvo.getMrn())
        .portNumber(dvo.getPortNumber())
        .positionDate(dvo.getPositionDate())
        .sIp(dvo.getSIp())
        .positionNo(dvo.getId())
        .build();
  }

  public static CoreTRDvo valueOf(CoreTR dvo) {
    return CoreTRDvo.builder()
        .commNo(dvo.getCommNo())
        .gateway(dvo.getGateway())
        .latitude(dvo.getLatitude())
        .longitude(dvo.getLongitude())
        .macAddress(dvo.getMacAddress())
        .mmsi(dvo.getMmsi())
        .mrn(dvo.getMrn())
        .portNumber(dvo.getPortNumber())
        .positionDate(dvo.getPositionDate())
        .sIp(dvo.getSIp())
        .id(dvo.getPositionNo())
        .build();
  }
}
