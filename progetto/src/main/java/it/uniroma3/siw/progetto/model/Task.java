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
import javax.persistence.PreUpdate;

@Entity
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String nome;
	@Column(nullable = false)
	private String descrizione;

	@Column(updatable = false, nullable = false)
	private LocalDateTime dataCreazione;

	@Column(nullable = false)
	private LocalDateTime dataUltimoAggiornamento;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "task")
	private List<Commento> commentiTask;

	@ManyToMany
	private List<Tag> tags;


	@ManyToOne
	private Progetto progetto;

	@ManyToOne
	private Utente utenteAddetto;

	public Task() {
		// TODO Auto-generated constructor stub
		this.tags= new ArrayList<Tag>();
		this.commentiTask= new ArrayList<Commento>();
	}

	public Task(String nome, String descrizione) {
		this();
		this.nome = nome;
		this.descrizione = descrizione;
	}

	/**
	 * This method initializes the creationTimestamp and lastUpdateTimestamp of this
	 * User to the current instant. This method is called automatically just before
	 * the User is persisted thanks to the @PrePersist annotation.
	 */
	@PrePersist
	protected void onPersist() {
		this.dataCreazione = LocalDateTime.now();
		this.dataUltimoAggiornamento = LocalDateTime.now();
	}

	/**
	 * This method updates the lastUpdateTimestamp of this User to the current
	 * instant. This method is called automatically just before the User is updated
	 * thanks to the @PreUpdate annotation.
	 */
	@PreUpdate
	protected void onUpdate() {
		this.dataUltimoAggiornamento = LocalDateTime.now();
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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public LocalDateTime getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(LocalDateTime dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public LocalDateTime getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}

	public void setDataUltimoAggiornamento(LocalDateTime dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}

	public List<Commento> getCommentiTask() {
		return commentiTask;
	}

	public void setCommentiTask(List<Commento> commentiTask) {
		this.commentiTask = commentiTask;
	}

	public Utente getUtenteAddetto() {
		return utenteAddetto;
	}

	public void setUtenteAddetto(Utente utenteAddetto) {
		this.utenteAddetto = utenteAddetto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataCreazione == null) ? 0 : dataCreazione.hashCode());
		result = prime * result + ((dataUltimoAggiornamento == null) ? 0 : dataUltimoAggiornamento.hashCode());
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
		Task other = (Task) obj;
		if (dataCreazione == null) {
			if (other.dataCreazione != null)
				return false;
		} else if (!dataCreazione.equals(other.dataCreazione))
			return false;
		if (dataUltimoAggiornamento == null) {
			if (other.dataUltimoAggiornamento != null)
				return false;
		} else if (!dataUltimoAggiornamento.equals(other.dataUltimoAggiornamento))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	public void addCommento(Commento commento) {
		this.commentiTask.add(commento);

	}

	public void addTag(Tag tag) {
		this.tags.add(tag);

	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Progetto getProgetto() {
		return progetto;
	}

	public void setProgetto(Progetto progetto) {
		this.progetto = progetto;
	}

}
