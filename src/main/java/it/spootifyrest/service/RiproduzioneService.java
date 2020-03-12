package it.spootifyrest.service;

import it.spootifyrest.model.Riproduzione;

public interface RiproduzioneService extends IBaseService<Riproduzione> {

	public Riproduzione ascoltaProssimoBranoDaRaccolta(Long idRaccolta, Long idUtente, boolean isRaccoltaAlbum);

}
