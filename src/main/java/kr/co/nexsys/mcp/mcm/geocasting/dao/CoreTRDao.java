package kr.co.nexsys.mcp.mcm.geocasting.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.nexsys.mcp.mcm.geocasting.dao.dvo.CoreTRDvo;

public interface CoreTRDao extends JpaRepository<CoreTRDvo, Integer> {
	@Query(value="SELECT * FROM CORE_T_R WHERE LATITUDE >= :key1 AND LATITUDE <= :key2 AND LONGITUDE >= :key3 AND LONGITUDE <= :key4", nativeQuery = true)
	List<CoreTRDvo> findByItemNameOrIdContainingCoreTRs(
			@Param("key1") double key1,
			@Param("key2") double key2,
			@Param("key3") double key3,
			@Param("key4") double key4
	);

	@Query(value="SELECT * FROM CORE_T_R WHERE MRN = :key1", nativeQuery = true)
	List<CoreTRDvo> findByMrn(@Param("key1") String key1);
	
}
