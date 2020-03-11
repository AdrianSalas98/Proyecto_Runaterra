package objects;

import java.util.ArrayList;

public class Usuario {

	private int id_usuario;
	private String contrasenya_usuario;
	private String nombre_usuario;
	private ArrayList<Integer> mazos_usuario;
	private ArrayList<Integer> coleccion_cartas;

	public Usuario() {

	}

	public Usuario(int id_usuario, String contrasenya_usuario, String nombre_usuario, ArrayList<Integer> mazos_usuario,
			ArrayList<Integer> coleccion_cartas) {
		super();
		this.id_usuario = id_usuario;
		this.contrasenya_usuario = contrasenya_usuario;
		this.nombre_usuario = nombre_usuario;
		this.mazos_usuario = mazos_usuario;
		this.coleccion_cartas = coleccion_cartas;
	}

	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getContrasenya_usuario() {
		return contrasenya_usuario;
	}

	public void setContrasenya_usuario(String contrasenya_usuario) {
		this.contrasenya_usuario = contrasenya_usuario;
	}

	public String getNombre_usuario() {
		return nombre_usuario;
	}

	public void setNombre_usuario(String nombre_usuario) {
		this.nombre_usuario = nombre_usuario;
	}

	public ArrayList<Integer> getMazos_usuario() {
		return mazos_usuario;
	}

	public void setMazos_usuario(ArrayList<Integer> mazos_usuario) {
		this.mazos_usuario = mazos_usuario;
	}

	public ArrayList<Integer> getColeccion_cartas() {
		return coleccion_cartas;
	}

	public void setColeccion_cartas(ArrayList<Integer> coleccion_cartas) {
		this.coleccion_cartas = coleccion_cartas;
	}

	@Override
	public String toString() {
		return "Usuario [id_usuario=" + id_usuario + ", contrasenya_usuario=" + contrasenya_usuario
				+ ", nombre_usuario=" + nombre_usuario + ", mazos_usuario=" + mazos_usuario + ", coleccion_cartas="
				+ coleccion_cartas + "]";
	}

	

}
