package it.spootifyrest.web.rest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	private UtenteService utenteService;
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
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
	
	

	@PostMapping("/auth/login")
	public ResponseEntity<UtenteDTO> eseguiLogin(@RequestBody UtenteDTORegistrazione utenteDTOInput) {
		boolean includeRuoli = true;
		Utente utenteLogin = UtenteDTORegistrazione.buildUtenteModelFromDTO(utenteDTOInput);
		Utente utenteLoggato = utenteService.eseguiAccesso(utenteLogin.getUsername(), utenteLogin.getPassword());
		if (utenteLoggato == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "login fallito");
		}
		UtenteDTOLogin utenteLoggatoDTO = UtenteDTOLogin.buildUtenteDTOFromModel(utenteLoggato, includeRuoli);

		utenteLoggatoDTO.setTokenDiAutenticazione(utenteLoggato.getSessione().getTokenDiAutenticazione());
		return ResponseEntity.ok(utenteLoggatoDTO);
	}
	
	@GetMapping("/auth/logout")
	public ResponseEntity<UtenteDTO> eseguiLogout() {
		
		Utente utenteLogout = utenteService.eseguiLogout(getUtenteInSessione());
		if (utenteLogout == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "logout fallito");
		}
		
		UtenteDTO utenteLogoutDTO = UtenteDTO.buildUtenteDTOFromModel(utenteLogout, true);

		return ResponseEntity.ok(utenteLogoutDTO);
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
