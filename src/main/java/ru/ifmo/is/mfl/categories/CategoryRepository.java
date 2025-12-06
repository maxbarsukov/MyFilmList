package ru.ifmo.is.mfl.categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
  Optional<Category> findByName(String name);
  List<Category> findByNameContainingIgnoreCase(String name);
}
