package es.uned.common;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

//import javax.management.RuntimeErrorException;

public class Gui {
	
	private static Console console = System.console();
	private static BufferedReader reader = new BufferedReader(
			new InputStreamReader(System.in));
	
	public static String[] input(String name, String[] msgs) {
		String[] inputs = new String[msgs.length];
		
		outLn("=== " + name + " ===");
		
		for (int i = 0; i < msgs.length; i++) {
			inputs[i] =  input(msgs[i]);
		}
		return inputs;
	}
	
	public static String input(String name, String msg) {
		outLn("=== " + name + " ===");
		
		return input(msg);
	}
	
	public static String input(String msg) {
		out(msg);
		String line = readLine();
		
		return line; 
	}
	
	public static int menu(String name, String[] entradas) {
		outLn("=== " + name + " ===");
		outLn("seleccione una opcion");
		newLine();
		
		for (int i=0; i<entradas.length; i++) {
			outLn((i + 1) + " - " + entradas[i]);			
		}
		
		int opt = -1;
		
		do {
				String temp = readLine();
				
			if (isInt(temp)) {
				opt = Integer.parseInt(temp.trim());
				
				if ((opt - 1) >= entradas.length || opt <= 0) {
					outLn("Ingrese una opcion del 1 al " + entradas.length);
					opt = -1;
				} 
			} else {
				outLn("Tiene que ingresar un numero entero del 1 al " + entradas.length);
				opt = -1;
			}
			
				
		} 
		while(opt == -1);
		
		newLine();
		
		return (opt - 1 );
		
	}
	
	
	public static int trinoMenu(String name, List<Trino> entradas) {
		outLn("=== " + name + " ===");
		outLn("seleccione una opcion");
		newLine();
		
		for (int i=0; i<entradas.size(); i++) {
			outLn((i + 1) + " - " + entradas.get(i).ObtenerTrino());			
		}
		
		int opt = -1;
		
		do {
				String temp = readLine();
				
			if (isInt(temp)) {
				opt = Integer.parseInt(temp.trim());
				
				if ((opt - 1) >= entradas.size() || opt <= 0) {
					outLn("Ingrese una opcion del 1 al " + entradas.size());
					opt = -1;
				} 
			} else {
				outLn("Tiene que ingresar un numero entero del 1 al " + entradas.size());
				opt = -1;
			}
			
				
		} 
		while(opt == -1);
		
		newLine();
		
		return (opt - 1 );
		
	}
	
	public static void outLn(String msg) {
		System.out.println(msg);
	}
	
	public static void outMessage(String title, String msg) {
		outLn("=== " + title + " ===");
		outLn(msg);
	}
	
	private static void newLine() {
		System.out.println();
	}
	public static void out(String msg) {
		System.out.print(msg);
	}
	
	private static String readLine() {
		if(console != null) return console.readLine();
		
		try {
			return reader.readLine();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static boolean isInt(String line) {
		boolean validInt = false;
		
		try {
			
			Integer.parseInt(line.trim());
			
			// es un int valido
			validInt = true;
		}
		catch (NumberFormatException ex)
		{
			// no es un int valido
		}
		
		
		return validInt;
	}
	 
	public static void outUserList(String title, List<User> users) {
		outLn("=== " + title + " ===");
		
		for (User user : users) {
			outLn("Nombre: " + user.getName() + "	Nick: @" + user.getNick() );
		}
	}
		
		public static void printTrino(List<Trino> trinos) {
			outLn("=== " + "Trinos" + " ===");
			
			for (Trino trino : trinos) {
				outLn("> " + trino.ObtenerNickPropietario()+"# "+ trino.ObtenerTrino());
			}
		
	}
	

	

}

