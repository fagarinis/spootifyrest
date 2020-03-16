package it.spootifyrest.web.rest.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import it.spootifyrest.model.Album;
import it.spootifyrest.model.Playlist;
import it.spootifyrest.model.Riproduzione;
import it.spootifyrest.model.Utente;
import it.spootifyrest.service.AlbumService;
import it.spootifyrest.service.PlaylistService;
import it.spootifyrest.service.RiproduzioneService;
import it.spootifyrest.service.UtenteService;
import it.spootifyrest.web.dto.riproduzione.RiproduzioneDTO;
import it.spootifyrest.web.dto.riproduzione.RiproduzioneDTOJson;

@RestController
@RequestMapping(value = "/player")
/**
 * questo controller è un supporto per altri controller e non viene mai chiamato
 * direttamente pertanto non è in whitelist nel CheckAuthFilter
 * 
 * FIXME togliere l'autowired di riproduzionecontroller da album e playlist e creando un bean di utility
 * 
 */
public class RiproduzioneController {

	@Autowired
	private AlbumService albumService;

	@Autowired
	private PlaylistService playlistService;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private RiproduzioneService riproduzioneService;

	private String getTokenFromRequest() {
		String token = httpServletRequest.getHeader("token");
		if (token == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"Token di autenticazione non presente nell'header");
		}
		return token;
	}

	private Utente getUtenteInSessione() {
		Utente utenteInSessione = utenteService.caricaUtenteAttivoConSessioneValidaDaToken(getTokenFromRequest());
		if (utenteInSessione == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token di autenticazione scaduto o non valido");
		}
		return utenteInSessione;
	}

	/**
	 * Chiamata ajax
	 * @return riproduzione con lista dei brani e brano in ascolto attuale
	 */
	@PostMapping("/{tipoRaccolta}/{idRaccolta}")
	public ResponseEntity<RiproduzioneDTOJson> caricaRiproduzione(
			@PathVariable(value = "tipoRaccolta") String tipoRaccolta, 
			@PathVariable(value = "idRaccolta") Long idRaccolta) {
		
		boolean isAlbum = tipoRaccolta.equals("album")? true : false;
		Riproduzione resultModel = riproduzioneService.caricaRiproduzioneDaIdRaccoltaEToken(idRaccolta, isAlbum, getTokenFromRequest());
		RiproduzioneDTOJson resultDTO = RiproduzioneDTOJson.buildRiproduzioneDTOFromModel(resultModel);

		return ResponseEntity.ok(resultDTO);
	}
	
	/**
	 * tale metodo non viene mai chiamato direttamente ma solo attraverso
	 * AlbumController o PlaylistController (classi nello stesso package).
	 * <p>
	 * 
	 * Si occupa della gestione dello scorrimento delle varie riproduzioni
	 * dell'utente in sessione al momento della chiamata
	 * 
	 * @param idRaccolta  id della raccolta, lancia errore se non viene trovato sul
	 *                    DB
	 * @param goNextTrack se la riproduzione già esiste, va al brano successivo
	 *                    (true), o al brano precedente (false)
	 * @param isAlbum     true se la raccolta è un album, false se è una playlist
	 * @return la response con il brano in esecuzione al momento
	 */
	ResponseEntity<RiproduzioneDTO> handlePlayRaccolta(long idRaccolta, boolean goNextTrack, boolean isAlbum) {
		Utente utenteInSessione = getUtenteInSessione();
		lanciaErroreSeRaccoltaInesistenteOVuota(idRaccolta, isAlbum);

		Riproduzione riproduzioneAggiornata = riproduzioneService.ascoltaProssimoBranoDaRaccolta(idRaccolta,
				utenteInSessione.getId(), isAlbum, goNextTrack);

		return ResponseEntity.ok(RiproduzioneDTO.buildRiproduzioneDTOFromModel(riproduzioneAggiornata));
	}

	ResponseEntity<RiproduzioneDTO> handleStopPlayRaccolta(Long idRaccolta, boolean isAlbum) {
		Utente utenteInSessione = getUtenteInSessione();
		lanciaErroreSeRaccoltaInesistenteOVuota(idRaccolta, isAlbum);
		String raccolta = isAlbum ? "album" : "playlist";

		Riproduzione riproduzioneEliminata = riproduzioneService.eliminaRiproduzione(idRaccolta,
				utenteInSessione.getId(), isAlbum);

		if (riproduzioneEliminata == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessuna riproduzione "+raccolta+ "(ID:"+idRaccolta+") presente tra le riproduzioni dell'utente "+utenteInSessione.getUsername());
		}

		RiproduzioneDTO riproduzioneEliminataDTO = RiproduzioneDTO.buildRiproduzioneDTOFromModel(riproduzioneEliminata);
		return ResponseEntity.ok(riproduzioneEliminataDTO);
	}

	private void lanciaErroreSeRaccoltaInesistenteOVuota(Long idRaccolta, boolean isAlbum) {
		if (isAlbum) {
			Album album = albumService.caricaSingolo(idRaccolta);
			if (album == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "album con id " + idRaccolta + " non trovato");
			}
			if (album.getBrani().size() <= 0) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "album con id " + idRaccolta + " vuoto!");
			}
		} else {
			Playlist playlist = playlistService.caricaSingolo(idRaccolta);
			if (playlist == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
						"playlist con id " + idRaccolta + " non trovata");
			}
			if (playlist.getBrani().size() <= 0) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "playlist con id " + idRaccolta + " vuota!");
			}
		}
	}

}
