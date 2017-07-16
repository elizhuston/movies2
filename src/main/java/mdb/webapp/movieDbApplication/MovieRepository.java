package mdb.webapp.movieDbApplication;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRepository  extends JpaRepository<Movie, Integer>  {

	@Query("Select m from Movie m where UPPER(title) like UPPER(CONCAT('%',:title, '%'))")
	public List<Movie> findByTitleLike(@Param("title") String title);

//	@Query("Select m from Movie as m, LEFT JOIN m.actors as actors, LEFT JOIN m.directors as directors, LEFT join m.authors as authors "
//			+ "where UPPER(actors.name) like UPPER(CONCAT('%',:name, '%')) and "
//			+ "UPPER(directors.name) like UPPER(CONCAT('%',:name, '%')) and "
//			+ "UPPER(authors.name) like UPPER(CONCAT('%',:name, '%'))")	
//	public List<Movie> findByPersonLike(@Param("name") String name);
	
	@Query("Select m from Movie as m LEFT JOIN m.actors as actors where UPPER(actors.name) like UPPER(CONCAT('%',:name, '%'))")
	public List<Movie> findByActorLike(@Param("name") String name);

	@Query("Select m from Movie as m LEFT JOIN m.directors as directors where UPPER(directors.name) like UPPER(CONCAT('%',:name, '%'))")
	public List<Movie> findByDirectorLike(@Param("name") String name);

	@Query("Select m from Movie as m LEFT JOIN m.authors as authors where UPPER(authors.name) like UPPER(CONCAT('%',:name, '%'))")
	public List<Movie> findByAuthorLike(@Param("name") String name);
	
	@Query("Select m from Movie m where UPPER(genre) like UPPER(CONCAT('%',:genre, '%'))")
	public List<Movie> findByGenreLike(@Param("genre") String genre);

	@Query("Select m from Movie m where release_date like CONCAT('%',:year, '%')")
	public List<Movie> findByYearLike(@Param("year") String year);
	
	@Query("Select m from Movie m where id = :id")
	public Movie findByID(@Param("id") int i) ;

	//@Query("Select m from Movie m where UPPER(title) = UPPER(CONCAT('%', :title, '%')) ")
	public Movie findMovieByTitle(@Param("title") String title);

	@Query("Select m from Movie m")
	public List<Movie> findAllMovies();

}