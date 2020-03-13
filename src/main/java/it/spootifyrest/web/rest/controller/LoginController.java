package it.spootifyrest.web.rest.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import it.spootifyrest.model.Utente;
import it.spootifyrest.model.en.CodiceRuolo;
import it.spootifyrest.service.UtenteService;
import it.spootifyrest.web.dto.utente.UtenteDTO;
import it.spootifyrest.web.dto.utente.UtenteDTOLogin;
import it.spootifyrest.web.dto.utente.UtenteDTORegistrazione;

@RestController
@RequestMapping
public class LoginController {

	@Autowired
	UtenteService utenteService;

	@PostMapping("/auth/login")
	public ResponseEntity<UtenteDTO> eseguiLogin(@RequestBody UtenteDTORegistrazione utenteDTOInput) {
		boolean includeRuoli = true;
		Utente utenteLogin = UtenteDTORegistrazione.buildUtenteModelFromDTO(utenteDTOInput);
		Utente utenteLoggato = utenteService.eseguiAccesso(utenteLogin.getUsername(), utenteLogin.getPassword());
		if (utenteLoggato == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "login fallito");
		}
		//TODO ritornare il token della sessione
		UtenteDTOLogin utenteLoggatoDTO = UtenteDTOLogin.buildUtenteDTOFromModel(utenteLoggato, includeRuoli);
		
		utenteLoggatoDTO.setTokenDiAutenticazione(utenteLoggato.getSessione().getTokenDiAutenticazione());
		return ResponseEntity.ok(utenteLoggatoDTO);
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

}
