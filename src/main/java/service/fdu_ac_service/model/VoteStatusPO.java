package service.fdu_ac_service.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="bd_vote_status")
public class VoteStatusPO {
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column
	private long voter_id;
	
	@Column
	private long action_id;
	
	@Column
	private Timestamp vote_time;
	
	@Column
	private int user_decision;
	
	public VoteStatusPO(){}
	
	public VoteStatusPO(long voter_id, long action_id, Timestamp vote_time, int user_decision){
		super();
		this.voter_id = voter_id;
		this.action_id = action_id;
		this.vote_time = vote_time;
		this.user_decision = user_decision;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getVoter_id(){
		return voter_id;
	}
	
	public void setVoter_id(long voter_id){
		this.voter_id = voter_id;
	}
	
	public long getAction_id(){
		return action_id;
	}
	
	public void setAction_id(long action_id) {
		this.action_id = action_id;
	}

	public Timestamp getVote_time(){
		return vote_time;
	}
	
	public void setVote_time(Timestamp vote_time){
		this.vote_time = vote_time;
	}
	
	public int getUser_decision(){
		return user_decision;
	}

	public void setUser_decision(int user_decision){
		this.user_decision = user_decision;
	}

}


