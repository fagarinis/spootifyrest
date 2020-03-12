package it.spootifyrest.service;

import java.util.List;

import it.spootifyrest.model.Utente;
import it.spootifyrest.model.en.CodiceRuolo;

public interface UtenteService extends IBaseService<Utente> {
	
	public Utente eseguiAccesso(String username, String password);

	public Utente caricaSingoloUtenteEager(long id);

	public void aggiornaUtenteConRuoli(Utente utenteModel, List<String> listaIdRuoli);
	
	public Utente cercaDaUsername(String username);

	public List<Utente> cercaUtentiByCodiceRuoloAndCognomeLike(CodiceRuolo codiceRuolo, String cognome);

	public List<Utente> findByExampleEager(Utente example);

	public Utente aggiornaUtenteConRuoli(Utente utenteInstance);

	public boolean isUsernameDisponibile(String username);

	public Utente registraUtente(Utente utenteInstance, CodiceRuolo ruolo);
	
	public Utente attivaUtenteDaId(Long id);

	public Utente caricaUtenteConSessioneValidaDaToken(String token);

}
