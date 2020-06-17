package it.uniroma3.siw.progetto.controller;



import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.progetto.model.Commento;
import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.CommentoRepository;
import it.uniroma3.siw.progetto.repository.TagRepository;
import it.uniroma3.siw.progetto.repository.TaskRepository;
import it.uniroma3.siw.progetto.repository.UtenteRepository;
import it.uniroma3.siw.progetto.service.CredenzialiService;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.TaskService;
import it.uniroma3.siw.progetto.service.UtenteService;
import it.uniroma3.siw.progetto.session.SessionData;
import it.uniroma3.siw.progetto.validatore.CredenzialiValidatore;
import it.uniroma3.siw.progetto.validatore.ProgettoValidatore;
import it.uniroma3.siw.progetto.validatore.TaskValidatore;

@Controller
public class TaskController {

	@Autowired
	ProgettoService progettoService;
	@Autowired
	UtenteService utenteService;
	@Autowired
	ProgettoValidatore progettoValidatore;

	@Autowired
	SessionData sessionData;
	@Autowired
	TaskService taskService;
	@Autowired
	UtenteRepository utenteRepository;
	@Autowired
	TaskValidatore taskValidatore;
	@Autowired
	TagRepository tagRepository;
	@Autowired
	CommentoRepository commentoRepository;
	@Autowired
	CredenzialiService credenzialiService;
	@Autowired
	TaskRepository taskRepository;
	@Autowired
	CredenzialiValidatore credenzialiValidatore;




	@RequestMapping(value = {"/addTask"}, method = RequestMethod.GET)
	public String creaTaskForm(Model model){
		Utente loggedUtente = sessionData.getLoggedUtente();
		Progetto loggedProgetto = sessionData.getLoggedProgetto();

		model.addAttribute("loggedProgetto", loggedProgetto);
		model.addAttribute("loggedUtente", loggedUtente);
		model.addAttribute("taskForm", new Task());
		model.addAttribute("credenziali", new Credenziali());
		return "aggiungiTask";

	}
	@RequestMapping(value = {"/addTask"}, method = RequestMethod.POST)
	public String progettoTask( @Valid @ModelAttribute("taskForm") Task task,@Valid @ModelAttribute Credenziali credenziali,
			BindingResult taskBindingResult ,BindingResult credenzialiBindingResult,Model model) {


		Utente loggedUtente = sessionData.getLoggedUtente();
		Progetto loggedProgetto = sessionData.getLoggedProgetto();

		Credenziali credenzialiCorr = this.credenzialiService.getCredenziali(credenziali.getUsername());//
		credenzialiValidatore.validateTask(credenzialiCorr, credenzialiBindingResult);
		if (credenzialiBindingResult.hasErrors()) {
			return "aggiungiTask";
		}
		Utente membro = credenzialiCorr.getUtente(); //prendo utente dalle credenziali


		taskValidatore.validate(task,taskBindingResult);
		if(!taskBindingResult.hasErrors() ) {

			task.setProgetto(loggedProgetto);
			task.setUtenteAddetto(membro);
			this.taskService.salvaTask(task);			
			sessionData.setLoggedTask(task);

			return "redirect:/progetti/"+loggedProgetto.getId();	
		}

		model.addAttribute("loggedUtente", loggedUtente);

		return "aggiungiTask";

	}



	@RequestMapping(value = {"/task/{taskId}"}, method = RequestMethod.GET)
	public String visualizzaTask(Model model, @PathVariable Long taskId) {

		Utente loggedUtente = sessionData.getLoggedUtente();
		Task task = this.taskService.getTask(taskId);
		sessionData.setLoggedTask(task);
		model.addAttribute("loggedTask", task);
		model.addAttribute("loggedUtente", loggedUtente);

		List<Commento> commenti = commentoRepository.findByTask(task);
		model.addAttribute("commenti", commenti);

		List<Tag> tags = tagRepository.findByTasks(task); 
		model.addAttribute("tags", tags);
		return "tasks";

	}


	@RequestMapping(value = {"/task/{id}/elimina"}, method = RequestMethod.POST)
	public String eliminaTask(@PathVariable Long id , Model model) {

		Task taskDaElim = this.taskService.getTask(id);
		Progetto progettoCorr = sessionData.getLoggedProgetto();

		progettoCorr.getTaskContenuti().remove(taskDaElim);
		this.progettoService.salvaProgetto(progettoCorr);
		this.taskService.cancellaTask(taskDaElim);



		//this.progettoService.salvaProgetto(progettoCorr);
		model.addAttribute("progetto", progettoCorr);
		//model.addAttribute("task", task);
		return "redirect:/progetti/" + progettoCorr.getId() ;
	}

	@RequestMapping(value = {"/task/{id}/modifica"}, method = RequestMethod.GET)
	public String taskDaModificare(Model model,@PathVariable Long id) {

		Task task = this.taskService.getTask(id);
		model.addAttribute("taskForm", task);
		return "modificaTask";
	}

	@RequestMapping(value = {"/task/{id}/modifica"}, method = RequestMethod.POST)
	public String ModificaTask( @Valid @ModelAttribute("taskForm") Task task,Model model,
			@PathVariable Long id,BindingResult taskBindingResult) {

		Task taskCorr = this.taskService.getTask(id);
		Progetto progettoCorr = taskCorr.getProgetto();
		taskValidatore.validate(task, taskBindingResult);
		if(!taskBindingResult.hasErrors()) {
			taskCorr.setNome(task.getNome());
			taskCorr.setDescrizione(task.getDescrizione());
			progettoService.salvaProgetto(progettoCorr);
			taskService.salvaTask(taskCorr);
			return "redirect:/progetti/" + progettoCorr.getId();
		}
		return "modificaTask";
	}

	@RequestMapping(value = {"/taskAssegnati"}, method =RequestMethod.GET)
	public String mieiTask(Model model) {
		Utente loggedUtente = sessionData.getLoggedUtente();
		List<Task> taskContenuti = taskRepository.findByUtenteAddetto(loggedUtente);
		model.addAttribute("taskContenuti", taskContenuti);
		return "mieiTask";
	}


}
