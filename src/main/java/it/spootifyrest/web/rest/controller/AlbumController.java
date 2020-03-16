package it.spootifyrest.web.rest.controller;

import java.util.List;

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

import it.spootifyrest.model.Album;
import it.spootifyrest.service.AlbumService;
import it.spootifyrest.service.ArtistaService;
import it.spootifyrest.web.dto.album.AlbumDTO;
import it.spootifyrest.web.dto.riproduzione.RiproduzioneDTO;

@RestController
@RequestMapping(value = "/albums")
public class AlbumController {

	@Autowired
	private AlbumService albumService;

	@Autowired
	private ArtistaService artistaService;

	@Autowired
	private RiproduzioneController riproduzioneController;

	/**
	 * @param id del brano
	 * @return l'ultimo brano della playlist riprodotto, il primo se non è ancora
	 *         mai stato riprodotto
	 */
	@PostMapping("/{id}/play")
	public ResponseEntity<RiproduzioneDTO> play(@PathVariable(value = "id") Long id) {
		final boolean goNext = true;
		final boolean isAlbum = true;
		return riproduzioneController.handlePlayRaccolta(id, goNext, isAlbum);
	}

	@PostMapping("/{id}/playPrevious")
	public ResponseEntity<RiproduzioneDTO> playPrevious(@PathVariable(value = "id") Long id) {
		final boolean goNext = false;
		final boolean isAlbum = true;
		return riproduzioneController.handlePlayRaccolta(id, goNext, isAlbum);
	}
	
	@DeleteMapping("/{id}/stop")
	public ResponseEntity<RiproduzioneDTO> stopRiproduzione(@PathVariable(value = "id") Long id) {
		final boolean isAlbum = true;
		return riproduzioneController.handleStopPlayRaccolta(id, isAlbum);
	}

	@GetMapping
	public ResponseEntity<List<AlbumDTO>> findByExample(AlbumDTO albumDTO) {

		boolean includeBrani = false;
		Album albumModel = AlbumDTO.buildAlbumModelFromDTO(albumDTO, includeBrani, false);
		List<Album> resultModel = albumService.findByExample(albumModel);
		List<AlbumDTO> resultDTO = AlbumDTO.buildDTOListFromModelList(resultModel, includeBrani, false);
		return ResponseEntity.ok(resultDTO);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AlbumDTO> getAlbum(@PathVariable(value = "id") Long id) {
		boolean includeBrani = true;
		Album albumModel = albumService.caricaSingoloEager(id);
		AlbumDTO albumDTO = AlbumDTO.buildAlbumDTOFromModel(albumModel, includeBrani, true);
		return ResponseEntity.ok(albumDTO);
	}

	@PostMapping("/admin/")
	public ResponseEntity<AlbumDTO> insertAlbum(@RequestBody @Valid AlbumDTO albumDTO) {
		boolean includeBrani = false;
		Album albumModel = AlbumDTO.buildAlbumModelFromDTO(albumDTO, includeBrani, true);
		albumService.inserisciNuovo(albumModel);

		Album albumPersist = albumService.caricaSingoloEager(albumModel.getId());

		// gli setto l'artista persist per mostrarlo nella resposne
		albumPersist.setArtista(artistaService.caricaSingolo(albumModel.getArtista().getId()));

		AlbumDTO albumDTOInserito = AlbumDTO.buildAlbumDTOFromModel(albumPersist, false, true);
		return ResponseEntity.ok(albumDTOInserito);
	}

	@PutMapping("/admin/{id}")
	public ResponseEntity<AlbumDTO> updateAlbum(@PathVariable(value = "id") Long id, @RequestBody AlbumDTO albumDTO) {
		albumDTO.setId(id);
		boolean includeBrani = false;
		Album albumUpdate = AlbumDTO.buildAlbumModelFromDTO(albumDTO, includeBrani, true);
		albumService.aggiornaSoloAlbum(albumUpdate);
		AlbumDTO albumModificatoDTO = AlbumDTO.buildAlbumDTOFromModel(albumUpdate, includeBrani, true);

		return ResponseEntity.ok(albumModificatoDTO);
	}

	@DeleteMapping("/admin/{id}")
	public ResponseEntity<String> deleteAlbum(@PathVariable(value = "id") Long id) {
		if (albumService.caricaSingolo(id) == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id " + id + " non trovato");
		}
		albumService.rimuovi(new Album(id));
		return ResponseEntity.ok("Album con id " + id + " cancellato con successo");
	}
}
