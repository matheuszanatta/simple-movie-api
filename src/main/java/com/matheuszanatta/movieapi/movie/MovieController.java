package com.matheuszanatta.movieapi.movie;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {

	private final MovieRepository movieRepository;

	public MovieController(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	@PostMapping
	public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
		Movie newMovie = movieRepository.save(movie);
		return ResponseEntity.created(URI.create("/movies/" + newMovie.getId())).body(newMovie);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Movie> findMovieById(@PathVariable Integer id) {
		Optional<Movie> optionalMovie = movieRepository.findById(id);
		return optionalMovie.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	public List<Movie> findMovies() {
		return movieRepository.findAll();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Movie> updateMovie(@PathVariable Integer id, @RequestBody Movie movieUpdated) {
		Optional<Movie> optionalMovie = movieRepository.findById(id);
		if (optionalMovie.isPresent()) {
			Movie movie = optionalMovie.get();
			movie.setTitle(movieUpdated.getTitle());
			movie.setDirector(movieUpdated.getDirector());
			movie.setGenre(movieUpdated.getGenre());
			movie.setReleasedYear(movieUpdated.getReleasedYear());
			movie.setEvaluation(movieUpdated.getEvaluation());
			movieRepository.save(movie);
			return ResponseEntity.ok(movie);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMovie(@PathVariable Integer id) {
		Optional<Movie> optionalMovie = movieRepository.findById(id);
		if (optionalMovie.isPresent()) {
			movieRepository.delete(optionalMovie.get());
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/{id}/evaluate")
	public ResponseEntity<Movie> evaluateMovie(@PathVariable Integer id, @RequestBody Movie movieEvaluation) {
		Optional<Movie> optionalMovie = movieRepository.findById(id);
		if (optionalMovie.isPresent()) {
			Movie movie = optionalMovie.get();
			movie.setEvaluation(movieEvaluation.getEvaluation());
			movieRepository.save(movie);
			return ResponseEntity.ok(movie);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/not-evaluated")
	public ResponseEntity<Movie> findMovieNotEvaluated() {
		List<Movie> moviesNotEvaluated = movieRepository.findByEvaluationIsNull();
		if (!moviesNotEvaluated.isEmpty()) {
			Movie movie = moviesNotEvaluated.get(0);
			return ResponseEntity.ok(movie);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
