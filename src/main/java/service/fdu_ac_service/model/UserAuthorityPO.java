package service.fdu_ac_service.model;

import javax.persistence.Column;  
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;  
import javax.persistence.Table;  

@Entity
@Table(name="bd_user_authority_list")
public class UserAuthorityPO {
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column
	private long table_id;
	
	@Column
	private long user_id;
	
	@Column
	private int type;
	
	@Column
	private int status;
	
	public UserAuthorityPO() {}

	public UserAuthorityPO(long table_id, long user_id, int type, int status) {
		super();
		this.table_id = table_id;
		this.user_id = user_id;
		this.type = type;
		this.status = status;	
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
	
	public long getUser_id(){
		return user_id;
	}
	
	public void setUser_id(long user_id){
		this.user_id = user_id;
	}
	
	public int getType(){
		return type;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public int getStatus(){
		return status;
	}
	
	public void setStatus(int status){
		this.status = status;
	}
}
