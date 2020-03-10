package it.spootifyrest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.spootifyrest.model.Sessione;
import it.spootifyrest.repository.SessioneRepository;

@Service
public class SessionServiceImpl implements SessioneService {

	@Autowired
	private SessioneRepository sessioneRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Sessione> listAll() {
		return (List<Sessione>) sessioneRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Sessione caricaSingolo(Long id) {
		return sessioneRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void aggiorna(Sessione o) {
		sessioneRepository.save(o);
	}

	@Override
	@Transactional
	public void inserisciNuovo(Sessione o) {
		sessioneRepository.save(o);
	}

	@Override
	@Transactional
	public void rimuovi(Sessione o) {
		sessioneRepository.delete(o);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Sessione> findByExample(Sessione example) {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
		return (List<Sessione>) sessioneRepository.findAll(Example.of(example, matcher));
	}

}
