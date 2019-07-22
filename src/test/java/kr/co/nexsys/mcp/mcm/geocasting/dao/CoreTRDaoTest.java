package kr.co.nexsys.mcp.mcm.geocasting.dao;

import kr.co.nexsys.mcp.mcm.geocasting.dao.CoreTRDao;
import kr.co.nexsys.mcp.mcm.geocasting.dao.dvo.CoreTRDvo;
import kr.co.nexsys.mcp.mcm.util.McmTestDvoDataGenerator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class CoreTRDaoTest {

  @Autowired
  private CoreTRDao dao;

  @Test
  public void shouldSave(){
    CoreTRDvo dvo = McmTestDvoDataGenerator.coreTRDvo();
    assertThat(dvo.getId(), nullValue());
    dao.saveAndFlush(dvo);
    assertThat(dvo.getId(), notNullValue());
  }


  @Test
  public void shouldFindById() {
    CoreTRDvo origin = McmTestDvoDataGenerator.coreTRDvo();
    dao.save(origin);

    Optional<CoreTRDvo> actual = dao.findById(origin.getId());
    assertThat(actual.isPresent(), is(true));
    assertThat(actual.get().getId(), is(origin.getId()));
  }

  @Test
  public void shouldDelete() {
    CoreTRDvo origin = dao.save(McmTestDvoDataGenerator.coreTRDvo());
    assertThat(dao.findById(origin.getId()).isPresent(), is(true));
    dao.delete(origin);
    assertThat(dao.findById(origin.getId()).isPresent(), is(false));
  }

}