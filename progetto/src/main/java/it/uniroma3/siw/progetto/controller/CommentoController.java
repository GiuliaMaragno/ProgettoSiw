package it.uniroma3.siw.progetto.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.progetto.model.Commento;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.CommentoRepository;
import it.uniroma3.siw.progetto.service.CommentoService;
import it.uniroma3.siw.progetto.service.TaskService;
import it.uniroma3.siw.progetto.service.UtenteService;
import it.uniroma3.siw.progetto.session.SessionData;
import it.uniroma3.siw.progetto.validatore.CommentoValidatore;

@Controller
public class CommentoController {

	@Autowired
	CommentoService commentoService;
	@Autowired
	SessionData sessionData;
	@Autowired
	TaskService taskService;
	@Autowired
	CommentoValidatore commentoValidatore;
	@Autowired
	CommentoRepository commentoRepository;
	@Autowired
	UtenteService utenteService;


	@RequestMapping(value = "/addCommento", method = RequestMethod.GET)
	public String aggiungiCommento(Model model) {
		model.addAttribute("loggedTask", sessionData.getLoggedTask());
		model.addAttribute("commentoForm", new Commento());
		return "commento";

	}

	/*aggiungo i commenti su qualsiasi task di un progetto su cui ho visibilità*/
	@RequestMapping(value = "/addCommento", method = RequestMethod.POST)
	public String commentoTask(@Valid @ModelAttribute("commentoForm") Commento commento, BindingResult commentoBindingResult,Model model) {

		Utente utente = sessionData.getLoggedUtente();

		Task task = sessionData.getLoggedTask();

		/*valido il campo descrizione di commento*/
		commentoValidatore.validate(commento, commentoBindingResult);
		if(!commentoBindingResult.hasErrors() ) { //se non ha errori
			commento.setTask(task);
			commento.setUtente(utente);
			this.commentoService.salvaCommento(commento);
			return "redirect:/task/" + task.getId();
		}
		return "commento";
	}

}
