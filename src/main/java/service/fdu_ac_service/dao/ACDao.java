package service.fdu_ac_service.dao;

import service.fdu_ac_service.model.Rule;

public interface ACDao {
	  public Rule[] getRuleList(Long[] tableIds, int type);
	    public long addRule(Long[] tableIds, long userId, int type, int status);
	    public long deleteRule(Long[] tableIds, long user_id, int type);
	    public String getUserName(long user_id);
	    // public Long[] getTableIdsByType(long userId, long catalogId, int type);
	    public long checkForAuthority(long userId, long tableId);
}
