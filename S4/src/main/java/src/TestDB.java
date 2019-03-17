package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class TestDB {
	
	
	public static void main(String[] args) {
		Connection conn;
		try {
			conn = DriverManager.getConnection(
				     "jdbc:sqlserver://LAPTOP_RICK\\sqlexpress:1433;databaseName=Mario;integratedSecurity=true" );
			PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO dbo.KLANT (Gebruikersnaam" + 
					"      ,Wachtwoord" + 
					"      ,Voornaam" + 
					"      ,Achternaam" + 
					"      ,Emailadres" + 
					"      ,Telefoon)"
					+ "VALUES (?, ?, ?, ?, ?, ?)");
				preparedStatement.setString(1, "TestNaam");
				preparedStatement.setString(2, "testWachtw7oord");
				preparedStatement.setString(3, "testVoornaam");
				preparedStatement.setString(4, "testAchternaam");
				preparedStatement.setString(5, "testl");
				preparedStatement.setString(6, "0623456789");
				
				
			preparedStatement.execute();
			
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
