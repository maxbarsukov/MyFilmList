-- Create sequences
CREATE SEQUENCE tags_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE genres_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE people_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE categories_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE countries_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE movie_people_id_seq START WITH 1 INCREMENT BY 1;

-- Create tables
CREATE TABLE tags (
  id   INTEGER      NOT NULL DEFAULT nextval('tags_id_seq'),
  name VARCHAR(255) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

CREATE TABLE genres (
  id   INTEGER      NOT NULL DEFAULT nextval('genres_id_seq'),
  name VARCHAR(255) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

CREATE TABLE people (
  id   INTEGER      NOT NULL DEFAULT nextval('people_id_seq'),
  name VARCHAR(255) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

CREATE TABLE categories (
  id   INTEGER      NOT NULL DEFAULT nextval('categories_id_seq'),
  name VARCHAR(255) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

CREATE TABLE countries (
  id   INTEGER      NOT NULL DEFAULT nextval('countries_id_seq'),
  name VARCHAR(255) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

-- Create join tables
CREATE TABLE movie_tags (
  movie_id INTEGER NOT NULL,
  tag_id   INTEGER NOT NULL,
  PRIMARY KEY (movie_id, tag_id),
  FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE,
  FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
);

CREATE TABLE movie_genres (
  movie_id INTEGER NOT NULL,
  genre_id INTEGER NOT NULL,
  PRIMARY KEY (movie_id, genre_id),
  FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE,
  FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE CASCADE
);

CREATE TABLE movie_categories (
  movie_id    INTEGER NOT NULL,
  category_id INTEGER NOT NULL,
  PRIMARY KEY (movie_id, category_id),
  FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE,
  FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);

CREATE TABLE movie_countries (
  movie_id   INTEGER NOT NULL,
  country_id INTEGER NOT NULL,
  PRIMARY KEY (movie_id, country_id),
  FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE,
  FOREIGN KEY (country_id) REFERENCES countries (id) ON DELETE CASCADE
);

CREATE TABLE movie_people (
  id        INTEGER     NOT NULL DEFAULT nextval('movie_people_id_seq'),
  movie_id  INTEGER     NOT NULL,
  person_id INTEGER     NOT NULL,
  role      VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE,
  FOREIGN KEY (person_id) REFERENCES people (id) ON DELETE CASCADE
);

-- Migrate Tags
INSERT INTO tags (name)
SELECT DISTINCT trim(unnest(string_to_array(tags, ',')))
FROM movies
WHERE tags IS NOT NULL
  AND tags != ''
ON CONFLICT (name) DO NOTHING;

INSERT INTO movie_tags (movie_id, tag_id)
SELECT DISTINCT m.id, t.id
FROM movies m
       CROSS JOIN LATERAL unnest(string_to_array(m.tags, ',')) AS tag_name
       JOIN tags t ON t.name = trim(tag_name);

-- Migrate Genres
INSERT INTO genres (name)
SELECT DISTINCT trim(unnest(string_to_array(genres, ',')))
FROM movies
WHERE genres IS NOT NULL
  AND genres != ''
ON CONFLICT (name) DO NOTHING;

INSERT INTO movie_genres (movie_id, genre_id)
SELECT DISTINCT m.id, g.id
FROM movies m
       CROSS JOIN LATERAL unnest(string_to_array(m.genres, ',')) AS genre_name
       JOIN genres g ON g.name = trim(genre_name);

-- Migrate Categories
INSERT INTO categories (name)
SELECT DISTINCT trim(unnest(string_to_array(categories, ',')))
FROM movies
WHERE categories IS NOT NULL
  AND categories != ''
ON CONFLICT (name) DO NOTHING;

INSERT INTO movie_categories (movie_id, category_id)
SELECT DISTINCT m.id, c.id
FROM movies m
       CROSS JOIN LATERAL unnest(string_to_array(m.categories, ',')) AS category_name
       JOIN categories c ON c.name = trim(category_name);

-- Migrate Countries
INSERT INTO countries (name)
SELECT DISTINCT trim(unnest(string_to_array(production_country, ',')))
FROM movies
WHERE production_country IS NOT NULL
  AND production_country != ''
ON CONFLICT (name) DO NOTHING;

INSERT INTO movie_countries (movie_id, country_id)
SELECT DISTINCT m.id, c.id
FROM movies m
       CROSS JOIN LATERAL unnest(string_to_array(m.production_country, ',')) AS country_name
       JOIN countries c ON c.name = trim(country_name);

-- Migrate People (Actors)
INSERT INTO people (name)
SELECT DISTINCT trim(unnest(string_to_array(actors, ',')))
FROM movies
WHERE actors IS NOT NULL
  AND actors != ''
ON CONFLICT (name) DO NOTHING;

INSERT INTO movie_people (movie_id, person_id, role)
SELECT DISTINCT m.id, p.id, 'ACTOR'
FROM movies m
       CROSS JOIN LATERAL unnest(string_to_array(m.actors, ',')) AS actor_name
       JOIN people p ON p.name = trim(actor_name);

-- Migrate People (Directors)
INSERT INTO people (name)
SELECT DISTINCT trim(unnest(string_to_array(director, ',')))
FROM movies
WHERE director IS NOT NULL
  AND director != ''
ON CONFLICT (name) DO NOTHING;

INSERT INTO movie_people (movie_id, person_id, role)
SELECT DISTINCT m.id, p.id, 'DIRECTOR'
FROM movies m
       CROSS JOIN LATERAL unnest(string_to_array(m.director, ',')) AS director_name
       JOIN people p ON p.name = trim(director_name);

-- Drop old columns
ALTER TABLE movies
  DROP COLUMN tags;
ALTER TABLE movies
  DROP COLUMN genres;
ALTER TABLE movies
  DROP COLUMN categories;
ALTER TABLE movies
  DROP COLUMN production_country;
ALTER TABLE movies
  DROP COLUMN actors;
ALTER TABLE movies
  DROP COLUMN director;
