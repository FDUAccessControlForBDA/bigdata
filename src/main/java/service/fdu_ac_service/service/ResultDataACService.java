package service.fdu_ac_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.fdu_ac_service.dao.ACDaoImp;
import service.fdu_ac_service.dao.ResultDataACDaoImp;
import service.fdu_ac_service.model.ACConstants;
import service.fdu_ac_service.model.VoteActionPO;
import service.fdu_ac_service.model.VoteStatusPO;

import java.util.List;

@Service("ResultDataACService")
public class ResultDataACService {
    @Autowired
    private ResultDataACDaoImp resultDataACDao;

    @Autowired
    private ACDaoImp userDao;

    @Transactional
    public List<Long> getResultTableOwnerIdList(long table_id) {
        List<Long> ret = resultDataACDao.getResultTableOwnerIdList(table_id);
        if (ret.size() > 0) {
            return ret;
        }
        return null;
    }

    @Transactional
    public int directGiveUpOwnership(long table_id, long user_id) {
        long ret = resultDataACDao.directGiveUpOwnerShip(table_id, user_id);
        if (ret > 0) {
            return 1;
        }
        return 0;
    }

    @Transactional
    public int transferOwnershipToAdmin(long table_id, long user_id) {
        long ret = resultDataACDao.transferOwnershipToAdmin(table_id, user_id);
        if (ret > 0) {
            return 1;
        }
        return 0;
    }


    @Transactional
    public int generateRuleList(Long[] userIds, long table_id, int type, int status) {
        long ret = resultDataACDao.generateRuleList(userIds, table_id, type, status);
        if (ret > 0) {
            return 1;
        }
        return 0;
    }

    @Transactional
    public Long[] getIntersectWhite(Long[] tableIds, int type) {
        Long[] ret = resultDataACDao.getIntersectWhite(tableIds, type);
        return ret;
    }

    @Transactional
    public Long[] getUnionBlack(Long[] tableIds, int type) {
        Long[] ret = resultDataACDao.getUnionBlack(tableIds, type);
        return ret;
    }

    @Transactional
    public int newVoteAction(long table_id,long sponsor_id,int type,int status,long user_id,Long[] voterIds,int user_decision){
        long ret=resultDataACDao.newVoteAction(table_id,sponsor_id,type,status,user_id,voterIds,user_decision);
        if(ret>0){
            return 1;
        }
        return 0;
    }

    @Transactional
    public int decisionDenyForApply(long voter_id,long action_id){
        long ret=resultDataACDao.decisionForApply(voter_id,action_id,ACConstants.DECISION_DENY);
        if(ret>0){
            /* 有一个所有者投了否决票,投票失败;
             * 1)删除所有表决状态;关闭投票活动,投票活动结果改为失败;
             * 2)删除所有投票状态
             */
            ret=resultDataACDao.closeVoteAction(action_id,ACConstants.STATUS_FINISH_FAIL);
            if(ret>0){
                return 1;
            }
        }
        return 0;
    }

    @Transactional
    public int decisionPermitForApply(long voter_id, long action_id) {
        long ret = resultDataACDao.decisionForApply(voter_id, action_id, ACConstants.DECISION_PERMIT);
        if (ret > 0) {
            /* 1)检查投票活动的所有表决是不是只有"弃权"和"同意申请"两种状态;
             * 2)是,删除所有表决状态,关闭投票活动,改为成功;
             * 3)执行申请业务;
             */
            ret=resultDataACDao.checkVoteSuccessForAction(action_id);
            if(ret>0){
                ret=resultDataACDao.closeVoteAction(action_id,ACConstants.STATUS_FINISH_SUCCESS);
                if(ret>0){
                    VoteActionPO voteActionPO=resultDataACDao.getVoteActionPOById(action_id);
                    if(voteActionPO!=null){
                        //将long变量转为长度为1的Long[],为了代码重用.
                        Long[] tableIds=new Long[1];
                        tableIds[0]=voteActionPO.getTable_id();

                        switch (voteActionPO.getType()){
                            case ACConstants.TYPE_DELETEBLACK:
                                //删除黑名单
                                ret=userDao.deleteRule(tableIds,voteActionPO.getUser_id(),ACConstants.BLACK);
                                break;
                            case ACConstants.TYPE_ADDWHITE:
                                //增加白名单
                                ret=userDao.addRule(tableIds,voteActionPO.getUser_id(),ACConstants.WHITE,ACConstants.NON_EXPORTABLE);
                                break;
                            case ACConstants.TYPE_VISIT:
                                //hive层授予数据的权限

                                break;
                            default:
                                break;
                        }
                        if(ret>0){
                            return 1;
                        }
                    }
                }
            }
        }
        return 0;

    }

    @Transactional
    public int decisionGiveUpForApply(long voter_id,long action_id){
        long ret=resultDataACDao.decisionForApply(voter_id,action_id, ACConstants.DECISION_GIVEUP);
        if(ret>0){
            /* 当全部所有者投了弃权票,投票失败;
             * 1)删除所有表决状态;关闭投票活动,投票活动结果改为失败;
             * 2)删除所有投票状态
             */
            ret=resultDataACDao.checkVoteGiveUpForAction(action_id);
            if(ret>0){
                ret=resultDataACDao.closeVoteAction(action_id,ACConstants.STATUS_FINISH_FAIL);
                if(ret>0){
                    return 1;
                }
            }

        }
        return 0;
    }

    @Transactional
    public List<VoteStatusPO> getApplyList(long voter_id){
        List<VoteStatusPO> voteStatusPOList = resultDataACDao.getApplyList(voter_id);
        if(voteStatusPOList.size()>0){
            return voteStatusPOList;
        }
        return null;
    }

    @Transactional
    public List<VoteActionPO> getMyApplyList(long sponsor_id){
        List<VoteActionPO> voteActionPOList = resultDataACDao.getMyApplyList(sponsor_id);
        if(voteActionPOList.size()>0){
            return voteActionPOList;
        }
        return null;
    }

}
