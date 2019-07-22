package kr.co.nexsys.mcp.mcm.geocasting.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.nexsys.mcp.mcm.geocasting.dao.dvo.CoreTRDvo;

public interface CoreTRDao extends JpaRepository<CoreTRDvo, Integer> {
}
