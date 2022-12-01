package telran.java2022.book.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java2022.book.dao.AuthorRepository;
import telran.java2022.book.dao.BookRepository;
import telran.java2022.book.dao.PublisherRepository;
import telran.java2022.book.dto.AuthorDto;
import telran.java2022.book.dto.BookDto;
import telran.java2022.book.dto.exceptions.EntityNotFoundException;
import telran.java2022.book.model.Author;
import telran.java2022.book.model.Book;
import telran.java2022.book.model.Publisher;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
	final BookRepository bookRepository;
	final AuthorRepository authorRepository;
	final PublisherRepository publisherRepository;
	final ModelMapper modelMapper;

	@Override
	@Transactional
	public boolean addBook(BookDto bookDto) {
		if(bookRepository.existsById(bookDto.getIsbn())) {
			return false;
		}
		Set<Author> authors = bookDto.getAuthors().stream()
				.map(a -> authorRepository.findById(a.getName())
						.orElse(authorRepository.save(new Author(a.getName(), a.getBirthDate()))))
				.collect(Collectors.toSet());
		Publisher publisher = publisherRepository.findById(bookDto.getPublisher())
				.orElse(publisherRepository.save(new Publisher(bookDto.getPublisher())));
		Book book = new Book(bookDto.getIsbn(), bookDto.getTitle(), authors, publisher);
		bookRepository.save(book);
		return true;
	}

	@Override
	public BookDto findBookByIsbn(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto removeBook(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		bookRepository.deleteById(isbn);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto updateBook(String isbn, String title) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		book.setTitle(title);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	public Iterable<BookDto> findBooksByAuthor(String authorName) {
		Author author = authorRepository.findById(authorName).orElseThrow(EntityNotFoundException::new);
		return author.getBooks().stream()
				.map(b->modelMapper.map(b, BookDto.class))
				.collect(Collectors.toList());
//		return bookRepository.findBooksByAuthors_Name(authorName).stream()
//				.map(b -> modelMapper.map(b, BookDto.class))
//				.collect(Collectors.toList());
	}

	@Override
	public Iterable<BookDto> findBooksByPublisher(String publisherName) {
		Publisher publisher = publisherRepository.findById(publisherName).orElseThrow(EntityNotFoundException::new);
		return publisher.getBooks().stream()
				.map(b -> modelMapper.map(b, BookDto.class))
				.collect(Collectors.toList());
//		Publisher publisher = publisherRepository.findById(publisherName).orElseThrow(EntityNotFoundException::new);
//		return bookRepository.findBooksByPublisher(publisher).stream()
//						.map(b -> modelMapper.map(b, BookDto.class))
//						.collect(Collectors.toList());
	}

	@Override
	public Iterable<AuthorDto> findBookAuthors(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		return book.getAuthors().stream()
					.map(a -> modelMapper.map(a, AuthorDto.class))
					.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<String> findPublishersByAuthor(String authorName) {
//		=================================================================
//		return bookRepository.findBooksByAuthors_Name(authorName).stream()
//				.map(b -> b.getPublisher().getPublisherName())
//				.collect(Collectors.toSet());
//		=================================================================
//		return publisherRepository.findPublishersByAuthor(authorName);
//		=================================================================
		return publisherRepository.findDistinctByBooksAuthorsName(authorName)
				.map(Publisher::getPublisherName)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public AuthorDto removeAuthor(String authorName) {
		Author author = authorRepository.findById(authorName).orElseThrow(EntityNotFoundException::new);
//		bookRepository.findBooksByAuthors_Name(authorName).stream()
//				.filter(b -> b.isAuthorContains(authorName))
//				.forEach(b -> removeBook(b.getIsbn()));
		authorRepository.deleteById(authorName);
		return modelMapper.map(author, AuthorDto.class);
	}

}
