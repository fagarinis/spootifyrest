package it.spootifyrest.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import it.spootifyrest.model.en.CodiceRuolo;
import it.spootifyrest.model.en.StatoUtente;

import javax.persistence.Entity;

@Entity
public class Utente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String cognome;
	@Column(unique = true, nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;
	@Temporal(TemporalType.DATE)
	private Date dataRegistrazione;

	// se non uso questa annotation viene gestito come un intero
	@Enumerated(EnumType.STRING)
	private StatoUtente stato = StatoUtente.CREATO;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "utente")
	private Sessione sessione;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
	@JoinTable(name = "utente_ruolo", joinColumns = @JoinColumn(name = "utente_id", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "ruolo_id", referencedColumnName = "ID"))
	private List<Ruolo> ruoli = new ArrayList<>();

	@OneToMany(mappedBy = "utente", orphanRemoval = true)
	private List<Playlist> playlist = new ArrayList<>();

	@OneToMany(mappedBy = "utente", orphanRemoval = true)
	private List<Riproduzione> riproduzioni = new ArrayList<>();

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public boolean isAdmin() {
		for (Ruolo ruoloItem : ruoli) {
			if (ruoloItem.getCodice().equals(CodiceRuolo.ADMIN_ROLE))
				return true;
		}
		return false;
	}

	public boolean isCustomer() {
		for (Ruolo ruoloItem : ruoli) {
			if (ruoloItem.getCodice().equals(CodiceRuolo.CUSTOMER_ROLE))
				return true;
		}
		return false;
	}

	public List<Playlist> getPlaylist() {
		return playlist;
	}

	public void setPlaylist(List<Playlist> playlist) {
		this.playlist = playlist;
	}

	public Sessione getSessione() {
		return sessione;
	}

	public void setSessione(Sessione sessione) {
		this.sessione = sessione;
		if (sessione != null) {
			this.sessione.setUtente(this);
		}
	}

	public List<Riproduzione> getRiproduzioni() {
		return riproduzioni;
	}

	public void setRiproduzioni(List<Riproduzione> riproduzioni) {
		this.riproduzioni = riproduzioni;
	}

	@Override
	public String toString() {
		return "Utente [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", username=" + username + ", stato="
				+ stato + ", ruoli=" + ruoli + "]";
	}

}
