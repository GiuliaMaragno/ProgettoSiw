package it.uniroma3.siw.progetto.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;




import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CredenzialiService;
import it.uniroma3.siw.progetto.validatore.CredenzialiValidatore;
import it.uniroma3.siw.progetto.validatore.UtenteValidatore;

@Controller
public class AutenticazioneController {
	@Autowired
	CredenzialiService credenzialiService;

	@Autowired
	UtenteValidatore utenteValidatore;

	@Autowired
	CredenzialiValidatore credenzialiValidatore;



	/**
	 * This method is called when a GET request is sent by the user to URL "/register".
	 * This method prepares and dispatches the User registration view.
	 *
	 * @param model the Request model
	 * @return the name of the target view, that in this case is "register"
	 */
	@RequestMapping(value = { "/utenti/registra" }, method = RequestMethod.GET)
	public String mostraRegistraForm(Model model) {
		model.addAttribute("utenteForm", new Utente());
		model.addAttribute("credenzialiForm", new Credenziali());

		return "registraUtente";
	}

	/**
	 * This method is called when a GET request is sent by the user to URL "/register".
	 * This method prepares and dispatches the User registration view.
	 *
	 * @param model the Request model
	 * @return the name of the target view, that in this case is "register"
	 */
	@RequestMapping(value = { "/utenti/registra" }, method = RequestMethod.POST)
	public String registraUtente(@Valid @ModelAttribute("utenteForm") Utente utente,
			BindingResult userBindingResult,
			@Valid @ModelAttribute("credenzialiForm") Credenziali credenziali,
			BindingResult credentialsBindingResult,
			Model model) {

		// validate user and credentials fields
		this.utenteValidatore.validate(utente, userBindingResult);
		this.credenzialiValidatore.validate(credenziali, credentialsBindingResult);

		// if neither of them had invalid contents, store the User and the Credentials into the DB
		if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
			// set the user and store the credentials;
			// this also stores the User, thanks to Cascade.ALL policy
			credenziali.setUtente(utente);
			credenzialiService.salvaCredenziali(credenziali);
			return "registrazioneSuccesso";
		}
		return "registraUtente";
	}
}
