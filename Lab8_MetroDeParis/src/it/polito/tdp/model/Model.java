package it.polito.tdp.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.cycle.DirectedSimpleCycles;
import org.jgrapht.alg.cycle.JohnsonSimpleCycles;
import org.jgrapht.alg.cycle.SzwarcfiterLauerSimpleCycles;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.bean.Connessione;
import it.polito.tdp.bean.Fermata;
import it.polito.tdp.bean.Linea;
import it.polito.tdp.db.MetroDAO;





public class Model {
	
	private List<FermataSuLinea> fermateSuLinea;
	private List<Fermata> fermate = new LinkedList<Fermata>();
	private List<Connessione> connessioni = new LinkedList<Connessione>();
	private List<Linea> linee = new LinkedList<Linea>();
	private List<Fermata> risultati = new LinkedList<Fermata>();
	SimpleWeightedGraph<Fermata,DefaultWeightedEdge> grafo = new SimpleWeightedGraph<Fermata,DefaultWeightedEdge>(DefaultWeightedEdge.class);
	private List<DefaultWeightedEdge> pathEdgeList = null;
	private double pathTempoTotale = 0;
	
	
	// Directed Weighted Graph
		private DefaultDirectedWeightedGraph<FermataSuLinea, DefaultWeightedEdge> grafo2 = null;
		private DirectedGraph<FermataSuLinea, DefaultWeightedEdge> grafo3 = null;
		

		public List<Fermata> getStazioni() {

			if (fermate == null)
				throw new RuntimeException("Lista delle fermate non disponibile!");

			return fermate;
		}
		
		public void creaGrafo2() {
			
		   MetroDAO metroDAO = new MetroDAO();
			
		
			fermate = metroDAO.getAllFermata();
			linee = metroDAO.getAllLinea();
			connessioni = metroDAO.getAllConnessione(linee, fermate);
			fermateSuLinea = metroDAO.getAllFermateSuLinea(fermate, linee);

			// Directed Weighted
			grafo2 = new DefaultDirectedWeightedGraph<FermataSuLinea, DefaultWeightedEdge>(DefaultWeightedEdge.class);

			// FASE1: Aggiungo un vertice per ogni fermata
			Graphs.addAllVertices(grafo2, fermateSuLinea);

			// FASE2: Aggiungo tutte le connessioni tra tutte le fermate
			for (Connessione c : connessioni) {

				double velocita = c.getLinea().getVelocita();
				double distanza = LatLngTool.distance(c.getStazP().getCoords(), c.getStazA().getCoords(),LengthUnit.KILOMETER);
				double tempo = (distanza / velocita) * 60 * 60;

				// Cerco la fermataSuLinea corretta all'interno della lista delle fermate
				List<FermataSuLinea> fermateSuLineaPerFermata = c.getStazP().getFermateSuLinea();
				FermataSuLinea fslPartenza = fermateSuLineaPerFermata.get(fermateSuLineaPerFermata.indexOf(new FermataSuLinea(c.getStazP(), c.getLinea())));

				// Cerco la fermataSuLinea corretta all'interno della lista delle fermate
				fermateSuLineaPerFermata = c.getStazA().getFermateSuLinea();
				FermataSuLinea fslArrivo = fermateSuLineaPerFermata.get(fermateSuLineaPerFermata.indexOf(new FermataSuLinea(c.getStazA(), c.getLinea())));

				if (fslPartenza != null && fslArrivo != null) {
					// Aggiungoun un arco pesato tra le due fermate
					Graphs.addEdge(grafo2, fslPartenza, fslArrivo, tempo);
				} else {
					System.err.println("Non ho trovato fslPartenza o fslArrivo. Salto alle prossime");
				}
			}
			// FASE3: Aggiungo tutte le connessioni tra tutte le fermateSuLinee
			// della stessa Fermata
			for (Fermata fermata : fermate) {
				for (FermataSuLinea fslP : fermata.getFermateSuLinea()) {
					for (FermataSuLinea fslA : fermata.getFermateSuLinea()) {
						if (!fslP.equals(fslA)) {
							Graphs.addEdge(grafo2, fslP, fslA, fslA.getLinea().getIntervallo() * 60);
						}
					}
				}
			}	
		}
		
		
		
		
		
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

