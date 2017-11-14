package service.fdu_ac_service.dao;

import service.fdu_ac_service.model.Rule;
import service.fdu_ac_service.model.VoteActionPO;
import service.fdu_ac_service.model.VoteStatusPO;

import java.util.Date;
import java.util.List;

public interface ResultDataACDao {


//        //初始化结果数据默认黑名单
//        public long generateBlackList(Long[] tableIds,long result_table_id);
//        //检查某个用户是否有查看结果数据的权限
//        public long checkAuthorityForView(Long[] tableIds,long user_id);

//        //数据所有者申请添加白名单
//        public long applyForWhite(long result_table_id,long user_id,int type,int status);
//        //数据所有者申请删除黑名单
//        public long applyForBlack(long result_table_id,long user_id,int type);
//        //查看所有他人(和自己有关)发起的申请列表
//        public VoteActionPO[] getApplyList(long user_id);
//        //查看所有自己发起的申请列表
//        public VoteActionPO[] getMyApplyList(long user_id);
//        //对申请进行审批,可同意或反对或弃权
//        public long decisionForApply(long action_id,int type);
//        //查看结果数据的白名单或黑名单
//        public Rule[] getRuleList(long result_table_id,int type);
//        //用户申请查看数据
//        public long applyForData(long table_id, long user_id, int type, int status);

    //生成结果数据所有者列表
    public long generateOwnerList(Long[] tableIds, long result_table_id);

    //初始化结果数据默认名单
    public long generateRuleList(Long[] userIds, long result_table_id, int type, int status);

    //获取原数据白名单的交集
    public Long[] getIntersectWhite(Long[] tableIds, int type);

    //获取原数据黑名单的并集
    public Long[] getUnionBlack(Long[] tableIds, int type);

    //查看结果数据的所有者id列表
    public List<Long> getResultTableOwnerIdList(long table_id);

    //直接放弃结果数据所有权
    public long directGiveUpOwnerShip(long table_id, long user_id);

    //转移所有权给试验场管理员
    public long transferOwnershipToAdmin(long table_id, long user_id);

    //新建投票活动
    public long newVoteAction(long table_id,long sponsor_id,int type,int status,long user_id,Long[] voterIds,int user_decision);

    //为投票活动表决
    public long decisionForApply(long voter_id,long action_id,int user_decision);

    //关闭投票活动，删除所有表决
    public long closeVoteAction(long action_id,int status);

    //用过action_id获取VoteActionPO对象
    public VoteActionPO getVoteActionPOById(long action_id);

    //查看投票活动表决允许人数
    public long checkVoteSuccessForAction(long action_id);

    //查看所有他人发起的申请活动
    public List<VoteStatusPO> getApplyList(long voter_id);

    //查看自己发起的申请活动
    public List<VoteActionPO> getMyApplyList(long sponsor_id);






}
