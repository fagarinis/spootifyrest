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
		rimuoviBranoDaTutteLePlaylist(o);
		rimuoviTutteLeRiproduzioniDelBrano(o);
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
		String query = "SELECT b FROM Brano b where 1=0 ";

		for (Brano brano : brani) {
			query += " or b.id = " + brano.getId() + " ";
		}
		
		query +=" ORDER BY b.id";

		return entityManager.createQuery(query, Brano.class).getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public Brano caricaSingoloEagerConAlbumEPlaylists(Long id) {
		return branoRepository.findByIdEagerIncludeAlbumAndPlaylists(id).orElse(null);
	}

	
	@Override
	@Transactional
	public void rimuoviBranoDaTutteLePlaylist(Brano o) {
		String query = "DELETE FROM playlist_brano pb where pb.brano_id ="+o.getId()+" ";
		entityManager.createNativeQuery(query).executeUpdate();
	}
	
	@Override
	@Transactional
	public void rimuoviBraniDaTutteLePlaylist(List<Brano> listaBrani) {
		String query = "DELETE FROM playlist_brano pb where 1=0 ";
		for(Brano branoItem : listaBrani) {
			query += " or brano_id ="+ branoItem.getId()+" ";
		}
		entityManager.createNativeQuery(query).executeUpdate();
	}
	
	@Override
	@Transactional
	public void rimuoviTutteLeRiproduzioniDelBrano(Brano o) {
		String query = "DELETE FROM riproduzione r where r.brano_id ="+o.getId()+" ";
		entityManager.createNativeQuery(query).executeUpdate();
	}

	@Override
	@Transactional
	public void rimuoviTutteLeRiproduzioneDeiBrani(List<Brano> brani) {
		String query = "DELETE FROM riproduzione r where 1=0 ";
		for(Brano branoItem : brani) {
			query += " or brano_id ="+ branoItem.getId()+" ";
		}
		entityManager.createNativeQuery(query).executeUpdate();
	}

	@Override
	@Transactional
	public void rimuoviBrani(List<Brano> brani) {
		rimuoviBraniDaTutteLePlaylist(brani);
		rimuoviTutteLeRiproduzioneDeiBrani(brani);
		
		branoRepository.deleteAll(brani);
	}

	@Override
	@Transactional
	public void inserisciNuoviBrani(List<Brano> brani) {
		branoRepository.saveAll(brani);
	}
	
	
}
