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

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.ProgettoRepository;
import it.uniroma3.siw.progetto.repository.TagRepository;
import it.uniroma3.siw.progetto.repository.TaskRepository;
import it.uniroma3.siw.progetto.repository.UtenteRepository;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.TagService;
import it.uniroma3.siw.progetto.service.TaskService;
import it.uniroma3.siw.progetto.service.UtenteService;
import it.uniroma3.siw.progetto.session.SessionData;
import it.uniroma3.siw.progetto.validatore.ProgettoValidatore;
import it.uniroma3.siw.progetto.validatore.TagValidatore;


@Controller
public class TagController {

	@Autowired
	ProgettoService progettoService;
	@Autowired
	UtenteService utenteService;
	@Autowired
	ProgettoValidatore progettoValidatore;

	@Autowired
	SessionData sessionData;
	@Autowired
	TagService tagService;
	@Autowired
	UtenteRepository utenteRepository;
	@Autowired
	TagValidatore tagValidatore;
	@Autowired
	TaskService taskService;
	@Autowired
	TaskRepository taskRepository;
	@Autowired
	TagRepository tagRepository;
	@Autowired
	ProgettoRepository progettoRepository;




	@RequestMapping(value = {"/addTag"}, method = RequestMethod.GET)
	public String creaTagForm(Model model){
		Utente loggedUtente = sessionData.getLoggedUtente();
		Progetto loggedProgetto = sessionData.getLoggedProgetto();

		model.addAttribute("loggedProgetto", loggedProgetto);
		model.addAttribute("loggedUtente", loggedUtente);
		model.addAttribute("tagForm", new Tag());
		return "aggiungiTagAlProgetto";

	}

	@RequestMapping(value = {"/addTag"}, method = RequestMethod.POST)
	public String progettoTag( @Valid @ModelAttribute("tagForm") Tag tag,
			BindingResult tagBindingResult ,Model model) {


		Utente loggedUtente = sessionData.getLoggedUtente();
		Progetto loggedProgetto = sessionData.getLoggedProgetto();
		//Task loggedTask = sessionData.getLoggedTask();

		tagValidatore.validate(tag,tagBindingResult);
		//if(progetto.getNome()!=null&&this.progettoService.getProgetto(progetto.getNome()).getProprietario().equals(loggedUtente)) {
		if(!tagBindingResult.hasErrors() ) {

			tag.setProgetto(loggedProgetto);
			loggedProgetto.getTags().add(tag);
			this.tagService.salvaTag(tag);
			this.progettoService.salvaProgetto(loggedProgetto);

			return "redirect:/progetti/" +loggedProgetto.getId();	
		}

		model.addAttribute("loggedUtente", loggedUtente);

		return "aggiungiTagAlProgetto";



	}

	@RequestMapping(value = {"/addTagAlTask"}, method =RequestMethod.GET )
	public String formTagAlTask(Model model) {


		Task loggedTask = sessionData.getLoggedTask();

		Progetto progettoCorr = progettoRepository.findByTaskContenuti(loggedTask);
		
		List<Tag> tags = tagRepository.findByProgetto(progettoCorr);   
		List<Tag> tags_gia_presenti = tagRepository.findByTasks(loggedTask);
		for (Tag tag : tags_gia_presenti) {
			if(tags.contains(tag)) {
				tags.remove(tag);
			}
		}

		model.addAttribute("tags", tags);
		model.addAttribute("progetto", progettoCorr);
		return "aggiungiTagAlTask";


	}
	@RequestMapping(value = {"/addTagAlTask/{id}"}, method =RequestMethod.GET )
	public String aggiungiTagAlTask(Model model, @PathVariable Long id) {

		Task loggedTask = sessionData.getLoggedTask();

		Tag tagScelto = tagService.getTag(id);
		List<Tag> tags = tagRepository.findByTasks(loggedTask);
        tags.add(tagScelto);
		loggedTask.setTags(tags);
		this.taskService.salvaTask(loggedTask);
		return "redirect:/task/"+loggedTask.getId();



	}

}
