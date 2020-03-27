package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import conector.Conexion;
import objects.Carta;
import objects.Mazo;
import objects.Usuario;

import static com.mongodb.client.model.Filters.*;

public class Main {

	public static Usuario usuarioLogeado = new Usuario();

	public static void main(String[] args) {
		disableLogs();
		MongoClient mongo;
		try {
			mongo = Conexion.crearConexion();
			login(mongo);
			menu(mongo);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

	public static void menu(MongoClient mongo) {
		Scanner sn = new Scanner(System.in);
		boolean salir = false;
		int opcion;

		while (!salir) {
			System.out.println();
			System.out.println("1. Generar Carta");
			System.out.println("2. Generar Mazo");
			System.out.println("3. Cargar Cartas");
			System.out.println("4. Cargar Mazos");
			System.out.println("5. Cargar Usuarios");
			System.out.println("6. Importar Mazo Predifinido");
			System.out.println("7. Salir");

			try {

				System.out.println("Escribe una de las opciones");
				opcion = sn.nextInt();

				switch (opcion) {
				case 1:
					generaCarta(mongo);
					break;
				case 2:
					generarMazo(mongo);
					break;
				case 3:
					cargarCartas(mongo);
					break;
				case 4:
					cargarMazos(mongo);
					break;
				case 5:
					cargarUsuarios(mongo);
					break;
				case 6:
					importarPredefinido(mongo);
					break;
				case 7:
					salir = true;
					break;
				default:
					System.out.println("Solo números entre 1 y 7");
				}
			} catch (InputMismatchException e) {
				System.out.println("Debes insertar un número");
				sn.next();
			}
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

	public static void login(MongoClient mongo) {
		MongoDatabase db = mongo.getDatabase("Runaterra");
		MongoCollection<Document> collection = db.getCollection("Usuarios");
		Usuario usu = new Usuario();
		Scanner lector = new Scanner(System.in);

		System.out.println("Introduce el nombre del usuario: ");
		String nUsuario = lector.nextLine();
		System.out.println("Introduce la contrasenya del usuario: ");
		String pUsuario = lector.nextLine();

		try {
			Document filterDoc = new Document();

			filterDoc.put("contrasenya_usuario", pUsuario);

			filterDoc.append("nombre_usuario", nUsuario);

			Iterator<Document> iter = collection.find(filterDoc).iterator();
			if (iter.hasNext()) {
				System.out.println();
				System.out.println("¡Usuario encontrado!");
				System.out.println("Mostrando datos...");
				System.out.println();

				Document d = iter.next();
				usu.setId_usuario(d.getInteger("id_usuario"));
				usu.setNombre_usuario(d.getString("nombre_usuario"));
				usu.setContrasenya_usuario(d.getString("contrasenya_usuario"));
				usu.setColeccion_cartas((ArrayList<Integer>) d.get("cartas_usuario"));
				usu.setMazos_usuario((ArrayList<Integer>) d.get("mazos_usuario"));

				usuarioLogeado.setId_usuario(usu.getId_usuario());
				usuarioLogeado.setNombre_usuario(usu.getNombre_usuario());
				usuarioLogeado.setContrasenya_usuario(usu.getContrasenya_usuario());
				usuarioLogeado.setColeccion_cartas(usu.getColeccion_cartas());
				usuarioLogeado.setMazos_usuario(usu.getMazos_usuario());

				System.out.println("ID: " + usuarioLogeado.getId_usuario());
				System.out.println("Nombre: " + usuarioLogeado.getNombre_usuario());
				System.out.println("Cartas disponibles: " + usuarioLogeado.getColeccion_cartas());
				System.out.println("Mazos disponibles: " + usuarioLogeado.getMazos_usuario());

			} else {
				System.err.println("ERROR: Usuario y/o contrasenya invalidos");
			}

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public static void comprarCartas(MongoClient mongo) {
		MongoDatabase db = mongo.getDatabase("Runaterra");
		MongoCollection<Document> collection = db.getCollection("Cartas");
		MongoCollection<Document> collectionUs = db.getCollection("Usuarios");
		Scanner lector = new Scanner(System.in);
		boolean salir = false;

		try {
			while (!salir) {
				System.out.println("Introduce ID de la carta a comprar: (-1 salir)");
				int id = lector.nextInt();
				Document filterDoc = new Document();
				filterDoc.put("id", id);
				Iterator<Document> iter = collection.find(filterDoc).iterator();

				if (id == -1) {
					salir = true;
				} else {
					if (iter.hasNext()) {
						ArrayList<Integer> arr = usuarioLogeado.getColeccion_cartas();
						for (Number carta_id : arr) {
							if (id == carta_id.intValue()) {
								System.out.println("YA TIENES ESTA CARTA");
							} else {
								arr.add(id);

								FindIterable<Document> findIt = collectionUs
										.find(eq("nombre_usuario", usuarioLogeado.getNombre_usuario()));
								Document doc = findIt.first();

								Document newDoc = new Document("id_usuario", usuarioLogeado.getId_usuario())
										.append("contrasenya_usuario", usuarioLogeado.getContrasenya_usuario())
										.append("nombre_usuario", usuarioLogeado.getNombre_usuario())
										.append("mazos_usuario", usuarioLogeado.getMazos_usuario())
										.append("cartas_usuario", arr);

								collectionUs.findOneAndReplace(doc, newDoc);

							}
						}
					} else {
						System.out.println("USUARIO NO EXISTE");
					}
				}
			}
		} catch (Exception e) {

		}
	}

	public static void generarMazo(MongoClient mongo) {
		MongoDatabase db = mongo.getDatabase("Runaterra");
		MongoCollection<Document> collection = db.getCollection("Cartas");
		MongoCollection<Document> collectionMz = db.getCollection("Mazos");
		Scanner lectorString = new Scanner(System.in);
		Scanner lectorInt = new Scanner(System.in);
		ArrayList<Integer> cartasMazo = new ArrayList<Integer>();
		ArrayList<Integer> cartasBorrar = new ArrayList<Integer>();
		boolean salir = false;
		int cont = 0;

		Mazo mazo = new Mazo();
		Document doc = new Document();

		int idMazo = (int) (collectionMz.countDocuments() + 1);

		System.out.println("Introduce el nombre del mazo: ");
		String nMazo = lectorString.nextLine();

		while (!salir) {
			System.out.println("Introduce el id de la carta a añadir al mazo: (-1 para salir)");
			int idCarta = lectorInt.nextInt();

			if (idCarta == -1) {
				salir = true;
			} else {
				Document filterDoc = new Document();
				filterDoc.put("id", idCarta);
				Iterator<Document> iter = collection.find(filterDoc).iterator();

				if (iter.hasNext()) {
					FindIterable<Document> findIt = collection.find(eq("id", idCarta));
					Document doc2 = findIt.first();
					if (cont < 60) {
						cartasMazo.add(idCarta);
						cont = cont + (Integer) doc2.get("coste_invocacion");
					} else {
						System.out.println("Mazo finalizado - Coste limite");
						salir = true;
					}

					int alt = 0;
					for (Integer num : cartasMazo) {

						if (num == idCarta) {
							alt++;
						}

						if (alt > 2) {
							cartasBorrar.add(idCarta);
						}
					}

				} else {
					System.err.println("ERROR: Esta carta no existe...");
					System.out.println();

				}
			}
		}

		for (Integer num2 : cartasMazo) {
			if (cartasMazo.indexOf(num2) == cartasBorrar.indexOf(num2)) {
				cartasMazo.remove(num2);
			}
		}

		mazo.setId_mazo(idMazo);
		mazo.setNombre_mazo(nMazo);
		mazo.setValor_mazo(cont);
		mazo.setCartas_en_mazo(cartasMazo);

		doc = new Document("id_mazo", mazo.getId_mazo()).append("nombre_mazo", mazo.getNombre_mazo())
				.append("valor_mazo", mazo.getValor_mazo()).append("cartas_en_mazo", mazo.getCartas_en_mazo());
		collectionMz.insertOne(doc);

	}

	public static void importarPredefinido(MongoClient mongo) {
		MongoDatabase db = mongo.getDatabase("Runaterra");
		MongoCollection<Document> collection = db.getCollection("Mazos");
		MongoCollection<Document> collectionUsu = db.getCollection("Usuarios");
		ArrayList<Integer> mazosUsuario = new ArrayList<Integer>();
		mazosUsuario = usuarioLogeado.getMazos_usuario();
		Scanner lectorInt = new Scanner(System.in);
		boolean salir = false;
		boolean coincide = false;

		while (!salir) {
			System.out.println("Introduce la id del mazo predefinido a importar: ");
			int idMazo = lectorInt.nextInt();

			if (idMazo > 0 && idMazo < 4) {
				Document filterDoc = new Document();
				filterDoc.put("id_mazo", idMazo);
				Iterator<Document> iter = collection.find(filterDoc).iterator();

				if (iter.hasNext()) {
					for (int i = 0; i < usuarioLogeado.getMazos_usuario().size(); i++) {
						if (usuarioLogeado.getMazos_usuario().indexOf(i) == idMazo) {
							coincide = true;
						}
					}

					if (coincide == false) {
						mazosUsuario.add(idMazo);
						FindIterable<Document> findIt = collectionUsu
								.find(eq("nombre_usuario", usuarioLogeado.getNombre_usuario()));
						Document doc = findIt.first();

						Document newDoc = new Document("id_usuario", usuarioLogeado.getId_usuario())
								.append("contrasenya_usuario", usuarioLogeado.getContrasenya_usuario())
								.append("nombre_usuario", usuarioLogeado.getNombre_usuario())
								.append("mazos_usuario", usuarioLogeado.getMazos_usuario())
								.append("cartas_usuario", mazosUsuario);

						collectionUsu.findOneAndReplace(doc, newDoc);
					}
				}
				
				salir = true;
				
			} else {
				System.out.println("Los mazos predefinidos son: 1, 2, 3");
			}
		}

	}
}
