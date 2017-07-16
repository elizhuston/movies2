package mdb.webapp.movieDbApplication;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.Controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@SpringBootApplication
@RestController

@Api(value = "Movies ", description = "Movies API")

public class MdbAppJsonController implements Controller {
	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private PersonRepository personRepository;

	// returns person object matching name
	@RequestMapping(path = "/api/person/{name}", method = RequestMethod.GET)

    @ApiOperation(value = "Get Person by Name", notes = "Returns person object matching name")

	public ResponseEntity<List<Person>> findPerson(@PathVariable(name = "name", required = true) String name) {
		System.out.println("/api/person GET " + name);
		List<Person> people = personRepository.findPersonsByNameLike(name);
		return new ResponseEntity<List<Person>>(people, HttpStatus.OK);
	}

	// adds a new person - name must be in request body and must not already
	// exist
	@RequestMapping(path = "/api/person", method = RequestMethod.POST)
	@ApiOperation(value = "Add a Person", notes = "adds a new person - name must be in request body and must not already\n" + " exist")
	public ResponseEntity<Person> createPerson(@Validated  @RequestBody Person p) {
		System.out.println("/api/person POST " + p.name);
		if (p.getName() == null) {
			return new ResponseEntity<Person>(HttpStatus.BAD_REQUEST);
		}
		if (personRepository.findPersonByName(p.getName()) != null) {
			return new ResponseEntity<Person>(HttpStatus.BAD_REQUEST);
		}

		personRepository.save(p);
		return new ResponseEntity<Person>(p, HttpStatus.CREATED);
	}

	// deletes an existing person whose ID matches parameter in URL
	
	@RequestMapping(path = "/api/person/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete Person", notes = "Deletes an existing person whose ID matches parameter in URL")
	public ResponseEntity<String> deletePerson(@PathVariable(name = "id", required = true) Integer id) {
		System.out.println("/api/person/{id} DELETE " + id);
		if (id == null) {
			return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
		}
		personRepository.delete(id);
		return new ResponseEntity<String>("Person deleted", HttpStatus.OK);
	}

	// update/merge for an existing person whose ID matches "id" in json body of
	// request
	@RequestMapping(path = "/api/person", method = RequestMethod.PUT)
	@ApiOperation(value = "Updated Person", notes = "Update/merge for an existing person whose ID matches \"id\" in json body of\n" + " request\n")
	public ResponseEntity<Person> updatePerson(@RequestBody Person p) {
		System.out.println("/api/person PUT ");
		if (p.getId() == 0) {
			return new ResponseEntity<Person>(HttpStatus.BAD_REQUEST);
		}
		Person existing = personRepository.findOne(p.getId());
		existing.merge(p);
		personRepository.save(existing);
		return new ResponseEntity<Person>(p, HttpStatus.OK);
	}


	// creates a new movie - at least title must be provided and must not
	// already exist
	@RequestMapping(path = "/api/movie", method = RequestMethod.POST)
	@ApiOperation(value = "Create New Movie", notes = "Creates a new movie - at least title must be provided and must not\n" + 
		"already exist")
	public ResponseEntity<Movie> createMovie(@Validated @RequestBody Movie m) {
		String title = m.getTitle();
		if (title == null) {
			System.out.println("/api/movie POST title is null");
			return new ResponseEntity<Movie>(HttpStatus.BAD_REQUEST);
		}
		System.out.println("/api/movie POST " + title);
		Movie test = movieRepository.findMovieByTitle(title);
		if (test != null) {
			System.out.println("/api/movie title of movie already exists " + title);
			return new ResponseEntity<Movie>(HttpStatus.BAD_REQUEST);
		}
		List<Person> directors = m.getDirectors();
		if (directors != null) {
			System.out.println("Directors not null");
			List<Person> newDirectors = new ArrayList<Person>();
			for (Person director : directors) {
				Person existing = personRepository.findPersonByName(director.name);
				director.merge(existing);
				director = personRepository.save(director);
				newDirectors.add(director);
			}

			m.setDirectors(newDirectors);
		} else {
			System.out.println("Directors NULL");
		}

		List<Person> actors = m.getActors();

		if (actors != null) {
			System.out.println("Actors not null");
			List<Person> newActors = new ArrayList<Person>();
			for (Person actor : actors) {
				Person existing = personRepository.findPersonByName(actor.name);
				actor.merge(existing);
				actor = personRepository.save(actor);
				newActors.add(actor);
			}
			m.setActors(newActors);
		} else {
			System.out.println("Actors NULL");
		}

		List<Person> authors = m.getAuthors();

		if (authors != null) {
			System.out.println("Authors not null");
			List<Person> newAuthors = new ArrayList<Person>();
			for (Person author : authors) {
				Person existing = personRepository.findPersonByName(author.name);
				author.merge(existing);
				author = personRepository.save(author);
				newAuthors.add(author);
			}

			m.setActors(newAuthors);
		} else {
			System.out.println("Authors NULL");
		}


		movieRepository.save(m);
		return new ResponseEntity<Movie>(m, HttpStatus.CREATED);
	}

