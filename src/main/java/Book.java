import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class Book {
  private int id;
  private String title;

  public Book(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public int getId() {
    return id;
  }

  public static List<Book> all() {
    String sql = "SELECT id, title FROM books";
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
      String sql = "INSERT INTO books (title) VALUES (:title)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("title", this.title)
      .executeUpdate()
      .getKey();
    }
  }

  public static Book find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM books WHERE id=:id";
      Book task = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Book.class);
      return task;
    }
  }

  public void addAuthor(Author author) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO books_authors (author_id, book_id) VALUES (:author_id, :book_id);";
      con.createQuery(sql)
        .addParameter("author_id", author.getId())
        .addParameter("book_id", this.getId())
        .executeUpdate();
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
        String taskQuery = "SELECT * FROM authors WHERE id = :authorId;";
        Author author = con.createQuery(taskQuery)
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

      String joinDeleteQuery = "DELETE FROM books_authors WHERE book_id = :taskId;";
        con.createQuery(joinDeleteQuery)
          .addParameter("taskId", this.getId())
          .executeUpdate();
    }
  }

}
