package it.spootifyrest.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Playlist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titoloPlaylist;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utente_id", nullable = false)
	private Utente utente;

	@OneToMany(mappedBy = "playlist", orphanRemoval = true)
	private List<Riproduzione> riproduzioni = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
	@JoinTable(name = "playlist_brano", joinColumns = @JoinColumn(name = "playlist_id", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "brano_id", referencedColumnName = "ID"))
	private List<Brano> brani = new ArrayList<>();

	@Override
	public String toString() {
		return "Playlist [id=" + id + ", titoloPlaylist=" + titoloPlaylist + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitoloPlaylist() {
		return titoloPlaylist;
	}

	public void setTitoloPlaylist(String titoloPlaylist) {
		this.titoloPlaylist = titoloPlaylist;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public List<Riproduzione> getRiproduzioni() {
		return riproduzioni;
	}

	public void setRiproduzioni(List<Riproduzione> riproduzioni) {
		this.riproduzioni = riproduzioni;
	}

	public List<Brano> getBrani() {
		return brani;
	}

	public void setBrani(List<Brano> brani) {
		this.brani = brani;
	}

}
