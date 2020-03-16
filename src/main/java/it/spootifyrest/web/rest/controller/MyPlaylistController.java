package it.spootifyrest.web.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import it.spootifyrest.model.Playlist;
import it.spootifyrest.model.Utente;
import it.spootifyrest.service.BranoService;
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
	private BranoService branoService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	private String getTokenFromRequest() {
		return httpServletRequest.getHeader("token");
	}

	private Utente getUtenteInSessione() {
		return utenteService.caricaUtenteAttivoConSessioneValidaDaToken(getTokenFromRequest());
	}

	@GetMapping
	public ResponseEntity<List<PlaylistDTO>> listAllMyPlaylist() {
		boolean includeBrani = false;
		boolean includeUtente = false;

		List<Playlist> resultModel = playlistService.findPlayListUtente(getUtenteInSessione());
		List<PlaylistDTO> resultDTO = PlaylistDTO.buildDTOListFromModelList(resultModel, includeBrani, includeUtente);

		return ResponseEntity.ok(resultDTO);
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

	@PostMapping("/{idPlaylist}/add/{idBrano}")
	public ResponseEntity<PlaylistDTO> aggiungiBranoAllaPlaylist(@PathVariable(value = "idPlaylist") Long idPlaylist,
			@PathVariable(value = "idBrano") Long idBrano) {
		Utente utenteInSessione = getUtenteInSessione();
		LanciaErroreSeBranoNonEsiste(idBrano);

		Playlist playlistAggiornata = playlistService.aggiungiBranoAllaPlaylistDellUtente(idBrano, idPlaylist,
				utenteInSessione.getId());

		if (playlistAggiornata == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"playlist con id " + idPlaylist + " utente " + utenteInSessione.getUsername() + " non trovata");
		}

		PlaylistDTO playlistDTOAggiornata = PlaylistDTO.buildPlaylistDTOFromModel(playlistAggiornata, true, true);

		playlistDTOAggiornata.setBrani(BranoDTO
				.buildDTOListFromModelList(branoService.caricaBraniDaIdPlaylist(playlistDTOAggiornata.getId()), false));

		return ResponseEntity.ok(playlistDTOAggiornata);
	}
	
	
	@DeleteMapping("/{idPlaylist}/remove/{idBrano}")
	public ResponseEntity<PlaylistDTO> rimuoviBranoDallaPlaylist(
			@PathVariable(value = "idPlaylist") Long idPlaylist,
			@PathVariable(value = "idBrano") Long idBrano) {
		
		Utente utenteInSessione = getUtenteInSessione();
		LanciaErroreSeBranoNonEsiste(idBrano);

		Playlist playlistAggiornata = playlistService.rimuoviBranoDallaPlaylistDellUtente(idBrano, idPlaylist,
				utenteInSessione.getId());

		if (playlistAggiornata == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"playlist con id " + idPlaylist + " utente " + utenteInSessione.getUsername() + " non trovata");
		}

		PlaylistDTO playlistDTOAggiornata = PlaylistDTO.buildPlaylistDTOFromModel(playlistAggiornata, true, true);

		playlistDTOAggiornata.setBrani(BranoDTO
				.buildDTOListFromModelList(branoService.caricaBraniDaIdPlaylist(playlistDTOAggiornata.getId()), false));

		return ResponseEntity.ok(playlistDTOAggiornata);
	}

	private void LanciaErroreSeBranoNonEsiste(Long idBrano) {
		if (idBrano == null || branoService.caricaSingolo(idBrano) == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "brano con id " + idBrano + " non trovato");
		}
	}

}
