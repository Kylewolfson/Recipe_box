import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class Ingredient {
  private int id;
  private String fixins;

  public Ingredient(String fixins) {
    this.fixins = fixins;
  }

  public String getFixins() {
    return fixins;
  }

  public static List<Ingredient> all() {
    String sql = "SELECT * FROM ingredients";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Ingredient.class);
    }
  }

  public int getId() {
    return id;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO ingredients(fixins) VALUES (:fixins)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("fixins", this.fixins)
      .executeUpdate()
      .getKey();
    }
  }

  public static Ingredient find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM ingredients WHERE id=:id";
      Ingredient ingredient = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Ingredient.class);
    return ingredient;
    }
  }

  @Override
  public boolean equals(Object otherIngredient) {
    if (!(otherIngredient instanceof Ingredient)) {
      return false;
    } else {
      Ingredient newIngredient = (Ingredient) otherIngredient;
      return this.getId() == newIngredient.getId();
    }
  }
}
