package it.spootifyrest.web.rest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.spootifyrest.model.Playlist;
import it.spootifyrest.model.Utente;
import it.spootifyrest.service.PlaylistService;
import it.spootifyrest.service.UtenteService;
import it.spootifyrest.web.dto.brano.BranoDTO;
import it.spootifyrest.web.dto.playlist.MyPlaylistDTO;
import it.spootifyrest.web.dto.playlist.PlaylistDTO;
import it.spootifyrest.web.dto.utente.UtenteDTO;

@RestController
@RequestMapping(value = "/myPlaylists")
public class MyPlaylistController {

	@Autowired
	private PlaylistService playlistService;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	private String getTokenFromRequest() {
		return httpServletRequest.getHeader("token");
	}

	private Utente getUtenteInSessione() {
		return utenteService.caricaUtenteAttivoConSessioneValidaDaToken(getTokenFromRequest());
	}

	@PostMapping("/new")
	public ResponseEntity<PlaylistDTO> createNewMyPlaylist(@RequestBody @Valid MyPlaylistDTO playlistDTO) {
		Playlist playlistModel = MyPlaylistDTO.buildPlaylistModelFromDTO(playlistDTO);
		playlistModel.setUtente(getUtenteInSessione());
		playlistService.inserisciNuovo(playlistModel);
		PlaylistDTO playlistDTOInserita = PlaylistDTO.buildPlaylistDTOFromModel(playlistModel, true, true);

		playlistDTOInserita.setUtente(UtenteDTO
				.buildUtenteDTOFromModel(utenteService.caricaSingolo(playlistDTOInserita.getUtente().getId()), false));
//		playlistDTOInserita.setBrani(BranoDTO.buildDTOListFromModelList(
//				branoService.caricaBraniDaIdPlaylist(playlistDTOInserita.getId()), includeAlbum));

		return ResponseEntity.ok(playlistDTOInserita);
	}

}
