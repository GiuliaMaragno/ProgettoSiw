package it.uniroma3.siw.progetto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.UtenteRepository;

@Service
public class UtenteService {
	@Autowired
	UtenteRepository utenteRepository;

	@Transactional
	public Utente salvaUtente(Utente utente) {
		return this.utenteRepository.save(utente);
	}

	@Transactional
	public Utente getUtenteDaId(Long id) {
		Optional<Utente> result = this.utenteRepository.findById(id);
		return result.orElse(null);

	}


	@Transactional
	public List<Utente> getAllUser() {
		List<Utente> result = new ArrayList<>();
		Iterable<Utente> i = this.utenteRepository.findAll();
		for (Utente u : i)
			result.add(u);
		return result;

	}

	@Transactional
	public Utente getUtenteDaNome(String nome) {
		Optional<Utente> result = this.utenteRepository.findByNome(nome);
		return result.orElse(null);

	}

}
