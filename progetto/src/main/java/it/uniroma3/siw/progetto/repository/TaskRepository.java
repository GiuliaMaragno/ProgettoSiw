package it.uniroma3.siw.progetto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;

public interface TaskRepository extends CrudRepository<Task, Long> {
	
	public Optional<Task> findById(Task task);
	public List<Task> findByUtenteAddetto(Utente utente);
	

}
