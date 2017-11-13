package service.fdu_ac_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.fdu_ac_service.dao.ResultDataACDaoImp;

import java.util.List;

@Service("ResultDataACService")
public class ResultDataACService {
    @Autowired
    private ResultDataACDaoImp resultDataACDao;


//    @Transactional
//    public int applyForData(long table_id, long user_id, int type, int status) {
//        long ret = resultDataACDao.applyForData(table_id, user_id, type, status);
//        if (ret > 0) {
//            return 1;
//        }
//        return 0;
//    }

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
    public int decisionForApply(long voter_id,long action_id,int user_decision){
        long ret=resultDataACDao.decisionForApply(voter_id,action_id,user_decision);
        if(ret>0){
            return 1;
        }
        return 0;
    }

    @Transactional
    public int checkVoteSuccessForActionCount(long action_id){
        int count = resultDataACDao.checkVoteSuccessForActionCount(action_id);
        if(count>0){
            return count;
        }
        return 0;
    }


}
