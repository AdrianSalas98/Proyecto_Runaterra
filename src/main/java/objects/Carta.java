package objects;

public class Carta {
	private int id;
	private String tipo;
	private String nombre_carta;
	private int coste_invocacion;
	private int ataque;
	private int vida;
	private String habilidad_especial;
	private String faccion;

	public Carta() {

	}

	public Carta(int id, String tipo, String nombre_carta, int coste_invocacion, int ataque, int vida,
			String habilidad_especial, String faccion) {
		this.id = id;
		this.tipo = tipo;
		this.nombre_carta = nombre_carta;
		this.coste_invocacion = coste_invocacion;
		this.ataque = ataque;
		this.vida = vida;
		this.habilidad_especial = habilidad_especial;
		this.faccion = faccion;
	}

	public String toString() {
		return "Carta [id=" + id + ", tipo=" + tipo + ", nombre_carta=" + nombre_carta + ", coste_invocacion="
				+ coste_invocacion + ", ataque=" + ataque + ", vida=" + vida + ", habilidad_especial="
				+ habilidad_especial + ", faccion=" + faccion + "]";
	}

}
