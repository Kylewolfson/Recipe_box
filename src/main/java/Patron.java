import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class Patron {
  private int id;
  private String name;

  public Patron(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO patrons(name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .executeUpdate()
      .getKey();
    }
  }

  public static Patron find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM patrons WHERE id=:id";
      Patron patron = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Patron.class);
    return patron;
    }
  }

  public List<Book> getHistory() {
    try(Connection con = DB.sql2o.open()) {
      String joinQuery = "SELECT books.* FROM patrons JOIN checkouts ON (patrons.id = checkouts.patron_id) JOIN books ON (checkouts.book_id = books.id) WHERE patrons.id = :patron_id;";
      List<Book> books = con.createQuery(joinQuery)
        .addParameter("patron_id", this.id)
        .executeAndFetch(Book.class);
      return books;
    }
  }

  public String checkout(String title) {
    List<Book> books = new ArrayList();
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM books WHERE title = :title";
      books = con.createQuery(sql)
        .addParameter("title", title)
        .executeAndFetch(Book.class);
    }

    boolean available = false;

    for (Book book : books) {
      boolean book_available = book.getAvailability();
      if (book_available == true) {
        available = true;
        book.checkout(this);
        break;
      }
    }
    if (available == true) {
      return "Your book was checked out to you";
    } else {
      return "Sorry, no copies were available";
    }
  }

  @Override
  public boolean equals(Object otherPatron) {
    if (!(otherPatron instanceof Patron)) {
      return false;
    } else {
      Patron newPatron = (Patron) otherPatron;
      return this.getName().equals(newPatron.getName()) &&
             this.getId() == newPatron.getId();
    }
  }
}
