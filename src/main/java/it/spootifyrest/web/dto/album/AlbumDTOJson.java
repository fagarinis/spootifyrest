package it.spootifyrest.web.dto.album;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import it.spootifyrest.model.Album;
import it.spootifyrest.model.Artista;
import it.spootifyrest.web.dto.brano.BranoDTOJson;

@JsonIgnoreProperties(value = { "artista" })
public class AlbumDTOJson {

	private Long id;
	private String nomeAlbum;
	private Integer annoDiUscita;
	private String genereMusicale;

	@JsonIgnoreProperties(value = { "album" })
	private Artista artista;

	@JsonIgnoreProperties(value = { "riproduzioni", "playlist" })
	private List<BranoDTOJson> brani = new ArrayList<>();

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

	public List<BranoDTOJson> getBrani() {
		return brani;
	}

	public void setBrani(List<BranoDTOJson> brani) {
		this.brani = brani;
	}

	public static Album buildAlbumModelFromDTO(AlbumDTOJson source, boolean includeBrani, boolean includeArtista) {
		if (source == null) {
			return null;
		}
		Album result = new Album();
		result.setId(source.getId());
		result.setNomeAlbum(source.getNomeAlbum());
		result.setAnnoDiUscita(source.getAnnoDiUscita());
		result.setGenereMusicale(source.getGenereMusicale());
		if (includeArtista) {
			result.setArtista(source.getArtista());
		}
		if (includeBrani) {
			result.setBrani(BranoDTOJson.buildModelListFromDTOList(source.getBrani(), true));
		}

		return result;
	}

	public static AlbumDTOJson buildAlbumDTOFromModel(Album source, boolean includeBrani, boolean includeArtista) {
		if (source == null) {
			return null;
		}

		AlbumDTOJson result = new AlbumDTOJson();
		result.setId(source.getId());
		result.setNomeAlbum(source.getNomeAlbum());
		result.setAnnoDiUscita(source.getAnnoDiUscita());
		result.setGenereMusicale(source.getGenereMusicale());
		if (includeArtista) {
			result.setArtista(source.getArtista());
		}
		if (includeBrani) {
			result.setBrani(BranoDTOJson.buildDTOListFromModelList(source.getBrani(), true));
		}
		return result;
	}

	public static List<AlbumDTOJson> buildDTOListFromModelList(List<Album> input, boolean includeBrani,
			boolean includeArtista) {
		List<AlbumDTOJson> resultList = new ArrayList<>();
		if (input == null) {
			return null;
		}
		for (Album albumItem : input) {
			AlbumDTOJson albumDTOtemp = AlbumDTOJson.buildAlbumDTOFromModel(albumItem, includeBrani, includeArtista);
			resultList.add(albumDTOtemp);
		}
		return resultList;
	}

	@Override
	public String toString() {
		return "AlbumDTO [id=" + id + ", nomeAlbum=" + nomeAlbum + ", annoDiUscita=" + annoDiUscita
				+ ", genereMusicale=" + genereMusicale + "]";
	}
}
