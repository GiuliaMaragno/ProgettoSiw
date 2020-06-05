package it.uniroma3.siw.progetto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.repository.TagRepository;

@Service
public class TagService {

	@Autowired
	TagRepository tagRepository;

	@Transactional
	public Tag salvaTag(Tag tag) {
		return this.tagRepository.save(tag);
	}

	@Transactional
	public void cancellaTag(Tag tag) {
		this.tagRepository.delete(tag);
	}

}
