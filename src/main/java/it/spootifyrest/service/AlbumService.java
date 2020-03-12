package it.spootifyrest.service;

import it.spootifyrest.model.Album;

public interface AlbumService extends IBaseService<Album> {

	public Album caricaSingoloEager(Long id);

	public void aggiornaSoloAlbum(Album o);

}
