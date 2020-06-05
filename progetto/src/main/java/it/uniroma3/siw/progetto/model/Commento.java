package it.uniroma3.siw.progetto.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Commento {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String descrizione;

	/*
	 * @ManyToOne private Task task;
	 */

	public Commento() {

	}

	public Commento(String descrizione) {
		this.descrizione = descrizione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/*
	 * public Task getTask() { return task; }
	 * 
	 * 
	 * public void setTask(Task task) { this.task = task; }
	 */

}
