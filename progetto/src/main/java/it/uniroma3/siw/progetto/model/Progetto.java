package it.uniroma3.siw.progetto.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

@Entity
public class Progetto {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String nome;

	@Column(updatable = false, nullable = false)
	private LocalDateTime dataCreazione;

	@ManyToOne
	private Utente proprietario;

	@ManyToMany
	private List<Utente> membri;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Task> taskContenuti;

	@OneToMany
	private List<Tag> tags;

	public Progetto() {
		// TODO Auto-generated constructor stub
		this.taskContenuti = new ArrayList<Task>();
		this.membri = new ArrayList<Utente>();
		this.tags = new ArrayList<Tag>();
	}

	public Progetto(String nome) {
		this();
		this.nome = nome;

	}

	/**
	 * This method initializes the creationTimestamp and lastUpdateTimestamp of this
	 * User to the current instant. This method is called automatically just before
	 * the User is persisted thanks to the @PrePersist annotation.
	 */
	@PrePersist
	protected void onPersist() {
		this.dataCreazione = LocalDateTime.now();
	}

	public void addMembro(Utente membro) {
		if(!this.membri.contains(membro))  //se non è gia presente
		this.membri.add(membro);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Utente getProprietario() {
		return proprietario;
	}

	public void setProprietario(Utente proprietario) {
		this.proprietario = proprietario;
	}

	public List<Utente> getMembri() {
		return membri;
	}

	public void setMembri(List<Utente> membri) {
		this.membri = membri;
	}

	public List<Task> getTaskContenuti() {
		return taskContenuti;
	}

	public void setTaskContenuti(List<Task> taskContenuti) {
		this.taskContenuti = taskContenuti;
	}

	public void addTags(Tag tag) {
		this.tags.add(tag);
	}
	
	
	public List<Tag> getTags(){
		return tags;
	}
	


	public LocalDateTime getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(LocalDateTime dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataCreazione == null) ? 0 : dataCreazione.hashCode());
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
		Progetto other = (Progetto) obj;
		if (dataCreazione == null) {
			if (other.dataCreazione != null)
				return false;
		} else if (!dataCreazione.equals(other.dataCreazione))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

}
