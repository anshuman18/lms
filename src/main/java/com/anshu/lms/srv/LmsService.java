/**
 * 
 */
package com.anshu.lms.srv;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anshu.lms.db.ILmsDAO;
import com.anshu.lms.db.LmsDAO.Filter;
import com.anshu.lms.model.Book;

/**
 * @author anshumank
 *
 */
@Service
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LmsService {

	private static Logger log = LoggerFactory.getLogger(LmsService.class);

	@Autowired
	private ILmsDAO lmsDAO;

	public LmsService() {
		log.info("LmsService initialized!");
	}

	@POST
	public Book addBook(Book book) {
		return lmsDAO.addBook(book);
	}

	@DELETE
	@Path("/{isbn}")
	public Book removeBook(@PathParam("isbn") final String isbn) {
		return lmsDAO.removeBook(isbn);
	}

	@PUT
	@Path("/user/{userid}/checkout/{isbn}")
	public List<Book> checkout(@PathParam("userid") final String userId, @PathParam("isbn") final String isbn) {
		return lmsDAO.checkoutBook(userId, isbn);
	}

	@PUT
	@Path("/user/{userid}/checkin/{isbn}")
	public List<Book> checkin(@PathParam("userid") final String userId, @PathParam("isbn") final String isbn) {
		return lmsDAO.checkinBook(userId, isbn);
	}

	@GET
	@Path("/user/{userid}")
	public List<Book> findByFilter(@PathParam("userid") String userId, @QueryParam("key") final String filterKey,
			@QueryParam("value") final String filterValue) {
		return lmsDAO.findBy(Filter.valueOf(filterKey), filterValue);
	}
}
