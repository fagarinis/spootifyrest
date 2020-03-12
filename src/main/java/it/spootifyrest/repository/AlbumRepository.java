package it.spootifyrest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import it.spootifyrest.model.Album;

public interface AlbumRepository extends IBaseRepository<Album> {

	@Query("select distinct a from Album a left join fetch a.brani left join fetch a.artista where a.id = ?1")
	Optional<Album> findByIdEager(Long id);

}
