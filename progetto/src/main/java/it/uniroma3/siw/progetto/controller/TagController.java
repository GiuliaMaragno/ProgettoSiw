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
	TagValidatore tagValidatore;
	@Autowired
	TaskService taskService;


	@RequestMapping(value = {"/addTag"}, method = RequestMethod.GET)
	public String creaTagForm(Model model){
		Utente loggedUtente = sessionData.getLoggedUtente();
		Progetto loggedProgetto = sessionData.getLoggedProgetto();

		model.addAttribute("loggedProgetto", loggedProgetto);
		model.addAttribute("loggedUtente", loggedUtente);
		model.addAttribute("tagForm", new Tag());
		return "aggiungiTagAlProgetto";

	}

	/*aggiungo un tag al progetto*/
	@RequestMapping(value = {"/addTag"}, method = RequestMethod.POST)
	public String progettoTag( @Valid @ModelAttribute("tagForm") Tag tag,
			BindingResult tagBindingResult ,Model model) {


		Utente loggedUtente = sessionData.getLoggedUtente();
		Progetto loggedProgetto = sessionData.getLoggedProgetto();

		//valido i campi di tag
		tagValidatore.validate(tag,tagBindingResult);
		if(!tagBindingResult.hasErrors() ) { //se non ha errori

			tag.setProgetto(loggedProgetto);  //basta salvare chiave esterna in tag
			//loggedProgetto.getTags().add(tag);
			this.tagService.salvaTag(tag);
			//this.progettoService.salvaProgetto(loggedProgetto); 
			return "redirect:/progetti/" +loggedProgetto.getId();	
		}

		model.addAttribute("loggedUtente", loggedUtente);
		return "aggiungiTagAlProgetto";



	}

	/*aggiungo un tag del progetto ad un task*/
	@RequestMapping(value = {"/addTagAlTask"}, method =RequestMethod.GET )
	public String formTagAlTask(Model model) {


		Task loggedTask = sessionData.getLoggedTask();

		Progetto progettoCorr = this.progettoService.getProgettoDaTask(loggedTask);

		List<Tag> tags = this.tagService.getTagDaProgetto(progettoCorr);   
		List<Tag> tags_gia_presenti = this.tagService.getTagDaTask(loggedTask);
		for (Tag tag : tags_gia_presenti) {  //filtro la lista dei tag con quelli già presenti, in modo che possa inserirli solo una volta
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
		List<Tag> tags =this.tagService.getTagDaTask(loggedTask);
		tags.add(tagScelto);
		loggedTask.setTags(tags);
		this.taskService.salvaTask(loggedTask);
		return "redirect:/task/"+loggedTask.getId();



	}

}
