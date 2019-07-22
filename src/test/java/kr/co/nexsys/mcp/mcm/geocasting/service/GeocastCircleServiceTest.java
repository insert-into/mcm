package kr.co.nexsys.mcp.mcm.geocasting.service;

import kr.co.nexsys.mcp.mcm.geocasting.dao.CoreTRDao;
import kr.co.nexsys.mcp.mcm.geocasting.dao.dvo.CoreTRDvo;
import kr.co.nexsys.mcp.mcm.geocasting.service.GeocastCircleService;
import kr.co.nexsys.mcp.mcm.geocasting.service.vo.CoreTR;
import kr.co.nexsys.mcp.mcm.util.McmTestDvoDataGenerator;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GeocastCircleServiceTest {

  @Autowired
  private CoreTRDao coreTRDao;

  @Autowired
  private GeocastCircleService geocastCircleService;

  @Test
  public void shouldFindCoreTR() {
    CoreTRDvo originDvo = coreTRDao.saveAndFlush(McmTestDvoDataGenerator.coreTRDvo());
    CoreTR actual = geocastCircleService.findCoreTR(originDvo.getId()).get();
    assertThat(actual.getPositionNo(), equalTo(originDvo.getId()));
  }


  @Test(expected = Exception.class)
  public void shouldRollback() {
   List<CoreTRDvo>  origins = Lists.list(
        coreTRDao.saveAndFlush(McmTestDvoDataGenerator.coreTRDvo()),
        McmTestDvoDataGenerator.coreTRDvo());

    List<Integer> ids = origins.stream()
        .map(CoreTRDvo::getId)
        .collect(Collectors.toList());

    String testData = "test:mrn";

    try {
      geocastCircleService.updateTestData(ids, testData);
    }catch (Exception e){
      throw e;
    }finally {
      String actualMrn = coreTRDao.findById(origins.get(0).getId()).get().getMrn();
      assertThat(actualMrn, not(equalTo(testData)));
      assertThat(actualMrn, equalTo(origins.get(0).getMrn()));
    }

  }
}