package it.uniroma3.siw.progetto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.repository.CredenzialiRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The CredentialService handles logic regarding Credentials.
 * This mainly consists in retrieving or storing Credentials in the DB.
 */
@Service
public class CredenzialiService {

    @Autowired
    protected CredenzialiRepository credenzialiRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

   
    @Transactional
    public Credenziali getCredenziali(long id) {
        Optional<Credenziali> result = this.credenzialiRepository.findById(id);
        return result.orElse(null);
    }

  
    @Transactional
    public Credenziali getCredenziali(String username) {
        Optional<Credenziali> result = this.credenzialiRepository.findByUsername(username);
        return result.orElse(null);
    }


    @Transactional
    public Credenziali salvaCredenziali(Credenziali credenziali) {
        credenziali.setRuolo(Credenziali.DEFAULT_RUOLO); //ruolo con pochi privilegi all'inizio
        //le credenziali in questo modo contengono la password cifrata, prima di salvarla
        credenziali.setPassword(this.passwordEncoder.encode(credenziali.getPassword()));
        return this.credenzialiRepository.save(credenziali);
    }

   
    @Transactional
    public List<Credenziali> getAllCredenziali() {
        List<Credenziali> result = new ArrayList<>();
        Iterable<Credenziali> iterable = this.credenzialiRepository.findAll();
        for(Credenziali credenziali : iterable)
            result.add(credenziali);
        return result;
    }
}
