package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.Adiacenza;
import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	ArtsmiaDAO dao ;
	Map<Integer,Artist> idMap;
	Graph<Artist,DefaultWeightedEdge> grafo;
	List<Artist> vertici;
	
	// PUNTO 2
	List<Artist> soluzioneMigliore;
	int pesoArco;
	
	public Model() {
		this.dao = new ArtsmiaDAO();
		this.idMap = new HashMap<>();
		this.vertici = new ArrayList<>();
	}
	
	public List<String> getRole() {
		return this.dao.listRole();
	}
	
	public String creaGrafo(String role) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// Aggiungo i vertici
		this.vertici = this.dao.getVertici(idMap,role);
		Graphs.addAllVertices(this.grafo, vertici);
		
		// Aggiungo gli archi
		for(Adiacenza a : this.dao.getArchi(idMap, role))
			if(!this.grafo.containsEdge(a.getA1(), a.getA2()))
				Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		
		return "Grafo creato!\n\n"+"Numero vertici: "+this.grafo.vertexSet().size()+"\nNumero archi: "+this.grafo.edgeSet().size();
	}
	
	public String artistiAdiacenti(String role) {
		List<Adiacenza> adiacenti = new ArrayList<>(this.dao.getArchi(idMap, role));
		Collections.sort(adiacenti);
		
		String result = "\n\nCoppie artisti connessi:\n";
		for(Adiacenza a : adiacenti)
			result += a.getA1() +" - "+a.getA2()+" ("+a.getPeso()+")\n";
		
		return result;
	}
	
	public String cercaCammino(Integer id) {
		Artist scelto = this.idMap.get(id);
		String result = "";
		if(scelto==null) {
			result += "ERRORE! L'artista inserito non è corretto!";
			return result;
		}
		
		List<Artist> parziale = new ArrayList<>();
		parziale.add(scelto);
		this.soluzioneMigliore= new ArrayList<>();
		this.pesoArco=0;
		
		ricorsione(parziale);
		
		result += "\n\nCammino trovato:\n";
		for(Artist a : this.soluzioneMigliore)
			result += a+"\n";
		
		return result;
	}

	private void ricorsione(List<Artist> parziale) {
		Artist ultimoInserito = parziale.get(parziale.size()-1);
		// Caso terminale
		if(parziale.size()>this.soluzioneMigliore.size()) {
			this.soluzioneMigliore= new ArrayList<>(parziale);
		}
		
		// ... altrimenti
		for(DefaultWeightedEdge arco : this.grafo.edgesOf(ultimoInserito)) {
			int peso = (int)this.grafo.getEdgeWeight(arco);
			Artist vicino = Graphs.getOppositeVertex(this.grafo, arco, ultimoInserito);
			
			// Se sono alla prima iterazione provo ad aggiungere un vicino
			if(parziale.size()==1) {
				List<Artist> nuovaParziale = new ArrayList<>(parziale);
				nuovaParziale.add(vicino);
				this.pesoArco = peso;
				ricorsione(nuovaParziale);
			}
			else if(peso==this.pesoArco && !parziale.contains(vicino)) { // Controllo che il peso dell'arco sia uguale a quelli precedenti
				List<Artist> nuovaParziale = new ArrayList<>(parziale);
				nuovaParziale.add(vicino);
				ricorsione(nuovaParziale);
			}
		}
	}

}
