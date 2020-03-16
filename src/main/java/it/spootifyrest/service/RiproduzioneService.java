package it.spootifyrest.service;

import it.spootifyrest.model.Riproduzione;

public interface RiproduzioneService extends IBaseService<Riproduzione> {

	public Riproduzione ascoltaProssimoBranoDaRaccolta(Long idRaccolta, Long idUtente, boolean isRaccoltaAlbum, boolean next);

	public Riproduzione caricaRiproduzioneDaIdRaccoltaEUserId(Long idRaccolta, Long idUtente, boolean isAlbum);

	public Riproduzione inizializzaNuovaRiproduzioneDaIdRaccoltaEUserId(Long idRaccolta, Long idUtente, boolean isAlbum);

	public Riproduzione eliminaRiproduzione(Long idRaccolta, Long idUtente, boolean isAlbum);

	public Riproduzione caricaRiproduzioneDaIdRaccoltaEToken(Long idRaccolta, boolean isAlbum, String token);

}
