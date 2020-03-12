package it.spootifyrest.web.dto.utente;

import it.spootifyrest.model.Utente;

public class UtenteDTOLogin extends UtenteDTO {

	private String tokenDiAutenticazione;

	public String getTokenDiAutenticazione() {
		return tokenDiAutenticazione;
	}

	public void setTokenDiAutenticazione(String tokenDiAutenticazione) {
		this.tokenDiAutenticazione = tokenDiAutenticazione;
	}

	public static UtenteDTOLogin buildUtenteDTOFromModel(Utente source, boolean includeRuoli) {
		if (source == null) {
			return null;
		}
		UtenteDTOLogin result = new UtenteDTOLogin();
		result.setId(source.getId());
		result.setNome(source.getNome());
		result.setCognome(source.getCognome());
		result.setUsername(source.getUsername());
		result.setDataRegistrazione(source.getDataRegistrazione());
		result.setStato(source.getStato());
		if (includeRuoli) {
			result.setRuoli(source.getRuoli());
		}
		return result;
	}
}
