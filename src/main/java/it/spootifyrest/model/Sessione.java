package it.spootifyrest.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import it.spootifyrest.model.constants.SpootifyConstants;
import it.spootifyrest.model.utils.DateUtils;

@Entity
public class Sessione {

	@Id
	private Long id;

	@Column(nullable = false)
	private String tokenDiAutenticazione;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInizioSessione;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataScadenzaSessione;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private Utente utente;

	private String tokenGenerator() {
		return UUID.randomUUID().toString();
	}
	
	public Sessione() {
		this(SpootifyConstants.DURATA_MINUTI_SESSIONE);
	}

	public Sessione(int durataMinuti) {
		Date dataInizio = new Date();
		this.setDataInizioSessione(dataInizio);
		Date dataFine = DateUtils.addMinutesToDate(durataMinuti, dataInizio);
		this.setDataScadenzaSessione(dataFine);
		this.setNewTokenDiAutenticazione();
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

	public void refresh() {
		int durataminutisessione = SpootifyConstants.DURATA_MINUTI_SESSIONE;
		Date dataInizio = new Date();
		this.setDataInizioSessione(dataInizio);
		Date dataFine = DateUtils.addMinutesToDate(durataminutisessione, dataInizio);
		this.setDataScadenzaSessione(dataFine);
	}

	public boolean isValid() {
		Date adesso = new Date();
		return dataScadenzaSessione.after(adesso);
	}

	public String setNewTokenDiAutenticazione() {
		String token = tokenGenerator();
		this.setTokenDiAutenticazione(token);
		return token;
	}

	public void termina() {
		this.dataScadenzaSessione = new Date();
	}

	public void refreshConToken() {
		this.refresh();
		this.setNewTokenDiAutenticazione();
	}

}
