package it.uniroma3.siw.progetto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Utente;



public interface ProgettoRepository extends CrudRepository<Progetto, Long> {

	public Optional<Progetto> findByNome(String nome);

	public List<Progetto> findByMembri(Utente membro);
	
    public List<Progetto> findByProprietario(Utente proprietario);



}
