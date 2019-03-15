/**
 * 
 */
package com.anshu.lms.srv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import com.anshu.lms.config.SpringApplication;
import com.anshu.lms.db.ILmsDAO;
import com.anshu.lms.db.LmsDAO.Filter;
import com.anshu.lms.model.Book;

/**
 * @author anshumank
 *
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = { SpringApplication.class })
public class LmsServiceTest {

	@Mock
	private ILmsDAO lmsDAO;

	@InjectMocks
	private LmsService lms;

//	/**
//	 * @throws java.lang.Exception
//	 */
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Before
//	public void setUp() throws Exception {
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@After
//	public void tearDown() throws Exception {
//	}
//
//	/**
//	 * Test method for {@link com.anshu.lms.srv.LmsService#LmsService()}.
//	 */
//	@Test
//	public final void testLmsService() {
//		fail("Not yet implemented"); // TODO
//	}

	/**
	 * Test method for
	 * {@link com.anshu.lms.srv.LmsService#addBook(com.anshu.lms.model.Book)}.
	 */
	@Test
	public final void testAddBook() {
		Book book = Book.builder().author("tom").isbn("xyz").category("java").title("Thinking in Java").build();
		Mockito.when(lmsDAO.addBook(book)).thenReturn(book);
		Book book1 = lms.addBook(book);
		assertEquals(book.getIsbn(), book1.getIsbn());
	}

	/**
	 * Test method for
	 * {@link com.anshu.lms.srv.LmsService#removeBook(java.lang.String)}.
	 */
	@Test
	public final void testRemoveBook() {
		Book book = Book.builder().author("tom").isbn("xyz").category("java").title("Thinking in Java").build();
		Mockito.when(lmsDAO.removeBook(book.getIsbn())).thenReturn(book);
		Book book1 = lms.removeBook(book.getIsbn());
		assertEquals(book.getIsbn(), book1.getIsbn());
	}

	/**
	 * Test method for
	 * {@link com.anshu.lms.srv.LmsService#checkout(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testCheckout() {
		String userid = "anshuman";
		Book book = Book.builder().author("tom").isbn("xyz").category("java").title("Thinking in Java").build();
		Mockito.when(lmsDAO.checkoutBook(userid, book.getIsbn())).thenReturn(Arrays.asList(book));
		List<Book> books = lms.checkout(userid, book.getIsbn());
		assertEquals(book.getIsbn(), books.get(0).getIsbn());
	}

	/**
	 * Test method for
	 * {@link com.anshu.lms.srv.LmsService#checkin(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testCheckin() {
		String userid = "anshuman";
		Book book = Book.builder().author("tom").isbn("xyz").category("java").title("Thinking in Java").build();
		Mockito.when(lmsDAO.checkinBook(userid, book.getIsbn())).thenReturn(Arrays.asList(book));
		
		List<Book> books = lms.checkin(userid, book.getIsbn());
		assertEquals(book.getIsbn(), books.get(0).getIsbn());
	}

	/**
	 * Test method for
	 * {@link com.anshu.lms.srv.LmsService#findByFilter(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testFindByFilter() {
		String userid = "anshuman";
		Book book = Book.builder().author("tom").isbn("xyz").category("java").title("Thinking in Java").build();
		Mockito.when(lmsDAO.findBy(Filter.OWNER, userid)).thenReturn(Arrays.asList(book));
		List<Book> books =  lms.findByFilter(userid, Filter.OWNER.name(), userid);
		assertEquals(book.getIsbn(), books.get(0).getIsbn());
	}

}
