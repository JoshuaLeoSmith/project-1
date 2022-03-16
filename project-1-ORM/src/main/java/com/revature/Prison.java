package com.revature;

import java.util.List;
import java.util.Objects;

import com.revature.annotations.Entity;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;
import com.revature.annotations.OneToMany;

@Entity
public class Prison {

	@Id
	private int id;
	private String name;
	private int StateId;
	@OneToMany
	@JoinColumn()
	private List<Villian> captureCells;

	public Prison() {
	}

	public Prison(String name, int stateId, List<Villian> captureCells) {
		this.name = name;
		StateId = stateId;
		this.captureCells = captureCells;
	}

	public Prison(int id, String name, int stateId, List<Villian> captureCells) {
		this.id = id;
		this.name = name;
		StateId = stateId;
		this.captureCells = captureCells;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getStateId() {
		return StateId;
	}

	public List<Villian> getCaptureCells() {
		return captureCells;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStateId(int stateId) {
		StateId = stateId;
	}

	public void setCaptureCells(List<Villian> captureCells) {
		this.captureCells = captureCells;
	}

	@Override
	public int hashCode() {
		return Objects.hash(StateId, captureCells, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Prison other = (Prison) obj;
		return (StateId == other.StateId) && Objects.equals(captureCells, other.captureCells) && (id == other.id)
				&& Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Prison [id=" + id + ", name=" + name + ", StateId=" + StateId + ", captureCells=" + captureCells + "]";
	}

}
