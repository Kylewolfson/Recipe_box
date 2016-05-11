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
    Recipe myRecipe = new Recipe("Snow Peas");
    assertEquals(true, myRecipe instanceof Recipe);
  }

  @Test
  public void getName_recipeInstantiatesWithName_String() {
    Recipe myRecipe = new Recipe("Moon Pie");
    assertEquals("Moon Pie", myRecipe.getName());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Recipe.all().size(), 0);
  }


  @Test
  public void save_assignsIdToObject() {
    Recipe myRecipe = new Recipe("Snow Peas");
    myRecipe.save();
    Recipe savedRecipe = Recipe.all().get(0);
    assertEquals(myRecipe.getId(), savedRecipe.getId());
  }

  @Test
  public void find_findsRecipeInDatabase_true() {
    Recipe myRecipe = new Recipe("Snow Peas");
    myRecipe.save();
    Recipe savedRecipe = Recipe.find(myRecipe.getId());
    assertTrue(myRecipe.equals(savedRecipe));
  }

  @Test
  public void addTag_addTagToRecipe() {
    Tag myTag = new Tag("Mexican");
    myTag.save();
    Recipe myRecipe = new Recipe("Enchiladas");
    myRecipe.save();
    myRecipe.addTag(myTag);
    Tag savedTag = myRecipe.getTags().get(0);
    assertTrue(myTag.equals(savedTag));
  }

  @Test
  public void getTags_returnsAllTags_List() {
    Tag myTag = new Tag("Mexican");
    myTag.save();
    Tag yourTag = new Tag("Russian");
    myTag.save();
    Recipe myRecipe = new Recipe("Snow Peas");
    myRecipe.save();
    myRecipe.addTag(myTag);
    myRecipe.addTag(yourTag);
    List savedTags = myRecipe.getTags();
    assertEquals(2, savedTags.size());
  }

  @Test
  public void delete_deletesAllRecipesAndTagsAssociations() {
    Tag myTag = new Tag("Chinese");
    myTag.save();
    Recipe myRecipe = new Recipe("Snow Peas");
    myRecipe.save();
    myRecipe.addTag(myTag);
    myRecipe.delete();
    assertEquals(0, myTag.getRecipes().size());
  }

  @Test
  public void findByName_returnsARecipeByName_true() {
    Recipe myRecipe = new Recipe("Snow Peas");
    myRecipe.save();
    myRecipe.save();
    assertEquals(Recipe.findByName("Snow Peas").get(1), myRecipe);
  }
  @Test
  public void findByName_returnsARecipeByNameAndNothingElse_true() {
    Recipe myRecipe = new Recipe("Snow Peas");
    myRecipe.save();
    Recipe yourRecipe = new Recipe("Moon Pie");
    yourRecipe.save();
    assertEquals(Recipe.findByName("Moon Pie").get(0), yourRecipe);
  }
  @Test
  public void findByTag_returnsARecipeByTagAndNothingElse_true() {
    Recipe myRecipe = new Recipe("Snow Peas");
    myRecipe.save();
    Tag myTag = new Tag("Mexican");
    myTag.save();
    myRecipe.addTag(myTag);
    Recipe yourRecipe = new Recipe("Moon Pie");
    yourRecipe.save();
    Tag yourTag = new Tag("Dessert");
    yourTag.save();
    yourRecipe.addTag(yourTag);
    assertEquals(Recipe.findByTag(yourTag).get(0), yourRecipe);
  }
  @Test
  public void findByTag_returnsARecipeByTagWithMultipleTags() {
    Recipe myRecipe = new Recipe("Borscht");
    myRecipe.save();
    Tag myTag = new Tag("Lithuanian");
    myTag.save();
    myRecipe.addTag(myTag);
    Tag yourTag = new Tag("Soup");
    yourTag.save();
    myRecipe.addTag(yourTag);
    assertEquals(Recipe.findByTag(myTag).get(0), myRecipe);
  }

  @Test
  public void addIngredient_addsIngredientsToRecipe_True() {
    Recipe myRecipe = new Recipe("Baked Potatoes");
    myRecipe.save();
    Ingredient myIngredient = new Ingredient("Potatoes");
    myIngredient.save();
    myRecipe.addIngredient(myIngredient);
    Ingredient savedIngredient = myRecipe.getIngredients().get(0);
    assertTrue(myIngredient.equals(savedIngredient));
  }

}
