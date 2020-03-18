package it.spootifyrest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import it.spootifyrest.model.Brano;

public interface BranoRepository extends IBaseRepository<Brano> {

	@Query("Select distinct b from Brano b left join fetch b.album a where b.id = ?1 ORDER BY b.id")
	Optional<Brano> findByIdEager(Long id);
	
	@Query("SELECT b from Brano b left join fetch b.album left join fetch b.playlist where b.id = ?1")
	Optional<Brano> findByIdEagerIncludeAlbumAndPlaylists(Long id);

	@Query("SELECT DISTINCT b FROM Brano b LEFT JOIN FETCH b.playlist p WHERE p.id = ?1 ORDER BY b.id")
	List<Brano> findAllByPlaylistId(Long idPlaylist);

	@Query("SELECT b from Brano b left join b.riproduzioni r where r.playlist.id = ?1 and r.utente.id = ?2")
	Brano findLastBranoByPlaylistIdAndUserId(Long idPlaylist, Long idUtente);

	@Query("SELECT b from Brano b left join b.riproduzioni r where r.album.id = ?1 and r.utente.id = ?2")
	Brano findLastBranoByAlbumIdAndUserId(Long idAlbum, Long idUtente);
}
