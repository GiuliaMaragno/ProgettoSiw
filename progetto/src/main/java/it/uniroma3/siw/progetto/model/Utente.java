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
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;


@Entity
@Table(name = "utenti")
public class Utente {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 100)
	private String nome;

	@Column(nullable = false, length = 100)
	private String cognome;

	@Column(updatable = false, nullable = false)
	private LocalDateTime dataCreazione;

	@Column(nullable = false)
	private LocalDateTime dataUltimoAggiornamento;

	@OneToMany(cascade = CascadeType.REMOVE)
	private List<Commento> commento;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "proprietario",fetch = FetchType.EAGER)
	private List<Progetto> progettiProprietario;

	@ManyToMany(mappedBy = "membri", fetch=FetchType.EAGER)
	private List<Progetto> progettiVisibili;

	@OneToMany
	List<Task> tasks;

	public Utente() {
		// TODO Auto-generated constructor stub
		this.progettiProprietario = new ArrayList<Progetto>();
		this.progettiVisibili = new ArrayList<Progetto>();

	}

	public Utente( String nome, String cognome) {
		this();
		this.nome = nome;
		this.cognome = cognome;
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

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
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

	public List<Progetto> getProgettiProprietario() {
		return progettiProprietario;
	}

	public void setProgettiProprietario(List<Progetto> progettiProprietario) {
		this.progettiProprietario = progettiProprietario;
	}

	public List<Progetto> getProgettiVisibili() {
		return progettiVisibili;
	}

	public void setProgettiVisibili(List<Progetto> progettiVisibili) {
		this.progettiVisibili = progettiVisibili;
	}

	public List<Commento> getCommento() {
		return commento;
	}

	public void setCommento(List<Commento> commento) {
		this.commento = commento;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cognome == null) ? 0 : cognome.hashCode());
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
		Utente other = (Utente) obj;
		if (cognome == null) {
			if (other.cognome != null)
				return false;
		} else if (!cognome.equals(other.cognome))
			return false;
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

}
