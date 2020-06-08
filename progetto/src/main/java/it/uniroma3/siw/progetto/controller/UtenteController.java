package it.uniroma3.siw.progetto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.session.SessionData;

@Controller
public class UtenteController {


	@Autowired
	SessionData sessionData;

	@RequestMapping(value= {"/home"}, method =RequestMethod.GET)
	public String home(Model model) {
		Utente utenteAutenticato= sessionData.getLoggedUtente();
		model.addAttribute("utente", utenteAutenticato);
		return "home";
	}



	@RequestMapping(value= { "/admin" }, method = RequestMethod.GET)
	public String admin(Model model) {
		Utente utenteAutenticato=sessionData.getLoggedUtente();
		model.addAttribute("utente", utenteAutenticato);
		return "admin";
	}
	
	@RequestMapping(value = {"/utenti/me"}, method = RequestMethod.GET)
	public String me(Model model) {
		Utente loggedUtente = sessionData.getLoggedUtente();
		Credenziali credenziali = sessionData.getLoggedCredenziali();
		model.addAttribute("loggedUtente", loggedUtente);
		model.addAttribute("credenziali", credenziali);
		return "utenteProfilo";
	}
}


