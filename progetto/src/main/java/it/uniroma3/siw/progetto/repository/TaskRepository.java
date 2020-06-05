package it.uniroma3.siw.progetto.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.progetto.model.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {

}
