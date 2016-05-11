import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class RecipeTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Recipe_instantiatesCorrectly_true() {
    Recipe myRecipe = new Recipe("Snow Crash");
    assertEquals(true, myRecipe instanceof Recipe);
  }

  @Test
  public void getName_recipeInstantiatesWithName_String() {
    Recipe myRecipe = new Recipe("Goodnight Moon");
    assertEquals("Goodnight Moon", myRecipe.getName());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Recipe.all().size(), 0);
  }


  @Test
  public void save_assignsIdToObject() {
    Recipe myRecipe = new Recipe("Snow Crash");
    myRecipe.save();
    Recipe savedRecipe = Recipe.all().get(0);
    assertEquals(myRecipe.getId(), savedRecipe.getId());
  }

  @Test
  public void find_findsRecipeInDatabase_true() {
    Recipe myRecipe = new Recipe("Snow Crash");
    myRecipe.save();
    Recipe savedRecipe = Recipe.find(myRecipe.getId());
    assertTrue(myRecipe.equals(savedRecipe));
  }

  @Test
  public void addTag_addTagToRecipe() {
    Tag myTag = new Tag("Neal Stephenson");
    myTag.save();
    Recipe myRecipe = new Recipe("Mow the lawn for Dummies");
    myRecipe.save();
    myRecipe.addTag(myTag);
    Tag savedTag = myRecipe.getTags().get(0);
    assertTrue(myTag.equals(savedTag));
  }

  @Test
  public void getTags_returnsAllTags_List() {
    Tag myTag = new Tag("Neal Stephenson");
    myTag.save();
    Tag yourTag = new Tag("Stephen King");
    myTag.save();
    Recipe myRecipe = new Recipe("Snow Crash");
    myRecipe.save();
    myRecipe.addTag(myTag);
    myRecipe.addTag(yourTag);
    List savedTags = myRecipe.getTags();
    assertEquals(2, savedTags.size());
  }

  @Test
  public void delete_deletesAllRecipesAndTagsAssociations() {
    Tag myTag = new Tag("Neal Stephenson");
    myTag.save();
    Recipe myRecipe = new Recipe("Snow Crash");
    myRecipe.save();
    myRecipe.addTag(myTag);
    myRecipe.delete();
    assertEquals(0, myTag.getRecipes().size());
  }

  @Test
  public void findByName_returnsARecipeByName_true() {
    Recipe myRecipe = new Recipe("Snow Crash");
    myRecipe.save();
    myRecipe.save();
    assertEquals(Recipe.findByName("Snow Crash").get(1), myRecipe);
  }
  @Test
  public void findByName_returnsARecipeByNameAndNothingElse_true() {
    Recipe myRecipe = new Recipe("Snow Crash");
    myRecipe.save();
    Recipe yourRecipe = new Recipe("Goodnight Moon");
    yourRecipe.save();
    assertEquals(Recipe.findByName("Goodnight Moon").get(0), yourRecipe);
  }
  @Test
  public void findByTag_returnsARecipeByTagAndNothingElse_true() {
    Recipe myRecipe = new Recipe("Snow Crash");
    myRecipe.save();
    Tag myTag = new Tag("Neal Stephenson");
    myTag.save();
    myRecipe.addTag(myTag);
    Recipe yourRecipe = new Recipe("Goodnight Moon");
    yourRecipe.save();
    Tag yourTag = new Tag("Shakespeare");
    yourTag.save();
    yourRecipe.addTag(yourTag);
    assertEquals(Recipe.findByTag(yourTag).get(0), yourRecipe);
  }
  @Test
  public void findByTag_returnsARecipeByTagWithMultipleTags() {
    Recipe myRecipe = new Recipe("Snow Crash");
    myRecipe.save();
    Tag myTag = new Tag("Neal Stephenson");
    myTag.save();
    myRecipe.addTag(myTag);
    Tag yourTag = new Tag("Shakespeare");
    yourTag.save();
    myRecipe.addTag(yourTag);
    assertEquals(Recipe.findByTag(myTag).get(0), myRecipe);
  }
}
