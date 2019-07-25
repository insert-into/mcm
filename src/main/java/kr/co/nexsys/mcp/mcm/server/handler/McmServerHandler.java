package kr.co.nexsys.mcp.mcm.server.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import kr.co.nexsys.mcp.mcm.geocasting.dao.dvo.CoreTRDvo;
import kr.co.nexsys.mcp.mcm.geocasting.service.GeocastCircleService;
import kr.co.nexsys.mcp.mcm.server.calc.CircleRangeCalculate;
import kr.co.nexsys.mcp.mcm.server.geometric.GeoFencing;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
@Component
public class McmServerHandler extends ChannelInboundHandlerAdapter {
	
	@Autowired
	private GeocastCircleService cs;
	
	@SuppressWarnings("unchecked")
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception
	{
		ByteBuf inBuffer = (ByteBuf) msg;

		String received = inBuffer.toString(CharsetUtil.UTF_8);

		MessageParser msgp = new MessageParser(received);
		
		Map<String, Object> msgMap = msgp.getObjectType();
		
		int mapType = (Integer) msgMap.get("mapType");
		JSONObject jsonObj = new JSONObject();
		JSONArray retJsonArray = new JSONArray();
		String dstMrn;
		if(null != msgMap.get("dstMRN")) {
			dstMrn = msgMap.get("dstMRN").toString();
		}else {
			dstMrn = "";
		}
		if(0==mapType) {
			log.debug("\r\r\r[srcMrn{}]\tdstMrn[{}]\tlati[{}]\tlongi[{}]\tradius[{}]",msgMap.get("srcMRN"), msgMap.get("dstMRN"),msgMap.get("lat"), msgMap.get("long"), msgMap.get("radius"));
			CircleRangeCalculate crc = new CircleRangeCalculate();
			
			Map<String, Double> cMapRangeResult = new ConcurrentHashMap<String, Double>();
			cMapRangeResult = crc.rangeCalculate(Double.parseDouble(msgMap.get("lat").toString()), Double.parseDouble(msgMap.get("long").toString()), Double.parseDouble(msgMap.get("radius").toString()));
			
			log.debug("circle range result... \n{}", cMapRangeResult);
			
			List<CoreTRDvo> trList = cs.findByItemNameOrIdContainingCoreTRs(
					cMapRangeResult.get("minLati") ,
					cMapRangeResult.get("maxLati") ,
					cMapRangeResult.get("minLongi"),
					cMapRangeResult.get("maxLongi")
					);
			
			int trSize = trList.size();
			if(trSize<1) {
				jsonObj.put("exception", "absent dst: " + dstMrn);
				retJsonArray.add(jsonObj);
			}else {
				for(int i=0;i<trSize;i++) {
					jsonObj = new JSONObject();
					jsonObj.put("connType", "polling");
					jsonObj.put("dstMRN", trList.get(i).getMrn());
					jsonObj.put("netType", getNetType(trList.get(i).getCommNo()));
					retJsonArray.add(jsonObj);
				}
				log.debug("\r///////////////////////////////////////objTMP????\t{}",retJsonArray);
			}
			
		}
		if(1==mapType) {
			log.debug("\r\r\rsrcMrn[{}]\rdstMrn[{}]\rlati--{}--\rlongi--{}--\r\r",msgMap.get("srcMRN"), msgMap.get("dstMRN"),msgMap.get("lat"), msgMap.get("long"));

			ArrayList<Double> objLat = (ArrayList<Double>) msgMap.get("lat");
			ArrayList<Double> objLon = (ArrayList<Double>) msgMap.get("long");
			log.debug("\r\rlat classssssssssssssssssss {}",objLat.getClass());
			
			GeoFencing geoFenc = new GeoFencing(cs);
			
			List<CoreTRDvo> trList = geoFenc.GeoFencingCalculation(objLat, objLon);
			
			int trSize = trList.size();
			
			if(trSize<1) {
				jsonObj.put("exception", "absent dst: " + dstMrn);
				retJsonArray.add(jsonObj);
			}else {
				for(int i=0;i<trSize;i++) {
					jsonObj = new JSONObject();
					jsonObj.put("connType", "polling");
					jsonObj.put("dstMRN", trList.get(i).getMrn());
					jsonObj.put("netType", getNetType(trList.get(i).getCommNo()));
					retJsonArray.add(jsonObj);
				}
				log.debug("\r///////////////////////////////////////objTMP????\t{}",retJsonArray);
			}
		}
		if(2==mapType) {
			log.debug("\r\r\rsrcMrn[{}]\rdstMrn[{}]\rip[{}]",msgMap.get("srcMRN"), msgMap.get("dstMRN"),msgMap.get("ip"));
			
			List<CoreTRDvo> trList = cs.findByMrn(dstMrn);
			int trSize = trList.size();
			
			if(trSize<1) {
				jsonObj.put("exception", "absent dst: " + dstMrn);
				retJsonArray.add(jsonObj);
			}else {
				
				if (dstMrn.contains("SP") || dstMrn.contains("sp")) {
					jsonObj = new JSONObject();
					jsonObj.put("connType", "push");
					jsonObj.put("dstMRN", dstMrn);
					jsonObj.put("IPAddr", trList.get(0).getSIp());
					jsonObj.put("portNum", trList.get(0).getPortNumber());
					retJsonArray.add(jsonObj);
				} else {
					jsonObj = new JSONObject();
					jsonObj.put("connType", "push");
					jsonObj.put("dstMRN", dstMrn);
					jsonObj.put("netType", getNetType(trList.get(0).getCommNo()));
				}
				log.debug("\r///////////////////////////////////////objTMP????\t{}",retJsonArray);
			}
		}

		log.debug("\r\r------------------------Server received: {}", received);
		ctx.write(Unpooled.copiedBuffer("Hello " + retJsonArray, CharsetUtil.UTF_8));
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	public String getNetType(int h) {
		int hh =h;
		String temp="";
		switch(hh) {
		case 0: temp="LTE-M";
		break;
		case 1: temp="VDES";
		break;
		case 2: temp="HF-MF";
		break;
		case 3: temp="Satellite";
		break;
		}
		return temp;
	}

}
