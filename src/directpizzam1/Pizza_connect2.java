package directpizzam1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Pizza_connect2 {

	public static void main(String[] args) {
		 	
		System.out.println("connection a mysql pizzaboxinnodb");
			
		String url = "jdbc:mysql://localhost/pizza";
		
		String login = "root";
		String passwd = "";
		
		Connection cn =null; 
		Statement st =null;
		ResultSet rs =null;
 
		try {
			// Etape 1 : Chargement du driver
			Class.forName("com.mysql.jdbc.Driver");
			
			// Etape 2 : récupération de la connexion
			cn = DriverManager.getConnection(url, login, passwd);

			// Etape 3 : Création d'un statement
			st = cn.createStatement();

			String sql = "SELECT * FROM PIZZA  "   ;

			// Etape 4 : exécution requête
			rs = st.executeQuery(sql);
			
			// Si récup données alors étapes 5 (parcours Resultset)
			System.out.println(rs);
			
			while (rs.next()) {
				System.out.println("-----------------------------------");
				System.out.println(rs.getString("DESIGNPIZZ"));
				System.out.println(rs.getFloat("TARIFPIZZ"));
				System.out.println(rs.getInt("NROPIZZ"));
				//	Pizza p2 = new Pizza("8 fromages",145.20,250);
			}
			 
			
		} catch (ClassNotFoundException e) {
		 
			e.printStackTrace();
		} catch (SQLException e) {
	 
			e.printStackTrace();
		}
		
		
		System.out.println("**************************");
	/*	Pizza p1= new Pizza();
		p1.nomPizza=" 3 fromages";
		p1.prixPizza=125;
		System.out.println(p1);
		System.out.println(p1.nomPizza);
		System.out.println(p1.prixPizza);*/
		
		pizza p2 = new pizza("8 fromages",145.20,250);
		System.out.println(p2);
		System.out.println(p2.nomPizza);
		System.out.println(p2.prixPizza);
		

	}//fin de la fonction main

}//fin de la classe