	// deletes an existing movie whose ID matches the URL parameter
	@RequestMapping(path = "/api/movie/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete Movie By ID", notes = "Deletes an existing movie whose ID matches the URL parameter")
	public ResponseEntity<String> deleteMovie(@PathVariable(name = "id", required = true) Integer id) {
		System.out.println("/api/movie/{id} DELETE " + id);
		;
		if (id == null) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		if (movieRepository.findByID(id) == null) {
			System.out.println("/api/movie/{id} DELETE " + id + "  movie does not exist");
			return new ResponseEntity<String>("Movie does not exist", HttpStatus.BAD_REQUEST);
		}

		movieRepository.delete(id);
		return new ResponseEntity<String>("Movie deleted ", HttpStatus.OK);
	}

	// updates/merges an existing movie where id matches the id contained in the
	// json body of the request
	@RequestMapping(path = "/api/movie", method = RequestMethod.PUT)
	@ApiOperation(value = "Update Movie", notes = "Updates/merges an existing movie where id matches the id contained in the\n" + 
			" json body of the request")
	public ResponseEntity<Movie> updateMovie(@RequestBody Movie m) {
		System.out.println(" api/movie  PUT  id is" + m.getId());
		if (m.getId() == 0) {
			return new ResponseEntity<Movie>(HttpStatus.BAD_REQUEST);
		}

		Movie existing = movieRepository.findByID(m.getId());
        if (existing == null){
        	return new ResponseEntity<Movie>(HttpStatus.BAD_REQUEST);
        }
		if (m.getDirectors() != null) {
			System.out.println("Directors included in request");
			List<Person> directors = existing.getDirectors();
			List<Person> newDirectors = new ArrayList<Person>();
			if (directors.size() != 0) {
				System.out.println("existing directors for movie found");
				for (Person director : directors) {
					Person otherDirector = personRepository.findPersonByName(director.name);
					director.merge(otherDirector);
					director = personRepository.save(director);
					newDirectors.add(director);
				}

				m.setDirectors(newDirectors);
			} else {
				System.out.println("existing directors not found for movie");
				for (Person director : m.getDirectors()) {
					Person otherDirector = personRepository.findPersonByName(director.name);
					director.merge(otherDirector);
					director = personRepository.save(director);
					newDirectors.add(director);
				}
			}
			m.setDirectors(newDirectors);
		}

		if (m.getActors() != null) {
			System.out.println("Actors included in request");
			List<Person> Actors = existing.getActors();
			List<Person> newActors = new ArrayList<Person>();
			if (Actors.size() != 0) {
				System.out.println("existing Actors for movie found");
				for (Person Actor : Actors) {
					Person otherActor = personRepository.findPersonByName(Actor.name);
					Actor.merge(otherActor);
					Actor = personRepository.save(Actor);
					newActors.add(Actor);
				}

				m.setActors(newActors);
			} else {
				System.out.println("existing Actors not found for movie");
				for (Person Actor : m.getActors()) {
					Person otherActor = personRepository.findPersonByName(Actor.name);
					Actor.merge(otherActor);
					Actor = personRepository.save(Actor);
					newActors.add(Actor);
				}
			}
			m.setActors(newActors);
		}

		if (m.getAuthors() != null) {
			System.out.println("authors included in request");
			List<Person> authors = existing.getAuthors();
			List<Person> newauthors = new ArrayList<Person>();
			if (authors.size() != 0) {
				System.out.println("existing authors for movie found");
				for (Person author : authors) {
					Person otherauthor = personRepository.findPersonByName(author.name);
					author.merge(otherauthor);
					author = personRepository.save(author);
					newauthors.add(author);
				}

				m.setAuthors(newauthors);
			} else {
				System.out.println("existing authors not found for movie");
				for (Person author : m.getAuthors()) {
					Person otherauthor = personRepository.findPersonByName(author.name);
					author.merge(otherauthor);
					author = personRepository.save(author);
					newauthors.add(author);
				}
			}
			m.setAuthors(newauthors);
		}
		System.out.println("before merge of request with existing");
		existing.merge(m);
		movieRepository.save(existing);
		return new ResponseEntity<Movie>(m, HttpStatus.OK);
	}


