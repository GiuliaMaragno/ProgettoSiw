package it.uniroma3.siw.progetto.validatore;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.progetto.model.Progetto;

@Component
public class ProgettoValidatatore implements Validator{
	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;


	@Override
	public void validate(Object o, Errors errors) {
		Progetto progetto = (Progetto) o;
		String nome = progetto.getNome().trim();
		
		if (nome.isEmpty())
			errors.rejectValue("nome", "required");
		else if (nome.length() < MIN_NAME_LENGTH || nome.length() > MAX_NAME_LENGTH)
			errors.rejectValue("nome", "size");


	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Progetto.class.equals(clazz);

	}

}
