package org.gnori.votingsystemrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class VotingSystemRestApplication {

  public static void main(String[] args) {
    SpringApplication.run(VotingSystemRestApplication.class, args);

  }

}