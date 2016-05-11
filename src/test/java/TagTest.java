import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class TagTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Tag_instantiatesCorrectly_true() {
    Tag myTag = new Tag("Sir Arthur Conan Doyle");
    assertEquals(true, myTag instanceof Tag);
  }

  @Test
  public void getName_tagInstantiatesWithTag_String() {
    Tag myTag = new Tag("Sir Arthur Conan Doyle");
    assertEquals("Sir Arthur Conan Doyle", myTag.getTag());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Tag.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAretheSame() {
    Tag firstTag = new Tag("Sir Arthur Conan Doyle");
    Tag secondTag = new Tag("Sir Arthur Conan Doyle");
    assertTrue(firstTag.equals(secondTag));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Tag myTag = new Tag("Sir Arthur Conan Doyle");
    myTag.save();
    Tag savedTag = Tag.find(myTag.getId());
    assertTrue(myTag.equals(savedTag));
  }

  @Test
  public void save_assignsIdToObject_int() {
    Tag myTag = new Tag("Sir Arthur Conan Doyle");
    myTag.save();
    Tag savedTag = Tag.all().get(0);
    assertEquals(myTag.getId(), savedTag.getId());
  }

  @Test
  public void find_findTagInDatabase_true() {
    Tag myTag = new Tag("Sir Arthur Conan Doyle");
    myTag.save();
    Tag savedTag = Tag.find(myTag.getId());
    assertTrue(myTag.equals(savedTag));
  }

  @Test
  public void addRecipe_addRecipeToTag_true() {
    Tag myTag = new Tag("Sir Arthur Conan Doyle");
    myTag.save();
    Recipe myRecipe = new Recipe("Sherlock Mows The Lawn");
    myRecipe.save();
    myTag.addRecipe(myRecipe);
    Recipe savedRecipe = myTag.getRecipes().get(0);
    assertTrue(myRecipe.equals(savedRecipe));
  }

  @Test
  public void getRecipes_returnsAllRecipes_List() {
    Tag myTag = new Tag("Sir Arthur Conan Doyle");
    myTag.save();
    Recipe myRecipe = new Recipe("Sherlock Mows The Lawn");
    myRecipe.save();
    myTag.addRecipe(myRecipe);
    List savedRecipes = myTag.getRecipes();
    assertEquals(1, savedRecipes.size());
  }

  @Test
  public void delete_deletesAllRecipesAndTagsAssociations() {
    Tag myTag = new Tag("Sir Arthur Conan Doyle");
    myTag.save();
    Recipe myRecipe = new Recipe("Sherlock Mows The Lawn");
    myRecipe.save();
    myTag.addRecipe(myRecipe);
    myTag.delete();
    assertEquals(0, myRecipe.getTags().size());
  }
}
