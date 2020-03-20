package it.spootifyrest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.spootifyrest.model.Artista;
import it.spootifyrest.repository.ArtistaRepository;

@Service
public class ArtistaServiceImpl implements ArtistaService {

	@Autowired
	private ArtistaRepository artistaRepository;
	
	@Autowired
	private AlbumService albumService;

	@Override
	@Transactional(readOnly = true)
	public List<Artista> listAll() {
		return (List<Artista>) artistaRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Artista caricaSingolo(Long id) {
		return artistaRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void aggiorna(Artista o) {
		artistaRepository.save(o);
	}

	@Override
	@Transactional
	public void inserisciNuovo(Artista o) {
		artistaRepository.save(o);
	}

	@Override
	@Transactional
	public void rimuovi(Artista o) {
		albumService.rimuoviAlbums(o.getAlbum());
		artistaRepository.delete(o);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Artista> findByExample(Artista example) {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
		return (List<Artista>) artistaRepository.findAll(Example.of(example, matcher));
	}

}
