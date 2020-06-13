package it.uniroma3.siw.progetto.validatore;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.progetto.model.Tag;


@Component
public class TagValidatore implements Validator{
	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;
	final Integer MAX_COLOR_LENGTH=15 ;
	final Integer MIN_COLOR_LENGTH=3;



	@Override
	public void validate(Object o ,Errors errors) {

		Tag tag = (Tag) o;
		String nome = tag.getNome().trim();
		String descrizione = tag.getDescrizione().trim();
		String colore = tag.getDescrizione().trim();
		

		if (nome.isEmpty()) 
			errors.rejectValue("nome", "required");

		else if (nome.length() < MIN_NAME_LENGTH || nome.length() > MAX_NAME_LENGTH)
			errors.rejectValue("nome", "size");
		if (descrizione.isEmpty())
			errors.rejectValue("descrizione", "required");
		else if (descrizione.length() < MIN_NAME_LENGTH || descrizione.length() > MAX_NAME_LENGTH)
			errors.rejectValue("descrizione", "size");
		
		if(colore.isEmpty())
			errors.rejectValue("colore", "required");
		else if(colore.length()> MAX_COLOR_LENGTH|| colore.length()<MIN_COLOR_LENGTH)
			errors.reject("colore", "size");
			
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Tag.class.equals(clazz);

	}



}



