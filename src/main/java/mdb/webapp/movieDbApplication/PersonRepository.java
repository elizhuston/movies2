package mdb.webapp.movieDbApplication;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Integer> {
	
	@Query("SELECT DISTINCT actors from Movie WHERE UPPER(name) like UPPER(CONCAT('%',:name, '%')))")
	public List<Person> findActorsByNameLike(@Param("name") String name);
	
	@Query("SELECT DISTINCT directors from Movie WHERE UPPER(name) like UPPER(CONCAT('%',:name, '%')))")
	public List<Person> findDirectorsByNameLike(@Param("name") String name);
	
	@Query("SELECT DISTINCT authors from Movie WHERE UPPER(name) like UPPER(CONCAT('%',:name, '%')))")
	public List<Person> findAuthorsByNameLike(@Param("name") String name);
	
	@Query("SELECT p from Person p WHERE UPPER(name) like UPPER(CONCAT('%',:name, '%')))")
	public List<Person> findPersonsByNameLike(@Param("name") String name);
	
	@Query("SELECT p from Person p WHERE UPPER(name) = UPPER(:name)")
	public Person findPersonByName(@Param("name") String name);

	
	@Query("SELECT DISTINCT directors from Movie")
	public List<Person> findAllDirectors();

	@Query("SELECT DISTINCT actors from Movie")
	public List<Person> findAllActors();
	
	@Query("SELECT DISTINCT authors from Movie")
	public List<Person> findAllAuthors();
}
