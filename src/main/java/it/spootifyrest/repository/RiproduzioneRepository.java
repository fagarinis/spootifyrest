package it.spootifyrest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import it.spootifyrest.model.Riproduzione;

public interface RiproduzioneRepository extends IBaseRepository<Riproduzione> {

	@Query("SELECT r from Riproduzione r left join fetch r.playlist p left join fetch p.brani b join fetch b.album alb join fetch alb.artista where r.playlist.id = ?1 and r.utente.id = ?2 and r.album.id = null")
	Optional<Riproduzione> findRiproduzioneByPlaylistIdAndUserId(Long idPlaylist, Long idUtente);

	@Query("SELECT r from Riproduzione r left join fetch r.album a left join fetch a.brani b join fetch b.album alb join fetch alb.artista where r.album.id = ?1 and r.utente.id = ?2 and r.playlist.id = null")
	Optional<Riproduzione> findRiproduzioneByAlbumIdAndUserId(Long idAlbum, Long idUtente);

}
