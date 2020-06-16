package it.uniroma3.siw.progetto.validatore;

import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CredenzialiService;
import it.uniroma3.siw.progetto.session.SessionData;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for Credentials
 */
@Component
public class CredenzialiValidatore implements Validator {

	final Integer MAX_USERNAME_LENGTH = 20;
	final Integer MIN_USERNAME_LENGTH = 4;
	final Integer MAX_PASSWORD_LENGTH = 20;
	final Integer MIN_PASSWORD_LENGTH = 6;

	@Autowired
	CredenzialiService credenzialiService;
	@Autowired
	SessionData sessionData;

	@Override
	public void validate(Object o, Errors errors) {
		Credenziali credenziali = (Credenziali) o;
		String username = credenziali.getUsername().trim();
		String password = credenziali.getPassword().trim();
		
		Credenziali credenziali2 = this.credenzialiService.getCredenziali(username);
		

		if (username.isEmpty())
			errors.rejectValue("username", "required");
		else if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH)
			errors.rejectValue("username", "size");
		else if (credenziali2!=null && !credenziali2.getUtente().equals(sessionData.getLoggedUtente()))
			errors.rejectValue("username", "duplicate");
		

		if (password.isEmpty())
			errors.rejectValue("password", "required");
		else if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH)
			errors.rejectValue("password", "size");
	}
	
	public void validateTask(Object o, Errors errors) {
		Credenziali credenziali = (Credenziali) o;
		//String username = credenziali.getUsername().trim();

		Progetto loggedProgetto = sessionData.getLoggedProgetto();
		List<Utente> utentiMembri = loggedProgetto.getMembri(); //


		if(credenziali==null) {
			errors.rejectValue("username", "null");
		}
		else if(!utentiMembri.contains(credenziali.getUtente())) {
			errors.rejectValue("username", "noMembro");
		}
		
	}
	
	
	public void validateMembro(Object o, Errors errors) {
		Credenziali credenziali = (Credenziali) o;
       
		Utente loggedUtente = sessionData.getLoggedUtente();
		if(credenziali==null ) 
			errors.rejectValue("username", "null");
		else if(credenziali.getUtente().equals(loggedUtente))
			errors.rejectValue("username", "tu");

	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Utente.class.equals(clazz);
	}

}
