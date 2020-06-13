package it.uniroma3.siw.progetto.model;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
public class Credenziali {

    public static final String DEFAULT_RUOLO = "DEFAULT";
    public static final String ADMIN_RUOLO = "ADMIN";

  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

   
    @Column(unique = true, nullable = false, length = 100)
    private String username;

    
    @Column(nullable = false, length = 100)
    private String password;

   
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)        // when the account is created...
                                                    //when the account is removed
    private Utente utente;

    
    @Column(updatable = false, nullable = false)
    private LocalDateTime dataCreazione;

    
    @Column(nullable = false)
    private LocalDateTime dataUltimoAggiornamento;

 
    @Column(nullable = false, length = 10)
    private String ruolo;

    public Credenziali() {
    }

    public Credenziali(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    
    @PrePersist
    protected void onPersist() {
        this.dataCreazione = LocalDateTime.now();
        this.dataUltimoAggiornamento = LocalDateTime.now();
    }

    
    @PreUpdate
    protected void onUpdate() {
        this.dataUltimoAggiornamento = LocalDateTime.now();
    }

    // GETTERS AND SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    // EQUALS AND HASHCODE

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Credenziali other = (Credenziali) obj;
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
		if (ruolo == null) {
			if (other.ruolo != null)
				return false;
		} else if (!ruolo.equals(other.ruolo))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataCreazione == null) ? 0 : dataCreazione.hashCode());
		result = prime * result + ((dataUltimoAggiornamento == null) ? 0 : dataUltimoAggiornamento.hashCode());
		result = prime * result + ((ruolo == null) ? 0 : ruolo.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}


    
    
}
