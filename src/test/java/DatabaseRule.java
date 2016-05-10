import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

  @Override
  protected void before() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/library_test", null, null);
  }

  @Override
  protected void after() {
    try(Connection con = DB.sql2o.open()) {
      String deleteAuthorsQuery = "DELETE FROM authors *;";
      String deleteBooksQuery = "DELETE FROM books *;";
      String deleteBooksAuthorsQuery = "DELETE FROM books_authors *;";
      String deleteCheckoutsQuery = "DELETE FROM checkouts *;";
      String deleteCopiesQuery = "DELETE FROM copies_and_due_dates *;";
      String deletePatronsQuery = "DELETE FROM patrons *;";
      con.createQuery(deleteAuthorsQuery).executeUpdate();
      con.createQuery(deleteBooksQuery).executeUpdate();
      con.createQuery(deleteBooksAuthorsQuery).executeUpdate();
      con.createQuery(deleteCheckoutsQuery).executeUpdate();
      con.createQuery(deleteCopiesQuery).executeUpdate();
      con.createQuery(deletePatronsQuery).executeUpdate();
    }
  }

}
