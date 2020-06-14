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
import it.uniroma3.siw.progetto.service.CommentoService;
import it.uniroma3.siw.progetto.service.TaskService;
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


	@RequestMapping(value = "/addCommento", method = RequestMethod.GET)
	public String aggiungiCommento(Model model) {
		
		model.addAttribute("commentoForm", new Commento());
		return "commento";

	}

	@RequestMapping(value = "/addCommento", method = RequestMethod.POST)
	public String commentoTask(@Valid @ModelAttribute("commentoForm") Commento commento, BindingResult commentoBindingResult,Model model) {

		Task task = sessionData.getLoggedTask();

		commentoValidatore.validate(commento, commentoBindingResult);

		if(!commentoBindingResult.hasErrors() ) {
			task.getCommentiTask().add(commento);
			commento.setTask(task);
			this.commentoService.salvaCommento(commento);
			this.taskService.salvaTask(task);
			return "redirect:/task/" + task.getId();
		}
		return "commento";
	}

}