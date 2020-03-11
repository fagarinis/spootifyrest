package it.spootifyrest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.spootifyrest.model.Ruolo;
import it.spootifyrest.model.en.CodiceRuolo;
import it.spootifyrest.repository.RuoloRepository;

@Service
public class RuoloServiceImpl implements RuoloService {

	@Autowired
	private RuoloRepository ruoloRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Ruolo> listAll() {
		return (List<Ruolo>) ruoloRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Ruolo caricaSingolo(Long id) {
		return ruoloRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void aggiorna(Ruolo o) {
		ruoloRepository.save(o);
	}

	@Override
	@Transactional
	public void inserisciNuovo(Ruolo o) {
		ruoloRepository.save(o);
	}

	@Override
	@Transactional
	public void rimuovi(Ruolo o) {
		ruoloRepository.delete(o);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Ruolo> findByExample(Ruolo example) {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
		return (List<Ruolo>) ruoloRepository.findAll(Example.of(example, matcher));
	}
	
	@Override
	@Transactional(readOnly = true)
	public Ruolo cercaDaCodiceRuolo(CodiceRuolo codiceRuolo) {
		return ruoloRepository.findByCodice(codiceRuolo).orElse(null);
	}
	
	

}
