import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class AuthorTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Author_instantiatesCorrectly_true() {
    Author myAuthor = new Author("Sir Arthur Conan Doyle");
    assertEquals(true, myAuthor instanceof Author);
  }

  @Test
  public void getName_authorInstantiatesWithName_String() {
    Author myAuthor = new Author("Sir Arthur Conan Doyle");
    assertEquals("Sir Arthur Conan Doyle", myAuthor.getName());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Author.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAretheSame() {
    Author firstAuthor = new Author("Sir Arthur Conan Doyle");
    Author secondAuthor = new Author("Sir Arthur Conan Doyle");
    assertTrue(firstAuthor.equals(secondAuthor));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Author myAuthor = new Author("Sir Arthur Conan Doyle");
    myAuthor.save();
    Author savedAuthor = Author.find(myAuthor.getId());
    assertTrue(myAuthor.equals(savedAuthor));
  }

  @Test
  public void save_assignsIdToObject_int() {
    Author myAuthor = new Author("Sir Arthur Conan Doyle");
    myAuthor.save();
    Author savedAuthor = Author.all().get(0);
    assertEquals(myAuthor.getId(), savedAuthor.getId());
  }

  @Test
  public void find_findAuthorInDatabase_true() {
    Author myAuthor = new Author("Sir Arthur Conan Doyle");
    myAuthor.save();
    Author savedAuthor = Author.find(myAuthor.getId());
    assertTrue(myAuthor.equals(savedAuthor));
  }

  @Test
  public void addBook_addBookToAuthor_true() {
    Author myAuthor = new Author("Sir Arthur Conan Doyle");
    myAuthor.save();
    Book myBook = new Book("Sherlock Mows The Lawn");
    myBook.save();
    myAuthor.addBook(myBook);
    Book savedBook = myAuthor.getBooks().get(0);
    assertTrue(myBook.equals(savedBook));
  }

  @Test
  public void getBooks_returnsAllBooks_List() {
    Author myAuthor = new Author("Sir Arthur Conan Doyle");
    myAuthor.save();
    Book myBook = new Book("Sherlock Mows The Lawn");
    myBook.save();
    myAuthor.addBook(myBook);
    List savedBooks = myAuthor.getBooks();
    assertEquals(1, savedBooks.size());
  }

  @Test
  public void delete_deletesAllBooksAndAuthorsAssociations() {
    Author myAuthor = new Author("Sir Arthur Conan Doyle");
    myAuthor.save();
    Book myBook = new Book("Sherlock Mows The Lawn");
    myBook.save();
    myAuthor.addBook(myBook);
    myAuthor.delete();
    assertEquals(0, myBook.getAuthors().size());
  }
}
