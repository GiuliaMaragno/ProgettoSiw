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
import it.uniroma3.siw.progetto.model.Tag;
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
class ProgettoApplicationTests2 {
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


		Credenziali credenziali1= new Credenziali("marioRossi", "password");
		Utente utente1= new Utente("Mario", "Rossi");

		credenziali1.setUtente(utente1);

		credenziali1= this.credenzialiService.salvaCredenziali(credenziali1); //quando si salvano le credenziali si salvano in DEFAULT 
		credenziali1.setRuolo(Credenziali.ADMIN_RUOLO);                       //fuori dal save set Ruolo funziona


		assertEquals(credenziali1.getId().longValue(), 1L);
		assertEquals(credenziali1.getUsername(), "marioRossi");
		assertEquals(credenziali1.getUtente(), utente1);
		assertEquals(credenziali1.getUtente().getNome(), "Mario");
		assertEquals(credenziali1.getUtente().getCognome(), "Rossi");
		assertEquals(credenziali1.getRuolo(), "ADMIN");


		Credenziali credenziali2= new Credenziali("luigiBianchi", "password"); //nuovo utente
		Utente utente2= new Utente("Luigi", "Bianchi");

		credenziali2.setUtente(utente2);
		this.credenzialiService.salvaCredenziali(credenziali2);


		assertEquals(credenziali2.getId().longValue(), 3L);
		assertEquals(utente2.getId().longValue(), 4L);
		assertEquals(credenziali2.getUtente().getNome(), "Luigi");
		assertEquals(credenziali2.getUtente().getCognome(), "Bianchi");


		//da qui salviamo il primo progetto nel DB

		Progetto progetto1= new Progetto("TestProgetto1");
		this.progettoService.salvaProgetto(progetto1);
		assertEquals(progetto1.getId().longValue(), 5L);
		
		//utente 1 proprietario di progetto1
		progetto1.setProprietario(utente1);
		this.progettoService.salvaProgetto(progetto1);
		utente1.getProgettiProprietario().add(progetto1);
		
		//aggiungo un task al progetto1
		Task task1 = new Task("eserc. 4", "ultima esercitazione");
		progetto1.getTaskContenuti().add(task1);
		taskService.salvaTask(task1);
		this.progettoService.salvaProgetto(progetto1);
		assertEquals(progetto1.getTaskContenuti().get(0), task1);
		
		
		
		
		
		/**
		 * AGGIUNGO UN TAG AL PROGETTO
		 */
		Tag tag1 = new Tag("in ritardo", "verde", "ultimo esercizio"); 
		tag1= tagService.salvaTag(tag1);
		Progetto progetto2= new Progetto("TestProgetto2");
		Utente utente4 = new Utente("Carlo", "Verdi");
		utenteService.salvaUtente(utente4);
		progetto2.setProprietario(utente4);
		
		progettoService.aggiungiTagAlProgetto(progetto2, tag1, utente4);
		assertEquals(progetto2.getTags().size(), 1);
		assertEquals(progetto2.getTags().get(0), tag1);
		
		/**
		 * ASSEGNARE IL TAG AD UN UTENTE CON VISIBILITA'
		 */
		
		Task task2 = new Task("Revisione es4", "Revisione ultimo esercizio");
		Progetto progetto3= new Progetto("TestProgetto3");
		Utente utente3= new Utente("Maria", "Bianchi");
		utenteService.salvaUtente(utente3);
		progetto3= progettoService.condividiConAltroUtente(progetto3, utente3);
		taskService.assegnaTaskAdUtenteConVisibilità(progetto3, utente3, task2);
		assertEquals(task2.getUtenteAddetto(), utente3);
		
		
		
		





	}



}



