package service.fdu_ac_service.model;

import java.sql.Timestamp;
import javax.persistence.Column;  
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;  
import javax.persistence.Table;  

@Entity
@Table(name="bd_vote_action")
public class VoteActionPO {
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column
	private long table_id;
	
	@Column
	private long sponsor_id;
	
	@Column
	private int type;
	
	@Column
	private int status;
	
	@Column
	private Timestamp sponsor_time;
	
	public VoteActionPO(){}
	
	public VoteActionPO(long table_id, long sponsor_id, int type, int status, Timestamp sponsor_time){
		super();
		this.table_id = table_id;
		this.sponsor_id = sponsor_id;
		this.type = type;
		this.status = status;
		this.sponsor_time = sponsor_time;
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
	
	public long getSponsor_id(){
		return sponsor_id;
	}
	
	public void setSponsor_id(long sponsor_id){
		this.sponsor_id = sponsor_id;
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
	
	public Timestamp getSponsor_time(){
		return sponsor_time;
	}
	
	public void setSponsor_time(Timestamp sponsor_time){
		this.sponsor_time = sponsor_time;
	}
}
