package it.spootifyrest.web.dto.playlist;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import it.spootifyrest.model.Playlist;
import it.spootifyrest.web.dto.brano.BranoDTO;
import it.spootifyrest.web.dto.utente.UtenteDTO;

public class PlaylistDTO {

	private Long id;
	private String titoloPlaylist;

	@JsonIgnoreProperties(value = { "ruoli" })
	private UtenteDTO utente;
	private List<BranoDTO> brani = new ArrayList<>();

	public static Playlist buildPlaylistModelFromDTO(PlaylistDTO source, boolean includeBrani, boolean includeUtente) {
		Playlist result = new Playlist();
		result.setId(source.getId());
		result.setTitoloPlaylist(source.getTitoloPlaylist());
		if (includeBrani) {
			result.setBrani(BranoDTO.buildModelListFromDTOList(source.getBrani(), false));
		}
		if (includeUtente) {
			result.setUtente(UtenteDTO.buildUtenteModelFromDTO(source.getUtente(), false));
		}

		return result;
	}

	public static PlaylistDTO buildPlaylistDTOFromModel(Playlist source, boolean includeBrani, boolean includeUtente) {
		if(source == null) {
			return null;
		}
		
		PlaylistDTO result = new PlaylistDTO();
		result.setId(source.getId());
		result.setTitoloPlaylist(source.getTitoloPlaylist());
		if (includeBrani) {
			result.setBrani(BranoDTO.buildDTOListFromModelList(source.getBrani(), false));
		}
		if (includeUtente) {
			result.setUtente(UtenteDTO.buildUtenteDTOFromModel(source.getUtente(), false));
		}

		return result;
	}

	public static List<PlaylistDTO> buildDTOListFromModelList(List<Playlist> input, boolean includeBrani,
			boolean includeUtente) {
		List<PlaylistDTO> resultList = new ArrayList<>();
		if (input == null) {
			return null;
		}
		for (Playlist playlistItem : input) {
			PlaylistDTO playlistDTOtemp = PlaylistDTO.buildPlaylistDTOFromModel(playlistItem, includeBrani,
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

	public List<BranoDTO> getBrani() {
		return brani;
	}

	public void setBrani(List<BranoDTO> brani) {
		this.brani = brani;
	}

}
