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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import it.spootifyrest.model.Album;
import it.spootifyrest.model.Brano;
import it.spootifyrest.service.AlbumService;
import it.spootifyrest.service.BranoService;
import it.spootifyrest.web.dto.album.AlbumDTO;
import it.spootifyrest.web.dto.brano.BranoDTO;

@RestController
@RequestMapping(value = "/songs")
public class BranoController {

	@Autowired
	BranoService branoService;

	@Autowired
	AlbumService albumService;

	@GetMapping
	public ResponseEntity<List<BranoDTO>> findByExample(BranoDTO branoDTO,
			@RequestParam(required = false, defaultValue = "true") boolean includeAlbum) {
		Brano branoModel = BranoDTO.buildBranoModelFromDTO(branoDTO, includeAlbum);
		List<Brano> resultModel = branoService.findByExample(branoModel);
		List<BranoDTO> resultDTO = BranoDTO.buildDTOListFromModelList(resultModel, includeAlbum);

		return ResponseEntity.ok(resultDTO);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BranoDTO> getBrano(@PathVariable(value = "id") Long id) {
		boolean includeAlbum = true;
		Brano branoModel = includeAlbum ? branoService.caricaSingoloEager(id) : branoService.caricaSingolo(id);
		if (branoModel == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id " + id + " non trovato");
		}
		BranoDTO branoDTO = BranoDTO.buildBranoDTOFromModel(branoModel, includeAlbum);
		return ResponseEntity.ok(branoDTO);
	}

	@PostMapping("/admin/")
	public ResponseEntity<BranoDTO> insertBrano(@RequestBody @Valid BranoDTO branoDTO) {
		boolean includeAlbum = true;
		
		Album albumDaAssociare = albumService.caricaSingolo(branoDTO.getAlbum().getId());

		if (branoDTO.getAlbum() == null || albumDaAssociare == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"id dell'album da associare al brano non trovato");
		}
		
		Brano branoModel = BranoDTO.buildBranoModelFromDTO(branoDTO, includeAlbum);
		branoService.inserisciNuovo(branoModel);
		BranoDTO branoDTOInserito = BranoDTO.buildBranoDTOFromModel(branoModel, includeAlbum);
		branoDTOInserito.setAlbum(AlbumDTO
				.buildAlbumDTOFromModel(albumService.caricaSingoloEager(branoModel.getAlbum().getId()), false, false));
		return ResponseEntity.ok(branoDTOInserito);
	}

	@PutMapping("/admin/{id}")
	public ResponseEntity<BranoDTO> updateBrano(@PathVariable(value = "id") Long id, @RequestBody @Valid BranoDTO branoDTO) {
		Album albumDaAssociare = albumService.caricaSingolo(branoDTO.getAlbum().getId());

		if (branoDTO.getAlbum() == null || albumDaAssociare == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"id dell'album da associare al brano non trovato");
		}

		branoDTO.setId(id);
		boolean includeAlbum = true;
		Brano branoUpdate = BranoDTO.buildBranoModelFromDTO(branoDTO, includeAlbum);
		branoService.aggiorna(branoUpdate);

		BranoDTO branoModificatoDTO = BranoDTO.buildBranoDTOFromModel(branoUpdate, includeAlbum);

		branoModificatoDTO.setAlbum(AlbumDTO.buildAlbumDTOFromModel(albumDaAssociare, false, false));

		return ResponseEntity.ok(branoModificatoDTO);
	}

	@DeleteMapping("/admin/{id}")
	public ResponseEntity<BranoDTO> deleteBrano(@PathVariable(value = "id") Long id) {
		boolean includeAlbum = true;
		Brano branoDaCancellare = branoService.caricaSingolo(id);
		if (branoDaCancellare == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id " + id + " non trovato");
		}
		branoService.rimuovi(branoDaCancellare);
		BranoDTO branoCancellato = BranoDTO.buildBranoDTOFromModel(branoDaCancellare, includeAlbum);
		return ResponseEntity.ok(branoCancellato);
	}

}
