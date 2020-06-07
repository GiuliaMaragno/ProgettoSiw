package it.uniroma3.siw.progetto.validatore;

import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CredenzialiService;

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

	@Override
	public void validate(Object o, Errors errors) {
		Credenziali credenziali = (Credenziali) o;
		String username = credenziali.getUsername().trim();
		String password = credenziali.getPassword().trim();

		if (username.isEmpty())
			errors.rejectValue("username", "required");
		else if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH)
			errors.rejectValue("username", "size");
		else if (this.credenzialiService.getCredenziali(username) != null)
			errors.rejectValue("username", "duplicate");

		if (password.isEmpty())
			errors.rejectValue("password", "required");
		else if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH)
			errors.rejectValue("password", "size");
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Utente.class.equals(clazz);
	}

}
