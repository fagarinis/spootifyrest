package it.spootifyrest.web.dto.utente;

import javax.validation.constraints.NotBlank;

import it.spootifyrest.model.Utente;

public class UtenteDTORegistrazione {

	
	@NotBlank
	private String nome;
	@NotBlank
	private String cognome;
	@NotBlank
	private String username;
	@NotBlank
	private String password;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static Utente buildUtenteModelFromDTO(UtenteDTORegistrazione source) {
		Utente result = new Utente();
		result.setNome(source.getNome());
		result.setCognome(source.getCognome());
		result.setUsername(source.getUsername());
		result.setPassword(source.getPassword());

		return result;
	}

	public static UtenteDTORegistrazione buildUtenteDTOFromModel(Utente source) {
		UtenteDTORegistrazione result = new UtenteDTORegistrazione();
		result.setNome(source.getNome());
		result.setCognome(source.getCognome());
		result.setUsername(source.getUsername());
		result.setPassword(source.getPassword());
		return result;
	}
}
