import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class Book {
  private int id;
  private String title;
  private boolean checked_in;
  private int author_id;

  public Book(String title) {
    this.title = title;
    this.checked_in = true;
  }

  public boolean getAvailability() {
    return checked_in;
  }

  public void checkout() {
    this.checked_in = false;
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE books SET checked_in = false WHERE id = :id";
      con.createQuery(sql)
      .addParameter("id", this.id)
      .executeUpdate();
    }
  }

  public String getTitle() {
    return title;
  }

  public int getId() {
    return id;
  }

  public static List<Book> all() {
    String sql = "SELECT * FROM books";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Book.class);
    }
  }

  @Override
  public boolean equals(Object otherBook){
    if (!(otherBook instanceof Book)) {
      return false;
    } else {
      Book newBook = (Book) otherBook;
      return this.getTitle().equals(newBook.getTitle()) &&
             this.getId() == newBook.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO books (title, checked_in) VALUES (:title, :checked_in)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("title", this.title)
      .addParameter("checked_in", this.checked_in)
      .executeUpdate()
      .getKey();
    }
  }

  public static Book find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM books WHERE id=:id";
      Book book = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Book.class);
      return book;
    }
  }

  public static List<Book> findByTitle(String title) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM books WHERE title=:title";
      List<Book> books = con.createQuery(sql)
        .addParameter("title", title)
        .executeAndFetch(Book.class);
      return books;
    }
  }

  public static List<Book> findByAuthor(Author author) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT books.* FROM authors JOIN books_authors ON (authors.id = books_authors.author_id) JOIN books ON (books_authors.book_id = books.id) WHERE authors.id = :author_id";
      List<Book> books = con.createQuery(sql)
        .addParameter("author_id", author.getId())
        .executeAndFetch(Book.class);
      return books;
    }
  }

  public void addAuthor(Author author) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO books_authors (author_id, book_id) VALUES (:author_id, :book_id);";
      con.createQuery(sql, true)
        .addParameter("author_id", author.getId())
        .addParameter("book_id", this.getId())
        .executeUpdate()
        .getKey();
    }
  }

  public List<Author> getAuthors() {
    try(Connection con = DB.sql2o.open()) {
      String joinQuery = "SELECT author_id FROM books_authors WHERE book_id = :book_id;";

      List<Integer> authorIds = con.createQuery(joinQuery)
        .addParameter("book_id", this.getId())
        .executeAndFetch(Integer.class);

      List<Author> authors = new ArrayList<Author>();

      for (Integer authorId : authorIds) {
        String bookQuery = "SELECT * FROM authors WHERE id = :authorId;";
        Author author = con.createQuery(bookQuery)
          .addParameter("authorId", authorId)
          .executeAndFetchFirst(Author.class);
        authors.add(author);
      }
      return authors;
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM books WHERE id = :id;";
        con.createQuery(deleteQuery)
          .addParameter("id", this.getId())
          .executeUpdate();

      String joinDeleteQuery = "DELETE FROM books_authors WHERE book_id = :bookId;";
        con.createQuery(joinDeleteQuery)
          .addParameter("bookId", this.getId())
          .executeUpdate();
    }
  }

}
