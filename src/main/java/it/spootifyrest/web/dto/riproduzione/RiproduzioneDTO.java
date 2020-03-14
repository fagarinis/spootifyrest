package it.spootifyrest.web.dto.riproduzione;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import it.spootifyrest.model.Riproduzione;
import it.spootifyrest.web.dto.album.AlbumDTO;
import it.spootifyrest.web.dto.brano.BranoDTO;
import it.spootifyrest.web.dto.playlist.PlaylistDTO;
import it.spootifyrest.web.dto.utente.UtenteDTO;

public class RiproduzioneDTO {
	private Long id;

	@JsonIgnoreProperties(value = { "ruoli" })
	private UtenteDTO utente;

	@JsonIgnoreProperties(value = { "album" })
	private BranoDTO brano;

	// In qualsiasi momento solo uno dei due è valorizzato!
	@JsonIgnoreProperties(value = { "artista", "brani" })
	private AlbumDTO album;
	@JsonIgnoreProperties(value = { "utente", "brani" })
	private PlaylistDTO playlist;

	public static RiproduzioneDTO buildRiproduzioneDTOFromModel(Riproduzione source) {
		RiproduzioneDTO result = new RiproduzioneDTO();
		result.setId(source.getId());
		result.setUtente(UtenteDTO.buildUtenteDTOFromModel(source.getUtente(), false));
		result.setBrano(BranoDTO.buildBranoDTOFromModel(source.getBrano(), false));
		result.setAlbum(AlbumDTO.buildAlbumDTOFromModel(source.getAlbum(), false, false));
		result.setPlaylist(PlaylistDTO.buildPlaylistDTOFromModel(source.getPlaylist(), false, false));
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

	public AlbumDTO getAlbum() {
		return album;
	}

	public void setAlbum(AlbumDTO album) {
		this.album = album;
	}

	public PlaylistDTO getPlaylist() {
		return playlist;
	}

	public void setPlaylist(PlaylistDTO playlist) {
		this.playlist = playlist;
	}

}
