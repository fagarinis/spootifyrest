package it.spootifyrest.service;

import java.util.List;

import it.spootifyrest.model.Playlist;
import it.spootifyrest.model.Utente;

public interface PlaylistService extends IBaseService<Playlist> {

	public Playlist caricaSingoloEager(Long id, boolean includeBrani, boolean includeUtente);
	
	public Playlist aggiungiBranoAllaPlaylistDellUtente(Long idBrano, Long idPlaylist, Long idUtente);
	
	public Playlist rimuoviBranoDallaPlaylistDellUtente(Long idBrano, Long idPlaylist, Long idUtente);

	public List<Playlist> findPlayListUtente(Utente utenteInSessione);

}
