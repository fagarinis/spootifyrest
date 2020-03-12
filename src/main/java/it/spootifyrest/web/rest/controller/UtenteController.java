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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import it.spootifyrest.model.Utente;
import it.spootifyrest.model.en.CodiceRuolo;
import it.spootifyrest.service.UtenteService;
import it.spootifyrest.web.dto.utente.UtenteDTO;
import it.spootifyrest.web.dto.utente.UtenteDTORegistrazione;
import it.spootifyrest.web.dto.utente.UtenteDTOUpdate;

//sintassi di spring boot per i controller simile a jaxrs di PizzaStoreRest
@RestController
@RequestMapping(value = "/users")
public class UtenteController {

	@Autowired
	private UtenteService utenteService;

	@GetMapping
	public ResponseEntity<List<UtenteDTO>> findByExample(UtenteDTO utenteDTO) {
		boolean includeRuoli = true;
		Utente utenteModel = UtenteDTO.buildUtenteModelFromDTO(utenteDTO, includeRuoli);
		List<Utente> resultModel = utenteService.findByExample(utenteModel);
		List<UtenteDTO> resultDTO = UtenteDTO.buildDTOListFromModelList(resultModel, includeRuoli);

		return ResponseEntity.ok(resultDTO);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UtenteDTO> getUtente(@PathVariable(value = "id") Long id) {
		Utente utenteModel = utenteService.caricaSingoloUtenteEager(id);
		UtenteDTO utenteDTO = UtenteDTO.buildUtenteDTOFromModel(utenteModel, true);
		return ResponseEntity.ok(utenteDTO);
	}

	@PostMapping("/register")
	public ResponseEntity<UtenteDTO> registraNuovoUtenteCustomer(
			@RequestBody @Valid UtenteDTORegistrazione utenteDTORegistrazione) {

		Utente utenteModel = UtenteDTORegistrazione.buildUtenteModelFromDTO(utenteDTORegistrazione);
		Utente utenteRegistrato = utenteService.registraUtente(utenteModel, CodiceRuolo.CUSTOMER_ROLE);
		if (utenteRegistrato == null) {
//			return ResponseEntity.status(HttpStatus.CONFLICT).body(DTOConMessaggioDentro); si dovrebbe fare così
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"username " + utenteModel.getUsername() + " non disponibile");
		}
		UtenteDTO utenteDTO = UtenteDTO.buildUtenteDTOFromModel(utenteRegistrato, true);
		return ResponseEntity.ok(utenteDTO);
	}

	@PutMapping("/{id}/activate")
	public ResponseEntity<UtenteDTO> activateUtente(@PathVariable(value = "id") Long id) {
		Utente utenteAttivato = utenteService.attivaUtenteDaId(id);
		UtenteDTO utenteAttivatoDTO = UtenteDTO.buildUtenteDTOFromModel(utenteAttivato, true);

		return ResponseEntity.ok(utenteAttivatoDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UtenteDTO> updateUtente(@PathVariable(value = "id") Long id,
			@RequestBody @Valid UtenteDTOUpdate utenteDTO) {
		utenteDTO.setId(id);
		Utente utenteDaModificare = UtenteDTOUpdate.buildUtenteModelFromDTO(utenteDTO, true);
		Utente utenteModificato = utenteService.aggiornaUtenteConRuoli(utenteDaModificare);
		UtenteDTO utenteModificatoDTO = UtenteDTO.buildUtenteDTOFromModel(utenteModificato, true);

		return ResponseEntity.ok(utenteModificatoDTO);
	}

}
