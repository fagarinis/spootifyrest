package it.spootifyrest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.spootifyrest.model.Brano;
import it.spootifyrest.repository.BranoRepository;

@Service
public class BranoServiceImpl implements BranoService {

	@Autowired
	private BranoRepository branoRepository;

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
	public Brano findLastBranoByUtenteIdAndPlaylistId(Long utenteId, Long playlistId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Brano findLastBranoByUtenteIdAndAlbumId(Long utenteId, Long albumId) {
		// TODO Auto-generated method stub
		return null;
	}

}
