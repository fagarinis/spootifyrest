package it.spootifyrest.repository;

import java.util.Optional;

import it.spootifyrest.model.Ruolo;
import it.spootifyrest.model.en.CodiceRuolo;

public interface RuoloRepository extends IBaseRepository<Ruolo>{

	Optional<Ruolo> findByCodice(CodiceRuolo codiceRuolo);

}
