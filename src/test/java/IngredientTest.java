import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

public class IngredientTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Ingredient_instantiatesCorrectly_true() {
    Ingredient myIngredient = new Ingredient("Recipe lover");
    assertEquals(true, myIngredient instanceof Ingredient);
  }

  @Test
  public void Ingredient_savesToDatabase_true() {
    Ingredient myIngredient = new Ingredient("Recipe lover");
    myIngredient.save();
    Ingredient savedIngredient = Ingredient.find(myIngredient.getId());
    assertTrue(myIngredient.equals(savedIngredient));
  }
}
