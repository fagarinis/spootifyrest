package it.spootifyrest.web.dto.utente;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import it.spootifyrest.model.Ruolo;
import it.spootifyrest.model.Utente;
import it.spootifyrest.model.en.StatoUtente;

public class UtenteDTOUpdate {

	private Long id;
	@NotBlank
	private String nome;
	@NotBlank
	private String cognome;
	@NotBlank
	private String username;

	private Date dataRegistrazione;

	private StatoUtente stato;

	@JsonIgnoreProperties(value = { "utenti" })
	private List<Ruolo> ruoli = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	public StatoUtente getStato() {
		return stato;
	}

	public void setStato(StatoUtente stato) {
		this.stato = stato;
	}

	public List<Ruolo> getRuoli() {
		return ruoli;
	}

	public void setRuoli(List<Ruolo> ruoli) {
		this.ruoli = ruoli;
	}

	public static Utente buildUtenteModelFromDTO(UtenteDTOUpdate source, boolean includeRuoli) {
		if (source == null) {
			return null;
		}
		Utente result = new Utente();
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

	public static UtenteDTOUpdate buildUtenteDTOFromModel(Utente source, boolean includeRuoli) {

		if (source == null) {
			return null;
		}

		UtenteDTOUpdate result = new UtenteDTOUpdate();
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

	public static List<UtenteDTOUpdate> buildDTOListFromModelList(List<Utente> input, boolean includeRuoli) {
		List<UtenteDTOUpdate> result = new ArrayList<>();

		for (Utente utenteItem : input) {
			UtenteDTOUpdate utenteDTOtemp = buildUtenteDTOFromModel(utenteItem, includeRuoli);
			result.add(utenteDTOtemp);
		}
		return result;
	}

	@Override
	public String toString() {
		return "UtenteDTOUpdate [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", username=" + username
				+ ", dataRegistrazione=" + dataRegistrazione + ", stato=" + stato + ", ruoli=" + ruoli + "]";
	}

}