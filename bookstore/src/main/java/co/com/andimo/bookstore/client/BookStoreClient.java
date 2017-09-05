package co.com.andimo.bookstore.client;

import java.util.ArrayList;
import java.util.List;

import co.com.andimo.bookstore.domain.Book;
import co.com.andimo.bookstore.domain.Chapter;
import co.com.andimo.bookstore.domain.Publisher;
import co.com.andimo.bookstore.service.BookStoreService;

public class BookStoreClient {
	
	private BookStoreClient(){}
	
	public static void main(String[] args) {
		 BookStoreService bookStoreService = new BookStoreService();

         //persisting object graph
         Publisher publisher = new Publisher("MANN", "Manning Publications Co.");

         Book book = new Book("9781617290459", "Java Persistence with Hibernate, Second Edition", publisher);

         List<Chapter> chapters = new ArrayList<>();
         Chapter chapter1 = new Chapter("Introducing JPA and Hibernate", 1);
         chapters.add(chapter1);
         Chapter chapter2 = new Chapter("Domain Models and Metadata", 2);
         chapters.add(chapter2);

         book.setChapters(chapters);
         
//         bookStoreService.persistObjectGraphs(book);
		
       //retrieving object graph
         Book bookRetrieve = bookStoreService.retrieveObjectGraph("9781617290459");
         System.out.println(bookRetrieve);

	}
}
