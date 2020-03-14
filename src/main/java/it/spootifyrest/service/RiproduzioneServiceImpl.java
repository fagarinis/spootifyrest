package it.spootifyrest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.spootifyrest.model.Album;
import it.spootifyrest.model.Playlist;
import it.spootifyrest.model.Riproduzione;
import it.spootifyrest.repository.RiproduzioneRepository;

@Service
public class RiproduzioneServiceImpl implements RiproduzioneService {

	@Autowired
	private RiproduzioneRepository riproduzioneRepository;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private PlaylistService playlistService;

	@Autowired
	private AlbumService albumService;

	@Override
	@Transactional(readOnly = true)
	public List<Riproduzione> listAll() {
		return (List<Riproduzione>) riproduzioneRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Riproduzione caricaSingolo(Long id) {
		return riproduzioneRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void aggiorna(Riproduzione o) {
		riproduzioneRepository.save(o);
	}

	@Override
	@Transactional
	public void inserisciNuovo(Riproduzione o) {
		riproduzioneRepository.save(o);
	}

	@Override
	@Transactional
	public void rimuovi(Riproduzione o) {
		riproduzioneRepository.delete(o);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Riproduzione> findByExample(Riproduzione example) {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
		return (List<Riproduzione>) riproduzioneRepository.findAll(Example.of(example, matcher));
	}

	/**
	 * @return oggetto Riproduzione persist in eager fetch con la lista di brani
	 *         (dell'eventuale album o playlist)
	 */
	@Override
	@Transactional(readOnly = true)
	public Riproduzione inizializzaNuovaRiproduzioneDaIdRaccoltaEUserId(Long idRaccolta, Long idUtente,
			boolean isAlbum) {
		Riproduzione nuovaRiproduzione = new Riproduzione();
		nuovaRiproduzione.setUtente(utenteService.caricaSingolo(idUtente));
		if (isAlbum) {
			Album albumInRiproduzione = albumService.caricaSingoloEager(idRaccolta);
			nuovaRiproduzione.setAlbum(albumInRiproduzione);
			nuovaRiproduzione.setBrano(albumInRiproduzione.getPrimoBrano());
		} else {
			Playlist playlistInRiproduzione = playlistService.caricaSingoloEager(idRaccolta, true, false);
			nuovaRiproduzione.setPlaylist(playlistInRiproduzione);
			nuovaRiproduzione.setBrano(playlistInRiproduzione.getPrimoBrano());
		}

		return nuovaRiproduzione;
	}

	/**
	 * @return oggetto Riproduzione persist in eager fetch con la lista di brani
	 *         (dell'eventuale album o playlist)
	 */
	@Override
	@Transactional(readOnly = true)
	public Riproduzione caricaRiproduzioneDaIdRaccoltaEUserId(Long idRaccolta, Long idUtente, boolean isAlbum) {
		Riproduzione riproduzionePersist = null;
		if (isAlbum) {
			riproduzionePersist = riproduzioneRepository.findRiproduzioneByAlbumIdAndUserId(idRaccolta, idUtente)
					.orElse(null);
		} else {
			riproduzionePersist = riproduzioneRepository.findRiproduzioneByPlaylistIdAndUserId(idRaccolta, idUtente)
					.orElse(null);
		}

		return riproduzionePersist;
	}

	/**
	 * Aggiorna la riproduzione sul database, cambiando brano
	 * 
	 * @param idRaccolta id dell'album o playlist da ascoltare
	 * @param idUtente   id dell'utente che ascolta la raccolta, serve per caricare
	 *                   un eventuale riproduzione già presente
	 * @param isAlbum    serve a specificare se la raccolta è un album (true), o se
	 *                   è una playlist (false)
	 * @param next       nel caso la riproduzione esista già, con true si va al
	 *                   brano successivo, con false se si va al brano precedente.
	 * @return la riproduzione aggiornata al prossimo/precedente brano o al primo
	 *         brano se la riproduzione non esisteva
	 */
	@Override
	@Transactional
	public Riproduzione ascoltaProssimoBranoDaRaccolta(Long idRaccolta, Long idUtente, boolean isAlbum, boolean next) {
		// carica la riproduzione in eager fetch con la lista di brani (dell'eventuale
		// album o playlist)
		Riproduzione riproduzionePersist = caricaRiproduzioneDaIdRaccoltaEUserId(idRaccolta, idUtente, isAlbum);

		// Se la riproduzione esiste
		if (riproduzionePersist != null) {
			if (next) {
				riproduzionePersist.next();
			} else {
				riproduzionePersist.previous();
			}
			aggiorna(riproduzionePersist);
			return riproduzionePersist;
		}

		// se la riproduzione non esiste;
		Riproduzione nuovaRiproduzione = inizializzaNuovaRiproduzioneDaIdRaccoltaEUserId(idRaccolta, idUtente, isAlbum);

		inserisciNuovo(nuovaRiproduzione);
		return nuovaRiproduzione;
	}

	@Override
	@Transactional
	/**
	 * @return la riproduzione cancellata, null se non esisteva
	 */
	public Riproduzione eliminaRiproduzione(Long idRaccolta, Long idUtente, boolean isAlbum) {
		Riproduzione riproduzioneDaCancellare = caricaRiproduzioneDaIdRaccoltaEUserId(idRaccolta, idUtente, isAlbum);
		if (riproduzioneDaCancellare == null) {
			return null;
		}
		this.rimuovi(riproduzioneDaCancellare);
		return riproduzioneDaCancellare;
	}

}
