package it.uniroma3.siw.progetto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.progetto.model.Commento;
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
}
