package it.spootifyrest.web.dto.artista;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import it.spootifyrest.model.Album;
import it.spootifyrest.model.Artista;

public class ArtistaDTO {

	private Long id;
	@NotBlank
	private String nome;
	@NotBlank
	private String cognome;
	private String soprannome;

	private List<Album> album = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getSoprannome() {
		return soprannome;
	}

	public void setSoprannome(String soprannome) {
		this.soprannome = soprannome;
	}

	public List<Album> getAlbum() {
		return album;
	}

	public void setAlbum(List<Album> album) {
		this.album = album;
	}

	@Override
	public String toString() {
		return "ArtistaDTO [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", soprannome=" + soprannome
				+ ", album=" + album + "]";
	}

	public static Artista buildArtistaModelFromDTO(ArtistaDTO source, boolean includeAlbum) {
		Artista result = new Artista();

		result.setId(source.getId());
		result.setNome(source.getNome());
		result.setCognome(source.getCognome());
		result.setSoprannome(source.getSoprannome());
		if (includeAlbum) {
			result.setAlbum(source.getAlbum());
		}
		return result;
	}

	public static ArtistaDTO buildArtistaDTOFromModel(Artista source, boolean includeAlbum) {
		ArtistaDTO result = new ArtistaDTO();

		result.setId(source.getId());
		result.setNome(source.getNome());
		result.setCognome(source.getCognome());
		result.setSoprannome(source.getSoprannome());
		if (includeAlbum) {
			result.setAlbum(source.getAlbum());
		}
		return result;
	}

	public static List<ArtistaDTO> buildDTOListFromModelList(List<Artista> resultModel, boolean includeAlbum) {
		List<ArtistaDTO> resultList = new ArrayList<>();
		if (resultModel == null) {
			return null;
		}
		for (Artista artistaItem : resultModel) {
			ArtistaDTO artistaDTOtemp = ArtistaDTO.buildArtistaDTOFromModel(artistaItem, includeAlbum);
			resultList.add(artistaDTOtemp);
		}
		return resultList;
	}

}
