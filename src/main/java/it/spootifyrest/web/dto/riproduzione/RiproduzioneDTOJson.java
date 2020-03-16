package it.spootifyrest.web.dto.riproduzione;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import it.spootifyrest.model.Riproduzione;
import it.spootifyrest.web.dto.album.AlbumDTOJson;
import it.spootifyrest.web.dto.brano.BranoDTO;
import it.spootifyrest.web.dto.playlist.PlaylistDTOJson;
import it.spootifyrest.web.dto.utente.UtenteDTO;

public class RiproduzioneDTOJson {
	private Long id;

	@JsonIgnoreProperties(value = { "ruoli" })
	private UtenteDTO utente;

	@JsonIgnoreProperties(value = { "album" })
	private BranoDTO brano;

	// In qualsiasi momento solo uno dei due è valorizzato!
	@JsonIgnoreProperties(value = { "artista",  })
	private AlbumDTOJson album;
	//@JsonIgnoreProperties(value = { "utente",  })
	private PlaylistDTOJson playlist;

	public static RiproduzioneDTOJson buildRiproduzioneDTOFromModel(Riproduzione source) {
		RiproduzioneDTOJson result = new RiproduzioneDTOJson();
		result.setId(source.getId());
		result.setUtente(UtenteDTO.buildUtenteDTOFromModel(source.getUtente(), false));
		result.setBrano(BranoDTO.buildBranoDTOFromModel(source.getBrano(), false));
		result.setAlbum(AlbumDTOJson.buildAlbumDTOFromModel(source.getAlbum(), true, false));
		result.setPlaylist(PlaylistDTOJson.buildPlaylistDTOFromModel(source.getPlaylist(), true, true));
		return result;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UtenteDTO getUtente() {
		return utente;
	}

	public void setUtente(UtenteDTO utente) {
		this.utente = utente;
	}

	public BranoDTO getBrano() {
		return brano;
	}

	public void setBrano(BranoDTO brano) {
		this.brano = brano;
	}

	public AlbumDTOJson getAlbum() {
		return album;
	}

	public void setAlbum(AlbumDTOJson album) {
		this.album = album;
	}

	public PlaylistDTOJson getPlaylist() {
		return playlist;
	}

	public void setPlaylist(PlaylistDTOJson playlist) {
		this.playlist = playlist;
	}
}
