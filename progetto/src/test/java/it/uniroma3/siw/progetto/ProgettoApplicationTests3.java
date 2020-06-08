package it.uniroma3.siw.progetto;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.CommentoRepository;
import it.uniroma3.siw.progetto.repository.CredenzialiRepository;
import it.uniroma3.siw.progetto.repository.ProgettoRepository;
import it.uniroma3.siw.progetto.repository.TagRepository;
import it.uniroma3.siw.progetto.repository.TaskRepository;
import it.uniroma3.siw.progetto.repository.UtenteRepository;
import it.uniroma3.siw.progetto.service.CommentoService;
import it.uniroma3.siw.progetto.service.CredenzialiService;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.TagService;
import it.uniroma3.siw.progetto.service.TaskService;
import it.uniroma3.siw.progetto.service.UtenteService;

@SpringBootTest
@RunWith(SpringRunner.class)
class ProgettoApplicationTests3 {
	@Autowired
	private CommentoService commentoService;

	@Autowired
	private CredenzialiService credenzialiService;

	@Autowired
	private ProgettoService progettoService;

	@Autowired
	private UtenteService utenteService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private TagService tagService;


	@Autowired
	private CommentoRepository commentoRepository;

	@Autowired
	private CredenzialiRepository credenzialiRepository;

	@Autowired
	private ProgettoRepository progettoRepository;

	@Autowired
	private UtenteRepository utenteRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TagRepository tagRepository;


	@Before
	public void deleteAll() {

		System.out.println("Stiamo cancellando i dati nel DB");
		this.commentoRepository.deleteAll();
		this.credenzialiRepository.deleteAll();
		this.progettoRepository.deleteAll();
		this.tagRepository.deleteAll();
		this.taskRepository.deleteAll();
		this.utenteRepository.deleteAll();
		System.out.println("Cancellazione fatta");


	}


	@Test
	public void aggiornamentoDatiUtente() {

		//utente 1 : mario rossi
		Utente utente1= new Utente("Mario", "Rossi");
		//credenziali utente 1
		Credenziali credenziali1= new Credenziali("marioRossi", "password");
		credenziali1.setUtente(utente1);
		credenziali1= this.credenzialiService.salvaCredenziali(credenziali1); 

		//utente 2 : luigi bianchi
		Utente utente2= new Utente("Luigi", "Bianchi");
		//credenziali utente 2
		Credenziali credenziali2= new Credenziali("luigiBianchi", "password"); 
		credenziali2.setUtente(utente2);
		this.credenzialiService.salvaCredenziali(credenziali2);


		assertEquals(credenziali1.getId().longValue(), 1L); //Id utente 1
		assertEquals(utente1.getId().longValue(), 2L);  //Id credenziali 1
		assertEquals(credenziali1.getUtente().getNome(), "Mario");  
		assertEquals(credenziali2.getUtente().getNome(), "Luigi");


		//da qui salviamo il primo progetto nel DB

		//Utente1(Mario) ha un progetto di cui è proprietario
		Progetto progetto1= new Progetto("TestProgetto1");
		progetto1.setProprietario(utente1);

		//lista di progetti di cui utente1 è proprietario
		List<Progetto> progettiProprietario = new ArrayList<>();
		progettiProprietario.add(progetto1);  //aggiungo progetto1
		utente1.setProgettiProprietario(progettiProprietario);

		this.progettoService.salvaProgetto(progetto1); //salvo progetto 1 
		assertEquals(progetto1.getProprietario(), utente1);
		assertEquals(utente1.getProgettiProprietario().get(0), progetto1);


		//secondo progetto 
		Progetto progetto2= new Progetto("TestProgetto2");
		progetto2.setProprietario(utente1); 
		progettiProprietario.add(progetto2);//utente 1 è proprietario anche di progetto2

		

		this.progettoService.salvaProgetto(progetto2); //salvo progetto 2
		assertEquals(progetto2.getProprietario(), utente1);
		assertEquals(utente1.getProgettiProprietario().get(1), progetto2);


		
		progettoService.condividiConAltroUtente(progetto1, utente1);
		progettoService.condividiConAltroUtente(progetto2, utente1);
		utente1.getProgettiVisibili().add(progetto1);
		utente1.getProgettiVisibili().add(progetto2);
		
		assertEquals(progetto1.getMembri().get(0), utente1);
        assertEquals(utenteRepository.findByProgettiVisibili(progetto1).get(0), utente1);
        assertEquals(utenteRepository.findByProgettiVisibili(progetto2).get(0), utente1);
        assertEquals(progettoService.getProgetto(5L), progetto1);


		//aggiungo un task al progetto1 
		Task task1 = new Task("lezione con andrea rossi", "fare la 4° esercitazione");
		progetto1.getTaskContenuti().add(task1);
		this.progettoService.salvaProgetto(progetto1); //aggiorno progetto1 -> anche task
		//this.taskService.salvaTask(task1);
		assertEquals(progetto1.getTaskContenuti().get(0), task1);

       



	}



}



