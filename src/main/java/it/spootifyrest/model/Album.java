package it.spootifyrest.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Album {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nomeAlbum;
	private Integer annoDiUscita;
	private String genereMusicale;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "artista_id")
	private Artista artista;
	@OneToMany(mappedBy = "album", orphanRemoval = true)
	private List<Brano> brani = new ArrayList<>();
	@OneToMany(mappedBy = "album", orphanRemoval = true)
	private List<Riproduzione> riproduzioni = new ArrayList<>();

	public Album() {
	}

	public Album(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeAlbum() {
		return nomeAlbum;
	}

	public void setNomeAlbum(String nomeAlbum) {
		this.nomeAlbum = nomeAlbum;
	}

	public Integer getAnnoDiUscita() {
		return annoDiUscita;
	}

	public void setAnnoDiUscita(Integer annoDiUscita) {
		this.annoDiUscita = annoDiUscita;
	}

	public String getGenereMusicale() {
		return genereMusicale;
	}

	public void setGenereMusicale(String genereMusicale) {
		this.genereMusicale = genereMusicale;
	}

	public Artista getArtista() {
		return artista;
	}

	public void setArtista(Artista artista) {
		this.artista = artista;
	}

	public List<Brano> getBrani() {
		return brani;
	}

	public void setBrani(List<Brano> brani) {
		this.brani = brani;
	}

	public List<Riproduzione> getRiproduzioni() {
		return riproduzioni;
	}

	public void setRiproduzioni(List<Riproduzione> riproduzioni) {
		this.riproduzioni = riproduzioni;
	}

	@Override
	public String toString() {
		return "Album [id=" + id + ", nomeAlbum=" + nomeAlbum + ", annoDiUscita=" + annoDiUscita + ", genereMusicale="
				+ genereMusicale + ", artista=" + artista + "]";
	}

	public Brano getPrimoBrano() {
		if (brani == null || brani.isEmpty()) {
			return null;
		}

		return brani.get(0);
	}

}
