package kr.co.nexsys.mcp.mcm.server.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class MessageParser {
	
	/**
	 * client에서 받은 message
	 */
	private String rcvMsg;
	
	/**
	 * circle요청 json
	 */
	private Map<String, Object> circleMap ;
	/**
	 * polygon json
	 */
	private Map<String, Object> polygonMap;
	/**
	 * unicasting json
	 */
	private Map<String, Object> unicastMap;
	
	
	MessageParser(String rcvMsg) {
		
		this.rcvMsg     = rcvMsg;
		this.circleMap  = new ConcurrentHashMap<>();
		this.polygonMap = new ConcurrentHashMap<>();
		this.unicastMap = new ConcurrentHashMap<>();
	}
	
	/**
	 * circle?polygon?unicasting?
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getObjectType() {
		Map<String, Object> returnMap = new ConcurrentHashMap<>();
		JsonParser springParser = JsonParserFactory.getJsonParser();
		Map<String, Object> map = new ConcurrentHashMap<>();
		
		map = springParser.parseMap(this.rcvMsg);
		circleMap = (Map<String, Object>) map.get("geocasting_circle");
		
		polygonMap = (Map<String, Object>) map.get("geocasting_polygon");
		
		unicastMap = (Map<String, Object>) map.get("unicasting");
		
		if(null != circleMap ) {
			returnMap = circleMap;
			returnMap.put("mapType", 0);
		}
		if(null != polygonMap ) {
			returnMap = polygonMap;
			returnMap.put("mapType", 1);
		}
		if(null != unicastMap ) {
			returnMap = unicastMap;
			returnMap.put("mapType", 2);
		}
		
		log.debug("\r\r==========={}, {}, {}", circleMap, polygonMap, unicastMap);
		return returnMap;
	}
}
