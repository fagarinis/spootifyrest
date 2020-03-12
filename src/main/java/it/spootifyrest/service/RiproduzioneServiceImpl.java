package it.spootifyrest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.spootifyrest.model.Brano;
import it.spootifyrest.model.Riproduzione;
import it.spootifyrest.repository.RiproduzioneRepository;

@Service
public class RiproduzioneServiceImpl implements RiproduzioneService {

	@Autowired
	private RiproduzioneRepository riproduzioneRepository;

	@Autowired
	private BranoService branoService;

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
	 * mette il prossimo brano in ascolto isAlbum è true se la raccolta è un album
	 */
	@Override
	@Transactional
	public Riproduzione ascoltaProssimoBranoDaRaccolta(Long idRaccolta, Long idUtente, boolean isAlbum) {
		// carica la riproduzione in eager fetch con la lista di brani (dell'eventuale
		// album o playlist)
		Riproduzione riproduzionePersist = null;
		if (isAlbum) {
			riproduzionePersist = riproduzioneRepository.findRiproduzioneByAlbumIdAndUserId(idRaccolta, idUtente)
					.orElse(null);
		} else {
			riproduzionePersist = riproduzioneRepository.findRiproduzioneByPlaylistIdAndUserId(idRaccolta, idUtente)
					.orElse(null);
		}

		// Se la riproduzione esiste
		if (riproduzionePersist != null) {
			riproduzionePersist.next();
			aggiorna(riproduzionePersist);
			return riproduzionePersist;
		}

		// se la riproduzione non esiste;
		Riproduzione nuovaRiproduzione = new Riproduzione();
		nuovaRiproduzione.setUtente(utenteService.caricaSingolo(idUtente));
		if (isAlbum) {
			nuovaRiproduzione.setAlbum(albumService.caricaSingoloEager(idRaccolta));
			nuovaRiproduzione.setBrano(nuovaRiproduzione.getAlbum().getPrimoBrano());
		} else {
			nuovaRiproduzione.setPlaylist(playlistService.caricaSingoloEager(idRaccolta, true, false));
			nuovaRiproduzione.setBrano(nuovaRiproduzione.getPlaylist().getPrimoBrano());
		}

		inserisciNuovo(nuovaRiproduzione);
		return nuovaRiproduzione;
	}

}
