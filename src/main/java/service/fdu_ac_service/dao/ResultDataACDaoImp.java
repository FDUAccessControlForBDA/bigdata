package service.fdu_ac_service.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import service.fdu_ac_service.model.Rule;
import service.fdu_ac_service.model.UserAuthorityPO;
import service.fdu_ac_service.model.VoteActionPO;
import service.fdu_ac_service.model.UserTableRelationPO;

import java.math.BigInteger;
import java.util.List;

@Repository("ResultDataDao")
public class ResultDataACDaoImp implements ResultDataACDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    //生成结果数据所有者列表
    public long generateOwnerList(Long[] tableIds, long result_table_id) {
        //TODO
        long rst = 0;
        return rst;
    }

    //初始化结果数据默认名单
    public long generateRuleList(Long[] userIds, long result_table_id, int type, int status) {
        long rst = 0;

        // add UserAuthorityPO to db
        Session session = sessionFactory.getCurrentSession();
        for (int i = 0; i < userIds.length; i++) {
            String hql = "from UserAuthorityPO ua where ua.user_id=? and ua.table_id=? and ua.type=?";
            Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter(0, userIds[i])
                    .setParameter(1, result_table_id).setParameter(2, type);
            if (query.list().size() <= 0) {
                UserAuthorityPO acUser = new UserAuthorityPO(result_table_id, userIds[i], type, status);
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
                + "(select * from bd_user_authority_list R2 where R2.user_id=R1.user_id and R2.table_id=S.table_id and R1.type = R2.type)) and R1.type = "
                + type;

        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("user_id", LongType.INSTANCE).setParameterList("tableIds", tableIds);
        List<Long> userIds = query.list();

        if (userIds.size() > 0) {
            Long[] rules = userIds.toArray(new Long[userIds.size()]);
            return rules;
        }
        return null;
    }

    //获取原数据黑名单的并集
    public Long[] getUnionBlack(Long[] tableIds, int type) {
        String sql="select distinct user_id FROM bd_user_authority_list where table_id in (:tableIds) and type="+type;
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addScalar("user_id", LongType.INSTANCE).setParameterList("tableIds", tableIds);
        List<Long> userIds=query.list();

        if(userIds.size()>0){
            Long[] rules = userIds.toArray(new Long[userIds.size()]);
            return rules;
        }
        return null;
    }


    @Override
    //查看结果数据的所有者列表
    public List<Long> getResultTableOwnerIdList(long result_table_id) {

        String hql = "select distinct utr.user_id from UserTableRelationPO utr where utr.table_id=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter(0, result_table_id);
        List<Long> userIdList = query.list();

        if (userIdList.size() > 0) {
            return userIdList;
        }
        return null;
    }

    @Override
    //直接放弃结果数据所有权
    public long directGiveUpOwnerShip(long result_table_id, long user_id) {
        long rst = 0;
        String hql = "delete UserTableRelationPO utr where utr.table_id=? and utr.user_id=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter(0, result_table_id).setParameter(1, user_id);
        rst = query.executeUpdate();
        return rst;
    }

    @Override
    //转移所有权给试验场管理员
    public long transferOwnershipToAdmin(long result_table_id, long user_id) {
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
                        .setParameter(0, admin_id).setParameter(1, result_table_id).setParameter(2, user_id);
                rst = query.executeUpdate();
            }
        }
        return rst;


    }

    @Override
    //用户申请查看数据
    public long applyForData(long result_table_id, long user_id, int type, int status) {
        long rst = 0;
        //add VoteActionPO to DB
        Session session = sessionFactory.getCurrentSession();
        return rst;
    }


}
