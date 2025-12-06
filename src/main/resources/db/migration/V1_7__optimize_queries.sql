-- Add indexes for frequently queried columns
CREATE INDEX idx_movies_rating ON movies(rating);
CREATE INDEX idx_movies_release_date ON movies(release_date);
CREATE INDEX idx_reviews_date ON reviews(date);
CREATE INDEX idx_movie_views_watch_date ON movie_views(watch_date);

-- Add indexes for foreign keys in new tables to improve join performance
CREATE INDEX idx_movie_tags_tag_id ON movie_tags(tag_id);
CREATE INDEX idx_movie_genres_genre_id ON movie_genres(genre_id);
CREATE INDEX idx_movie_categories_category_id ON movie_categories(category_id);
CREATE INDEX idx_movie_countries_country_id ON movie_countries(country_id);
CREATE INDEX idx_movie_people_person_id ON movie_people(person_id);
CREATE INDEX idx_movie_people_movie_id ON movie_people(movie_id);
