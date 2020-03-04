package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import conector.Conexion;
import objects.Carta;

public class Main {

	public static void main(String[] args) {
		disableLogs();
		MongoClient mongo;
		try {
			mongo = Conexion.crearConexion();
			cargarCartas(mongo);
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
		Carta carta = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			carta = mapper.readValue(new File("Cartas.json"), Carta.class);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(carta);
	}

}
