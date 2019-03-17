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

public class ImportPizzabodem{
	public static void main(String[] args) throws IOException{
		File excelFile = new File("C:/School/S4/pizzabodems.xlsx");
		FileInputStream fis = new FileInputStream(excelFile);
		
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		ArrayList<Pizzabodem> pizzabodems = new ArrayList<Pizzabodem>();
				
		Iterator<Row> rowIt = sheet.iterator();
		rowIt.next();
		
		while (rowIt.hasNext()) {
			Row row = rowIt.next();
			Iterator<Cell> cellIt = row.cellIterator();			
		
			while (cellIt.hasNext()) {
				String naam = cellIt.next().toString();
				Double diameter = cellIt.next().getNumericCellValue();
				String omschrijving = cellIt.next().toString();
				Double toeslag = cellIt.next().getNumericCellValue();
				String beschikbaarString = cellIt.next().toString();
				Boolean beschikbaar = false;
				if (beschikbaarString.equals("Ja")) {
					beschikbaar = true;
				} else {
					beschikbaar = false;
				}
				System.out.println("Naam: " + naam);
				System.out.println("Diameter: " + diameter.toString());
				System.out.println("Omschrijving: " + omschrijving);
				System.out.println("Toeslag: " + toeslag.toString());
				System.out.println("beschikbaar: " + beschikbaar.toString());
				Pizzabodem pizzabodem = new Pizzabodem(naam, diameter, omschrijving, toeslag, beschikbaar);
				pizzabodem.toString();
				System.out.println(pizzabodem.getNaam());
				pizzabodems.add(pizzabodem);
			}
			
			System.out.println();
		}
		workbook.close();
		fis.close();
		
		try {
			Connection conn = DriverManager.getConnection(
				     "jdbc:sqlserver://LAPTOP_RICK\\sqlexpress:1433;databaseName=Mario;integratedSecurity=true" );
			PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO dbo.PIZZABODEM (Naam, Diameter, toeslag, Omschrijving, beschikbaar) "
					+ "VALUES (?, ?, ?, ?, ?)");
			for (int k = 0; k < pizzabodems.size()-1; k++) {
				preparedStatement.setString(1, pizzabodems.get(k).getNaam());
				preparedStatement.setDouble(2, pizzabodems.get(k).getDiameter());
				preparedStatement.setDouble(3, pizzabodems.get(k).getToeslag());
				preparedStatement.setString(4, pizzabodems.get(k).getOmschrijving());
				preparedStatement.setBoolean(5, pizzabodems.get(k).getBeschikbaar());
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
			
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
}
