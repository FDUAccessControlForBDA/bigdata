package web.fdu_ac_service.controllers;

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

        Long ret = userService.deleteRule(LtableIds, userId, ACConstants.WHITE);
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
        if (size > 1) {
            //直接放弃
            int ret = resultDataACService.directGiveUpOwnership(tableId, userId);
            if (ret > 0) {
                rm.put("result", "success");
                rm.put("message", "give up success");
            } else {
                rm.put("result", "error");
                rm.put("message", "error");
            }
        } else if (size == 1) {
            //转移给试验场管理人员
            int ret = resultDataACService.transferOwnershipToAdmin(tableId, userId);
            if (ret > 0) {
                rm.put("result", "success");
                rm.put("message", "transfer to admin success");
            } else {
                rm.put("result", "error");
                rm.put("message", "error");
            }
        } else {
            //出错
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
        if(size>1){
            //所有者中排除发起人,其余为表决人
            Iterator<Long> sListIterator = resultTableUserIdList.iterator();
            while(sListIterator.hasNext()){
                long e = sListIterator.next();
                if(e==sponsorId){
                    sListIterator.remove();
                }
            }
            //resultTableUserIdList.remove(resultTableId);
            //list转array
            Long[] LvoterIdList = resultTableUserIdList.toArray(new Long[resultTableUserIdList.size()]);
            //新建投票活动
            int ret = resultDataACService.newVoteAction(resultTableId, sponsorId, ACConstants.TYPE_VISIT,
                    ACConstants.STATUS_UNDERWAY, ACConstants.VALUE_DEFAULT, LvoterIdList, ACConstants.DECISON_DEFAULT);
            if(ret>0){
                rm.put("result", "success");
                rm.put("message", " new apply for view data success");
            }else{
                rm.put("result", "error");
                rm.put("message", "error");
            }
        }else if(size==1){
            //无需申请,直接查看数据
            //TODO hive层授权
        }else{
            //出错
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
        if(size>1){
            //所有者中排除发起人,其余为表决人
            Iterator<Long> sListIterator = resultTableUserIdList.iterator();
            while(sListIterator.hasNext()){
                long e = sListIterator.next();
                if(e==sponsorId){
                    sListIterator.remove();
                }
            }
            //resultTableUserIdList.remove(resultTableId);
            //list转array
            Long[] LvoterIdList = resultTableUserIdList.toArray(new Long[resultTableUserIdList.size()]);
            //新建投票活动
            int ret = resultDataACService.newVoteAction(resultTableId, sponsorId, ACConstants.TYPE_ADDWHITE,
                    ACConstants.STATUS_UNDERWAY,userId, LvoterIdList, ACConstants.DECISON_DEFAULT);
            if(ret>0){
                rm.put("result", "success");
                rm.put("message", " new apply for add white user success");
            }else{
                rm.put("result", "error");
                rm.put("message", "error");
            }
        }else if(size==1){
            //无需申请,直接查看数据
            //TODO hive层授权
        }else{
            //出错
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
        if(size>1){
            //所有者中排除发起人,其余为表决人
            Iterator<Long> sListIterator = resultTableUserIdList.iterator();
            while(sListIterator.hasNext()){
                long e = sListIterator.next();
                if(e==sponsorId){
                    sListIterator.remove();
                }
            }
            //resultTableUserIdList.remove(resultTableId);
            //list转array
            Long[] LvoterIdList = resultTableUserIdList.toArray(new Long[resultTableUserIdList.size()]);
            //新建投票活动
            int ret = resultDataACService.newVoteAction(resultTableId, sponsorId, ACConstants.TYPE_DELETEBLACK,
                    ACConstants.STATUS_UNDERWAY, userId, LvoterIdList, ACConstants.DECISON_DEFAULT);
            if(ret>0){
                rm.put("result", "success");
                rm.put("message", " new apply for delete black user success");
            }else{
                rm.put("result", "error");
                rm.put("message", "error");
            }
        }else if(size==1){
            //无需申请,直接查看数据
            //TODO hive层授权
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
        long voterId = Long.parseLong(request.getParameter("userId"));
        long actionId = Long.parseLong(request.getParameter("actionId"));
        int decision = Integer.parseInt(request.getParameter("decision"));

        int ret;//service层返回值

        if(decision==ACConstants.DECISION_DENY){
            //当前所有者表决为:拒绝申请
            ret = resultDataACService.decisionForApply(voterId,actionId,ACConstants.DECISION_DENY);
            if(ret>0){
                /* 有一个所有者投了否决票,投票失败;
                 * 1)删除所有表决状态;关闭投票活动,投票活动结果改为失败;
                 * 2)删除所有投票状态
                 */
                int ret_close=resultDataACService.closeVoteAction(actionId,ACConstants.STATUS_FINISH_FAIL);

            }

        }else if(decision==ACConstants.DECISION_PERMIT){
            //当前所有者表决为:同意申请
            ret = resultDataACService.decisionForApply(voterId,actionId,ACConstants.DECISION_PERMIT);
            if(ret>0){
                /* 1)检查投票活动的所有表决是不是只有"弃权"和"同意申请"两种状态;
                 * 2)是,删除所有表决状态,关闭投票活动,改为成功;
                 * 3)执行申请业务;
                 */
                int ret_check=resultDataACService.checkVoteSuccessForActionCount(actionId);
                if(ret_check>0){
                    int ret_close=resultDataACService.closeVoteAction(actionId,ACConstants.STATUS_FINISH_SUCCESS);
                    if(ret_close>0){
                        List<VoteActionPO> voteActionPOList=resultDataACService.getUserIdFromVoteAction(actionId);
                        if(voteActionPOList.size()>0){
                            for(VoteActionPO voteActionPO:voteActionPOList){
                                switch (voteActionPO.getType()){
                                    case ACConstants.TYPE_DELETEBLACK:
                                        //删除黑名单
                                        int ret_delete=resultDataACService.deleteBlack();
                                        break;
                                    case ACConstants.TYPE_ADDWHITE:

                                        break;
                                    case ACConstants.TYPE_VISIT:

                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }


            }

        }else if(decision==ACConstants.DECISION_GIVEUP){
            //当前所有者表决为:弃权
            ret = resultDataACService.decisionForApply(voterId,actionId,ACConstants.DECISION_GIVEUP);
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

}
