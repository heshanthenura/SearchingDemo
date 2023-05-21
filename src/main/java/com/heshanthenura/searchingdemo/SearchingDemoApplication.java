package com.heshanthenura.searchingdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;

@SpringBootApplication
public class SearchingDemoApplication implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SearchingDemoApplication.class, args);
    }

    //sqlite functions ** this part is not necessary if you are using database server.
    @Override
    public void run(String... args) throws Exception {
        //reading movie.txt file
        File file = new File(System.getProperty("user.dir") + File.separator + "movies.txt");
        System.out.println(file.getName());

        //creating table if not exists
        try {
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS movies (id INTEGER PRIMARY KEY AUTOINCREMENT,movie_name TEXT NOT NULL);");
            System.out.println("Added Data");
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Error in Creating or Inserting Data. May be data already exists");
        }


// adding movies to table. uncomment below code and run if databse is empty.

//        try {
//            List<String> lines = Files.readAllLines(Path.of(file.getAbsolutePath()));
//            for (String line : lines) {
//                System.out.println(line);
//                jdbcTemplate.execute("INSERT INTO movies (movie_name) VALUES ("+String.valueOf(line)+");");
//            }
//            System.out.println("All added");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
