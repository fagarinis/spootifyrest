package it.spootifyrest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.spootifyrest.model.Album;
import it.spootifyrest.model.Brano;
import it.spootifyrest.repository.AlbumRepository;

@Service
public class AlbumServiceImpl implements AlbumService {

	@Autowired
	private AlbumRepository albumRepository;

	@Autowired
	private BranoService branoService;

	@Override
	@Transactional(readOnly = true)
	public List<Album> listAll() {
		return (List<Album>) albumRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Album caricaSingolo(Long id) {
		return albumRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void aggiorna(Album o) {
		albumRepository.save(o);
	}

	@Override
	@Transactional
	public void inserisciNuovo(Album o) {
		albumRepository.save(o); //salvo l'album per ottenere il suo id
		for (Brano branoItem : o.getBrani()) {
			branoItem.setAlbum(o); //setto l'id_album da associare al brano
			branoService.inserisciNuovo(branoItem);
		}
	}

	@Override
	@Transactional
	public void rimuovi(Album o) {
		for(Brano branoItem : o.getBrani()) {
			branoService.rimuovi(branoItem);
		}
		
		albumRepository.delete(o);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Album> findByExample(Album example) {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
		return (List<Album>) albumRepository.findAll(Example.of(example, matcher));
	}

	@Override
	@Transactional(readOnly = true)
	public Album caricaSingoloEager(Long id) {
		return albumRepository.findByIdEager(id).orElse(null);
	}

	/**
	 * aggiorna mantenendo i field artista e brani dell'album presenti sul DB
	 */
	@Override
	@Transactional
	public void aggiornaSoloAlbum(Album o) {
		Album albumPersist = caricaSingoloEager(o.getId());

		o.setArtista(albumPersist.getArtista());
		o.setBrani(albumPersist.getBrani());

		albumRepository.save(o);
	}

}
