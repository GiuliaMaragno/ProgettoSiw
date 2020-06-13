package it.uniroma3.siw.progetto.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;

import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.UtenteRepository;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.TagService;

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
	
	


	@RequestMapping(value = {"/tags/aggiungi"}, method = RequestMethod.GET)
	public String creaTaskForm(Model model){
		Utente loggedUtente = sessionData.getLoggedUtente();
		Progetto loggedProgetto = sessionData.getLoggedProgetto();

		model.addAttribute("loggedProgetto", loggedProgetto);
		model.addAttribute("loggedUtente", loggedUtente);
		model.addAttribute("tagForm", new Tag());
		//model.addAttribute("progettoForm", new Progetto());
		return "aggiungiTag";

	}
	
	@RequestMapping(value = {"/tags/aggiungi"}, method = RequestMethod.POST)
	public String progettoTag( @Valid @ModelAttribute("tagForm") Tag tag,
			BindingResult tagBindingResult ,Model model) {


		Utente loggedUtente = sessionData.getLoggedUtente();
		Progetto loggedProgetto = sessionData.getLoggedProgetto();

		tagValidatore.validate(tag,tagBindingResult);
		//if(progetto.getNome()!=null&&this.progettoService.getProgetto(progetto.getNome()).getProprietario().equals(loggedUtente)) {
		if(!tagBindingResult.hasErrors() ) {
			
			tag.setProgetto(loggedProgetto);

			this.tagService.salvaTag(tag);
			
			return "redirect:/progetti/" +loggedProgetto.getId();	
		}

		model.addAttribute("loggedUtente", loggedUtente);

		return "aggiungiTag";



	}

}
