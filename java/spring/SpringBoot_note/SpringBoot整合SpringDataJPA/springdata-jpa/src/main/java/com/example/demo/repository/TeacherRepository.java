package com.example.demo.repository;

import com.example.demo.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {

    List<Teacher> findByNameStartingWithAndAgeLessThan(String start, Integer age);

    List<Teacher> findByNameEndingWithAndAgeLessThan(String start, Integer age);

    List<Teacher> findByNameInOrAgeGreaterThan(List<String> names, Integer age);

    @Query("SELECT o FROM Teacher o WHERE id = (SELECT max(id) FROM Teacher t1)")
    Teacher getTeacherByMaxId();

    @Query("SELECT o FROM Teacher o WHERE o.name = ?1 AND o.age = ?2")
    List<Teacher> queryParam1(String name, Integer age);

    @Query("SELECT o from Teacher o WHERE o.name = :name AND o.age = :age")
    List<Teacher> queryParam2(@Param("name") String name, @Param("age") Integer age);

    @Query(nativeQuery = true, value = "SELECT * FROM teacher")
    List<Teacher> getAll();

    @Modifying
    @Transactional
    @Query("UPDATE Teacher o SET o.name = ?2 WHERE o.id = ?1")
    void update(Long id, String name);

    /******************************************************************/

    Page<Teacher> findByNameStartingWith(String name, Pageable pageable);

    List<Teacher> findByNameStartingWith(String name, Sort sort);
}
