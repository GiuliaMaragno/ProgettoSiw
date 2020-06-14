package it.uniroma3.siw.progetto.validatore;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.progetto.model.Commento;

@Component
public class CommentoValidatore  implements Validator {


	@Override
	public void validate(Object o, Errors errors) {

		Commento commento = (Commento) o;
		String descrizione = commento.getDescrizione().trim();
		if (descrizione.isEmpty())
			errors.rejectValue("descrizione", "required");

	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Commento.class.equals(clazz);

	}
}
