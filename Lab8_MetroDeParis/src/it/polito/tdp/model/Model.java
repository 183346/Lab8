package it.polito.tdp.model;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.bean.Connessione;
import it.polito.tdp.bean.Fermata;
import it.polito.tdp.bean.Linea;
import it.polito.tdp.db.MetroDAO;

public class Model {
	
	List<Fermata> fermate = new LinkedList<Fermata>();
	List<Connessione> connessioni = new LinkedList<Connessione>();
	List<Linea> linee = new LinkedList<Linea>();
	List<Fermata> risultati = new LinkedList<Fermata>();
	SimpleWeightedGraph<Fermata,DefaultWeightedEdge> grafo = new SimpleWeightedGraph<Fermata,DefaultWeightedEdge>(DefaultWeightedEdge.class);

	public String calcolaDistanza(Fermata partenza, Fermata arrivo) {
		String result="";
		//Creazione del Grafo
		// prima cosa caricare tutti i vertici , cioè tutte le fermate-stazioni
		MetroDAO dao = new MetroDAO();
		fermate=dao.getAllFermata();
		Graphs.addAllVertices(grafo, fermate);
		//costruzione degli archi con peso
		linee=dao.getAllLinea();
		connessioni=dao.getAllConnessione(linee, fermate);
		//
		for(Connessione c:connessioni){
        DefaultWeightedEdge arco = grafo.addEdge(c.getStazP(), c.getStazA());
			
			if (arco!=null) {
				double velocita = c.getLinea().getVelocita();
				
				double distanza = LatLngTool.distance(c.getStazP().getCoords(), c.getStazA().getCoords(), LengthUnit.KILOMETER);
				
				double tempo = (distanza / velocita) * 60;
				
				grafo.setEdgeWeight(arco, tempo);
			}}
			
			
			
				DijkstraShortestPath<Fermata,DefaultWeightedEdge> dk = new DijkstraShortestPath<Fermata,DefaultWeightedEdge>(grafo,partenza,arrivo);
				double peso = dk.getPathLength();
				//result = result + stazione.getNome() + " = " + peso + "\n";
				
				risultati = Graphs.getPathVertexList(dk.getPath());
				int nFermate=risultati.size();
				for(Fermata f: risultati){
				result= result+ " Stazione:   "+f.getNome()+ "\n";
				
			
			
				
			
		}result = result+String.format("tempo di percorrenza totale (minuti): %.2f " , peso+nFermate*0.5)+"\n";
		
		
		
		

		
		
		
		
		
		return result;
	}

}
