import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      model.put("tags", Tag.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/recipes", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("recipes", Recipe.all());
      model.put("template", "templates/recipes.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/recipes", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Recipe newRecipe = new Recipe(name);
      newRecipe.save();
      response.redirect("/recipes");
      return null;
    });

    get("/recipes/:id", (request,response) ->{
      Map<String, Object> model = new HashMap<String, Object>();
      Recipe recipe = Recipe.find(Integer.parseInt(request.params("id")));
      model.put("recipe", recipe);
      model.put("allTags", Tag.all());
      model.put("ingredientsAndQuantity", recipe.getIngredientsAndQuantity());
      model.put("template", "templates/recipe.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/recipes/:id/edit", (request,response) ->{
      Map<String, Object> model = new HashMap<String, Object>();
      Recipe recipe = Recipe.find(Integer.parseInt(request.params("id")));
      model.put("recipe", recipe);
      model.put("allTags", Tag.all());
      model.put("allIngredients", Ingredient.all());
      model.put("ingredientsAndQuantity", recipe.getIngredientsAndQuantity());
      model.put("template", "templates/recipe-edit.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add_tags", (request, response) -> {
      int recipeId = Integer.parseInt(request.queryParams("recipe_id"));
      int tagId = Integer.parseInt(request.queryParams("tag_id"));
      Tag tag = Tag.find(tagId);
      Recipe recipe = Recipe.find(recipeId);
      recipe.addTag(tag);
      response.redirect("/recipes/" + recipeId + "/edit");
      return null;
    });

    post("/edit_instructions", (request, response) -> {
      int recipeId = Integer.parseInt(request.queryParams("recipe_id"));
      String instructions = request.queryParams("instructions");
      Recipe recipe = Recipe.find(recipeId);
      recipe.editInstructions(instructions);
      response.redirect("/recipes/" + recipeId + "/edit");
      return null;
    });

    post("/add_ingredient", (request, response) -> {
      int recipeId = Integer.parseInt(request.queryParams("recipe_id"));
      Integer ingredientId = Integer.parseInt(request.queryParams("ingredient_id"));
      Ingredient ingredient = Ingredient.find(ingredientId);
      String measurementType = request.queryParams("measurement");
      double quantity = Double.parseDouble(request.queryParams("quantity"));
      Recipe recipe = Recipe.find(recipeId);
      recipe.addIngredient(ingredient, quantity, measurementType);
      response.redirect("/recipes/" + recipeId + "/edit");
      return null;
    });

    get("new_tag", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("allTags", Tag.all());
      model.put("template", "templates/new_tag.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/new_tag", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Tag newTag = new Tag(name);
      newTag.save();
      response.redirect("/new_tag");
      return null;
    });

    get("/tags/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Tag tag = Tag.find(Integer.parseInt(request.params("id")));
      model.put("tag", tag);
      model.put("recipes", Recipe.findByTag(tag));
      model.put("allRecipes", Recipe.all());
      model.put("template", "templates/tag.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add_recipes", (request, response) -> {
      int recipeId = Integer.parseInt(request.queryParams("recipe_id"));
      int tagId = Integer.parseInt(request.queryParams("tag_id"));
      Tag tag = Tag.find(tagId);
      Recipe recipe = Recipe.find(recipeId);
      tag.addRecipe(recipe);
      response.redirect("/tags/" + tagId);
      return null;
    });

    get("/ingredients", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("allIngredients", Ingredient.all());
      model.put("template", "templates/ingredients.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add_new_ingredient", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String fixins = request.queryParams("fixins");
      Ingredient newIngredient = new Ingredient(fixins);
      newIngredient.save();
      response.redirect("/ingredients");
      return null;
    });

    get("/ingredients/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Ingredient ingredient = Ingredient.find(Integer.parseInt(request.params("id")));
      model.put("ingredient", ingredient);
      model.put("template", "templates/ingredient.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
