package it.spootifyrest.service;

import it.spootifyrest.model.Ruolo;
import it.spootifyrest.model.en.CodiceRuolo;

public interface RuoloService extends IBaseService<Ruolo> {

	public Ruolo cercaDaCodiceRuolo(CodiceRuolo codiceRuolo);

}
