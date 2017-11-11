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


    @Transactional
    public int applyForData(long result_table_id, long user_id, int type, int status) {
        long ret = resultDataACDao.applyForData(result_table_id, user_id, type, status);
        if (ret > 0) {
            return 1;
        }
        return 0;
    }

    @Transactional
    public List<Long> getResultTableOwnerIdList(long result_table_id) {
        List<Long> ret = resultDataACDao.getResultTableOwnerIdList(result_table_id);
        if (ret.size() > 0) {
            return ret;
        }
        return null;
    }

    @Transactional
    public int directGiveUpOwnership(long result_table_id, long user_id) {
        long ret = resultDataACDao.directGiveUpOwnerShip(result_table_id, user_id);
        if (ret > 0) {
            return 1;
        }
        return 0;
    }

    @Transactional
    public int transferOwnershipToAdmin(long result_table_id, long user_id) {
        long ret = resultDataACDao.transferOwnershipToAdmin(result_table_id, user_id);
        if (ret > 0) {
            return 1;
        }
        return 0;
    }

    @Transactional
    public int generateRuleList(Long[] userIds, long result_table_id, int type, int status) {
        long ret = resultDataACDao.generateRuleList(userIds, result_table_id, type, status);
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

    //新建投票活动
    public int newVoteAction(long result_table_id,long sponsor_id,int type,int status,long user_id,Long[] voterIds,int user_decision){
        long ret=resultDataACDao.newVoteAction(result_table_id,sponsor_id,type,status,user_id,voterIds,user_decision);
        if(ret>0){
            return 1;
        }
        return 0;
    }

//    //新建其余所有者的表决活动
//    public int newVoteStatus(Long[] voterIds,long action_id,int user_decision){
//        long ret = resultDataACDao.newVoteStatus(voterIds,action_id,user_decision);
//        if(ret>0){
//            return 1;
//        }
//        return 0;
//    }

    //查看投票活动表决允许人数
    public int checkVoteSuccessForActionCount(long action_id){
        int count = resultDataACDao.checkVoteSuccessForActionCount(action_id);
        if(count>0){
            return count;
        }
        return 0;
    }


}
