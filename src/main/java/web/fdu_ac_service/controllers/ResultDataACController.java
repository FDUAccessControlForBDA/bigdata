package web.fdu_ac_service.controllers;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.fdu_ac_service.model.ACConstants;
import service.fdu_ac_service.model.VoteActionPO;
import service.fdu_ac_service.model.VoteStatusPO;
import service.fdu_ac_service.service.DBService;
import service.fdu_ac_service.service.ResultDataACService;
import service.fdu_ac_service.utils.UtilsHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
public class ResultDataACController {
    @Autowired
    private ResultDataACService resultDataACService;
    @Autowired
    private DBService userService;

    @RequestMapping("/deleteWhite")
    @ResponseBody
    public Map<String, Object> deleteWhite(HttpServletRequest request, HttpServletResponse response) {
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

        int ret = userService.deleteRule(LtableIds, userId, ACConstants.WHITE);
        if (ret > 0) {
            rm.put("result", "success");
            rm.put("message", "success");
        } else {
            rm.put("result", "error");
            rm.put("message", "error");
        }
        return rm;
    }

    @RequestMapping("/giveUpOwnership")
    @ResponseBody
    public Map<String, Object> giveUpOwnerShip(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        Map<String, Object> rm = new HashMap<String, Object>();
        long tableId = Long.parseLong(request.getParameter("tableId"));
        long userId = Long.parseLong(request.getParameter("userId"));

        List<Long> resultTableUserIdList = resultDataACService.getResultTableOwnerIdList(tableId);
        int size = resultTableUserIdList.size();
        int ret;
        if (size > 1) {
            //直接放弃
            ret = resultDataACService.directGiveUpOwnership(tableId, userId);
        } else if (size == 1) {
            //转移给试验场管理人员
            ret = resultDataACService.transferOwnershipToAdmin(tableId, userId);
        }else{
            ret=0;
        }
        if (ret > 0) {
            rm.put("result", "success");
            rm.put("message", "success");
        } else {
            rm.put("result", "error");
            rm.put("message", "error");
        }
        return rm;
    }

    @RequestMapping("/applyForData")
    @ResponseBody
    public Map<String, Object> applyForData(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        Map<String, Object> rm = new HashMap<String, Object>();
        long resultTableId=Long.parseLong(request.getParameter("tableId"));
        long sponsorId=Long.parseLong(request.getParameter("sponsorId"));

        List<Long> resultTableUserIdList=resultDataACService.getResultTableOwnerIdList(resultTableId);
        int size=resultTableUserIdList.size();

        int ret=0;
        if(size>1){
            //所有者中排除发起人,其余为表决人
            Iterator<Long> sListIterator = resultTableUserIdList.iterator();
            while(sListIterator.hasNext()){
                long e = sListIterator.next();
                if(e==sponsorId){
                    sListIterator.remove();
                }
            }
            //list转array
            Long[] LvoterIdList = resultTableUserIdList.toArray(new Long[resultTableUserIdList.size()]);
            //新建投票活动
            ret = resultDataACService.newVoteAction(resultTableId, sponsorId, ACConstants.TYPE_VISIT,
                    ACConstants.STATUS_UNDERWAY, ACConstants.VALUE_DEFAULT, LvoterIdList, ACConstants.DECISON_DEFAULT);

        }else if(size==1){
            //无需申请,直接查看数据
            //TODO hive层授权
        }else{
            //出错
            ret=0;
        }

        if(ret>0){
            rm.put("result", "success");
            rm.put("message", "success");
        }else{
            rm.put("result", "error");
            rm.put("message", "error");
        }
        return rm;
    }

    @RequestMapping("/applyForWhite")
    @ResponseBody
    public Map<String, Object> applyForWhite(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        Map<String, Object> rm = new HashMap<String, Object>();
        long resultTableId=Long.parseLong(request.getParameter("tableId"));
        long sponsorId=Long.parseLong(request.getParameter("sponsorId"));
        long userId=Long.parseLong(request.getParameter("userId"));

        List<Long> resultTableUserIdList=resultDataACService.getResultTableOwnerIdList(resultTableId);
        int size=resultTableUserIdList.size();

        int ret;
        if(size>1){
            //所有者中排除发起人,其余为表决人
            Iterator<Long> sListIterator = resultTableUserIdList.iterator();
            while(sListIterator.hasNext()){
                long e = sListIterator.next();
                if(e==sponsorId){
                    sListIterator.remove();
                }
            }
            //list转array
            Long[] LvoterIdList = resultTableUserIdList.toArray(new Long[resultTableUserIdList.size()]);
            //新建投票活动
            ret = resultDataACService.newVoteAction(resultTableId, sponsorId, ACConstants.TYPE_ADDWHITE,
                    ACConstants.STATUS_UNDERWAY,userId, LvoterIdList, ACConstants.DECISON_DEFAULT);

        }else if(size==1){
            //无需申请,直接增加白名单
            Long[] tableIds=new Long[1];
            tableIds[0]=resultTableId;
            ret=userService.addRule(tableIds,userId,ACConstants.BLACK,ACConstants.NON_EXPORTABLE);
        }else{
            ret=0;
        }

        if(ret>0){
            rm.put("result", "success");
            rm.put("message", "success");
        }else{
            rm.put("result", "error");
            rm.put("message", "error");
        }
        return rm;
    }

