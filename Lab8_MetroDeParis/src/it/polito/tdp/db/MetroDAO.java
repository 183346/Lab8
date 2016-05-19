
package it.polito.tdp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;


import it.polito.tdp.bean.Connessione;
import it.polito.tdp.bean.Fermata;
import it.polito.tdp.bean.Linea;
import it.polito.tdp.model.FermataSuLinea;




public class MetroDAO {
	
	
	
	
	
	public List<Fermata> getAllFermata() {
		final String sql = "SELECT id_fermata, nome, coordx, coordy FROM fermata ORDER BY nome ASC" ;
		
		List<Fermata> l = new ArrayList<Fermata>() ;
		
		Connection conn = DBConnect.getConnection() ;
		try {
			
			
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			while( rs.next() ) {
				Fermata f = new Fermata(
						rs.getInt("id_Fermata"),
						rs.getString("nome"),
						new LatLng(rs.getDouble("coordx"), rs.getDouble("coordy")) ) ;
				l.add(f) ;
			}
						
			st.close() ;
			conn.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e) ;
		}
		
		return l ;
	}
public List<Connessione> getAllConnessione(List <Linea> linee, List <Fermata> fermate) {
		
	    // la linea mi serve perchè quando costruisco le Connessioni non uso l'ID ma l'intero oggetto
		final String sql = "select id_connessione, id_linea, id_stazP, id_stazA from connessione" ;
		
		List<Connessione> l = new ArrayList<Connessione>() ;
		Connection conn = DBConnect.getConnection() ;

		try {
			
			
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			while( rs.next() ) {
				
				int idLinea = rs.getInt("id_linea") ;
				int idStazP = rs.getInt("id_stazP") ;
				int idStazA = rs.getInt("id_stazA") ;
				
				Linea myLinea = linee.get(linee.indexOf(new Linea(idLinea, null, 0, 0))) ;
				Fermata myStazP = fermate.get(fermate.indexOf(new Fermata(idStazP, null, null))) ;
				Fermata myStazA = fermate.get(fermate.indexOf(new Fermata(idStazA, null, null))) ;
				
				
				Connessione cnx = new Connessione(
						rs.getInt("id_connessione"),
						myLinea,
						myStazP,
						myStazA ) ;
				
				l.add(cnx) ;
				
			}
						
			st.close() ;
			conn.close();
			
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e) ;
		}
		return l ;
	}
public List<Linea> getAllLinea() {
	final String sql = "SELECT id_linea, nome, velocita, intervallo FROM linea ORDER BY nome ASC" ;
	
	List<Linea> l = new ArrayList<Linea>() ;
	Connection conn = DBConnect.getConnection() ;
	try {
		
		
		PreparedStatement st = conn.prepareStatement(sql) ;
		
		ResultSet rs = st.executeQuery() ;
		
		while( rs.next() ) {
			Linea f = new Linea(
					rs.getInt("id_linea"),
					rs.getString("nome"),
					rs.getDouble("velocita"),
					rs.getDouble("intervallo")) ;
			l.add(f) ;
		}
					
		st.close() ;
		conn.close();
	
	} catch (SQLException e) {
		e.printStackTrace();
		throw new RuntimeException(e) ;
	}
	
	return l ;
}
public List<FermataSuLinea> getAllFermateSuLinea(List<Fermata> fermate, List<Linea> linee) {

	final String sql = "SELECT DISTINCT fermata.id_fermata, linea.id_linea FROM fermata, linea, connessione WHERE (fermata.id_fermata = connessione.id_stazP OR fermata.id_fermata = connessione.id_stazA) AND connessione.id_linea = linea.id_linea";
	List<FermataSuLinea> fermateSuLinea = new ArrayList<FermataSuLinea>();
	Connection conn = DBConnect.getConnection() ;

	try {
		
		PreparedStatement st = conn.prepareStatement(sql);
		ResultSet rs = st.executeQuery();

		while (rs.next()) {

			int idLinea = rs.getInt("id_linea");
			int idFermata = rs.getInt("id_fermata");

			// Creo un nuovo oggetto, per trovarne uno esistente
			Linea linea = linee.get(linee.indexOf(new Linea(idLinea)));
			Fermata fermata = fermate.get(fermate.indexOf(new Fermata(idFermata)));

			FermataSuLinea fermataSuLinea = new FermataSuLinea(fermata, linea);
			fermata.addFermataSuLinea(fermataSuLinea);
			fermateSuLinea.add(fermataSuLinea);
			//System.out.println("sto caricando "+fermateSuLinea.indexOf(fermataSuLinea)+"  " +fermataSuLinea.getNome());
		}

		st.close();
		conn.close();

	} catch (SQLException e) {
		e.printStackTrace();
		throw new RuntimeException("Errore di connessione al Database.");
	}

	return fermateSuLinea;
}

}
