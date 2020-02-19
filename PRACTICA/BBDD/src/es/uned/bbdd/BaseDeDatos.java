package es.uned.bbdd;


import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

import es.uned.common.Gui;
import es.uned.common.User;
import es.uned.common.Utils;
import es.uned.interfaces.ServicioDatosInterface;

public class BaseDeDatos {
	

	private static int rmiRegistryPort;
	private static Registry registry;
	private static ServicioDatosImpl bbdd;
	
	public static void main (String[] args) throws Exception {
		try {
			
			LocateRegistry.createRegistry(1099); 

			Utils.setCodeBase(ServicioDatosInterface.class);
			
			bbdd = new ServicioDatosImpl();
			
		
			registry = LocateRegistry.getRegistry(getRmiRegistryPort());
			registry.rebind(getURLNombre(), bbdd); 
			
			gui();
			
		} catch(RemoteException  e) {
			Gui.outMessage("Error", "La base de datos no podia arrancar o ya tienes una abierta");
			System.out.println(e);
		}
		
	

		

		
	}
	
	
	
	private static void gui() throws RemoteException {
		
		int opt;
			
				opt = Gui.menu("Base De Datos Menu Principal", 
						new String[] {"Información de la Base de Datos",
						"Listar Usuarios Registrados",
						"Listar Trinos",
						"Salir"});
				
				switch(opt) {
					case 0: info(); break;
					case 1: listRegUsers(); break;
					case 2: listTrinos(); break;
					case 3: close();
					
				}
			
	
	}
	
	// metodos privados
	
	private static void info() throws RemoteException {
		try {
			
			Gui.outMessage("Informacion del sistema", getURLNombre());
			Gui.out("");
			Gui.input("Pulse retorno para volver al munu principal: ");
			gui();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	private static void listRegUsers() throws RemoteException{
	try {
			List<User> users = new LinkedList<User>();
			users.addAll(bbdd.getRegisteredUsers().values());
			if(users!= null) {
            Gui.outUserList("Usuarios Registrados", users);	
            Gui.out("");
			}else {
			Gui.out("No hay usuarios registrados");
			}
			
			Gui.input("Pulse cualquier tecla para volver al menu principal");
			gui();
			
			
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	private static void listTrinos() {
		try {
			
            Gui.printTrino(bbdd.getAllTrinos());	
            Gui.out("");
			Gui.input("Pulse cualquier tecla para volver al munu principal");
			gui();
			
			
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	private static void close() throws RemoteException{
		
		
		try {
			registry.unbind(getURLNombre());
			UnicastRemoteObject.unexportObject(bbdd, true);
			
			System.out.println("=== Base de datos cerrado ===");

			
		} catch (RemoteException | NotBoundException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
	

	private static int getRmiRegistryPort()
	{
		int port;
				
		if(rmiRegistryPort == 0)
		{
			// El puerto no ha sido configurado. Le asignamos por defecto (1099)
			port = Registry.REGISTRY_PORT;
		}
		else
		{
			// El puerto ha sido configurado ...
			port = rmiRegistryPort;
		}
		
		return port;
	}
	
	private static String getURLNombre() throws UnknownHostException 
	{
		return "rmi://" + java.net.InetAddress.getLocalHost().getHostAddress() + ":" + getRmiRegistryPort() + "/" + "ServicioDatos";		 	
	}
	

	
}
