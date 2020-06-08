package it.uniroma3.siw.progetto.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.ProgettoRepository;

@Service
public class ProgettoService {

	@Autowired
	ProgettoRepository progettoRepository;

	@Transactional
	public Progetto salvaProgetto(Progetto progetto) {
		return this.progettoRepository.save(progetto);

	}

	@Transactional
	public Progetto getProgetto(Long id) {
		Optional<Progetto> result = this.progettoRepository.findById(id);
		return result.orElse(null);
	}

	@Transactional
	public Progetto getProgetto(String nome) {
		Optional<Progetto> result = this.progettoRepository.findByNome(nome);
		return result.orElse(null);
	}

	@Transactional
	public Progetto condividiConAltroUtente(Progetto progetto, Utente utente) {
		progetto.addMembro(utente);
		return this.progettoRepository.save(progetto);

	}
	
	/**
	 * 
	 * Colui che aggiunge il tag al progetto deve essere l'utente proprietario
	 * @param progetto
	 * @param tag
	 * @param utente
	 * @return
	 */

	@Transactional
	public Progetto aggiungiTagAlProgetto(Progetto progetto, Tag tag, Utente utente) {
		if(progetto.getProprietario().equals(utente)) {
		progetto.addTags(tag);
		}
		return this.progettoRepository.save(progetto);
	}

	@Transactional
	public void cancellaProgetto(Progetto progetto) {
		this.progettoRepository.delete(progetto);
	}
}
