package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import conector.Conexion;
import objects.Carta;
import objects.Mazo;
import objects.Usuario;

public class Main {

	public static void main(String[] args) {
		disableLogs();
		MongoClient mongo;
		try {
			mongo = Conexion.crearConexion();
			cargarCartas(mongo);
			cargarUsuarios(mongo);
			cargarMazos(mongo);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

	private static void disableLogs() {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);
		Logger.getLogger("org.mongodb.driver.connection").setLevel(Level.OFF);
		Logger.getLogger("org.mongodb.driver.management").setLevel(Level.OFF);
		Logger.getLogger("org.mongodb.driver.cluster").setLevel(Level.OFF);
		Logger.getLogger("org.mongodb.driver.protocol.insert").setLevel(Level.OFF);
		Logger.getLogger("org.mongodb.driver.protocol.query").setLevel(Level.OFF);
		Logger.getLogger("org.mongodb.driver.protocol.update").setLevel(Level.OFF);
	}

	public static void generaCarta(MongoClient mongo) {
		MongoDatabase db = mongo.getDatabase("Runaterra");
		MongoCollection<Document> collection = db.getCollection("Cartas");

		Scanner lectorInt = new Scanner(System.in);
		Scanner lectorString = new Scanner(System.in);

		System.out.println("Introduce ID de carta: ");
		int id = lectorInt.nextInt();
		System.out.println("Introduce el tipo de carta: ");
		String tipo = lectorString.nextLine();
		System.out.println("Introduce el nombre de la carta: ");
		String nombre = lectorString.nextLine();
		System.out.println("Introduce el coste de la carta: ");
		int coste = lectorInt.nextInt();
		System.out.println("Introduce el ataque de la carta: ");
		int ataque = lectorInt.nextInt();
		System.out.println("Introduce la vida de la carta: ");
		int vida = lectorInt.nextInt();
		System.out.println("Introduce habilidad especial de la carta: ");
		String habEspecial = lectorString.nextLine();
		System.out.println("Introduce la faccion de la carta: ");
		String faccion = lectorString.nextLine();

		Document carta = new Document("id", id).append("tipo", tipo).append("nombre_carta", nombre)
				.append("coste_invocacion", coste).append("ataque", ataque).append("vida", vida)
				.append("habilidad_especial", habEspecial).append("faccion", faccion);
		collection.insertOne(carta);
	}

	public static void generaMazo(MongoClient mongo) {
		MongoDatabase db = mongo.getDatabase("Runaterra");
		MongoCollection<Document> collection = db.getCollection("Mazos");
		boolean salir = false;
		ArrayList<Integer> cartas = new ArrayList<Integer>();

		Scanner lectorInt = new Scanner(System.in);
		Scanner lectorString = new Scanner(System.in);

		System.out.println("Introduce ID de mazo: ");
		int id = lectorInt.nextInt();
		System.out.println("Introduce el nombre del mazo: ");
		String nombre = lectorString.nextLine();
		System.out.println("Introduce el valor del mazo: ");
		int coste = lectorInt.nextInt();
		System.out.println("Introduce las id de las cartas del mazo: (Introduce -1 para acabar): ");
		while (!salir) {
			int id_carta = lectorInt.nextInt();
			if (id_carta == -1) {
				salir = true;
			} else {
				cartas.add(id_carta);
			}
		}

		Document mazo = new Document("id_mazo", id).append("nombre_mazo", nombre).append("valor_mazo", coste)
				.append("mazo", cartas);
		collection.insertOne(mazo);
	}

	public static void cargarCartas(MongoClient mongo) {
		MongoDatabase db = mongo.getDatabase("Runaterra");
		MongoCollection<Document> collection = db.getCollection("Cartas");

		collection.drop();

		JSONParser parser = new JSONParser();
		File f = new File("..\\UF3_ProyectoRunaterra_salasA\\src\\main\\resources\\Cartas.json");

		try {
			FileReader fr = new FileReader(f);
			JSONArray array = (JSONArray) parser.parse(fr);
			Iterator<?> iterator = array.iterator();

			while (iterator.hasNext()) {
				JSONObject object = (JSONObject) iterator.next();
				Carta carta = new Carta();
				carta.setId(Integer.parseInt(object.get("id").toString()));
				carta.setTipo((String) object.get("tipo"));
				carta.setNombre_carta((String) object.get("nombre_carta"));
				carta.setCoste_invocacion(Integer.parseInt(object.get("coste_invocacion").toString()));
				carta.setAtaque(Integer.parseInt(object.get("ataque").toString()));
				carta.setVida(Integer.parseInt(object.get("vida").toString()));
				carta.setHabilidad_especial((String) object.get("habilidad_especial"));
				carta.setFaccion((String) object.get("faccion"));

				Document doc = new Document("id", carta.getId()).append("tipo", carta.getTipo())
						.append("nombre_carta", carta.getNombre_carta())
						.append("coste_invocacion", carta.getCoste_invocacion()).append("ataque", carta.getAtaque())
						.append("vida", carta.getVida()).append("habilidad_especial", carta.getHabilidad_especial())
						.append("faccion", carta.getFaccion());
				collection.insertOne(doc);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println("¡Cartas cargadas correctamente en la coleccion!");
	}

	public static void cargarUsuarios(MongoClient mongo) {
		MongoDatabase db = mongo.getDatabase("Runaterra");
		MongoCollection<Document> collection = db.getCollection("Usuarios");

		collection.drop();

		JSONParser parser = new JSONParser();
		File f = new File("..\\UF3_ProyectoRunaterra_salasA\\src\\main\\resources\\Usuarios.json");

		try {
			FileReader fr = new FileReader(f);
			JSONArray array = (JSONArray) parser.parse(fr);
			Iterator<?> iterator = array.iterator();

			while (iterator.hasNext()) {
				JSONObject object = (JSONObject) iterator.next();
				Usuario usuario = new Usuario();
				usuario.setId_usuario(Integer.parseInt(object.get("id_usuario").toString()));
				usuario.setContrasenya_usuario((String) object.get("contrasenya_usuario"));
				usuario.setNombre_usuario((String) object.get("nombre_usuario"));
				usuario.setMazos_usuario((ArrayList<Integer>) object.get("mazos_usuario"));
				usuario.setColeccion_cartas((ArrayList<Integer>) object.get("cartas_usuario"));

				Document doc = new Document("id_usuario", usuario.getId_usuario())
						.append("contrasenya_usuario", usuario.getContrasenya_usuario())
						.append("nombre_usuario", usuario.getNombre_usuario())
						.append("mazos_usuario", usuario.getMazos_usuario())
						.append("cartas_usuario", usuario.getColeccion_cartas());
				collection.insertOne(doc);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println("¡Usuarios cargados correctamente en la coleccion!");
	}

	public static void cargarMazos(MongoClient mongo) {
		MongoDatabase db = mongo.getDatabase("Runaterra");
		MongoCollection<Document> collection = db.getCollection("Mazos");

		collection.drop();

		JSONParser parser = new JSONParser();
		File f = new File("..\\UF3_ProyectoRunaterra_salasA\\src\\main\\resources\\Mazos.json");

		try {
			FileReader fr = new FileReader(f);
			JSONArray array = (JSONArray) parser.parse(fr);
			Iterator<?> iterator = array.iterator();

			while (iterator.hasNext()) {
				JSONObject object = (JSONObject) iterator.next();
				Mazo mazo = new Mazo();
				mazo.setId_mazo(Integer.parseInt(object.get("id_mazo").toString()));
				mazo.setNombre_mazo((String) object.get("nombre_mazo"));
				mazo.setValor_mazo(Integer.parseInt(object.get("valor_mazo").toString()));
				mazo.setCartas_en_mazo((ArrayList<Integer>) object.get("cartas_en_mazo"));

				Document doc = new Document("id_mazo", mazo.getId_mazo()).append("nombre_mazo", mazo.getNombre_mazo())
						.append("valor_mazo", mazo.getValor_mazo()).append("cartas_en_mazo", mazo.getCartas_en_mazo());
				collection.insertOne(doc);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println("¡Mazos cargados correctamente en la coleccion!");
	}

}
