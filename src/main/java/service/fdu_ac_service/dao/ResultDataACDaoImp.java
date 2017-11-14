package service.fdu_ac_service.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import service.fdu_ac_service.model.ACConstants;
import service.fdu_ac_service.model.UserAuthorityPO;
import service.fdu_ac_service.model.VoteActionPO;
import service.fdu_ac_service.model.VoteStatusPO;
import service.fdu_ac_service.utils.UtilsHelper;

import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository("ResultDataDao")
public class ResultDataACDaoImp implements ResultDataACDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    //生成结果数据所有者列表
    public long generateOwnerList(Long[] tableIds, long table_id) {
        //TODO
        long rst = 0;
        return rst;
    }

    //初始化结果数据默认名单
    public long generateRuleList(Long[] userIds, long table_id, int type, int status) {
        long rst = 0;

        // add UserAuthorityPO to db
        Session session = sessionFactory.getCurrentSession();
        for (int i = 0; i < userIds.length; i++) {
            String hql = "from UserAuthorityPO ua where ua.user_id=? and ua.table_id=? and ua.type=?";
            Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter(0, userIds[i])
                    .setParameter(1, table_id).setParameter(2, type);
            if (query.list().size() <= 0) {
                UserAuthorityPO acUser = new UserAuthorityPO(table_id, userIds[i], type, status);
                rst = (Long) session.save(acUser);
            }
        }
        return rst;
    }

    //获取原数据白名单的交集
    public Long[] getIntersectWhite(Long[] tableIds, int type) {
        // 通过关系代数除法获取这些表共有的用户id
        String sql = "select distinct user_id from bd_user_authority_list R1 " + "where not exists "
                + "( select table_id from (select table_id from bd_user_authority_list where table_id in (:tableIds)) S "
                + "where not exists "
                + "(select * from bd_user_authority_list R2 where R2.user_id=R1.user_id and R2.table_id=S.table_id and" +
                " R1.type = R2.type)) and R1.type = " + type;

        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .addScalar("user_id", LongType.INSTANCE).setParameterList("tableIds", tableIds);
        List<Long> userIds = query.list();

        if (userIds.size() > 0) {
            Long[] rules = userIds.toArray(new Long[userIds.size()]);
            return rules;
        }
        return null;
    }

    //获取原数据黑名单的并集
    public Long[] getUnionBlack(Long[] tableIds, int type) {
        String sql = "select distinct user_id FROM bd_user_authority_list where table_id in (:tableIds) and type=" + type;
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
                .addScalar("user_id", LongType.INSTANCE).setParameterList("tableIds", tableIds);
        List<Long> userIds = query.list();

        if (userIds.size() > 0) {
            Long[] rules = userIds.toArray(new Long[userIds.size()]);
            return rules;
        }
        return null;
    }


    @Override
    //查看结果数据的所有者列表
    public List<Long> getResultTableOwnerIdList(long table_id) {

        String hql = "select distinct utr.user_id from UserTableRelationPO utr where utr.table_id=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter(0, table_id);
        List<Long> userIdList = query.list();

        if (userIdList.size() > 0) {
            return userIdList;
        }
        return null;
    }

    @Override
    //直接放弃结果数据所有权
    public long directGiveUpOwnerShip(long table_id, long user_id) {
        long rst = 0;
        String hql = "delete UserTableRelationPO utr where utr.table_id=? and utr.user_id=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter(0, table_id).setParameter(1, user_id);
        rst = query.executeUpdate();
        return rst;
    }

    @Override
    //转移所有权给试验场管理员
    public long transferOwnershipToAdmin(long table_id, long user_id) {
        long rst = 0;

        String sql = "select distinct id from bd_user where roles like 'admin%'";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(sql);
        List<BigInteger> adminIds = query.list();

        if (adminIds.size() > 0) {
            if (adminIds.size() == 1) {
                long admin_id = adminIds.get(0).longValue();
                String hql = "update UserTableRelationPO utr set utr.user_id=? where utr.table_id=? and utr.user_id=?";
                query = sessionFactory.getCurrentSession().createQuery(hql)
                        .setParameter(0, admin_id).setParameter(1, table_id).setParameter(2, user_id);
                rst = query.executeUpdate();
            }
        }
        return rst;

    }

    //新建投票活动
    public long newVoteAction(long table_id, long sponsor_id, int type, int status, long user_id, Long[] voterIds, int user_decision) {
        int result = 1;
        long actionId = 0;
        String hql = "from VoteActionPO va where va.table_id=? and va.sponsor_id=? and va.type=? and va.status=? and va.user_id=?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql).setParameter(0, table_id).setParameter(1, sponsor_id).setParameter(2, type)
                .setParameter(3, status).setParameter(4, user_id);

        if (query.list().size() <= 0) {
            Timestamp currentTime = UtilsHelper.getCurrentTime();
            VoteActionPO voteActionPO = new VoteActionPO(table_id, sponsor_id, type, user_id, status, currentTime);
            actionId = (Long) session.save(voteActionPO);


            if (actionId != 0) {
                for (long voteId : voterIds) {
                    VoteStatusPO voteStatusPO = new VoteStatusPO(voteId, actionId, currentTime, user_decision);
                    long id = (Long) session.save(voteStatusPO);
                    int tmp = id > 0 ? 1 : 0;
                    result = result & tmp;
                }
                return result;
            }
        }
        return 0;
    }

    @Override
    //为投票活动表决
    public long decisionForApply(long voter_id, long action_id, int user_decision) {
        long rst = 0;
        String hql = "from VoteStatusPO vs where vs.voter_id=? and vs.action_id=?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql).setParameter(0, voter_id).setParameter(1, action_id);

        if (query.list().size() > 0) {
            Timestamp current_time = UtilsHelper.getCurrentTime();
            hql = "update VoteStatusPO vs set vs.user_decision=? , vs.vote_time=? where vs.voter_id=? and vs.action_id=?";
            query = session.createQuery(hql)
                    .setParameter(0, user_decision).setParameter(1, current_time)
                    .setParameter(2, voter_id).setParameter(3, action_id);
            rst = query.executeUpdate();
        }
        return rst;
    }

    @Override
    //关闭投票活动，删除所有表决
    public long closeVoteAction(long action_id, int status) {
        long rst = 0;
        String hql = "delete VoteStatusPO where action_id=?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql).setParameter(0, action_id);
        int count = query.executeUpdate();//返回值是执行操作的条数
        if (count > 0) {
            hql="update VoteActionPO va set va.status=? , va.sponsor_time=? where va.id=?";
            Timestamp currentTime=UtilsHelper.getCurrentTime();
            query=session.createQuery(hql).setParameter(0,status).setParameter(1,currentTime).setParameter(2,action_id);
            rst=query.executeUpdate();
        }
        return rst;
    }

    //用过action_id获取VoteActionPO对象
    public VoteActionPO getVoteActionPOById(long action_id) {
        String hql="from VoteActionPO va where va.id=?";
        Session session=sessionFactory.getCurrentSession();
        Query query=session.createQuery(hql).setParameter(0,action_id);
        VoteActionPO voteActionPO=(VoteActionPO) query.uniqueResult();
        if(voteActionPO!=null){
            return voteActionPO;
        }
        return null;
    }

    @Override
    //查看投票活动表决允许人数
    public long checkVoteSuccessForAction(long action_id) {
        //TODO
        long rst=0;
        String hql="from VoteStatusPO vs where vs.user_decision not in (?,?) and vs.action_id=?";
        Session session=sessionFactory.getCurrentSession();
        Query query=session.createQuery(hql).setParameter(0,ACConstants.DECISION_PERMIT)
                .setParameter(1,ACConstants.DECISION_GIVEUP).setParameter(2,action_id);
        if(query.list().size()<=0){
            rst=1;
        }
        return rst;
    }

    @Override
    //查看所有他人发起的申请活动
    public List<VoteStatusPO> getApplyList(long voter_id) {
        //TODO
        return null;
    }

    @Override
    //查看自己发起的申请活动
    public List<VoteActionPO> getMyApplyList(long sponsor_id) {
        //TODO
        return null;
    }

}
