package it.spootifyrest.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Riproduzione {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utente_id", nullable = false)
	private Utente utente;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brano_id", nullable = false)
	private Brano brano;

	// In qualsiasi momento solo uno dei due è valorizzato!
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "album_id", nullable = true)
	private Album album;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "playlist_id", nullable = true)
	private Playlist playlist;

	@Override
	public String toString() {
		return "Riproduzione [id=" + id + ", utente=" + utente + ", brano=" + brano + ", album=" + album + ", playlist="
				+ playlist + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Brano getBrano() {
		return brano;
	}

	public void setBrano(Brano brano) {
		this.brano = brano;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public Playlist getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}

	/**
	 * mette il prossimo brano in ascolto
	 * 
	 * @return il prossimo brano
	 */
	public Brano next() {
		List<Brano> listaBrani = getListaBrani();
		Brano branoInRiproduzione = this.getBrano();

		this.brano = prossimoBrano(listaBrani, branoInRiproduzione);
		return this.brano;
	}

	private Brano prossimoBrano(List<Brano> listaBrani, Brano branoInRiproduzione) {
		for (int i = 0; i < listaBrani.size(); i++) {
			if (listaBrani.get(i).getId() == branoInRiproduzione.getId()) {
				Brano prossimoBrano = listaBrani.get((i + 1) % listaBrani.size());
				return prossimoBrano;
			}
		}
		return null;
	}

	private Brano precedenteBrano(List<Brano> listaBrani, Brano branoInRiproduzione) {
		for (int i = 0; i < listaBrani.size(); i++) {
			if (listaBrani.get(i).getId() == branoInRiproduzione.getId()) {
				if (i == 0) {
					return listaBrani.get(listaBrani.size() - 1);
				}
				Brano precedenteBrano = listaBrani.get((i - 1));
				return precedenteBrano;
			}
		}
		return null;
	}

	private List<Brano> getListaBrani() {
		if (album != null && playlist != null) {
			// c'è un errore...
			return null;
		}
		if (playlist != null) {
			return this.getPlaylist().getBrani();
		}
		if (album != null) {
			return this.getAlbum().getBrani();
		}

		return null;
	}

	public Brano previous() {
		List<Brano> listaBrani = getListaBrani();
		Brano branoInRiproduzione = this.getBrano();
		this.brano = precedenteBrano(listaBrani, branoInRiproduzione);

		return this.brano;
	}

}
