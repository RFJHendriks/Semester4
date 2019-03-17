package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.microsoft.sqlserver.jdbc.*;

public class ImportIngredienten{
	public static void main(String[] args) throws IOException{
		File excelFile = new File("C:/School/S4/pizza_ingredienten.xlsx");
		FileInputStream fis = new FileInputStream(excelFile);
		
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		ArrayList<String> categorien = new ArrayList<String>();
		ArrayList<String> subcategorien = new ArrayList<String>();
		ArrayList<String> productnamen = new ArrayList<String>();
		ArrayList<String> pizzasauzen = new ArrayList<String>();
		ArrayList<String> ingredients = new ArrayList<String>();
		
				
		Iterator<Row> rowIt = sheet.iterator();
		rowIt.next();
		
		while (rowIt.hasNext()) {
			Row row = rowIt.next();
			Iterator<Cell> cellIt = row.cellIterator();			
		
			while (cellIt.hasNext()) {
				String categorienaam = cellIt.next().toString();
				String subcategorienaam = cellIt.next().toString();
				String productnaam = cellIt.next().toString();
				String productomschrijving = cellIt.next().toString();
				Double prijs = cellIt.next().getNumericCellValue();
				Double bezorgtoeslag = cellIt.next().getNumericCellValue();
				String spicyString = cellIt.next().toString();
				Boolean spicy = false;
				if (spicyString.equals("Ja")) {
					spicy = true;
				} else {
					spicy = false;
				}
				String vegetarischString = cellIt.next().toString();
				Boolean vegetarisch = false;
				if (vegetarischString.equals("Ja")) {
					vegetarisch = true;
				} else {
					vegetarisch = false;
				}
				String beschikbaarString = cellIt.next().toString();
				Boolean beschikbaar = false;
				if (beschikbaarString.equals("Ja")) {
					beschikbaar = true;
				} else {
					beschikbaar = false;
				}
				int aantalkeerIngredient = (int) cellIt.next().getNumericCellValue();
				String ingredientnaam = cellIt.next().toString();
				String pizzasaus = cellIt.next().toString();
				// Categorie toevoegen aan database
				if (!categorien.contains(categorienaam)) {
					categorien.add(categorienaam);
					try {
						Connection conn = DriverManager.getConnection(
							     "jdbc:sqlserver://LAPTOP_RICK\\sqlexpress:1433;databaseName=Mario;integratedSecurity=true" );
						PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO dbo.CATEGORIE (Naam) "
								+ "VALUES (?)");
						preparedStatement.setString(1, categorienaam);
						preparedStatement.execute();
						conn.close();
						System.out.println("hallo, ik heb een categorie toegevoegd");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// Subcategorie toevoegen aan database
				if (!subcategorien.contains(subcategorienaam)) {
					subcategorien.add(subcategorienaam);
					try {
						Connection conn = DriverManager.getConnection(
							     "jdbc:sqlserver://LAPTOP_RICK\\sqlexpress:1433;databaseName=Mario;integratedSecurity=true" );
						PreparedStatement preparedStatement = conn.prepareStatement("Select CategorieId from Categorie where Naam = ?");
						preparedStatement.setString(1, categorienaam);
						ResultSet resultset = preparedStatement.executeQuery();
						resultset.next();
						int categorieId = resultset.getInt(1);
						preparedStatement = conn.prepareStatement("INSERT INTO dbo.SUBCATEGORIE (CategorieId, Naam) "
								+ "VALUES (?, ?)");
						preparedStatement.setInt(1, categorieId);
						preparedStatement.setString(2, subcategorienaam);
						preparedStatement.execute();
						conn.close();
						System.out.println("hallo, ik heb een subcategorie toegevoegd");
						} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// Pizzasaus toevoegen aan database
				if (!pizzasauzen.contains(pizzasaus)) {
					pizzasauzen.add(pizzasaus);
					try {
						Connection conn = DriverManager.getConnection(
							     "jdbc:sqlserver://LAPTOP_RICK\\sqlexpress:1433;databaseName=Mario;integratedSecurity=true" );
						PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO dbo.PizzaSaus (Naam) "
								+ "VALUES (?)");
						preparedStatement.setString(1, pizzasaus);
						preparedStatement.execute();
						conn.close();
						System.out.println("hallo, ik heb een pizzasaus toegevoegd");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				// Product toevoegen aan database
				if (!productnamen.contains(productnaam)) {
					productnamen.add(productnaam);
					try {
						Connection conn = DriverManager.getConnection(
							     "jdbc:sqlserver://LAPTOP_RICK\\sqlexpress:1433;databaseName=Mario;integratedSecurity=true" );
						//koppelen aan juiste subcategorie in database
						PreparedStatement preparedStatement = conn.prepareStatement("Select SubcategorieId from Subcategorie where Naam = ?");
						preparedStatement.setString(1, subcategorienaam);
						ResultSet resultset = preparedStatement.executeQuery();
						resultset.next();
						int subcategorieId = resultset.getInt(1);
						// ervan uitgegaan dat alle pizza's standaard met een medium pizza bodem geleverd worden.
						preparedStatement = conn.prepareStatement("Select PizzaBodemId from PizzaBodem where Naam = ?");
						preparedStatement.setString(1, "Medium Pizza");
						resultset = preparedStatement.executeQuery();
						resultset.next();
						int pizzaBodemId = resultset.getInt(1);
						//pizzasaus koppelen aan product
						preparedStatement = conn.prepareStatement("Select PizzaSausId from PizzaSaus where Naam = ?");
						preparedStatement.setString(1, pizzasaus);
						resultset = preparedStatement.executeQuery();
						resultset.next();
						int pizzaSausId = resultset.getInt(1);
						//juiste informatie in database zetten
						preparedStatement = conn.prepareStatement("INSERT INTO dbo.PRODUCT ("
								+ "Naam, "
								+ "Omschrijving, "
								+ "Beschikbaar, "
								+ "PizzaBodemId, "
								+ "PizzaSausId, "
								+ "SubcategorieId, "
								+ "Vegetarisch, "
								+ "Spicy) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
						preparedStatement.setString(1, productnaam);
						preparedStatement.setString(2, productomschrijving);
						preparedStatement.setBoolean(3, beschikbaar);
						preparedStatement.setInt(4, pizzaBodemId);
						preparedStatement.setInt(5, pizzaSausId);
						preparedStatement.setInt(6, subcategorieId);
						preparedStatement.setBoolean(7, vegetarisch);
						preparedStatement.setBoolean(8, spicy);
						preparedStatement.execute();
						System.out.println("hallo, ik heb een product toegevoegd");
						//Prijs toevoegen aan juiste product.
						preparedStatement = conn.prepareStatement("Select ProductId from Product where Naam = ?");
						preparedStatement.setString(1, productnaam);
						resultset = preparedStatement.executeQuery();
						resultset.next();
						int productId = resultset.getInt(1);
						preparedStatement = conn.prepareStatement("INSERT INTO dbo.Product_Prijs (Prijs, Begindatum, Bezorgtoeslag, ProductId) VALUES ( ? , ? , ?, ? )");
						preparedStatement.setDouble(1, prijs);
						preparedStatement.setDate(2, java.sql.Date.valueOf("2010-01-01"));
						preparedStatement.setDouble(3, bezorgtoeslag);
						preparedStatement.setInt(4, productId);
						preparedStatement.execute();
						System.out.println("hallo, ik heb een prijs aan een product gekoppeld");
						conn.close();
						} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						}
					}
					if (!ingredients.contains(ingredientnaam)) {
						ingredients.add(ingredientnaam);
						try {
							Connection conn = DriverManager.getConnection(
								     "jdbc:sqlserver://LAPTOP_RICK\\sqlexpress:1433;databaseName=Mario;integratedSecurity=true" );
							PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO dbo.Ingredient (Naam) "
									+ "VALUES (?)");
							preparedStatement.setString(1, ingredientnaam);
							preparedStatement.execute();
							conn.close();
							System.out.println("hallo, ik heb een ingredient toegevoegd");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					//Product aan aantal ingredienten koppelen
					try {
						Connection conn = DriverManager.getConnection(
							     "jdbc:sqlserver://LAPTOP_RICK\\sqlexpress:1433;databaseName=Mario;integratedSecurity=true" );
						PreparedStatement preparedStatement = conn.prepareStatement("Select ProductId from Product where Naam = ?");
						preparedStatement.setString(1, productnaam);
						ResultSet resultset = preparedStatement.executeQuery();
						resultset.next();
						int productId = resultset.getInt(1);
						preparedStatement = conn.prepareStatement("Select IngredientId from Ingredient where Naam = ?");
						preparedStatement.setString(1, ingredientnaam);
						resultset = preparedStatement.executeQuery();
						resultset.next();
						int ingredientId = resultset.getInt(1);
						preparedStatement = conn.prepareStatement("INSERT INTO dbo.Product_Ingredient (ProductId, IngredientId, Aantal) "
								+ "VALUES ( ? , ? , ? )");
						preparedStatement.setInt(1, productId);
						preparedStatement.setInt(2, ingredientId);
						preparedStatement.setInt(3, aantalkeerIngredient);
						preparedStatement.execute();
						conn.close();
						System.out.println("hallo, ik heb een product en ingredient gekoppeld");
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		workbook.close();
		fis.close();
		}
	}
}	
