package ru.ifmo.is.mfl.countries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
  Optional<Country> findByName(String name);
  List<Country> findByNameContainingIgnoreCase(String name);
}
