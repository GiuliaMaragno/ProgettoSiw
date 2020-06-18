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

import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CredenzialiService;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.UtenteService;
import it.uniroma3.siw.progetto.session.SessionData;
import it.uniroma3.siw.progetto.validatore.CredenzialiValidatore;
import it.uniroma3.siw.progetto.validatore.UtenteValidatore;

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
	CredenzialiValidatore credenzialiValidatore;
	@Autowired
	UtenteValidatore utenteValidatore;


	
	@RequestMapping(value= {"/home"}, method =RequestMethod.GET)
	public String home(Model model) {
		Utente utenteAutenticato= sessionData.getLoggedUtente();
		model.addAttribute("utente", utenteAutenticato);
		return "home";
	}

	/*Mostrare il profilo dell'utente*/
	@RequestMapping(value = {"/utenti/me"}, method = RequestMethod.GET)
	public String me(Model model) {
		Utente loggedUtente = sessionData.getLoggedUtente();
		Credenziali credenziali = sessionData.getLoggedCredenziali();
		model.addAttribute("loggedUtente", loggedUtente);
		model.addAttribute("credenziali", credenziali);
		return "utenteProfilo";
	}


	/*quando cambio il ruolo di un utente ad admin posso accedere a questa vista*/
	@RequestMapping(value= { "/admin" }, method = RequestMethod.GET)
	public String admin(Model model) {
		Utente utenteAutenticato=sessionData.getLoggedUtente();
		model.addAttribute("utente", utenteAutenticato);
		return "admin";
	}


	/*
	 * @RequestMapping(value = {"/utenti/{progId}"}, method = RequestMethod.GET)
	 * public String proprietario(Model model, @PathVariable Long progId) {
	 * 
	 * Utente utente = utenteService.getUtenteDaId(progId); Credenziali credenziali
	 * = this.credenzialiService.getCredenzialiDaUtente(utente);
	 * model.addAttribute("loggedUtente", utente); model.addAttribute("credenziali",
	 * credenziali); return "utenteProfilo"; }
	 */
	
	/*da admin posso vedere tutti gli utenti registrati*/
	@RequestMapping(value = {"/admin/utenti"}, method = RequestMethod.GET)
	public String listaUtenti(Model model) {
		Utente loggedUtente = sessionData.getLoggedUtente();
		List<Credenziali> tuttiCredenziali = this.credenzialiService.getAllCredenziali(); 
		model.addAttribute("loggedUtente", loggedUtente);
		model.addAttribute("credenzialiLista", tuttiCredenziali);

		return "utenti";

	}

	/*da admin posso eliminare gli utenti nel sistema*/
	@RequestMapping(value = {"/admin/utenti/{username}/delete"}, method = RequestMethod.POST)
	public String rimuoviUtenti(Model model, @PathVariable String username) {
		this.credenzialiService.eliminaCredenziali(username);
		return "redirect:/admin/utenti";

	}



	/*vista per cambiare le informazioni del proprio profilo*/
	@RequestMapping(value = {"/utenti/me/modifica"}, method = RequestMethod.GET)
	public String modificaProfilo(Model model) {

		Utente loggedUtente = sessionData.getLoggedUtente();
		Credenziali credenzialiCorr = sessionData.getLoggedCredenziali();

		model.addAttribute("credenzialiForm", credenzialiCorr);
		model.addAttribute("utenteForm", loggedUtente);
		return "modificaProfilo";

	}

	@RequestMapping(value = {"/utenti/me/modifica"}, method = RequestMethod.POST)
	public String aggiornoProfilo(Model model, @Valid @ModelAttribute("credenzialiForm") Credenziali credenziali,
			BindingResult credenzialiBindingResult,
			@Valid @ModelAttribute("utenteForm") Utente utente,BindingResult utenteBindingResult ) {


		Utente utenteCorr = sessionData.getLoggedUtente();
		Credenziali credenzialiCorr = sessionData.getLoggedCredenziali();


		// valido i campi di utente e credenziali
		this.utenteValidatore.validate(utente, utenteBindingResult);
		this.credenzialiValidatore.validate(credenziali, credenzialiBindingResult);

		// se non hanno campi invalidi salvo nel DB
		if(!utenteBindingResult.hasErrors() && !credenzialiBindingResult.hasErrors()) {
			
			utenteCorr.setNome(utente.getNome());
			utenteCorr.setCognome(utente.getCognome());
			credenzialiCorr.setUsername(credenziali.getUsername());
			credenzialiCorr.setPassword(credenziali.getPassword());
			credenzialiCorr.setUtente(utenteCorr);
			credenzialiService.salvaCredenziali(credenzialiCorr); //salvo anche utente(cascade.all)
			return "redirect:/utenti/" + utenteCorr.getId();

		}
		return "modificaProfilo";

	}

}