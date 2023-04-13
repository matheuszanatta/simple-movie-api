package com.matheuszanatta.movieapi.movie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRepository movieRepository;

    @AfterEach
    public void limparBanco() {
        movieRepository.deleteAll();
    }
    
    @Test
    public void mustCreateMovie() throws Exception {
        Movie movie = new Movie(null, "Star Wars: Episódio IV - Uma Nova Esperança", "George Lucas", "Ficção Científica", 1977, null);

        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value(movie.getTitle()))
                .andExpect(jsonPath("$.director").value(movie.getDirector()))
                .andExpect(jsonPath("$.genre").value(movie.getGenre()))
                .andExpect(jsonPath("$.releasedYear").value(movie.getReleasedYear()))
                .andExpect(jsonPath("$.evaluation").doesNotExist());

        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).hasSize(1);
        assertThat(movies.get(0).getId()).isNotNull();
        assertThat(movies.get(0).getTitle()).isEqualTo(movie.getTitle());
        assertThat(movies.get(0).getDirector()).isEqualTo(movie.getDirector());
        assertThat(movies.get(0).getGenre()).isEqualTo(movie.getGenre());
        assertThat(movies.get(0).getReleasedYear()).isEqualTo(movie.getReleasedYear());
        assertThat(movies.get(0).getEvaluation()).isNull();
    }
    
    @Test
    public void mustFindMovieById() throws Exception {
        Movie movie = new Movie(null, "Star Wars: Episódio IV - Uma Nova Esperança", "George Lucas", "Ficção Científica", 1977, null);
        Movie movieSaved = movieRepository.save(movie);

        mockMvc.perform(get("/movies/{id}", movieSaved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movieSaved.getId()))
                .andExpect(jsonPath("$.title").value(movieSaved.getTitle()))
                .andExpect(jsonPath("$.director").value(movieSaved.getDirector()))
                .andExpect(jsonPath("$.genre").value(movieSaved.getGenre()))
                .andExpect(jsonPath("$.releasedYear").value(movieSaved.getReleasedYear()))
                .andExpect(jsonPath("$.avaliacao").doesNotExist());
    }
    
    @Test
    public void mustReturn404WhenFindByIdThatDoesNotExist() throws Exception {
        mockMvc.perform(get("/movies/{id}", 1))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void mustFindAllMovies() throws Exception {
        Movie movie1 = new Movie(null, "Star Wars: Episódio IV - Uma Nova Esperança", "George Lucas", "Ficção Científica", 1977, null);
        Movie movie2 = new Movie(null, "Matrix", "Lana Wachowski, Lilly Wachowski", "Ficção Científica", 1999, null);
        movieRepository.saveAll(Arrays.asList(movie1, movie2));

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value(movie1.getTitle()))
                .andExpect(jsonPath("$[0].director").value(movie1.getDirector()))
                .andExpect(jsonPath("$[0].genre").value(movie1.getGenre()))
                .andExpect(jsonPath("$[0].releasedYear").value(movie1.getReleasedYear()))
                .andExpect(jsonPath("$[0].avaliacao").doesNotExist())
                .andExpect(jsonPath("$[1].title").value(movie2.getTitle()))
                .andExpect(jsonPath("$[1].director").value(movie2.getDirector()))
                .andExpect(jsonPath("$[1].genre").value(movie2.getGenre()))
                .andExpect(jsonPath("$[1].releasedYear").value(movie2.getReleasedYear()))
                .andExpect(jsonPath("$[1].avaliacao").doesNotExist());
    }
    
    @Test
    public void deveAtualizarMovie() throws Exception {
        Movie movie = new Movie(null, "Star Wars: Episódio IV - Uma Nova Esperança", "George Lucas", "Ficção Científica", 1977, null);
        Movie movieSaved = movieRepository.save(movie);
        movieSaved.setTitle("Star Wars: Episódio IV - A New Hope");

        mockMvc.perform(put("/movies/{id}", movieSaved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieSaved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movieSaved.getId()))
                .andExpect(jsonPath("$.title").value(movieSaved.getTitle()))
                .andExpect(jsonPath("$.director").value(movieSaved.getDirector()))
                .andExpect(jsonPath("$.genre").value(movieSaved.getGenre()))
                .andExpect(jsonPath("$.releasedYear").value(movieSaved.getReleasedYear()))
                .andExpect(jsonPath("$.avaliacao").doesNotExist());

        Movie movieUpdated = movieRepository.findById(movieSaved.getId()).orElseThrow();
        assertThat(movieUpdated.getTitle()).isEqualTo(movieSaved.getTitle());
    }

    @Test
    public void mustDeleteMovie() throws Exception {
        Movie movie = new Movie(null, "Star Wars: Episódio IV - Uma Nova Esperança", "George Lucas", "Ficção Científica", 1977, null);
        Movie movieSaved = movieRepository.save(movie);

        mockMvc.perform(delete("/movies/{id}", movieSaved.getId()))
                .andExpect(status().isNoContent());

        assertThat(movieRepository.findById(movieSaved.getId())).isEmpty();
    }
    
    @Test
    public void mustEvaluateMovie() throws Exception {
        Movie movie = new Movie(null, "Star Wars: Episódio IV - Uma Nova Esperança", "George Lucas", "Ficção Científica", 1977, null);
        Movie movieSaved = movieRepository.save(movie);
        Movie movieEvaluation = new Movie();
        movieEvaluation.setEvaluation(10);

        mockMvc.perform(post("/movies/{id}/evaluate", movieSaved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieEvaluation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movieSaved.getId()))
                .andExpect(jsonPath("$.title").value(movieSaved.getTitle()))
                .andExpect(jsonPath("$.director").value(movieSaved.getDirector()))
                .andExpect(jsonPath("$.genre").value(movieSaved.getGenre()))
                .andExpect(jsonPath("$.releasedYear").value(movieSaved.getReleasedYear()))
                .andExpect(jsonPath("$.evaluation").value(movieEvaluation.getEvaluation()));
        
        Movie movieEvaluated = movieRepository.findById(movieSaved.getId()).orElseThrow();
        assertThat(movieEvaluated.getEvaluation()).isEqualTo(movieEvaluation.getEvaluation());
    }
    
    @Test
    public void mustReturnMovieNotEvaluated() throws Exception {
        Movie movieEvaluated = new Movie(null, "Star Wars: Episódio IV - Uma Nova Esperança", "George Lucas", "Ficção Científica", 1977, 10);
        Movie movieNotEvaluated = new Movie(null, "Matrix", "Lana Wachowski, Lilly Wachowski", "Ficção Científica", 1999, null);
        movieRepository.saveAll(Arrays.asList(movieEvaluated, movieNotEvaluated));

        mockMvc.perform(get("/movies/not-evaluated"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movieNotEvaluated.getId()))
                .andExpect(jsonPath("$.title").value(movieNotEvaluated.getTitle()))
                .andExpect(jsonPath("$.director").value(movieNotEvaluated.getDirector()))
                .andExpect(jsonPath("$.genre").value(movieNotEvaluated.getGenre()))
                .andExpect(jsonPath("$.releasedYear").value(movieNotEvaluated.getReleasedYear()))
                .andExpect(jsonPath("$.evaluated").doesNotExist());
    }
}