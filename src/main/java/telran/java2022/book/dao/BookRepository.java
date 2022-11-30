package telran.java2022.book.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import telran.java2022.book.model.Book;
import telran.java2022.book.model.Publisher;

public interface BookRepository extends CrudRepository<Book, String> {

	List<Book> findBooksByAuthors_Name(String name);
	
	List<Book> findBooksByPublisher(Publisher publisher);
}
