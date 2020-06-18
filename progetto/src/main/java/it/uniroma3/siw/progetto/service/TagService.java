package it.uniroma3.siw.progetto.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.model.Task;
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
	
	@Transactional
	public Tag getTag(Long id) {
		Optional<Tag> result = this.tagRepository.findById(id);
		return result.orElse(null);
	}

	@Transactional
	public List<Tag> getTagDaTask(Task task){
		return this.tagRepository.findByTasks(task);
	}
	
	@Transactional
	public List<Tag> getTagDaProgetto(Progetto progetto){
		return this.tagRepository.findByProgetto(progetto);
		
	}
}
