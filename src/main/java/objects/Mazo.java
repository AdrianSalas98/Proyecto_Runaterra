package objects;

import java.util.Arrays;

public class Mazo {

	private int id_mazo;
	private String nombre_mazo;
	private int valor_mazo;
	private int[] mazo;

	public Mazo() {

	}

	public Mazo(int id_mazo, String nombre_mazo, int valor_mazo, int[] mazo) {
		super();
		this.id_mazo = id_mazo;
		this.nombre_mazo = nombre_mazo;
		this.valor_mazo = valor_mazo;
		this.mazo = mazo;
	}

	public int getId_mazo() {
		return id_mazo;
	}

	public void setId_mazo(int id_mazo) {
		this.id_mazo = id_mazo;
	}

	public String getNombre_mazo() {
		return nombre_mazo;
	}

	public void setNombre_mazo(String nombre_mazo) {
		this.nombre_mazo = nombre_mazo;
	}

	public int getValor_mazo() {
		return valor_mazo;
	}

	public void setValor_mazo(int valor_mazo) {
		this.valor_mazo = valor_mazo;
	}

	public int[] getMazo() {
		return mazo;
	}

	public void setMazo(int[] mazo) {
		this.mazo = mazo;
	}

	public String toString() {
		return "Mazo [id_mazo=" + id_mazo + ", nombre_mazo=" + nombre_mazo + ", valor_mazo=" + valor_mazo + ", mazo="
				+ Arrays.toString(mazo) + "]";
	}

}
