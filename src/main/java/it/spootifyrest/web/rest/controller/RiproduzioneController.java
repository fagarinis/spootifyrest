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

import it.spootifyrest.model.Riproduzione;
import it.spootifyrest.service.RiproduzioneService;
import it.spootifyrest.web.dto.riproduzione.RiproduzioneDTOJson;

@RestController
@RequestMapping(value = "/player")
public class RiproduzioneController {

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
		
		if(resultModel == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					tipoRaccolta+"[ID:"+idRaccolta+"] non trovato");
		}
		
		RiproduzioneDTOJson resultDTO = RiproduzioneDTOJson.buildRiproduzioneDTOFromModel(resultModel);

		return ResponseEntity.ok(resultDTO);
	}
	
	

}