	// returns movie object(s) matching whose title matches or partially matches
	// title in parameter, case insensitive
	@RequestMapping(path = "/api/movie/title/{title}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Movie By Title", notes = "Returns movie object(s) matching whose title matches or partially matches\n" + 
			" title in parameter, case insensitive")
	public ResponseEntity<List<Movie>> findByTitleLike(@PathVariable(name = "title", required = true) String title) {
		System.out.println("/api/movie/title/{title} is " + title);
		if (title == null) {

			return new ResponseEntity<List<Movie>>(HttpStatus.BAD_REQUEST);
		}
		List<Movie> movies = movieRepository.findByTitleLike(title);
		System.out.println("Size of movies is" + movies.size());
		return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);

	}

	// returns an array of movie objects where actors name at
	// least partially match name parameter
	@RequestMapping(path = "/api/movie/actor/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Actor By Name", notes = "Returns an array of movie objects where actors name at\n" + 
			" least partially match name parameter")
	public ResponseEntity<List<Movie>> findByActorLike(@PathVariable(name = "name", required = true) String name) {
		System.out.println("/api/movie/actor/{name} name is " + name);
		if (name == null) {
			return new ResponseEntity<List<Movie>>(HttpStatus.BAD_REQUEST);
		}
		List<Movie> movies = movieRepository.findByActorLike(name);
		System.out.println("Size of movies is" + movies.size());
		return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);

	}

	// returns an array of movie objects where directors name at
	// least partially match name parameter
	@RequestMapping(path = "/api/movie/director/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Director By Name", notes = "Returns an array of movie objects where directors name at\n" + 
			" least partially match name parameter")
	public ResponseEntity<List<Movie>> findByDirectorLike(@PathVariable(name = "name", required = true) String name) {
		System.out.println("/api/movie/director/{name} name is " + name);
		if (name == null) {
			return new ResponseEntity<List<Movie>>(HttpStatus.BAD_REQUEST);
		}
		List<Movie> movies = movieRepository.findByDirectorLike(name);
		System.out.println("Size of movies is" + movies.size());
		return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);

	}

	// returns an array of movie objects where authors name at
	// least partially match name parameter
	@RequestMapping(path = "/api/movie/author/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Author By Name", notes = "Returns an array of movie objects where authors name at\n" + 
			" least partially match name parameter")
	public ResponseEntity<List<Movie>> findByAuthorLike(@PathVariable(name = "name", required = true) String name) {
		System.out.println("/api/movie/director/{name} name is " + name);
		if (name == null) {
			return new ResponseEntity<List<Movie>>(HttpStatus.BAD_REQUEST);
		}
		List<Movie> movies = movieRepository.findByAuthorLike(name);
		System.out.println("Size of movies is" + movies.size());
		return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);

	}

	// returns an array of movie objects whose genre matches or partially
	// matches URL parameter, case insensitive
	@RequestMapping(path = "/api/movie/genre/{genre}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Movie By Genre", notes = "Returns an array of movie objects whose genre matches or partially\n" + 
			" matches URL parameter, case insensitive")
	public ResponseEntity<List<Movie>> findByGenreLike(@PathVariable(name = "genre", required = true) String genre) {
		System.out.println("/api/movie/genre/ is " + genre);
		if (genre == null) {
			return new ResponseEntity<List<Movie>>(HttpStatus.BAD_REQUEST);
		}
		List<Movie> movies = movieRepository.findByGenreLike(genre);
		System.out.println("Size of movies is" + movies.size());
		return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);

	}

	// returns an array of movie objects whose year matches or partially matches
	// URL parameter, case insensitive
	@RequestMapping(path = "/api/movie/year/{year}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Movie By Release Year", notes = "Returns an array of movie objects whose year matches or partially matches\n" + 
			"	// URL parameter, case insensitive")
	public ResponseEntity<List<Movie>> findByYearLike(@PathVariable(name = "year", required = true) String year) {
		System.out.println("/api/movie/year/ is " + year);
		if (year == null) {
			return new ResponseEntity<List<Movie>>(HttpStatus.BAD_REQUEST);
		}
		List<Movie> movies = movieRepository.findByYearLike(year);
		System.out.println("Size of movies is" + movies.size());
		return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);

	}

	// returns an array of Person objects who are actors and whose name matches
	// or partially matches URL parameter, case insensitive
	@RequestMapping(path = "/api/actor/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Actor By Name", notes = "Returns an array of Person objects who are actors and whose name matches\n" + 
			"	or partially matches URL parameter, case insensitive")
	public ResponseEntity<List<Person>> findActorsByNameLike(
			@PathVariable(name = "name", required = true) String name) {
		System.out.println("/api/actor/name/ is " + name);
		if (name == null) {
			return new ResponseEntity<List<Person>>(HttpStatus.BAD_REQUEST);
		}
		List<Person> actors = personRepository.findActorsByNameLike(name);
		System.out.println("Size of actors is" + actors.size());
		return new ResponseEntity<List<Person>>(actors, HttpStatus.OK);

	}

	// returns an array of Person objects who are directors and whose name
	// matches or partially matches URL parameter, case insensitive
	@RequestMapping(path = "/api/director/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Directory By Name", notes = "Returns an array of Person objects who are directors and whose name\n" + 
			" matches or partially matches URL parameter, case insensitive")
	public ResponseEntity<List<Person>> findDirectorsByNameLike(
			@PathVariable(name = "name", required = true) String name) {
		System.out.println("/api/director/name/ is " + name);
		if (name == null) {
			return new ResponseEntity<List<Person>>(HttpStatus.BAD_REQUEST);
		}
		List<Person> directors = personRepository.findDirectorsByNameLike(name);
		System.out.println("Size of directors is" + directors.size());
		return new ResponseEntity<List<Person>>(directors, HttpStatus.OK);

	}

	// returns an array of Person objects who are authors and whose name matches
	// or partially matches URL parameter, case insensitive
	@RequestMapping(path = "/api/author/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Author By Name", notes = "Returns an array of Person objects who are authors and whose name matches\n" + 
			"	// or partially matches URL parameter, case insensitive")
	public ResponseEntity<List<Person>> findAuthorsByNameLike(
			@PathVariable(name = "name", required = true) String name) {
		System.out.println("/api/author/name/ is " + name);
		if (name == null) {
			return new ResponseEntity<List<Person>>(HttpStatus.BAD_REQUEST);
		}
		List<Person> authors = personRepository.findAuthorsByNameLike(name);
		System.out.println("Size of authors is" + authors.size());
		return new ResponseEntity<List<Person>>(authors, HttpStatus.OK);

	}

	// returns an array of Person objects who are directors
	@RequestMapping(path = "/api/directors", method = RequestMethod.GET)
	@ApiOperation(value = "Get All Directors", notes = "Returns an array of Person objects who are directors")
	public ResponseEntity<List<Person>> findAllDirectors() {
		System.out.println("/api/directors GET");

		List<Person> directors = personRepository.findAllDirectors();
		System.out.println("Size of directors is" + directors.size());
		return new ResponseEntity<List<Person>>(directors, HttpStatus.OK);

	}

	// returns an array of Person objects who are actors
	@RequestMapping(path = "/api/actors", method = RequestMethod.GET)
	@ApiOperation(value = "Get All Actors", notes = "Returns an array of Person objects who are actors")
	public ResponseEntity<List<Person>> findAllActors() {
		System.out.println("/api/actors GET");

		List<Person> actors = personRepository.findAllActors();
		System.out.println("Size of actors is" + actors.size());
		return new ResponseEntity<List<Person>>(actors, HttpStatus.OK);

	}

	// returns an array of Person objects who are authors
	@RequestMapping(path = "/api/authors", method = RequestMethod.GET)
	@ApiOperation(value = "Get All Authors", notes = "Returns news items")
	public ResponseEntity<List<Person>> findAllAuthors() {
		System.out.println("/api/authors GET");

		List<Person> authors = personRepository.findAllAuthors();
		System.out.println("Size of authors is" + authors.size());
		return new ResponseEntity<List<Person>>(authors, HttpStatus.OK);

	}
	
	// returns an array of all movie objects 
		@RequestMapping(path = "/api/movies", method = RequestMethod.GET)
		@ApiOperation(value = "Get All Movies", notes = "Returns an array of all movie objects")
		public ResponseEntity<List<Movie>> findAllMovies() {
			System.out.println("/api/movies GET " );
			
			List<Movie> movies = movieRepository.findAllMovies();
			System.out.println("Size of movies is" + movies.size());
			return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);

		}

		@Override
		public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
			 // process the request...
	        ModelAndView mav = new ModelAndView();
	        // add data as necessary to the model...
	        return mav;
	        // notice that no View or logical view name has been set
		}
		

}