package mdb.webapp.movieDbApplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

@Entity
@Table(name = "person")
public class Person implements Serializable {

	/**
	 * 
	 */
//	@Autowired
//	private PersonRepository personRepo;
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;

	@Column(unique=true)
	@NotNull
	@Size(min=2)
	String name;

	// I tried the Many to Many here - it causes data duplication and will add
	// an additional, unneeded table Person_Movies_In
	// @ManyToMany
	// private List<Movie> moviesIn;

	// String dob; // verify how to store dates with database used

	public Person() {
		// moviesIn = new ArrayList<Movie>();
	}

	public Person(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void merge(Person other) {
		if (other != null) {
			this.id=other.id;
		}
		if (other != null) {
			if (other.name != null){
				this.name = other.name;
			}
			
		}
	}
	// public List<Movie> getMoviesIn() {
	// return moviesIn;
	// }
	//
	// public void setMovieIn(List<Movie> moviesIn) {
	// this.moviesIn = moviesIn;
	// }
}