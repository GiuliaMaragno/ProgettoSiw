package it.uniroma3.siw.progetto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.ProgettoRepository;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.UtenteService;
import it.uniroma3.siw.progetto.session.SessionData;
import it.uniroma3.siw.progetto.validatore.ProgettoValidatatore;

@Controller
public class ProgettoController {

	@Autowired
	ProgettoService progettoService;
	@Autowired
	UtenteService utenteService;
	//@Autowired
	//ProgettoValidatatore progettoValidatore;
	
	@Autowired
	SessionData sessionData;
	@Autowired
	ProgettoRepository progettoRepository;

	
	@RequestMapping(value = {"/progetti"}, method = RequestMethod.GET)
	public String mieiProgetti(Model model) {
		Utente loggedUtente = sessionData.getLoggedUtente();
		List<Progetto> listaProgetti = progettoRepository.findByProprietario(loggedUtente);
		model.addAttribute("loggedUtente", loggedUtente);
		model.addAttribute("listaProgetti", listaProgetti);
		return "mieiProgetti";
	}
    


}
