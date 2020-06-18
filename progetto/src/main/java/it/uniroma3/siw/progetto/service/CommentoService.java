package it.uniroma3.siw.progetto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.progetto.model.Commento;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.CommentoRepository;

@Service
public class CommentoService {

	@Autowired
	CommentoRepository commentoRepository;

	@Transactional
	public Commento salvaCommento(Commento commento) {
		return this.commentoRepository.save(commento);
	}

	@Transactional
	public void cancellaCommento(Commento commento) {
		this.commentoRepository.delete(commento);
	}
	
	@Transactional
	public List<Commento> getCommentiDaUtente(Utente utente){
		return this.commentoRepository.findByUtente(utente);
	}
	
	@Transactional
	public List<Commento> getCommentiDaTask(Task task){
		return this.commentoRepository.findByTask(task);
	}
}
