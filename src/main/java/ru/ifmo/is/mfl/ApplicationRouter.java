package ru.ifmo.is.mfl;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ApplicationRouter {

  private static final List<String> crudResources = Arrays.asList(
    "watchlists"
  );

  public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> getSecurityRoutes() {
    return request -> {
      request
        // Only authenticated user can log out
        .requestMatchers("/api/auth/sign-out").authenticated()
        .requestMatchers("/api/auth/resend-confirmation").authenticated()
        // Only authenticated user can get current user
        .requestMatchers("/api/auth/me").authenticated()
        // Everyone can /api/auth/**
        .requestMatchers("/api/auth/**").permitAll()

        // Swagger UI (documentation)
        .requestMatchers("/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**").permitAll()

        // Actuator
        .requestMatchers("/actuator/**").permitAll()

        // Feed
        .requestMatchers(HttpMethod.GET, "/api/movies/feed").permitAll() // Every user can view recommended movies
        .requestMatchers(HttpMethod.GET, "/api/watchlists/feed").permitAll() // Every user can view recommended watchlists

        // Reports
        .requestMatchers(HttpMethod.POST, "/api/reviews/*/reports").permitAll() // User can report review
        .requestMatchers(HttpMethod.POST, "/api/comments/*/reports").permitAll() // User can report comment
        .requestMatchers(HttpMethod.GET, "/api/reports/**").hasAnyRole("ADMIN", "MODERATOR") // Admin can view all reports
        .requestMatchers(HttpMethod.GET, "/api/reports/pending").hasAnyRole("ADMIN", "MODERATOR") // Admin can view pending report
        .requestMatchers(HttpMethod.POST, "/api/reports/search").hasAnyRole("ADMIN", "MODERATOR") // Admin can search reports
        .requestMatchers(HttpMethod.PATCH, "/api/reports/**").hasAnyRole("ADMIN", "MODERATOR") // Admin can update reports
        .requestMatchers(HttpMethod.DELETE, "/api/reports/**").hasAnyRole("ADMIN") // Only admin can delete reports

        // Movie views
        .requestMatchers(HttpMethod.GET, "/api/users/*/views").permitAll() // Get all user's views
        .requestMatchers(HttpMethod.GET, "/api/movies/*/views").permitAll() // Get all movie's views
        .requestMatchers(HttpMethod.POST, "/api/movies/*/views").hasRole("USER") // Current user can mark movie as watched
        .requestMatchers(HttpMethod.DELETE, "/api/movies/*/views").hasRole("USER") // Current user can unmark watched movie

        // Comments
        .requestMatchers(HttpMethod.GET, "/api/movies/*/comments").permitAll() // Get all movie comments
        .requestMatchers(HttpMethod.GET, "/api/reviews/*/comments").permitAll() // Get all review comments
        .requestMatchers(HttpMethod.GET, "/api/watchlists/*/comments").permitAll() // Get all watchlist comments
        .requestMatchers(HttpMethod.POST, "/api/movies/*/comments").hasRole("USER") // Add comment to movie
        .requestMatchers(HttpMethod.POST, "/api/reviews/*/comments").hasRole("USER") // Add comment to review
        .requestMatchers(HttpMethod.POST, "/api/watchlists/*/comments").hasRole("USER") // Add comment to watchlist
        .requestMatchers(HttpMethod.PATCH, "/api/comments/**").hasRole("USER") // Change comment by ID
        .requestMatchers(HttpMethod.DELETE, "/api/comments/**").hasRole("USER") // Delete comment by ID

        // Ratings
        .requestMatchers(HttpMethod.GET, "/api/users/*/ratings").permitAll() // Get all user's ratings
        .requestMatchers(HttpMethod.GET, "/api/movies/*/ratings").permitAll() // Get all movie's ratings
        .requestMatchers(HttpMethod.POST, "/api/movies/*/ratings").hasRole("USER") // Add movie's rating by current user
        .requestMatchers(HttpMethod.PATCH, "/api/movies/*/ratings").hasRole("USER") // Change movie's rating by current user
        .requestMatchers(HttpMethod.DELETE, "/api/movies/*/ratings").hasRole("USER") // Delete movie's rating by current user

        // Reviews
        .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll() // Get all reviews
        .requestMatchers(HttpMethod.POST, "/api/reviews/search").permitAll() // Search review
        .requestMatchers(HttpMethod.PATCH, "/api/reviews/**").hasRole("USER") // Change review by ID
        .requestMatchers(HttpMethod.DELETE, "/api/reviews/**").hasRole("USER") // Delete review by ID
        .requestMatchers(HttpMethod.GET, "/api/movies/*/reviews").permitAll() // Get all movie's reviews
        .requestMatchers(HttpMethod.GET, "/api/users/*/reviews").permitAll() // Get all user's reviews
        .requestMatchers(HttpMethod.POST, "/api/movies/*/reviews").hasRole("USER") // Add review to movie

        // Movies
        .requestMatchers(HttpMethod.GET, "/api/movies/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/movies/*/poster").hasRole("ADMIN")
        .requestMatchers(HttpMethod.POST, "/api/movies/search").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/movies/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PATCH, "/api/movies/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/movies/**").hasRole("ADMIN")

        // Watchlists
        .requestMatchers(HttpMethod.GET, "/api/users/*/watchlists").permitAll() // Get user's watchlists
        .requestMatchers(HttpMethod.GET, "/api/movies/*/watchlists").permitAll() // Get movie's watchlists
        .requestMatchers(HttpMethod.POST, "/api/watchlists/search").permitAll() // Upload photo to watchlist
        .requestMatchers(HttpMethod.POST, "/api/watchlists/*/photo").hasRole("USER") // Upload photo to watchlist
        .requestMatchers(HttpMethod.GET, "/api/watchlists/*/movies/**").permitAll() // Get movies from watchlist
        .requestMatchers(HttpMethod.POST, "/api/watchlists/*/movies/**").hasRole("USER") // Add movie to watchlist
        .requestMatchers(HttpMethod.DELETE, "/api/watchlists/*/movies/**").hasRole("USER") // Delete movie from watchlist

        // Users
        .requestMatchers(HttpMethod.POST, "/api/users/photo").authenticated()
        .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/users/search").permitAll()
        .requestMatchers(HttpMethod.PATCH, "/api/users/**").authenticated()
        .requestMatchers(HttpMethod.DELETE, "/api/users/**").authenticated()

        // User roles
        .requestMatchers(HttpMethod.POST, "/api/users/*/roles").hasAnyRole("ADMIN", "MODERATOR")
        .requestMatchers(HttpMethod.DELETE, "/api/users/*/roles").hasAnyRole("ADMIN", "MODERATOR");

      crudResources.forEach(resource ->
        request
          // Access to CRUD resources
          .requestMatchers(HttpMethod.GET, "/api/" + resource + "/**").permitAll() // all users can read data
          .requestMatchers(HttpMethod.POST, "/api/" + resource + "/search").permitAll() // all users can search data
          .requestMatchers(HttpMethod.POST, "/api/" + resource + "/**").hasRole("USER") // only authorized users can create data
          .requestMatchers(HttpMethod.PUT, "/api/" + resource + "/**").hasRole("USER") // update is available only to authors or administrators
          .requestMatchers(HttpMethod.PATCH, "/api/" + resource + "/**").hasRole("USER") // update is available only to authors or administrators
          .requestMatchers(HttpMethod.DELETE, "/api/" + resource + "/**").hasRole("USER") // deletion is available only to authors or administrators
      );

      request
        // Any other request must be authenticated
        .anyRequest().authenticated();
    };
  }
}
