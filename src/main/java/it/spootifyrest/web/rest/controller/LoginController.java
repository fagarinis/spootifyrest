package it.spootifyrest.web.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import it.spootifyrest.model.Utente;
import it.spootifyrest.service.UtenteService;
import it.spootifyrest.web.dto.utente.UtenteDTO;
import it.spootifyrest.web.dto.utente.UtenteDTORegistrazione;

@RestController
@RequestMapping(value = "/auth/login")
public class LoginController {

	@Autowired
	UtenteService utenteService;

	@PostMapping
	public ResponseEntity<UtenteDTO> eseguiLogin(UtenteDTORegistrazione utenteDTOInput) {
		boolean includeRuoli = true;
		Utente utenteLogin = UtenteDTORegistrazione.buildUtenteModelFromDTO(utenteDTOInput);
		Utente utenteLoggato = utenteService.eseguiAccesso(utenteLogin.getUsername(), utenteLogin.getPassword());
		if (utenteLoggato == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "login fallito");
		}

		UtenteDTO utenteLoggatoDTO = UtenteDTO.buildUtenteDTOFromModel(utenteLoggato, includeRuoli);
		return ResponseEntity.ok(utenteLoggatoDTO);
	}

}
