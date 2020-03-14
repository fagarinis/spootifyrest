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

import it.spootifyrest.model.utils.SortedList;

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
	private List<Brano> brani = new SortedList<>();

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

	public Brano getPrimoBrano() {
		if (brani == null || brani.isEmpty()) {
			return null;
		}

		return brani.get(0);
	}

	public void addBrano(Brano brano) {
		if (!this.getBrani().contains(brano)) {
		this.getBrani().add(brano);
		brano.getPlaylist().add(this);
		}
	}

	public void removeBrano(Brano brano) {
		this.getBrani().remove(brano);
		brano.getPlaylist().remove(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Playlist other = (Playlist) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
