package web.fdu_ac_service.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.json.JSONArray;

import service.fdu_ac_service.model.ACConstants;
import service.fdu_ac_service.model.Rule;
import service.fdu_ac_service.service.DBService;

@Controller
public class UserACController {
	@Autowired
	private DBService userService;

	@RequestMapping("/getWhiteList")
	@ResponseBody
	public Map<String, Object> getWhiteList(HttpServletRequest request) {
		Map<String, Object> rm = new HashMap<>();
		// response.addHeader("Access-Control-Allow-Origin", "*");
		String tableIds = request.getParameter("tableIds");
		String tmp = tableIds.substring(0, tableIds.length() - 1);
		String[] StableIds = tmp.split(",");

		Long[] LtableIds = new Long[StableIds.length];
		for (int i = 0; i < StableIds.length; i++) {
			LtableIds[i] = new Long(StableIds[i]);
		}
		try {
			Rule[] ruleList = userService.getRuleList(LtableIds, ACConstants.WHITE);
			JSONArray jsonArray = new JSONArray();

			if (ruleList != null && ruleList.length > 0) {
				for (Rule r : ruleList) {
					Map<String, Object> map = new HashMap<>();
					map.put("userId", r.getUserId());
					map.put("userName", r.getUserName());
					map.put("status", r.getStatus());
					jsonArray.put(map);
				}
			}
			rm.put("result", "success");
			rm.put("message", "success");
			rm.put("code", "200");
			rm.put("ruleList", jsonArray.toString());
		} catch (Exception e) {
			e.printStackTrace();
			rm.put("result", "error");
			rm.put("message", "error");
		}
		return rm;
	}

	@RequestMapping("/getBlackList")
	@ResponseBody
	public Map<String, Object> getBlackList(HttpServletRequest request) {
		Map<String, Object> rm = new HashMap<>();
		// response.addHeader("Access-Control-Allow-Origin", "*");
		String tableIds = request.getParameter("tableIds");
		String tmp = tableIds.substring(0, tableIds.length() - 1);
		String[] StableIds = tmp.split(",");

		Long[] LtableIds = new Long[StableIds.length];
		for (int i = 0; i < StableIds.length; i++) {
			LtableIds[i] = new Long(StableIds[i]);
		}
		try {
			Rule[] ruleList = userService.getRuleList(LtableIds, ACConstants.BLACK);
			JSONArray jsonArray = new JSONArray();

			if (ruleList != null && ruleList.length > 0) {
				for (Rule r : ruleList) {
					Map<String, Object> map = new HashMap<>();
					map.put("userId", r.getUserId());
					map.put("userName", r.getUserName());
					jsonArray.put(map);
				}
			}
			rm.put("result", "success");
			rm.put("message", "success");
			rm.put("code", "200");
			rm.put("ruleList", jsonArray.toString());
		} catch (Exception e) {
			e.printStackTrace();
			rm.put("result", "error");
			rm.put("message", "error");
		}
		return rm;
	}

	@RequestMapping("/addWhiteList")
	@ResponseBody
	public Map<String, Object> addToWhite(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> rm = new HashMap<String, Object>();

		String tableIds = request.getParameter("tableIds");
		long userId = Long.parseLong(request.getParameter("userId"));

		String tmp = tableIds.substring(0, tableIds.length() - 1);
		String[] StableIds = tmp.split(",");

		Long[] LtableIds = new Long[StableIds.length];
		for (int i = 0; i < StableIds.length; i++) {
			LtableIds[i] = new Long(StableIds[i]);
		}

		long ret = userService.addRule(LtableIds, userId, ACConstants.WHITE, ACConstants.NON_EXPORTABLE);
		if (ret > 0) {
			rm.put("result", "success");
			rm.put("message", "success");
		} else {
			rm.put("result", "error");
			rm.put("message", "duplicate");
		}
		return rm;
	}

	@RequestMapping("/addBlackList")
	@ResponseBody
	public Map<String, Object> addBlackList(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> rm = new HashMap<String, Object>();

		String tableIds = request.getParameter("tableIds");
		long userId = Long.parseLong(request.getParameter("userId"));
		String tmp = tableIds.substring(0, tableIds.length() - 1);
		String[] StableIds = tmp.split(",");

		Long[] LtableIds = new Long[StableIds.length];
		for (int i = 0; i < StableIds.length; i++) {
			LtableIds[i] = new Long(StableIds[i]);
		}

		long ret = userService.addRule(LtableIds, userId, ACConstants.BLACK, ACConstants.NON_EXPORTABLE);
		if(ret > 0){
			rm.put("result", "success");
			rm.put("message", "success");
		}else{
			rm.put("result", "error");
			rm.put("message", "error");
		}
		return rm;
	}

	@RequestMapping("/deleteRule")
	@ResponseBody
	public Map<String, Object> deleteRule(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> rm = new HashMap<String, Object>();
		String tableIds = request.getParameter("tableIds");
		long userId = Long.parseLong(request.getParameter("userId"));
		int type = Integer.parseInt(request.getParameter("type"));
		String tmp = tableIds.substring(0, tableIds.length() - 1);
		String[] StableIds = tmp.split(",");

		Long[] LtableIds = new Long[StableIds.length];
		for (int i = 0; i < StableIds.length; i++) {
			LtableIds[i] = new Long(StableIds[i]);
		}

		Long ret = userService.deleteRule(LtableIds, userId, type);
		if(ret > 0){
			rm.put("result", "success");
			rm.put("message", "success");
		}else{
			rm.put("result", "error");
			rm.put("message", "error");
		}
		return rm;
	}


	@RequestMapping("/checkForAuthority")
	@ResponseBody
	public Map<String, Object> checkForAuthority(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> rm = new HashMap<String, Object>();
		long userId = Long.parseLong(request.getParameter("userId"));
		long tableId = Long.parseLong(request.getParameter("tableId"));

		long ret = userService.checkForAuthority(userId, tableId);
		if (ret == ACConstants.WHITE) {
			rm.put("code", "1");
			rm.put("result", "Permit");
		} else if (ret == ACConstants.BLACK) {
			rm.put("code", "0");
			rm.put("result", "Deny");
		} else {
			rm.put("code", "2");
			rm.put("result", "NotApplicable");
		}

		return rm;
	}

	/*
	@RequestMapping("/getTableIdsFromAC")
	@ResponseBody
	public Map<String, Object> getTableIdsFromAC(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> rm = new HashMap<String, Object>();
		long userId = Long.parseLong(request.getParameter("userId"));
		long catalogId = Long.parseLong(request.getParameter("catalogId"));

		try {
			Long[] whiteList = dbService.getTableIdsByType(userId, catalogId, ACConstants.WHITE);
			Long[] blackList = dbService.getTableIdsByType(userId, catalogId, ACConstants.BLACK);

			if (whiteList == null)
				whiteList = new Long[0];

			if (blackList == null)
				blackList = new Long[0];

			rm.put("result", "success");
			rm.put("message", "success");

			String white = "[";
			for (int i = 0; i < whiteList.length; i++) {
				white += whiteList[i];
				if (i < whiteList.length - 1)
					white += ",";
			}
			white += "]";

			String black = "[";
			for (int i = 0; i < blackList.length; i++) {
				black += blackList[i];
				if (i < blackList.length - 1)
					black += ",";
			}
			black += "]";

			rm.put("whiteList", white);
			rm.put("blackList", black);

		} catch (Exception e) {
			e.printStackTrace();
			rm.put("result", "error");
			rm.put("message", "error");
		}
		return rm;
	}
	*/

}
