package co.com.andimo.bookstore.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import co.com.andimo.bookstore.domain.Book;
import co.com.andimo.bookstore.domain.Chapter;
import co.com.andimo.bookstore.domain.Publisher;

public class BookStoreService {

	Connection connection = null;
	public void persistObjectGraphs(Book book){
		try{
			//1.Load the driver
			//2. Get the connection
			//3. Get the PreparedStatement
			
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore","postgres", "andimoBD");
			
			String insertPublisher = "INSERT INTO PUBLISHER (CODE, PUBLISHER_NAME) VALUES (?, ?)";
			String insertBook = "INSERT INTO BOOK (ISBN, BOOK_NAME, PUBLISHER_CODE) VALUES (?, ?, ?)";
			String insertChapter = "INSERT INTO CHAPTER (BOOK_ISBN, CHAPTER_NUM, TITLE) VALUES (?, ?, ?)";
			
			PreparedStatement ps = connection.prepareStatement(insertPublisher);
			ps.setString(1, book.getPublisher().getCode());
			ps.setString(2, book.getPublisher().getName());
			
			ps.executeUpdate();
			ps.close();
			
			ps = connection.prepareStatement(insertBook);
			ps.setString(1, book.getIsbn());
			ps.setString(2, book.getName());
			ps.setString(3, book.getPublisher().getCode());
			ps.executeUpdate();
			ps.close();
			
			ps = connection.prepareStatement(insertChapter);
			for (Chapter chapter : book.getChapters()) {
				ps.setString(1, book.getIsbn());
				ps.setInt(2, chapter.getChapterNumber());
				ps.setString(3, chapter.getTitle());
				ps.executeUpdate();
			}
			ps.close();
		}catch(ClassNotFoundException e){
			
		}catch(SQLException e){
			
		}finally { 
			try { 
				connection.close(); 
			} catch (SQLException e) { 
				e.printStackTrace(); 
			} 
		}
	}
	
	
	public Book retrieveObjectGraph(String isbn){
		
		Book book = null;
		try{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore", "postgres", "andimoBD");
			
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM BOOK, PUBLISHER WHERE BOOK.PUBLISHER_CODE = PUBLISHER.CODE AND BOOK.ISBN = ?");
			ps.setString(1, isbn);
			
			ResultSet rs = ps.executeQuery();

            book = new Book();
            if (rs.next()) {
                    book.setIsbn(rs.getString("ISBN"));
                    book.setName(rs.getString("BOOK_NAME"));

                    Publisher publisher = new Publisher();
                    publisher.setCode(rs.getString("CODE"));
                    publisher.setName(rs.getString("PUBLISHER_NAME"));
                    book.setPublisher(publisher);
            }

            rs.close();
            ps.close();

            List<Chapter> chapters = new ArrayList<>();
            ps = connection.prepareStatement("SELECT * FROM CHAPTER WHERE BOOK_ISBN = ?");
            ps.setString(1, isbn);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Chapter chapter = new Chapter();
                chapter.setTitle(rs.getString("TITLE"));
                chapter.setChapterNumber(rs.getInt("CHAPTER_NUM"));             
                chapters.add(chapter);
	        }
	        book.setChapters(chapters);
	
	        rs.close();
	        ps.close();



		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
            try {
                connection.close();
	        } catch (SQLException e) {
	                e.printStackTrace();
	        } 
		}
		
		return book;
		
	}
	
	
	
	
	
	
}
