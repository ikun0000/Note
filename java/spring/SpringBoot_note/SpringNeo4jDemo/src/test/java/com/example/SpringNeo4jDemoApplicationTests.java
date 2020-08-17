package com.example;

import com.example.entity.Actor;
import com.example.entity.Movie;
import com.example.repository.ActorRepository;
import com.example.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.List;

@SpringBootTest
class SpringNeo4jDemoApplicationTests {

	@Autowired
	private ActorRepository actorRepository;

	@Autowired
	private MovieRepository movieRepository;


	@Test
	void testConnect() {
		Assert.notNull(actorRepository, "ActorRepository is null");
		Assert.notNull(movieRepository, "MovieRepository is null");
	}

	@Test
	void testSave() {
		Actor actor = new Actor("aa");
		Actor actor1 = new Actor("bb");
		actorRepository.save(actor);
		actorRepository.save(actor1);
	}

	@Test
	void testSave2() {
		Movie movie = new Movie("aa-movie");
		Movie movie1 = new Movie("bb-movie");
		movieRepository.save(movie);
		movieRepository.save(movie1);
	}

	@Test
	void addRelationship() {
		Actor actor0 = actorRepository.findById(0L).get();
		Actor actor1 = actorRepository.findById(1L).get();
		Movie movie2 = movieRepository.findById(2L).get();
		Movie movie3 = movieRepository.findById(3L).get();

		actor0.actionIn(movie2, "导演");
		actor0.actionIn(movie3, "编剧");
		actor1.actionIn(movie3, "导演");
		actor1.actionIn(movie2, "编剧");

		actorRepository.save(actor0);
		actorRepository.save(actor1);
		movieRepository.save(movie2);
		movieRepository.save(movie3);
	}

	@Test
	void testFindNodeByRelation() {
		List<Actor> all = actorRepository.findAllByRolesMovieTitle("aa-movie");
		all.forEach(actor -> {
			System.out.println(actor.getId() + " : " + actor.getName());
		});

		System.out.println("--------------------------------------");
		List<Movie> all2 = movieRepository.findAllByRolesActorName("update_bb");
		all2.forEach(movie -> {
			System.out.println(movie.getId() + " : " + movie.getTitle());
		});
	}

	@Test
	void testUpdateNode() {
		Actor aa = actorRepository.findByName("aa").get(0);
		Actor bb = actorRepository.findByName("bb").get(0);
		aa.setName("update_aa");
		bb.setName("update_bb");

		actorRepository.save(aa);
		actorRepository.save(bb);
	}

	@Test
	void testPrintRelation() {
		Actor aa = actorRepository.findByName("update_aa").get(0);
		aa.getRoles().forEach(role -> {
			System.out.println(role.getActor() + " -> " + role.getMovie());
		});

		System.out.println("---------------------------------");

		Movie movie = movieRepository.findByTitle("bb-movie").get(0);
		movie.getRoles().forEach(role -> {
			System.out.println(role.getActor() + " -> " + role.getMovie());
		});
	}

	@Test
	void testFindAll() {
		Iterable<Actor> all = actorRepository.findAll();

		Iterator<Actor> iterator = all.iterator();

		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}

}
