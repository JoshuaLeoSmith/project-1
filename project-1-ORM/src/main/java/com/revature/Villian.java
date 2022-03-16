package com.revature;

import com.revature.annotations.Entity;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;
import com.revature.annotations.ManyToOne;

@Entity
public class Villian {

	@Id
	private int id;
	private String name;
	@ManyToOne
	@JoinColumn(mappedByTable = "Prison", mappedByColumn = "id")
	private Prison jailCell;

}
