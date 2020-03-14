package it.spootifyrest.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.spootifyrest.model.Playlist;
import it.spootifyrest.model.Utente;
import it.spootifyrest.repository.PlaylistRepository;

@Service
public class PlaylistServiceImpl implements PlaylistService {

	@Autowired
	private PlaylistRepository playlistRepository;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private BranoService branoService;

	@Autowired
	private EntityManager entityManager;

	@Override
	@Transactional(readOnly = true)
	public List<Playlist> listAll() {
		return (List<Playlist>) playlistRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Playlist caricaSingolo(Long id) {
		return playlistRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void aggiorna(Playlist o) {
		o.setUtente(utenteService.caricaSingolo(o.getUtente().getId()));
		o.setBrani(branoService.caricaBraniDaListaBraniTransient(o.getBrani()));
		playlistRepository.save(o);
	}

	@Override
	@Transactional
	public void inserisciNuovo(Playlist o) {
		playlistRepository.save(o);
	}

	@Override
	@Transactional
	public void rimuovi(Playlist o) {
		playlistRepository.delete(o);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Playlist> findByExample(Playlist example) {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
		return (List<Playlist>) playlistRepository.findAll(Example.of(example, matcher));
	}

	@Override
	@Transactional(readOnly = true)
	public Playlist caricaSingoloEager(Long id, boolean includeBrani, boolean includeUtente) {
		Playlist result = null;
		String query = "select p from Playlist p ";
		if (includeBrani)
			query += " left join fetch p.brani ";
		if (includeUtente)
			query += " left join fetch p.utente ";

		query += " where p.id = " + id + " ";

		try {
			result = entityManager.createQuery(query, Playlist.class).getSingleResult();
		} catch (NoResultException e) {
		}
		return result;
	}

	@Override
	@Transactional
	public Playlist aggiungiBranoAllaPlaylistDellUtente(Long idBrano, Long idPlaylist, Long idUtente) {
		Playlist playlistPersist = this.caricaSingoloEager(idPlaylist, true, true);

		if (playlistPersist == null || playlistPersist.getUtente().getId() != idUtente) {
			return null;
		}

		playlistPersist.addBrano(branoService.caricaSingolo(idBrano));

		return playlistPersist;
	}

	@Override
	@Transactional
	public Playlist rimuoviBranoDallaPlaylistDellUtente(Long idBrano, Long idPlaylist, Long idUtente) {
		Playlist playlistPersist = this.caricaSingoloEager(idPlaylist, true, true);
		if (playlistPersist.getUtente().getId() != idUtente) {
			return null;
		}

		playlistPersist.removeBrano(branoService.caricaSingolo(idBrano));

		return playlistPersist;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Playlist> findPlayListUtente(Utente utente) {
		return playlistRepository.findAllPlaylistsByUtenteId(utente.getId());
	}

}