    @RequestMapping("/applyForBlack")
    @ResponseBody
    public Map<String, Object> applyForBlack(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        Map<String, Object> rm = new HashMap<String, Object>();
        long resultTableId=Long.parseLong(request.getParameter("tableId"));
        long sponsorId=Long.parseLong(request.getParameter("sponsorId"));
        long userId=Long.parseLong(request.getParameter("userId"));

        List<Long> resultTableUserIdList=resultDataACService.getResultTableOwnerIdList(resultTableId);
        int size=resultTableUserIdList.size();
        int ret=0;
        if(size>1){
            //所有者中排除发起人,其余为表决人
            Iterator<Long> sListIterator = resultTableUserIdList.iterator();
            while(sListIterator.hasNext()){
                long e = sListIterator.next();
                if(e==sponsorId){
                    sListIterator.remove();
                }
            }
            //list转array
            Long[] LvoterIdList = resultTableUserIdList.toArray(new Long[resultTableUserIdList.size()]);
            //新建投票活动
            ret = resultDataACService.newVoteAction(resultTableId, sponsorId, ACConstants.TYPE_DELETEBLACK,
                    ACConstants.STATUS_UNDERWAY, userId, LvoterIdList, ACConstants.DECISON_DEFAULT);
        }else if(size==1){
            //无需申请,直接删除黑名单
            Long[] tableIds=new Long[1];
            tableIds[0]=resultTableId;
            ret=userService.deleteRule(tableIds,userId,ACConstants.BLACK);

        }else{
            ret=0;
        }

        if(ret>0){
            rm.put("result", "success");
            rm.put("message", "success");
        }else{
            //出错
            rm.put("result", "error");
            rm.put("message", "error");
        }
        return rm;
    }

    @RequestMapping("/decisionForApply")
    @ResponseBody
    public Map<String, Object> decisionForApply(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        Map<String, Object> rm = new HashMap<String, Object>();
        long voterId = Long.parseLong(request.getParameter("voterId"));
        long actionId = Long.parseLong(request.getParameter("actionId"));
        int decision = Integer.parseInt(request.getParameter("decision"));

        int ret;//service层返回值

        if(decision==ACConstants.DECISION_DENY){
            //当前所有者表决为:拒绝申请
            ret = resultDataACService.decisionDenyForApply(voterId,actionId);
        }else if(decision==ACConstants.DECISION_PERMIT){
            //当前所有者表决为:同意申请
            ret = resultDataACService.decisionPermitForApply(voterId,actionId);
        }else if(decision==ACConstants.DECISION_GIVEUP){
            //当前所有者表决为:弃权
            ret = resultDataACService.decisionGiveUpForApply(voterId,actionId);
        }else{
            ret=0;
        }

        if (ret > 0) {
            rm.put("result", "success");
            rm.put("message", "success");
        }else{
            rm.put("result", "error");
            rm.put("message", "error");
        }
        return rm;
    }



