package com.anshu.lms.db;

import java.util.List;

import com.anshu.lms.db.LmsDAO.Filter;
import com.anshu.lms.model.Book;
import com.anshu.lms.model.User;

public interface ILmsDAO {

	User register(final User user);

	User unRegister(final User user);

	Book addBook(final Book book);

	Book removeBook(final String title);

	List<Book> checkoutBook(final String userId, final String isbn);

	List<Book> checkinBook(final String userId, final String isbn);

	List<Book> findBy(Filter filterKey, String filterValue);
}