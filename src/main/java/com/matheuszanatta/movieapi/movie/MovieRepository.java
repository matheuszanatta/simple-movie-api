package com.matheuszanatta.movieapi.movie;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

	Optional<Movie> findByTitle(String title);

	List<Movie> findByGenre(String genre);

	List<Movie> findByEvaluationIsNull();

}
