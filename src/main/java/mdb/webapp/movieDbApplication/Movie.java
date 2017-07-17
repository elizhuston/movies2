
package mdb.webapp.movieDbApplication;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.logging.Message;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Persistable;

import java.util.List;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "movie")

public class Movie implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;

	@Column(unique = true)
	@NotNull(message = "Title is required to add a movie.")
	@Size(min = 1)
	private String title;

	@NotNull(message = "Released Year is required to add a movie.")
	@Min(1900)
	@Max(2018)
	private String releaseDate;

	private String genre;

	// I tried the mappedBy here - it only works with the first many-to-many
	// relationship to Person and we have 3
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "directorsId")
	private List<Person> directors;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "actorsId")
	private List<Person> actors;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "authorsId")
	private List<Person> authors;

	public Movie() {

	}

	public Movie(String title, String releaseDate, String genre, List<Person> directors, List<Person> actors,
			List<Person> authors) {
		this.title = title;
		this.releaseDate = releaseDate;
		this.genre = genre;
		this.directors = directors;
		this.actors = actors;
		this.authors = authors;
	}

	public Movie(String title, String releaseDate, String genre) {
		this.title = title;
		this.releaseDate = releaseDate;
		this.genre = genre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Person> getDirectors() {
		return directors;
	}

	public void setDirectors(List<Person> directors) {
		this.directors = directors;
	}

	public List<Person> getActors() {
		return actors;
	}

	public void setActors(List<Person> actors) {
		this.actors = actors;
	}

	public List<Person> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Person> authors) {
		this.authors = authors;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;

	}

	public void merge(Movie other) {
		if (other.title != null) {
			this.title = other.title;
		}
		if (other.releaseDate != null) {
			this.releaseDate = other.releaseDate;
		}
		if (other.genre != null) {
			this.genre = other.genre;
		}
		if (other.directors != null) {
			this.directors = other.directors;
		}
		if (other.actors != null) {
			this.actors = other.actors;
		}
		if (other.authors != null) {
			this.authors = other.authors;
		}
	}
}
