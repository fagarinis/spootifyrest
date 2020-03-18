package it.spootifyrest.service;

import java.util.List;

import it.spootifyrest.model.Brano;

public interface BranoService extends IBaseService<Brano> {
	
	public Brano caricaSingoloEagerConAlbumEPlaylists(Long id);
	
	public Brano caricaSingoloEager(Long id);

	public List<Brano> caricaBraniDaIdPlaylist(Long idPlaylist);

	public List<Brano> caricaBraniDaListaBraniTransient(List<Brano> brani);
}
