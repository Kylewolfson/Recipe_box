import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

public class PatronTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Patron_instantiatesCorrectly_true() {
    Patron myPatron = new Patron("Book lover");
    assertEquals(true, myPatron instanceof Patron);
  }

  @Test
  public void Patron_savesToDatabase_true() {
    Patron myPatron = new Patron("Book lover");
    myPatron.save();
    Patron savedPatron = Patron.find(myPatron.getId());
    assertTrue(myPatron.equals(savedPatron));
  }

  @Test
  public void Checkout_testv2() {
    Patron myPatron = new Patron("Book lover");
    myPatron.save();
    Book myBook = new Book("Mow the lawn for Dummies");
    myBook.save();
    assertEquals("Your book was checked out to you", myPatron.checkout(myBook.getTitle()));
  }

  @Test
  public void Checkout_testv3() {
    Patron myPatron = new Patron("Book lover");
    myPatron.save();
    Book myBook = new Book("Mow the lawn for Dummies");
    myBook.save();
    myPatron.checkout(myBook.getTitle());
    assertEquals("Sorry, no copies were available", myPatron.checkout(myBook.getTitle()));
  }
}
