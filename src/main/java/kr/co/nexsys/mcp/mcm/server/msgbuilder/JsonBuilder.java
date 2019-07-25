package kr.co.nexsys.mcp.mcm.server.msgbuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonBuilder {

	@Deprecated
	public JSONObject makeNoResult(String dstMrn) {
		JSONObject jObj = new JSONObject();
		String tmpStr = "";
		tmpStr = dstMrn;
		try {
			jObj.put("exception", "absent dst: " + tmpStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(jObj);
		return jObj;
	}
}
