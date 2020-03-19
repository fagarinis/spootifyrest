package it.spootifyrest.repository;

import java.util.Optional;

import it.spootifyrest.model.Sessione;

public interface SessioneRepository extends IBaseRepository<Sessione> {

	Optional<Sessione> findByTokenDiAutenticazione(String token);

}
