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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import it.spootifyrest.model.Brano;
import it.spootifyrest.model.Playlist;
import it.spootifyrest.model.Riproduzione;
import it.spootifyrest.model.Utente;
import it.spootifyrest.service.BranoService;
import it.spootifyrest.service.PlaylistService;
import it.spootifyrest.service.RiproduzioneService;
import it.spootifyrest.service.UtenteService;
import it.spootifyrest.web.dto.brano.BranoDTO;
import it.spootifyrest.web.dto.playlist.PlaylistDTO;
import it.spootifyrest.web.dto.utente.UtenteDTO;

@RestController
@RequestMapping(value = "/playlists")
public class PlaylistController {

	@Autowired
	private PlaylistService playlistService;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private RiproduzioneService riproduzioneService;

	@Autowired
	private BranoService branoService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	/**
	 * @param id della playlist
	 * @return l'ultimo brano della playlist riprodotto, il primo se non è ancora
	 *         mai stato riprodotto
	 */
	@GetMapping("/{id}/play")
	public ResponseEntity<BranoDTO> play(@PathVariable(value = "id") Long id) {
		String token = httpServletRequest.getHeader("token"); // ricevo il token per identificare l'utente
		if (token == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"Token di autenticazione non presente nell'header");
		}

		Utente utenteInSessione = utenteService.caricaUtenteAttivoConSessioneValidaDaToken(token);
		if (utenteInSessione == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token di autenticazione scaduto o non valido");
		}
		if (playlistService.caricaSingolo(id) == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "playlist con id " + id + " non trovata");
		}

		Riproduzione riproduzioneAggiornata = riproduzioneService.ascoltaProssimoBranoDaRaccolta(id,
				utenteInSessione.getId(), false);
		Brano prossimoBrano = riproduzioneAggiornata.getBrano();
		BranoDTO prossimoBranoDTO = BranoDTO.buildBranoDTOFromModel(prossimoBrano, true);

		return ResponseEntity.ok(prossimoBranoDTO);
	}

	@GetMapping
	public ResponseEntity<List<PlaylistDTO>> findByExample(PlaylistDTO playlistDTO) {
		boolean includeBrani = false;
		boolean includeUtente = true;

		Playlist playlistModel = PlaylistDTO.buildPlaylistModelFromDTO(playlistDTO, includeBrani, includeUtente);
		List<Playlist> resultModel = playlistService.findByExample(playlistModel);
		List<PlaylistDTO> resultDTO = PlaylistDTO.buildDTOListFromModelList(resultModel, includeBrani, includeUtente);

		return ResponseEntity.ok(resultDTO);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PlaylistDTO> getPlaylist(@PathVariable(value = "id") Long id) {
		boolean includeBrani = true;
		boolean includeUtente = true;

		Playlist playlistModel = playlistService.caricaSingoloEager(id, includeBrani, includeUtente);

		PlaylistDTO playlistDTO = PlaylistDTO.buildPlaylistDTOFromModel(playlistModel, includeBrani, includeUtente);
		return ResponseEntity.ok(playlistDTO);
	}

	@PostMapping("/admin/")
	public ResponseEntity<PlaylistDTO> insertPlaylist(@RequestBody @Valid PlaylistDTO playlistDTO) {
		boolean includeBrani = true;
		boolean includeUtente = true;
		boolean includeAlbum = false; // per i brani della playlist
		Playlist playlistModel = PlaylistDTO.buildPlaylistModelFromDTO(playlistDTO, includeBrani, includeUtente);
		playlistService.inserisciNuovo(playlistModel);
		PlaylistDTO playlistDTOInserita = PlaylistDTO.buildPlaylistDTOFromModel(playlistModel, includeBrani,
				includeUtente);

		playlistDTOInserita.setUtente(UtenteDTO
				.buildUtenteDTOFromModel(utenteService.caricaSingolo(playlistDTOInserita.getUtente().getId()), false));
		playlistDTOInserita.setBrani(BranoDTO.buildDTOListFromModelList(
				branoService.caricaBraniDaIdPlaylist(playlistDTOInserita.getId()), includeAlbum));

		return ResponseEntity.ok(playlistDTOInserita);
	}

	@PutMapping("/admin/{id}")
	public ResponseEntity<PlaylistDTO> updatePlaylist(@PathVariable(value = "id") Long id,
			@RequestBody PlaylistDTO playlistDTO) {
		playlistDTO.setId(id);
		boolean includeBrani = true;
		boolean includeUtente = true;
		Playlist playlistUpdate = PlaylistDTO.buildPlaylistModelFromDTO(playlistDTO, includeBrani, includeUtente);
		playlistService.aggiorna(playlistUpdate);

		PlaylistDTO playlistModificataDTO = PlaylistDTO.buildPlaylistDTOFromModel(playlistUpdate, includeBrani,
				includeUtente);

		return ResponseEntity.ok(playlistModificataDTO);
	}

	@DeleteMapping("/admin/{id}")
	public ResponseEntity<PlaylistDTO> deletePlaylist(@PathVariable(value = "id") Long id) {
		boolean includeBrani = true;
		boolean includeUtente = true;
		Playlist playlistDaCancellare = playlistService.caricaSingoloEager(id, true, true);
		if (playlistDaCancellare == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id " + id + " non trovato");
		}
		playlistService.rimuovi(playlistDaCancellare);
		PlaylistDTO playlistCancellata = PlaylistDTO.buildPlaylistDTOFromModel(playlistDaCancellare, includeBrani,
				includeUtente);
		return ResponseEntity.ok(playlistCancellata);
	}
}
