package it.spootifyrest.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.spootifyrest.model.Brano;
import it.spootifyrest.model.Playlist;
import it.spootifyrest.repository.BranoRepository;

@Service
public class BranoServiceImpl implements BranoService {

	@Autowired
	private BranoRepository branoRepository;

	@Autowired
	private EntityManager entityManager;

	@Override
	@Transactional(readOnly = true)
	public List<Brano> listAll() {
		return (List<Brano>) branoRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Brano caricaSingolo(Long id) {
		return branoRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void aggiorna(Brano o) {
		branoRepository.save(o);
	}

	@Override
	@Transactional
	public void inserisciNuovo(Brano o) {
		branoRepository.save(o);
	}

	@Override
	@Transactional
	public void rimuovi(Brano o) {
		//rimuovo il brano da tutte le playlist in cui è presente
		for(Playlist playlistItem : o.getPlaylist()) {
			playlistItem.getBrani().remove(o);
		}
		
		branoRepository.delete(o);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Brano> findByExample(Brano example) {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
		return (List<Brano>) branoRepository.findAll(Example.of(example, matcher));
	}

	@Override
	@Transactional(readOnly = true)
	public Brano caricaSingoloEager(Long id) {
		return branoRepository.findByIdEager(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Brano> caricaBraniDaIdPlaylist(Long idPlaylist) {
		return branoRepository.findAllByPlaylistId(idPlaylist);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Brano> caricaBraniDaListaBraniTransient(List<Brano> brani) {
		String query = "SELECT DISTINCT b FROM Brano b where 1=0 ";

		for (Brano brano : brani) {
			query += " or b.id = " + brano.getId() + " ";
		}

		return entityManager.createQuery(query, Brano.class).getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public Brano caricaSingoloEagerConAlbumEPlaylists(Long id) {
		return branoRepository.findByIdEagerIncludeAlbumAndPlaylists(id).orElse(null);
	}

}