	public void calcolaPercorso(Fermata partenza, Fermata arrivo) {
		this.creaGrafo2();
		MetroDAO dao = new MetroDAO();
		fermate=dao.getAllFermata();
		System.out.println("fermate:"+fermate.toString());
		linee = dao.getAllLinea();
		System.out.println("linee:"+linee.toString());
		this.fermateSuLinea=dao.getAllFermateSuLinea(fermate, linee);
		System.out.println("Fermate su Linea:"+fermateSuLinea.toString());
		
		System.out.println("eseguo operazioni di DAO");
		for(Fermata ff:fermate){
			if(ff.equals(partenza)){
				partenza=ff;
			}
			if(ff.equals(arrivo)){
				arrivo=ff;
			}
			
			
		}
		
		DijkstraShortestPath<FermataSuLinea, DefaultWeightedEdge> dijkstra;
		

		// Usati per salvare i valori temporanei
		double pathTempoTotaleTemp;

		// Usati per salvare i valori migliori
		List<DefaultWeightedEdge> bestPathEdgeList = null;
		double bestPathTempoTotale = Double.MAX_VALUE;
		

		for (FermataSuLinea fslP : partenza.getFermateSuLinea()) {
			
			for (FermataSuLinea fslA : arrivo.getFermateSuLinea()) {
				dijkstra = new DijkstraShortestPath<FermataSuLinea, DefaultWeightedEdge>(grafo2, fslP, fslA);

				pathTempoTotaleTemp = dijkstra.getPathLength();

				if (pathTempoTotaleTemp < bestPathTempoTotale) {
					bestPathTempoTotale = pathTempoTotaleTemp;
					bestPathEdgeList = dijkstra.getPathEdgeList();
					System.out.println(dijkstra.toString());
				}
			}this.pathEdgeList = bestPathEdgeList;
			this.pathTempoTotale = bestPathTempoTotale;
			

			if (pathEdgeList == null)
				throw new RuntimeException("Non è stato creato un percorso.");

			// Nel calcolo del tempo non tengo conto della prima e dell'ultima
			if (pathEdgeList.size() - 1 > 0) {
				pathTempoTotale += (pathEdgeList.size() - 1) * 30;
			}
			}
		
	}

	public double getPercorsoTempoTotale() {
		if (pathEdgeList == null)
			throw new RuntimeException("Non è1 stato creato un percorso.");

		return pathTempoTotale;
	}

	public String getPercorsoEdgeList() {
		if (pathEdgeList == null)
			throw new RuntimeException("Non è2 stato creato un percorso.");

		StringBuilder risultato = new StringBuilder();
		risultato.append("Percorso:\n\n");

		Linea lineaTemp = grafo2.getEdgeTarget(pathEdgeList.get(0)).getLinea();
		risultato.append("Prendo Linea: " + lineaTemp.getNome() + "\n[");

		for (DefaultWeightedEdge edge : pathEdgeList) {
			
			risultato.append(grafo2.getEdgeTarget(edge).getNome());

			if (!grafo2.getEdgeTarget(edge).getLinea().equals(lineaTemp)) {
				risultato.append("]\n\nCambio su Linea: " + grafo2.getEdgeTarget(edge).getLinea().getNome() + "\n[");
				lineaTemp = grafo2.getEdgeTarget(edge).getLinea();
				
			} else {
				risultato.append(", ");
			}
		}
		risultato.setLength(risultato.length() - 2);
		risultato.append("]");

		return risultato.toString();
	}

	public String doCiclo() {
		String result="";
		this.creaGrafo2();
		DirectedSimpleCycles<FermataSuLinea, DefaultWeightedEdge> contaCircuiti= new SzwarcfiterLauerSimpleCycles<FermataSuLinea, DefaultWeightedEdge>(grafo2);
		List<FermataSuLinea> listatemporanea = new LinkedList<FermataSuLinea>();
		/*List<List<FermataSuLinea>> circuiti=contaCircuiti.findSimpleCycles();
		/*int massimo=0;
		for(List<FermataSuLinea> circu:circuiti){
			if(massimo<circu.size()){listatemporanea=circu;massimo=circu.size();}
		}
		for(FermataSuLinea lt:listatemporanea){
			result=result+"stazione :  "+ lt.getNome()+"  in linea :  " + lt.getLinea().getNome()+"\n";
		}*/
		//risultati = Graphs.getPathVertexList(dk.getPath());
		grafo3=contaCircuiti.getGraph();
		Set<DefaultWeightedEdge> pathEdgeList1 = null;
		pathEdgeList1=grafo3.edgeSet();
		
		for(DefaultWeightedEdge ddd:pathEdgeList1){
			
			result=result+"stazione :  " + grafo3.getEdgeSource(ddd).getNome()+"  "+grafo3.getEdgeSource(ddd).getLinea().getNome()+"\n";
		}
		
		return result;
	}

}
