package it.uniroma3.siw.progetto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.CredenzialiRepository;
import it.uniroma3.siw.progetto.service.CredenzialiService;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.UtenteService;
import it.uniroma3.siw.progetto.session.SessionData;

@Controller
public class UtenteController {


	@Autowired
	SessionData sessionData;
	@Autowired
	CredenzialiService credenzialiService;
	@Autowired
	ProgettoService progettoService;
	@Autowired
	UtenteService utenteService;
	@Autowired
	CredenzialiRepository credenzialiRepository;


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

	@RequestMapping(value = {"/utenti/{progId}"}, method = RequestMethod.GET)
	public String proprietario(Model model, @PathVariable Long progId) {

		Utente utente = utenteService.getUtente(progId);
		Credenziali credenziali = credenzialiRepository.findByUtente(utente);
		model.addAttribute("loggedUtente", utente);
		model.addAttribute("credenziali", credenziali);
		return "utenteProfilo";
	}

	@RequestMapping(value = {"/admin/utenti"}, method = RequestMethod.GET)
	public String listaUtenti(Model model) {
		Utente loggedUtente = sessionData.getLoggedUtente();
		List<Credenziali> tuttiCredenziali = this.credenzialiService.getAllCredenziali();
		model.addAttribute("loggedUtente", loggedUtente);
		model.addAttribute("credenzialiLista", tuttiCredenziali);

		return "utenti";

	}

	@RequestMapping(value = {"/admin/utenti/{username}/delete"}, method = RequestMethod.POST)
	public String rimuoviUtenti(Model model, @PathVariable String username) {
		this.credenzialiService.eliminaCredenziali(username);
		return "redirect:/admin/utenti";

	}





}