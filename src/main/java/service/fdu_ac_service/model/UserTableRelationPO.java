package service.fdu_ac_service.model;

import javax.persistence.Column;  
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;  
import javax.persistence.Table; 

@Entity
@Table(name="bd_user_table_relation")
public class UserTableRelationPO {
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)//用AUTO出错,hibernate_sequence' doesn't exist
	private long id;
	
	@Column
	private long table_id;
	
	@Column
	private long user_id;
	
	public UserTableRelationPO(){}

	public UserTableRelationPO(long table_id, long user_id) {
		super();
		this.table_id = table_id;
		this.user_id = user_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTable_id() {
		return table_id;
	}

	public void setTable_id(long table_id) {
		this.table_id = table_id;
	}
	
	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
}
