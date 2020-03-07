package com.example.demo;

import com.example.demo.entity.Teacher;
import com.example.demo.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private TeacherRepository teacherRepository;

	@Test
	void testFindByNameStartingWithAndAgeLessThan() {
		List<Teacher> test = teacherRepository.findByNameStartingWithAndAgeLessThan("test", 22);

		for (Teacher teacher : test) {
			Assert.isTrue(teacher.getName().startsWith("test"), "not test start");
			Assert.isTrue(teacher.getAge() < 22, "age gt 22");
		}
	}

	@Test
	void testFindByNameEndingWithAndAgeLessThan() {
		List<Teacher> test = teacherRepository.findByNameEndingWithAndAgeLessThan("16", 23);
		Assert.isTrue(test.size() == 1, "check you test data");
	}

	@Test
	void testFindByNameInOrAgeGreaterThan() {
		List<Teacher> test = teacherRepository.findByNameInOrAgeGreaterThan(
				Arrays.asList("test1", "test2", "test16"), 22);

		test.forEach((obj) -> {
			System.out.println(obj);
		});
	}

	@Test
	void testGetTeacherByMaxId() {
		Teacher teacher = teacherRepository.getTeacherByMaxId();
		Assert.notNull(teacher, "can not find");
		Assert.isTrue(teacher.getId() == 8, "false");
	}

	@Test
	void testQueryParam1_2() {
		List<Teacher> test = teacherRepository.queryParam1("test1", 20);
		Assert.isTrue(test.size() == 1, "check test data");
		test = teacherRepository.queryParam2("test1", 20);
		Assert.isTrue(test.size() == 1, "check test data");
	}

	@Test
	void testGetAll() {
		List<Teacher> test = teacherRepository.getAll();
		Assert.isTrue(test.size() == teacherRepository.count(), "select fail");
	}

	@Test
	void testUpdate() {
		teacherRepository.update((long) 8, "lisi");
	}

	@Test
	public void testSort() {
		List<Teacher> all = teacherRepository.findAll(Sort.by("id"));
		all.forEach((te) -> {
			System.out.println(te.getAge());
		});
	}

	@Test
	public void testSortDirection() {
		List<Teacher> all = teacherRepository.findAll(Sort.by(Sort.Direction.DESC, "age"));
		all.forEach((te) -> {
			System.out.println(te.getAge());
		});
		System.out.println("=============================");
		all = teacherRepository.findAll(Sort.by(Sort.Direction.ASC, "age"));
		all.forEach((te) -> {
			System.out.println(te.getAge());
		});
	}

	@Test
	public void testPageable() {
		Page<Teacher> all = teacherRepository.findAll(PageRequest.of(0, 3));
		System.out.println(all.getTotalElements());
		System.out.println(all.getTotalPages());
		List<Teacher> teachers = all.getContent();

		teachers.forEach((te) -> {
			System.out.println(te.getName());
		});
	}

	@Test
	public void testJpaSpecificationExecutor() {
		Pageable pageable = PageRequest.of(0, 3);
		Specification<Teacher> specification = new Specification<Teacher>() {
			@Override
			public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 注意是哪个包 javax.persistence.criteria.Path
				Path name = root.get("name");
				return criteriaBuilder.like(name, "test%");
			}
		};

		Page<Teacher> page = teacherRepository.findAll(specification, pageable);
		page.getContent().forEach(th -> System.out.println(th.getName()));
	}

	@Test
	public void testFindByNameStartingWithPageable() {
		Page<Teacher> page = teacherRepository.findByNameStartingWith("test",
				PageRequest.of(0, 10));

		System.out.println("total page: " + page.getTotalPages());
		System.out.println("total elements: " + page.getTotalElements());
		page.getContent().forEach( th -> System.out.println(th.getName()));
	}

	@Test
	public void testFindByNameStartingWithSort() {
		List<Teacher> teachers = teacherRepository.findByNameStartingWith("test",
				Sort.by(Sort.Direction.DESC, "id"));

		teachers.forEach(th -> System.out.println(th.getId() + "   " + th.getName()));

		System.out.println("=======================================");
		teachers = teacherRepository.findByNameStartingWith("test",
				Sort.by(Sort.Direction.ASC, "id"));

		teachers.forEach(th -> System.out.println(th.getId() + "   " + th.getName()));
	}

}
