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
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CredenzialiService;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.UtenteService;
import it.uniroma3.siw.progetto.session.SessionData;
import it.uniroma3.siw.progetto.validatore.CredenzialiValidatore;
import it.uniroma3.siw.progetto.validatore.ProgettoValidatore;

@Controller
public class ProgettoController {

	@Autowired
	ProgettoService progettoService;
	@Autowired
	UtenteService utenteService;
	@Autowired
	ProgettoValidatore progettoValidatore;
	@Autowired
	CredenzialiValidatore credenzialiValidatore;
	@Autowired
	CredenzialiService credenzialiService;
	@Autowired
	SessionData sessionData;


	/*lista dei miei progetti*/
	@RequestMapping(value = {"/progetti"}, method = RequestMethod.GET)
	public String mieiProgetti(Model model) {
		Utente loggedUtente = sessionData.getLoggedUtente();
		List<Progetto> listaProgetti = this.progettoService.getProgettoDaProprietario(loggedUtente);
		model.addAttribute("loggedUtente", loggedUtente);
		model.addAttribute("listaProgetti", listaProgetti);
		return "mieiProgetti";
	}


	@RequestMapping(value = {"/progetti/{progettoId}"}, method = RequestMethod.GET)
	public String progetto(Model model, @PathVariable Long progettoId) {

		Utente loggedUtente = sessionData.getLoggedUtente();
		Progetto progetto = progettoService.getProgetto(progettoId); 
		if(progetto.getProprietario().getId()!=loggedUtente.getId()  && !progetto.getMembri().contains(loggedUtente) ) 
			return "redirect:/progetti";

		model.addAttribute("loggedUtente", loggedUtente);
		model.addAttribute("progetto", progetto);	
		sessionData.setLoggedProgetto(progetto);
		return "progetto";


	}

	@RequestMapping(value = {"/progetti/aggiungi"}, method = RequestMethod.GET)
	public String creaProgettoForm(Model model){
		Utente loggedUtente = sessionData.getLoggedUtente();
		model.addAttribute("loggedUtente", loggedUtente);
		model.addAttribute("progettoForm", new Progetto());
		return "aggiungiProgetto";

	}

	@RequestMapping(value = {"/progetti/aggiungi"}, method = RequestMethod.POST)
	public String creaProgetto(@Valid @ModelAttribute("progettoForm") Progetto progetto,
			BindingResult progettoBindingResult,Model model) {

		Utente loggedUtente = sessionData.getLoggedUtente();
		//valido i campi
		progettoValidatore.validate(progetto, progettoBindingResult);
		if(!progettoBindingResult.hasErrors()) {
			progetto.setProprietario(loggedUtente);
			this.progettoService.salvaProgetto(progetto);
			return "redirect:/progetti/" + progetto.getId();
		}
		model.addAttribute("loggedUtente", loggedUtente);
		return "aggiungiProgetto";

	}

	@RequestMapping(value = {"/progetti/membro"}, method = RequestMethod.GET)
	public String aggiungiMembroForm(Model model) {
		Utente loggedUtente = sessionData.getLoggedUtente();
		Progetto loggedProgetto = sessionData.getLoggedProgetto();

		model.addAttribute("loggedUtente", loggedUtente);

		model.addAttribute("loggedProgetto", loggedProgetto);

		model.addAttribute("credenziali", new Credenziali());
		return "condividiProgetto";

	}


	@RequestMapping(value = {"/progetti/membro"}, method = RequestMethod.POST)
	public String aggiungiMembro(Model model, @ModelAttribute Credenziali credenziali , BindingResult credenzialiBindingResult) {


		Progetto loggedProgetto = sessionData.getLoggedProgetto();
		Credenziali credenzialiMembro = credenzialiService.getCredenziali(credenziali.getUsername());
		//valido i campi
		credenzialiValidatore.validateMembro(credenzialiMembro, credenzialiBindingResult);
		if(!credenzialiBindingResult.hasErrors()) { // se non ci sono errori
			Utente utenteMembro = credenzialiMembro.getUtente();
			loggedProgetto=progettoService.condividiConAltroUtente(loggedProgetto, utenteMembro);
			return "redirect:/progetti/"+loggedProgetto.getId();

		}
		return "condividiProgetto";
	}


	@RequestMapping(value= {"/progetti/{id}/elimina"}, method= RequestMethod.POST)
	public String eliminaProgetto(@PathVariable Long id, Model model) {

		Progetto progetto =this.progettoService.getProgetto(id);
		this.progettoService.cancellaProgetto(progetto);
		model.addAttribute("progetto", progetto);
		return "redirect:/progetti";


	}

	@RequestMapping(value= {"/progettiCondivisi"}, method= RequestMethod.GET)
	public String progettiCondivisiConMe(Model model) {
		Utente loggedUtente = sessionData.getLoggedUtente();
		List<Progetto> progettiCondivisiConMe = this.progettoService.getProgettiDaMembri(loggedUtente);
		model.addAttribute("loggedUtente", loggedUtente);
		model.addAttribute("progettiCondivisiConMe", progettiCondivisiConMe);
		return "progettiCondivisiConMe";

	}

	@RequestMapping(value= {"/progetti/modifica"}, method= RequestMethod.GET)
	public String ProgettoNome(  Model model) {

		model.addAttribute("progettoForm", new Progetto());
		return "progettoModifica";

	}

	@RequestMapping(value= {"/progetti/modifica"}, method= RequestMethod.POST)
	public String modificaProgettoNome(@Valid @ModelAttribute("progettoForm") Progetto progetto1, Model model, BindingResult progettoBindingResult) {

		Progetto progettoCorr = sessionData.getLoggedProgetto();

		//valido i campi
		progettoValidatore.validate(progetto1, progettoBindingResult);
		if(!progettoBindingResult.hasErrors()) {
			progettoCorr.setNome(progetto1.getNome());
			this.progettoService.salvaProgetto(progettoCorr);
			return "redirect:/progetti/"+progettoCorr.getId();
		}
		return "progettoModifica";


	}

}


