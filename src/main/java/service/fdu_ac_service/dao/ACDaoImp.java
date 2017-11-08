package service.fdu_ac_service.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import service.fdu_ac_service.model.ACConstants;
import service.fdu_ac_service.model.UserAuthorityPO;
import service.fdu_ac_service.model.Rule;

@Repository("ACDao")
public class ACDaoImp implements ACDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Rule[] getRuleList(Long[] tableIds, int type) {

		// 通过关系代数除法获取这些表共有的用户id
		String sql = "select distinct user_id from bd_user_authority_list R1 " + "where not exists "
				+ "( select table_id from (select table_id from bd_user_authority_list where table_id in (:tableIds)) S "
				+ "where not exists "
				+ "(select * from bd_user_authority_list R2 where R2.user_id=R1.user_id and R2.table_id=S.table_id and R1.type = R2.type)) and R1.type = "
				+ type;

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameterList("tableIds", tableIds);
		List<BigInteger> userIds = query.list();

		if (userIds.size() > 0) {
			Rule[] rules = new Rule[userIds.size()];
			for (int i = 0; i < userIds.size(); i++) {
				// Rule rule = new Rule(userIds.get(i),
				// getUserName(userIds.get(i)), type, 0);
				Rule rule = new Rule(userIds.get(i).longValue(), "UserName", type, 0);
				rules[i] = rule;
			}
			return rules;
		}
		return null;
	}

	@Override
	public long addRule(Long[] tableIds, long userId, int type, int status) {
		long rst = 0;

		// add UserAuthorityPO to db
		Session session = sessionFactory.getCurrentSession();
		for (int i = 0; i < tableIds.length; i++) {
			String hql = "from UserAuthorityPO ua where ua.user_id=? and ua.table_id=? and ua.type=?";
			Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter(0, userId)
					.setParameter(1, tableIds[i]).setParameter(2, type);
			if (query.list().size() <= 0) {
				UserAuthorityPO acUser = new UserAuthorityPO(tableIds[i], userId, type, status);
				rst = (Long) session.save(acUser);
			}
		}
		return rst;
	}

	@Override
	public long deleteRule(Long[] tableIds, long user_id, int type) {

		String hql = "delete UserAuthorityPO ua where ua.user_id = ? and ua.type = ? and ua.table_id in (:tableIds)";
		Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter(0, user_id).setParameter(1, type)
				.setParameterList("tableIds", tableIds);
		return query.executeUpdate();
	}

	@Override
	public String getUserName(long user_id) {

		String sql = "select name from bd_user where id=" + user_id;
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		List<String> userName = query.list();
		if (userName.size() > 0)
			return userName.get(0);

		return null;
	}

	/*
	@Override
	public Long[] getTableIdsByType(long userId, long catalogId, int type) {
		String hql = "select au.id from ACUser au where au.user_id = ? and au.catalog_id = ? and au.type = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter(0, userId)
				.setParameter(1, catalogId).setParameter(2, type);
		List<Long> ruleIds = query.list();
		if (ruleIds.size() > 0) {
			String hql2 = "select distinct ar.table_id from ACRule ar where ar.rule_id in (";
			for (int i = 0; i < ruleIds.size(); i++) {
				hql2 += ruleIds.get(i) + ",";
			}
			hql2 = hql2.substring(0, hql2.length() - 1);
			hql2 = hql2 + ")";

			query = sessionFactory.getCurrentSession().createQuery(hql2);
			List<Long> tableIds = query.list();
			Long[] LtableIds = new Long[tableIds.size()];
			tableIds.toArray(LtableIds);
			return LtableIds;
		}
		return null;
	}
	*/

	@Override
	public long checkForAuthority(long userId, long tableId) {
		String hql = "from UserAuthorityPO ua where ua.table_id = ? and ua.user_id = ?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter(0, tableId).setParameter(1, userId);
        List<UserAuthorityPO> user = query.list();
        if(user.size() > 0){
        	int result = 1;
        	for(int i = 0; i < user.size(); i++)
        		result = result & user.get(i).getType();
        	return result;
        }
        return ACConstants.NORMAL;
	}
}
