package it.spootifyrest.web.dto.playlist;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import it.spootifyrest.model.Playlist;
import it.spootifyrest.web.dto.brano.BranoDTOJson;
import it.spootifyrest.web.dto.utente.UtenteDTO;

public class PlaylistDTOJson {
	private Long id;
	private String titoloPlaylist;

	@JsonIgnoreProperties(value = { "ruoli" })
	private UtenteDTO utente;
	
	@JsonIgnoreProperties(value = { "riproduzioni", "playlist" })
	private List<BranoDTOJson> brani = new ArrayList<>();

	public static Playlist buildPlaylistModelFromDTO(PlaylistDTOJson source, boolean includeBrani, boolean includeUtente) {
		Playlist result = new Playlist();
		result.setId(source.getId());
		result.setTitoloPlaylist(source.getTitoloPlaylist());
		if (includeBrani) {
			result.setBrani(BranoDTOJson.buildModelListFromDTOList(source.getBrani(), false));
		}
		if (includeUtente) {
			result.setUtente(UtenteDTO.buildUtenteModelFromDTO(source.getUtente(), false));
		}

		return result;
	}

	public static PlaylistDTOJson buildPlaylistDTOFromModel(Playlist source, boolean includeBrani, boolean includeUtente) {
		if(source == null) {
			return null;
		}
		
		PlaylistDTOJson result = new PlaylistDTOJson();
		result.setId(source.getId());
		result.setTitoloPlaylist(source.getTitoloPlaylist());
		if (includeBrani) {
			result.setBrani(BranoDTOJson.buildDTOListFromModelList(source.getBrani(), true));
		}
		if (includeUtente) {
			result.setUtente(UtenteDTO.buildUtenteDTOFromModel(source.getUtente(), false));
		}

		return result;
	}

	public static List<PlaylistDTOJson> buildDTOListFromModelList(List<Playlist> input, boolean includeBrani,
			boolean includeUtente) {
		List<PlaylistDTOJson> resultList = new ArrayList<>();
		if (input == null) {
			return null;
		}
		for (Playlist playlistItem : input) {
			PlaylistDTOJson playlistDTOtemp = PlaylistDTOJson.buildPlaylistDTOFromModel(playlistItem, includeBrani,
					includeUtente);
			resultList.add(playlistDTOtemp);
		}
		return resultList;
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

	public UtenteDTO getUtente() {
		return utente;
	}

	public void setUtente(UtenteDTO utente) {
		this.utente = utente;
	}

	public List<BranoDTOJson> getBrani() {
		return brani;
	}

	public void setBrani(List<BranoDTOJson> brani) {
		this.brani = brani;
	}

}
