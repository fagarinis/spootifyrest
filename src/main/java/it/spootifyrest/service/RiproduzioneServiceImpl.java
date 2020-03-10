package it.spootifyrest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.spootifyrest.model.Riproduzione;
import it.spootifyrest.repository.RiproduzioneRepository;

@Service
public class RiproduzioneServiceImpl implements RiproduzioneService {

	@Autowired
	private RiproduzioneRepository riproduzioneRepository;

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

}
