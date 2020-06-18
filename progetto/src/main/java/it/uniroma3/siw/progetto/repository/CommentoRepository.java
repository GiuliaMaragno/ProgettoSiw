package it.uniroma3.siw.progetto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.progetto.model.Commento;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;

public interface CommentoRepository extends CrudRepository<Commento, Long> {
	
	public List<Commento> findByTask(Task task);
	public List<Commento> findByUtente(Utente utente);
	

}
