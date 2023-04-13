package com.matheuszanatta.movieapi.movie;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String title;

	private String director;

	private String genre;

	private Integer releasedYear;

	private Integer evaluation;

	public Movie() {
	}

	public Movie(Integer id, String title, String director, String genre, Integer releasedYear, Integer evaluation) {
		super();
		this.id = id;
		this.title = title;
		this.director = director;
		this.genre = genre;
		this.releasedYear = releasedYear;
		this.evaluation = evaluation;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Integer getReleasedYear() {
		return releasedYear;
	}

	public void setReleasedYear(Integer releasedYear) {
		this.releasedYear = releasedYear;
	}

	public Integer getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Integer evaluation) {
		this.evaluation = evaluation;
	}

}
