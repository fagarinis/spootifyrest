package it.spootifyrest.web.rest.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.spootifyrest.model.Artista;
import it.spootifyrest.model.Utente;
import it.spootifyrest.service.ArtistaService;
import it.spootifyrest.web.dto.artista.ArtistaDTO;

@RestController
@RequestMapping(value = "/artists")
public class ArtistaController {

	@Autowired
	ArtistaService artistaService;

	@GetMapping
	public ResponseEntity<List<ArtistaDTO>> findByExample(ArtistaDTO artistaDTO) {
		boolean includeAlbum = false;
		Artista artistaModel = ArtistaDTO.buildArtistaModelFromDTO(artistaDTO, includeAlbum);
		List<Artista> resultModel = artistaService.findByExample(artistaModel);
		List<ArtistaDTO> resultDTO = ArtistaDTO.buildDTOListFromModelList(resultModel, includeAlbum);

		return ResponseEntity.ok(resultDTO);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ArtistaDTO> getArtista(@PathVariable(value = "id") Long id) {
		boolean includeAlbum = false;
		Artista artistaModel = artistaService.caricaSingolo(id);
		ArtistaDTO artistaDTO = ArtistaDTO.buildArtistaDTOFromModel(artistaModel, includeAlbum);
		return ResponseEntity.ok(artistaDTO);
	}

	@PostMapping
	public ResponseEntity<ArtistaDTO> insertArtista(@Valid ArtistaDTO artistaDTO) {
		boolean includeAlbum = false;
		Artista artistaModel = ArtistaDTO.buildArtistaModelFromDTO(artistaDTO, includeAlbum);
		artistaService.inserisciNuovo(artistaModel);
		ArtistaDTO artistaDTOInserito = ArtistaDTO.buildArtistaDTOFromModel(artistaModel, includeAlbum);
		return ResponseEntity.ok(artistaDTOInserito);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ArtistaDTO> updateArtista(@PathVariable(value = "id") Long id, ArtistaDTO artistaDTO) {
		artistaDTO.setId(id);
		boolean includeAlbum = false;
		Artista artistaUpdate = ArtistaDTO.buildArtistaModelFromDTO(artistaDTO, includeAlbum);
		artistaService.aggiorna(artistaUpdate);

		ArtistaDTO artistaModificatoDTO = ArtistaDTO.buildArtistaDTOFromModel(artistaUpdate, includeAlbum);

		return ResponseEntity.ok(artistaModificatoDTO);
	}

}
