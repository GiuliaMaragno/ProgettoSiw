package it.uniroma3.siw.progetto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Tag {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String colore;
	private String descrizione;
	@Column(nullable = false)
	private String nome;

	/*
	 * @ManyToMany(mappedBy = "tags") private List<Task> tasks;
	 */

	public Tag() {
		// TODO Auto-generated constructor stub
	}

	public Tag(String nome, String colore, String descrizione) {
		this.nome = nome;
		this.colore = colore;
		this.descrizione = descrizione;
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getColore() {
		return colore;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	/*
	 * public List<Task> getTasks() { return tasks; }
	 * 
	 * public void setTasks(List<Task> tasks) { this.tasks = tasks; }
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

}
