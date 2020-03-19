package it.spootifyrest.service;

import java.util.List;

import it.spootifyrest.model.Brano;

public interface BranoService extends IBaseService<Brano> {
	
	public void inserisciNuoviBrani(List<Brano> brani);
	
	public Brano caricaSingoloEagerConAlbumEPlaylists(Long id);
	
	public Brano caricaSingoloEager(Long id);

	public List<Brano> caricaBraniDaIdPlaylist(Long idPlaylist);

	public List<Brano> caricaBraniDaListaBraniTransient(List<Brano> brani);

	public void rimuoviBranoDaTutteLePlaylist(Brano o);
	
	public void rimuoviBraniDaTutteLePlaylist(List<Brano> listaBrani);

	public void rimuoviTutteLeRiproduzioniDelBrano(Brano o);

	public void rimuoviTutteLeRiproduzioneDeiBrani(List<Brano> brani);

	public void rimuoviBrani(List<Brano> brani);
	
}
