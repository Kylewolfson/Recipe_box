import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

public class BookTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Book_instantiatesCorrectly_true() {
    Book myBook = new Book("Snow Crash");
    assertEquals(true, myBook instanceof Book);
  }

  @Test
  public void getTitle_bookInstantiatesWithTitle_String() {
    Book myBook = new Book("Goodnight Moon");
    assertEquals("Goodnight Moon", myBook.getTitle());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Book.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfTitlesAreTheSame() {
    Book firstBook = new Book("Snow Crash");
    Book secondBook = new Book("Snow Crash");
    assertTrue(firstBook.equals(secondBook));
  }

  @Test
  public void save_returnsTrueIfTitlesAreTheSame() {
    Book myBook = new Book("Snow Crash");
    myBook.save();
    assertTrue(Book.all().get(0).equals(myBook));
  }

  @Test
  public void save_assignsIdToObject() {
    Book myBook = new Book("Snow Crash");
    myBook.save();
    Book savedBook = Book.all().get(0);
    assertEquals(myBook.getId(), savedBook.getId());
  }

  @Test
  public void find_findsBookInDatabase_true() {
    Book myBook = new Book("Snow Crash");
    myBook.save();
    Book savedBook = Book.find(myBook.getId());
    assertTrue(myBook.equals(savedBook));
  }

  @Test
  public void addAuthor_addAuthorToBook() {
    Author myAuthor = new Author("Neal Stephenson");
    myAuthor.save();
    Book myBook = new Book("Mow the lawn for Dummies");
    myBook.save();
    myBook.addAuthor(myAuthor);
    Author savedAuthor = myBook.getAuthors().get(0);
    assertTrue(myAuthor.equals(savedAuthor));
  }

  @Test
  public void getAuthors_returnsAllAuthors_List() {
    Author myAuthor = new Author("Neal Stephenson");
    myAuthor.save();
    Author yourAuthor = new Author("Stephen King");
    myAuthor.save();
    Book myBook = new Book("Snow Crash");
    myBook.save();
    myBook.addAuthor(myAuthor);
    myBook.addAuthor(yourAuthor);
    List savedAuthors = myBook.getAuthors();
    assertEquals(2, savedAuthors.size());
  }

  @Test
  public void delete_deletesAllBooksAndAuthorsAssociations() {
    Author myAuthor = new Author("Neal Stephenson");
    myAuthor.save();
    Book myBook = new Book("Snow Crash");
    myBook.save();
    myBook.addAuthor(myAuthor);
    myBook.delete();
    assertEquals(0, myAuthor.getBooks().size());
  }

  @Test
  public void checkout_setsAvailablilityToFalse_true() {
    Book myBook = new Book("Snow Crash");
    myBook.save();
    myBook.checkout();
    assertFalse(myBook.getAvailability());
  }

  @Test
  public void findByTitle_returnsABookByTitle_true() {
    Book myBook = new Book("Snow Crash");
    myBook.save();
    myBook.save();
    assertEquals(Book.findByTitle("Snow Crash").get(1), myBook);
  }
  @Test
  public void findByTitle_returnsABookByTitleAndNothingElse_true() {
    Book myBook = new Book("Snow Crash");
    myBook.save();
    Book yourBook = new Book("Goodnight Moon");
    yourBook.save();
    assertEquals(Book.findByTitle("Goodnight Moon").get(0), yourBook);
  }
  @Test
  public void findByAuthor_returnsABookByAuthorAndNothingElse_true() {
    Book myBook = new Book("Snow Crash");
    myBook.save();
    Author myAuthor = new Author("Neal Stephenson");
    myAuthor.save();
    myBook.addAuthor(myAuthor);
    Book yourBook = new Book("Goodnight Moon");
    yourBook.save();
    Author yourAuthor = new Author("Shakespeare");
    yourAuthor.save();
    yourBook.addAuthor(yourAuthor);
    assertEquals(Book.findByAuthor(yourAuthor).get(0), yourBook);
  }
}
