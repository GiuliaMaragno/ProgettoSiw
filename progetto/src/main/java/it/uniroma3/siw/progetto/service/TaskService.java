package it.uniroma3.siw.progetto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.progetto.model.Commento;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.TaskRepository;

@Service
public class TaskService {

	@Autowired
	TaskRepository taskRepository;

	@Transactional
	public Task salvaTask(Task task) {
		return this.taskRepository.save(task);
	}

	@Transactional
	public void cancellaTask(Task task) {
		this.taskRepository.delete(task);
	}

	public Task assegnaTaskAdUtenteConVisibilità(Progetto progetto, Utente utente, Task task) {
		if (progetto.getMembri().contains(utente)) {
			task.setUtenteAddetto(utente);
		}
		return this.taskRepository.save(task);

	}

	/**************************************
	 * DA RIVEDERE
	 ***********************************/

	public Task aggiungiUnTagAlTaskMioProgetto(Progetto progetto, Utente utente, Tag tag, Task task) {
		if (progetto.getProprietario().equals(utente)) {
			task.addTag(tag);
		}
		return this.taskRepository.save(task);

	}

	public Task aggiungiUnCommentoAdUnTaskDiProgettoConVisibilità(Progetto progetto, Utente utente, Task task, Commento commento) {
		if (progetto.getMembri().contains(utente)) {
			task.addCommento(commento);
		}
		return this.taskRepository.save(task);

	}
	
	
	@Transactional
	public Task getTask(Long id) {
		Optional<Task> result = this.taskRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public List<Task> getUtenteDaTags(Tag tag){
		return this.taskRepository.findByTags(tag);
	}
	
	@Transactional
	public List<Task> getDaProgetto(Progetto progetto){
		return this.taskRepository.findByProgetto(progetto);
	}
	
	@Transactional
	public List<Task> getTaskDaUtenteAddetto(Utente utente){
		return this.taskRepository.findByUtenteAddetto(utente);
	}
}
