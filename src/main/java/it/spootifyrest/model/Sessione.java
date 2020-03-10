package it.spootifyrest.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import it.spootifyrest.model.utils.DateUtils;

@Entity
public class Sessione {

	@Id
	private Long id;
	private String tokenDiAutenticazione;
	private Date dataInizioSessione;
	private Date dataScadenzaSessione;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private Utente utente;

	public Sessione() {
	}

	public Sessione(int durataMinuti) {
		Date dataInizio = new Date();
		this.setDataInizioSessione(dataInizio);
		Date dataFine = DateUtils.addMinutesToDate(durataMinuti, dataInizio);
		this.setDataScadenzaSessione(dataFine);
		this.setTokenDiAutenticazione(UUID.randomUUID().toString());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTokenDiAutenticazione() {
		return tokenDiAutenticazione;
	}

	public void setTokenDiAutenticazione(String tokenDiAutenticazione) {
		this.tokenDiAutenticazione = tokenDiAutenticazione;
	}

	public Date getDataInizioSessione() {
		return dataInizioSessione;
	}

	public void setDataInizioSessione(Date dataInizioSessione) {
		this.dataInizioSessione = dataInizioSessione;
	}

	public Date getDataScadenzaSessione() {
		return dataScadenzaSessione;
	}

	public void setDataScadenzaSessione(Date dataScadenzaSessione) {
		this.dataScadenzaSessione = dataScadenzaSessione;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	@Override
	public String toString() {
		return "Sessione [id=" + id + ", tokenDiAutenticazione=" + tokenDiAutenticazione + ", dataInizioSessione="
				+ dataInizioSessione + ", dataScadenzaSessione=" + dataScadenzaSessione + "]";
	}

}