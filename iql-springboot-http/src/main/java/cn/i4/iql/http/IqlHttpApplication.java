package cn.i4.iql.http;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IqlHttpApplication {
	public static void main(String[] args) {
		System.out.println( " ___ ___  _     \n" +
							"|_ _/ _ \\| |    \n" +
							" | | | | | |    \n" +
							" | | |_| | |___ \n" +
							"|___\\__\\_\\_____|");
		SpringApplication.run(IqlHttpApplication.class, args);
	}
}