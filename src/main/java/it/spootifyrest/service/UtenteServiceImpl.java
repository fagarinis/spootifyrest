package it.spootifyrest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.spootifyrest.model.Ruolo;
import it.spootifyrest.model.Sessione;
import it.spootifyrest.model.Utente;
import it.spootifyrest.model.en.CodiceRuolo;
import it.spootifyrest.model.en.StatoUtente;
import it.spootifyrest.model.utils.DateUtils;
import it.spootifyrest.model.utils.StringUtils;
import it.spootifyrest.repository.UtenteRepository;

@Service
public class UtenteServiceImpl implements UtenteService {

	//creare classe di costanti TODO
	private final static int DURATA_MINUTI_SESSIONE = 5;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private UtenteRepository repository;

	@Autowired
	private RuoloService ruoloService;

	@Autowired
	private SessioneService sessioneService;

	@Transactional(readOnly = true)
	public List<Utente> listAll() {
		return (List<Utente>) repository.findAll();
	}

	@Transactional(readOnly = true)
	public Utente caricaSingolo(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Transactional
	public void aggiorna(Utente utenteInstance) {
		repository.save(utenteInstance);
	}

	@Transactional
	public void inserisciNuovo(Utente utenteInstance) {
		repository.save(utenteInstance);
	}

	@Transactional
	public void rimuovi(Utente utenteInstance) {
		repository.delete(utenteInstance);

	}

	@Transactional(readOnly = true)
	public List<Utente> findByExample(Utente utenteExample) {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);// Match string
		return (List<Utente>) repository.findAll(Example.of(utenteExample, matcher));
	}

	@Transactional
	public Utente eseguiAccesso(String username, String password) {
		Utente utenteLoggato = repository.findEagerByUsernameAndPasswordAndStato(username, password, StatoUtente.ATTIVO)
				.orElse(null);
		if (utenteLoggato == null) {
			return null;
		}
		
		//se non è mai stata creata una sessione sul db
		if (utenteLoggato.getSessione() == null) {
			utenteLoggato.setSessione(new Sessione(DURATA_MINUTI_SESSIONE));
		}
		else {
			utenteLoggato.getSessione().refresh(DURATA_MINUTI_SESSIONE);
		}

		sessioneService.aggiorna(utenteLoggato.getSessione());

		return utenteLoggato;
	}

	@Transactional(readOnly = true)
	@Override
	public Utente caricaSingoloUtenteEager(long id) {
		return repository.findByIdEager(id).orElse(null);
	}

	@Transactional
	@Override
	public void aggiornaUtenteConRuoli(Utente utenteModel, List<String> listaIdRuoli) {
		utenteModel.getRuoli().clear();
		for (String idRuolo : listaIdRuoli) {
			Ruolo ruoloDaAggiungere = ruoloService.caricaSingolo(Long.parseLong(idRuolo));
			if (ruoloDaAggiungere != null)
				utenteModel.getRuoli().add(ruoloDaAggiungere);
		}
		aggiorna(utenteModel);
	}

	@Transactional(readOnly = true)
	@Override
	public Utente cercaDaUsername(String username) {
		return repository.findByUsername(username).orElse(null);
	}

	@Transactional(readOnly = true)
	@Override
	public boolean isUsernameDisponibile(String username) {
		return cercaDaUsername(username) == null;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Utente> cercaUtentiByCodiceRuoloAndCognomeLike(CodiceRuolo codiceRuolo, String cognome) {
		return repository.findUsersByRoleCodeAndSurnameLike(codiceRuolo, cognome);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Utente> findByExampleEager(Utente example) {
		List<Utente> result = new ArrayList<>();
		String query = "select distinct u from Utente u left join fetch u.ruoli r " + " where u.id = u.id ";

		if (StringUtils.isNotBlank(example.getNome()))
			query += " and u.nome like '%" + example.getNome() + "%' ";
		if (StringUtils.isNotBlank(example.getCognome()))
			query += " and u.cognome like '%" + example.getCognome() + "%' ";
		if (StringUtils.isNotBlank(example.getUsername()))
			query += " and u.username like '%" + example.getUsername() + "%' ";
		if (example.getDataRegistrazione() != null)
			query += " and u.dataRegistrazione ='"
					+ DateUtils.convertDateToSqlDateString(example.getDataRegistrazione()) + "' ";
		if (example.getStato() != null)
			query += "and u.stato = '" + example.getStato() + "' ";
		result = entityManager.createQuery(query, Utente.class).getResultList();

		return result;
	}

	/**
	 * aggiorna l'utente tranne la password e la data di registrazione
	 */
	@Transactional
	@Override
	public Utente aggiornaUtenteConRuoli(Utente utenteInstance) {
		Utente utentePersist = this.caricaSingolo(utenteInstance.getId());
		List<Ruolo> RuoliPersist = new ArrayList<>();

		for (Ruolo ruolo : utenteInstance.getRuoli()) {
			RuoliPersist.add(ruoloService.caricaSingolo(ruolo.getId()));
		}
		utentePersist.setNome(utenteInstance.getNome());
		utentePersist.setCognome(utenteInstance.getCognome());
		if (isUsernameDisponibile(utenteInstance.getUsername())) {
			utentePersist.setUsername(utenteInstance.getUsername());
		}
		utentePersist.setStato(utenteInstance.getStato());
		utentePersist.setRuoli(RuoliPersist);

		return utentePersist;
	}

	@Transactional
	@Override
	public Utente registraUtente(Utente utenteInstance, CodiceRuolo codiceRuolo) {
		if (!isUsernameDisponibile(utenteInstance.getUsername())) {
			return null;
		}

		utenteInstance.setStato(StatoUtente.CREATO);
		utenteInstance.setDataRegistrazione(new Date());
		Ruolo ruoloDaSettare = ruoloService.cercaDaCodiceRuolo(codiceRuolo);
		if (ruoloDaSettare != null) {
			utenteInstance.getRuoli().add(ruoloDaSettare);
		}
		inserisciNuovo(utenteInstance);
		return utenteInstance;
	}

	@Transactional
	@Override
	public Utente attivaUtenteDaId(Long id) {
		Utente utenteDaAttivare = this.caricaSingoloUtenteEager(id);
		utenteDaAttivare.setStato(StatoUtente.ATTIVO);
		return utenteDaAttivare;
	}

}
