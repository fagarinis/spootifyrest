package it.spootifyrest.service;

import it.spootifyrest.model.Playlist;

public interface PlaylistService extends IBaseService<Playlist> {

	public Playlist caricaSingoloEager(Long id, boolean includeBrani, boolean includeUtente);

}
