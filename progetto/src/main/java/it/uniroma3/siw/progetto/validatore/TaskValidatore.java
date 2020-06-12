package it.uniroma3.siw.progetto.validatore;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.progetto.model.Task;


@Component
public class TaskValidatore implements Validator{
	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;



	@Override
	public void validate(Object o ,Errors errors) {

		Task task = (Task) o;
		String nome = task.getNome().trim();
		String descrizione = task.getDescrizione().trim();

		if (nome.isEmpty()) 
			errors.rejectValue("nome", "required");

		else if (nome.length() < MIN_NAME_LENGTH || nome.length() > MAX_NAME_LENGTH)
			errors.rejectValue("nome", "size");
		if (descrizione.isEmpty())
			errors.rejectValue("descrizione", "required");
		else if (descrizione.length() < MIN_NAME_LENGTH || descrizione.length() > MAX_NAME_LENGTH)
			errors.rejectValue("descrizione", "size");

	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Task.class.equals(clazz);

	}

}
