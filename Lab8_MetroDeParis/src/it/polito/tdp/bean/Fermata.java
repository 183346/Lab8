package it.polito.tdp.bean;

import java.util.ArrayList;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.model.FermataSuLinea;



public class Fermata {
	
	private int idFermata;
	private String nome;
	private LatLng coords;
	private List<FermataSuLinea> fermateSuLinea;
	
	
	public Fermata(int idFermata, String nome, LatLng coords) {
		super();
		this.idFermata = idFermata;
		this.nome = nome;
		this.coords = coords;
		this.fermateSuLinea = new ArrayList<FermataSuLinea>();
	}
	public Fermata(int idFermata) {
		this.idFermata = idFermata;
	}
	
	
	public int getIdFermata() {
		return idFermata;
	}
	public void setIdFermata(int id_fermata) {
		this.idFermata = id_fermata;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public LatLng getCoords() {
		return coords;
	}
	public void setCoords(LatLng coords) {
		this.coords = coords;
		
		}
	public List<FermataSuLinea> getFermateSuLinea() {
		return this.fermateSuLinea;
	}

	public void addFermataSuLinea(FermataSuLinea fermataSuLinea) {
		this.fermateSuLinea.add(fermataSuLinea);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idFermata;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fermata other = (Fermata) obj;
		if (idFermata != other.idFermata)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return   nome;
	}
	
	
	

}