    @RequestMapping("/generateWhiteList")
    @ResponseBody
    public Map<String, Object> generateWhiteList(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        Map<String, Object> rm = new HashMap<String, Object>();
        String tableIds = request.getParameter("tableIds");
        long resultTableId = Long.parseLong(request.getParameter("resultTableId"));
        String tmp = tableIds.substring(0, tableIds.length() - 1);
        String[] StableIds = tmp.split(",");

        Long[] LtableIds = new Long[StableIds.length];
        for (int i = 0; i < StableIds.length; i++) {
            LtableIds[i] = new Long(StableIds[i]);
        }
        try {
            //取原数据白名单的交集
            Long[] LuserIdList = resultDataACService.getIntersectWhite(LtableIds, ACConstants.WHITE);
            //取结果数据所有者集合
            List<Long> resultTableUserIdList = resultDataACService.getResultTableOwnerIdList(resultTableId);

            if (LuserIdList.length > 0 && resultTableUserIdList.size() > 0) {
                //list转array
                Long[] LresultTableUserIdList = resultTableUserIdList.toArray(new Long[resultTableUserIdList.size()]);
                //取交集
                Long[] defaultWhiteList = UtilsHelper.intersect(LuserIdList, LresultTableUserIdList);
                //生成结果数据默认白名单
                long ret = resultDataACService.generateRuleList(defaultWhiteList, resultTableId, ACConstants.WHITE, ACConstants.NON_EXPORTABLE);
                if (ret > 0) {
                    rm.put("result", "success");
                    rm.put("message", "success");
                } else {
                    rm.put("result", "error");
                    rm.put("message", "error");
                }
            } else {
                rm.put("result", "success");
                rm.put("message", "don't have to generate white rule list.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            rm.put("result", "error");
            rm.put("message", "error");
        }
        return rm;
    }

    @RequestMapping("/generateBlackList")
    @ResponseBody
    public Map<String, Object> generateBlackList(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        Map<String, Object> rm = new HashMap<String, Object>();
        String tableIds = request.getParameter("tableIds");
        long resultTableId = Long.parseLong(request.getParameter("resultTableId"));
        String tmp = tableIds.substring(0, tableIds.length() - 1);
        String[] StableIds = tmp.split(",");

        Long[] LtableIds = new Long[StableIds.length];
        for (int i = 0; i < StableIds.length; i++) {
            LtableIds[i] = new Long(StableIds[i]);
        }
        try {
            //取原数据黑名单的并集
            Long[] LuserIdList = resultDataACService.getUnionBlack(LtableIds, ACConstants.BLACK);
            if (LuserIdList.length > 0) {
                //生成结果数据默认黑名单
                long ret = resultDataACService.generateRuleList(LuserIdList, resultTableId, ACConstants.BLACK, ACConstants.NON_EXPORTABLE);
                if (ret > 0) {
                    rm.put("result", "success");
                    rm.put("message", "success");
                } else {
                    rm.put("result", "error");
                    rm.put("message", "error");
                }
            } else {
                rm.put("result", "success");
                rm.put("message", "don't have to generate white rule list.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            rm.put("result", "error");
            rm.put("message", "error");
        }
        return rm;
    }

    @RequestMapping("/getApplyList")
    @ResponseBody
    public Map<String, Object> getApplyList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> rm = new HashMap<>();
        response.addHeader("Access-Control-Allow-Origin", "*");
        long voterId = Long.parseLong(request.getParameter("voterId"));

        try {
            List<VoteStatusPO> voteStatusPOList = resultDataACService.getApplyList(voterId);
            JSONArray voteStatusList = new JSONArray();
            for (VoteStatusPO vs : voteStatusPOList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", vs.getId());
                map.put("voterId", vs.getVoter_id());
                map.put("actionId", vs.getAction_id());
                map.put("voteTime", vs.getVote_time());
                map.put("userDecision", vs.getUser_decision());
                voteStatusList.put(map);
            }
            rm.put("result", "success");
            rm.put("message", "success");
            rm.put("code", "200");
            rm.put("voteStatusList", voteStatusList.toString());
        } catch (Exception e) {
            e.printStackTrace();
            rm.put("result", "error");
            rm.put("message", "error");
        }
        return rm;
    }

    @RequestMapping("/getMyApplyList")
    @ResponseBody
    public Map<String, Object> getMyApplyList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> rm = new HashMap<>();
        response.addHeader("Access-Control-Allow-Origin", "*");
        long sponsorId = Long.parseLong(request.getParameter("sponsorId"));

        try {
            List<VoteActionPO> voteActionPOList = resultDataACService.getMyApplyList(sponsorId);
            JSONArray voteActionList = new JSONArray();
            for (VoteActionPO va : voteActionPOList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", va.getId());
                map.put("tableId", va.getTable_id());
                map.put("sponsorId", va.getSponsor_id());
                map.put("type", va.getType());
                map.put("status", va.getStatus());
                map.put("sponsorTime", va.getSponsor_time());
                voteActionList.put(map);
            }
            rm.put("result", "success");
            rm.put("message", "success");
            rm.put("code", "200");
            rm.put("voteActionList", voteActionList.toString());
        } catch (Exception e) {
            e.printStackTrace();
            rm.put("result", "error");
            rm.put("message", "error");
        }
        return rm;
    }

}
