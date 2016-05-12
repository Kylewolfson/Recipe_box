import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class IngredientAndQuantity {
  private int id;
  private String fixins;
  private double quantity;
  private String measurement;

  public IngredientAndQuantity(String fixins, double quantity, String measurement) {
    this.fixins = fixins;
    this.quantity = quantity;
    this.measurement = measurement;
  }

  public String getFixins() {
    return fixins;
  }

  public double getQuantity() {
    return quantity;
  }

  public String getMeasurement() {
    return measurement;
  }
}
