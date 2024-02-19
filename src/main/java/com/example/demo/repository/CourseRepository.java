package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Courses;

public interface CourseRepository extends JpaRepository<Courses, Integer> {

}
