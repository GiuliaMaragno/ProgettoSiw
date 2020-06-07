package it.uniroma3.siw.progetto.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.CredenzialiRepository;

@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS) //questo oggetto va istanziato separatamente in ogni sessione che ogni utente instaura con la nostra app
                                                                 // visuale limitata alla singola sessione
public class SessionData {
	
	/**
	 * Utente loggato in sessione 
	 */
	private Utente utente;
	
	/**
	 * credenziali dell'utente loggato in sessione
	 */
	private Credenziali credenziali;

	@Autowired
	private CredenzialiRepository  credenzialiRepository;
	
	/*
	 * Quando si vogliono ottenere le credenziali per l'utente loggato o per l'utente loggato come oggetto User
	 * ci basta cercarlo nell'oggetto credenziali
	 *
	 */
	
	public Credenziali getLoggedCredenziali() {
		if(this.credenziali==null)
			this.update();
		return this.credenziali;
		
	}
	
	public Utente getLoggedUtente() {
		if(this.utente==null)
			this.update();
		return this.utente;
	}
	
	public void update() {
		Object obj= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails dettagliUtenteAutenticato=(UserDetails) obj;
		
		this.credenziali= this.credenzialiRepository.findByUsername(dettagliUtenteAutenticato.getUsername()).get();
		this.credenziali.setPassword("[PROTECTED]");
		this.utente= this.credenziali.getUtente();
	}
	
}
	
	
