import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

  @Override
  protected void before() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/recipe_box_test", null, null);
  }

  @Override
  protected void after() {
    try(Connection con = DB.sql2o.open()) {
      String deleteTagsQuery = "DELETE FROM tags *;";
      String deleteRecipesQuery = "DELETE FROM recipes *;";
      String deleteRecipesTagsQuery = "DELETE FROM recipes_tags *;";
      String deleteRecipes_ingredientsQuery = "DELETE FROM recipes_ingredients *;";
      String deleteIngredientsQuery = "DELETE FROM ingredients *;";
      con.createQuery(deleteTagsQuery).executeUpdate();
      con.createQuery(deleteRecipesQuery).executeUpdate();
      con.createQuery(deleteRecipesTagsQuery).executeUpdate();
      con.createQuery(deleteRecipes_ingredientsQuery).executeUpdate();
      con.createQuery(deleteIngredientsQuery).executeUpdate();
    }
  }

}
