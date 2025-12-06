package ru.ifmo.is.mfl.tags;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
  Optional<Tag> findByName(String name);
  List<Tag> findByNameContainingIgnoreCase(String name);
}
