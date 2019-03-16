package com.anshu.lms.db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.anshu.lms.model.Book;
import com.anshu.lms.model.User;

//@Resource
//@Component
@Repository
public class LmsDAO implements ILmsDAO {

	private static Logger log = LoggerFactory.getLogger(LmsDAO.class);

	public enum Filter {
		CAT, OWNER, AUTHOR
	}

	// members
	private Map<String, User> members;

	// isbnBooks map represents library
	private Map<String, Book> availableBooks;

	// isbnBooks map represents borrowed books
	private Map<String, Book> borrowedBooks;

	// borrower books map
	private Map<String, List<Book>> borrowerBooks;

	// views
	private Map<String, List<Book>> titleBooks;
	private Map<String, List<Book>> catBooks;

	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	public LmsDAO() {
		members = new HashMap<>();
		availableBooks = new HashMap<>();
		borrowedBooks = new HashMap<>();
		borrowerBooks = new HashMap<>();
		titleBooks = new HashMap<>();
		catBooks = new HashMap<>();
		log.info("LmsDAO initialized");
	}

	@Override
	public Book addBook(Book book) {

		readWriteLock.writeLock().lock();
		try {

			if (!availableBooks.containsKey(book.getIsbn())) {
				availableBooks.put(book.getIsbn(), book);
				if (!titleBooks.containsKey(book.getTitle())) {
					titleBooks.put(book.getTitle(), new LinkedList<Book>());
				}
				titleBooks.get(book.getTitle()).add(book);

				if (!catBooks.containsKey(book.getCategory())) {
					catBooks.put(book.getCategory(), new LinkedList<Book>());
				}
				catBooks.get(book.getCategory()).add(book);
				return book;
			}

			throw new IllegalArgumentException("duplicate " + book.getIsbn());
		} finally {
			readWriteLock.writeLock().unlock();
		}
	}

	@Override
	public Book removeBook(final String isbn) {
		readWriteLock.writeLock().lock();
		try {

			if (availableBooks.containsKey(isbn)) {
				Book book = availableBooks.get(isbn);
				catBooks.get(book.getCategory()).remove(book);
				titleBooks.get(book.getTitle()).remove(book);
				return book;
			}
			throw new NoSuchElementException(isbn);

		} finally {
			readWriteLock.writeLock().unlock();
		}
	}

	@Override
	public List<Book> checkoutBook(final String userId, final String isbn) {

		readWriteLock.writeLock().lock();
		try {

			if (!availableBooks.containsKey(isbn)) {
				throw new NoSuchElementException(isbn);
			}
			if (!members.containsKey(userId)) {
				throw new NoSuchElementException(userId);
			}

			Book book = availableBooks.get(isbn);
			if (!borrowerBooks.containsKey(userId)) {
				borrowerBooks.put(userId, new LinkedList<Book>());
			}
			borrowerBooks.get(userId).add(book);
			borrowedBooks.put(isbn, book);

			return borrowerBooks.get(userId);

		} finally {
			readWriteLock.writeLock().unlock();
		}
	}

	@Override
	public List<Book> checkinBook(final String userId, final String isbn) {

		readWriteLock.writeLock().lock();
		try {

			if (!borrowedBooks.containsKey(isbn)) {
				throw new NoSuchElementException(isbn);
			}
			if (!members.containsKey(userId)) {
				throw new NoSuchElementException(userId);
			}

			Book book = borrowedBooks.get(isbn);
			borrowerBooks.get(userId).remove(book);
			availableBooks.put(isbn, book);
			borrowedBooks.remove(isbn);

			return borrowerBooks.get(userId);

		} finally {
			readWriteLock.writeLock().unlock();
		}
	}

	@Override
	public List<Book> findBy(final Filter key, final String value) {

		final long start = System.currentTimeMillis();
		readWriteLock.readLock().lock();
		try {
			if (Filter.AUTHOR.equals(key)) {
				return availableBooks.values().stream().filter(b -> b.getAuthor().equals(value))
						.collect(Collectors.toList());
			} else if (Filter.CAT.equals(key)) {
				return availableBooks.values().stream().filter(b -> b.getCategory().equals(value))
						.collect(Collectors.toList());
			} else if (Filter.OWNER.equals(key)) {
				return borrowerBooks.get(value);
			}
			throw new IllegalArgumentException(key + "is not supported!");

		} finally {
			readWriteLock.readLock().unlock();
			long curr = System.currentTimeMillis();
			log.info("findBy time in millis : " + (curr - start));
		}
	}

	@Override
	public User register(User user) {
		if (!members.containsKey(user.getUserId())) {
			members.put(user.getUserId(), user);
		}
		return user;
	}

	@Override
	public User unRegister(User user) {
		if (members.containsKey(user.getUserId())) {
			members.remove(user.getUserId());
		}
		return user;
	}

}
