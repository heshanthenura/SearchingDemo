package com.heshanthenura.searchingdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

// Main controller
@Controller
public class MainController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    // Index URL
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String Index(Model model) {
        // Retrieve movies from the database
        List<String> movies = jdbcTemplate.query("SELECT * FROM movies", (data, row) -> new String(data.getString("movie_name")));

        // Print movies to console
//        for (String movie : movies) {
//            System.out.println(movie);
//        }

//        System.out.println("Movies printed");

        // Add movieList attribute to the model
        model.addAttribute("movieList", movies);

        // Return the index template
        return "index";
    }

    @MessageMapping("/search/{user}/{movieName}")
    public void Search(@DestinationVariable String user, @DestinationVariable String movieName) {
        // Log user and movieName values to console
//        System.out.println(user);
//        System.out.println(movieName);

        List<String> movies;
        if (movieName == null || movieName.trim().isEmpty()) {
            // If movieName is empty or contains only spaces, retrieve all movies from the database
            movies = jdbcTemplate.query("SELECT movie_name FROM movies", (resultSet, rowNum) -> resultSet.getString("movie_name"));

            // Send all movies to the specified user via messaging template
            simpMessagingTemplate.convertAndSend("/topic/" + user, movies);
//            System.out.println("null and sent all");
        } else {
            // If movieName is not empty, retrieve movies from the database that match the search criteria
            movies = jdbcTemplate.query("SELECT movie_name FROM movies WHERE movie_name LIKE '%" + movieName + "%'", (resultSet, rowNum) -> resultSet.getString("movie_name"));

            // Send matching movies to the specified user via messaging template
            simpMessagingTemplate.convertAndSend("/topic/" + user, movies);
        }
    }
}
