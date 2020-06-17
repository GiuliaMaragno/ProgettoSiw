package it.uniroma3.siw.progetto.validatore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.UtenteService;
import it.uniroma3.siw.progetto.session.SessionData;

@Component
public class ProgettoValidatore implements Validator{
	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;

	@Autowired
	private ProgettoService progettoService;
	@Autowired
	UtenteService utenteService;
	@Autowired
	SessionData sessionData;


	@Override
	public void validate(Object o, Errors errors) {
		Progetto progetto = (Progetto) o;
		String nome = progetto.getNome().trim();
		Utente utenteCorr = utenteService.getUtenteDaId(sessionData.getLoggedUtente().getId());


		if (nome.isEmpty())
			errors.rejectValue("nome", "required");
		else if (nome.length() < MIN_NAME_LENGTH || nome.length() > MAX_NAME_LENGTH)
			errors.rejectValue("nome", "size");


		else if(this.progettoService.progettiTuoi(utenteCorr,progetto)) 
			errors.rejectValue("nome","duplicato");




	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Progetto.class.equals(clazz);

	}

}
