import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class Tag {
  private int id;
  private String tag;

  public Tag(String tag) {
    this.tag = tag;
  }

  public String getTag() {
    return tag;
  }

  public static List<Tag> all() {
    String sql = "SELECT id, tag FROM tags";
    try(Connection con = DB.sql2o.open()) {
        return con.createQuery(sql).executeAndFetch(Tag.class);
    }
  }

  @Override
  public boolean equals(Object otherTag) {
    if (!(otherTag instanceof Tag)) {
      return false;
    } else {
      Tag newTag = (Tag) otherTag;
      return this.getId() == newTag.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO tags(tag) VALUES (:tag)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("tag", this.tag)
      .executeUpdate()
      .getKey();
    }
  }

  public int getId() {
    return id;
  }

  public static Tag find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tags WHERE id=:id";
      Tag tag = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Tag.class);
    return tag;
    }
  }

  public void addRecipe(Recipe recipe) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO recipes_tags (tags_id, recipes_id) VALUES (:tags_id, :recipes_id)";
        con.createQuery(sql)
          .addParameter("tags_id", this.getId())
          .addParameter("recipes_id", recipe.getId())
          .executeUpdate();
    }
  }

  public List<Recipe> getRecipes() {
    try(Connection con = DB.sql2o.open()) {
      String joinQuery = "SELECT recipes_id FROM recipes_tags WHERE tags_id = :tags_id;";

      List<Integer> recipeIds = con.createQuery(joinQuery)
        .addParameter("tags_id", this.getId())
        .executeAndFetch(Integer.class);

      List<Recipe> recipes = new ArrayList<Recipe>();

      for (Integer recipeId : recipeIds) {
        String recipeQuery = "SELECT * FROM recipes WHERE id = :recipeId;";
        Recipe recipe = con.createQuery(recipeQuery)
          .addParameter("recipeId", recipeId)
          .executeAndFetchFirst(Recipe.class);
        recipes.add(recipe);
      }
      return recipes;
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM tags WHERE id = :id;";
        con.createQuery(deleteQuery)
          .addParameter("id", this.getId())
          .executeUpdate();

      String joinDeleteQuery = "DELETE FROM recipes_tags WHERE tags_id = :tagId;";
        con.createQuery(joinDeleteQuery)
          .addParameter("tagId", this.getId())
          .executeUpdate();
    }
  }



}
