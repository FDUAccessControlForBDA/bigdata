package service.fdu_ac_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import service.fdu_ac_service.dao.ACDaoImp;
import service.fdu_ac_service.model.Rule;

@Service("DBService")
public class DBService {
	@Autowired
	private ACDaoImp userDao;

	@Transactional
    public Rule[] getRuleList(Long[] tableIds, int type ){
        Rule[] ruleList = userDao.getRuleList(tableIds, type);
        return ruleList;
    }

    @Transactional
    public int addRule(Long[] tableIds, long userId, int type, int status){
        long ret = userDao.addRule(tableIds, userId, type, status);
        if(ret > 0)
            return 1;
        return 0;
    }

    @Transactional
    public long deleteRule(Long[] tableIds, long user_id, int type){
        return userDao.deleteRule(tableIds, user_id, type);
    }

//    @Transactional
//    public Long[] getTableIdsByType(long userId, long catalogId, int type){
//        return userDao.getTableIdsByType(userId, catalogId, type);
//    }

    @Transactional
    public long checkForAuthority(long userId, long tableId){
        return userDao.checkForAuthority(userId, tableId);
    }
}
