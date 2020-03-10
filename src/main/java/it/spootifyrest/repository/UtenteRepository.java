package it.spootifyrest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import it.spootifyrest.model.Utente;
import it.spootifyrest.model.en.CodiceRuolo;
import it.spootifyrest.model.en.StatoUtente;

public interface UtenteRepository extends IBaseRepository<Utente> {
	
	@Query("select u from Utente u left join fetch u.ruoli r where u.username = ?1 and u.password = ?2 and u.stato = ?3")
	Optional<Utente> findByUsernameAndPasswordAndStato(String username, String password, StatoUtente stato);

	@Query("select u from Utente u left join fetch u.ruoli r where u.id = ?1")
	Optional<Utente> findByIdEager(Long id);

	Optional<Utente> findByUsername(String username);

	@Query("select distinct u from Utente u join fetch u.ruoli r where u.stato ='ATTIVO' and u.cognome like %?2% and r.codice = ?1")
	List<Utente> findUsersByRoleCodeAndSurnameLike(CodiceRuolo codiceRuolo, String cognome);
}
