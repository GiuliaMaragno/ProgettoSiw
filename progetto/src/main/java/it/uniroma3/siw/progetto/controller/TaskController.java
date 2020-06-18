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
import it.uniroma3.siw.progetto.service.CommentoService;
import it.uniroma3.siw.progetto.service.CredenzialiService;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.TagService;
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
	TaskValidatore taskValidatore;	
	@Autowired
	CredenzialiService credenzialiService;
	@Autowired
	TagService tagService;
	@Autowired
	CredenzialiValidatore credenzialiValidatore;
	@Autowired
	CommentoService commentoService;




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
	public String progettoTask( @Valid @ModelAttribute("taskForm") Task task,BindingResult taskBindingResult ,@Valid @ModelAttribute("credenziali") Credenziali credenziali,
			BindingResult credenzialiBindingResult,Model model) {


		Utente loggedUtente = sessionData.getLoggedUtente();
		Progetto loggedProgetto = sessionData.getLoggedProgetto();
		
		//valido i campi del task
		taskValidatore.validate(task,taskBindingResult);

		Credenziali credenzialiCorr = this.credenzialiService.getCredenziali(credenziali.getUsername());
		
		//valido le credenziali dell' utente al quale voglio affidare il task
		credenzialiValidatore.validateTask(credenzialiCorr, credenzialiBindingResult);
		if (credenzialiBindingResult.hasErrors()) { //se non ci sono errori
			return "aggiungiTask";
		}
		Utente membro = credenzialiCorr.getUtente(); //prendo utente dalle credenziali


		if(!taskBindingResult.hasErrors() ) { //se non ci sono errori

			task.setProgetto(loggedProgetto);
			task.setUtenteAddetto(membro);
			this.taskService.salvaTask(task);			
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

		List<Commento> commenti = this.commentoService.getCommentiDaTask(task);
		model.addAttribute("commenti", commenti);

		List<Tag> tags = this.tagService.getTagDaTask(task); 
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
		return "redirect:/progetti/" + progettoCorr.getId() ;
	}

	@RequestMapping(value = {"/task/{id}/modifica"}, method = RequestMethod.GET)
	public String taskDaModificare(Model model,@PathVariable Long id) {

		Task task = this.taskService.getTask(id);
		Progetto progettoCorr = task.getProgetto();
		model.addAttribute("taskForm", task);
		model.addAttribute("progettoCorr", progettoCorr);
		model.addAttribute("loggedUtente", sessionData.getLoggedUtente());
		return "modificaTask";
	}

	@RequestMapping(value = {"/task/{id}/modifica"}, method = RequestMethod.POST)
	public String ModificaTask( @Valid @ModelAttribute("taskForm") Task task,BindingResult taskBindingResult,Model model,
			@PathVariable Long id) {

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

	
		model.addAttribute("loggedUtente", sessionData.getLoggedUtente());
		model.addAttribute("progettoCorr", progettoCorr);
		return "modificaTask";
	}

	@RequestMapping(value = {"/taskAssegnati"}, method =RequestMethod.GET)
	public String mieiTask(Model model) {
		Utente loggedUtente = sessionData.getLoggedUtente();
		List<Task> taskContenuti = this.taskService.getTaskDaUtenteAddetto(loggedUtente);
		model.addAttribute("taskContenuti", taskContenuti);
		return "mieiTask";
	}


}
