package es.uned.servidor;

import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.net.UnknownHostException;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import es.uned.common.Gui;
import es.uned.common.User;
import es.uned.common.Utils;
import es.uned.interfaces.CallbackUsuarioInterface;
import es.uned.interfaces.ServicioAutenticacionInterface;
import es.uned.interfaces.ServicioDatosInterface;
import es.uned.interfaces.ServicioGestorInterface;


public class Servidor{
	
	//private static final long serialVersionUID = -2271321309918375321L;
	private static Registry rmiregistry;
	private static Registry registryAuth;
	private static Registry registryGest;

	private String hostAddress;
	private static int hostPort;
	private static int rmiRegistryPort;
	private static int rmiRegistryGest;
	
	private static ServicioDatosInterface bbdd;
	private static ServicioAutenticacionImpl servicioAuth;
	private static ServicioGestorImpl servicioGest;
	
	public static void main (String[] args) throws Exception {
		
	
		// rmi
		try {
		// servicio autenticacion
		
		Utils.setCodeBase(ServicioAutenticacionInterface.class);
		
		servicioAuth = new ServicioAutenticacionImpl();
		
		servicioAuth.setHostAddress(getHostaddress());
		servicioAuth.setHostPort(getHostPort());
		
		registryAuth = LocateRegistry.getRegistry(getHostPort());
		registryAuth.rebind(getURLServAuth() , servicioAuth);
		
		// servicio Gestion
	
		Utils.setCodeBase(ServicioGestorInterface.class);
		
		servicioGest = new ServicioGestorImpl();
		
		
		servicioGest.setHostAddress(getHostaddress());
		servicioGest.setHostPort(getHostPort());
				
		registryGest = LocateRegistry.getRegistry(getHostPort());
		registryGest.rebind(getURLServGest() , servicioGest);
		
			
		// add some stuff in database
		servicioAuth.register("Liam", "liam", "123");
		servicioAuth.register("ana", "ana", "123");
		servicioAuth.register("tom", "tom", "123");
		
				
		gui();
		
	}catch(RemoteException  e) {
		Gui.outMessage("Error", "servidor no podia arrancar");
		System.out.println(e);
	}
		
	}
	

	
	private static void gui() throws RemoteException {
		
		int opt;
			
				opt = Gui.menu("Servidor Menu Principal", 
						new String[] {"Información del Servidor", 
						"Listar Usuarios Logeados",
						"Salir"});
				
				switch(opt) {
					case 0: info(); break;
					case 1: loggedInUsers(); break;
					case 2: close();
					
				}
			
	
	}
	private static void close() throws RemoteException{
		
		
		try {
			registryAuth.unbind(getURLServAuth() );
			UnicastRemoteObject.unexportObject(servicioAuth, true);
			
			registryGest.unbind(getURLServGest() );
			UnicastRemoteObject.unexportObject(servicioGest, true);
			
			System.out.println("=== Servidor cerrado ===");
			
		} catch (RemoteException | NotBoundException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}



	private static void loggedInUsers() {
		try {
			List<User> users = servicioAuth.loggedInUsers();
			
			Gui.outUserList("Usuarios del sistema", users);
			
			Gui.input("Pulse retorno para volver al munu principal: ");
			gui();
			
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		
	}



	private static void info() {
		// TODO Auto-generated method stub
		System.out.println("Información del Servidor");
		try {
			System.out.println(getURLServAuth());
			System.out.println(getURLServGest());
			
			Gui.input("Pulse una tecla para volver al menu principal: ");
			gui();
			
		} catch (UnknownHostException | RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}



	// functions to get bbdd registry info
	private static String getURLServAuth() throws UnknownHostException 
	{
		return "rmi://" + java.net.InetAddress.getLocalHost().getHostAddress() + ":" + getRmiRegistryPort() + "/" + "ServicioAutenticacion";
	}	
	
	private static String getURLServGest() throws UnknownHostException
	{
		return "rmi://" + java.net.InetAddress.getLocalHost().getHostAddress() + ":" + getRmiRegistryPort() + "/" + "ServicioGestor";
	}
	private static int getRmiRegistryPort() {
		if(0 == rmiRegistryPort)
		{
			// El puerto no ha sido configurado. Le asignamos por defecto ()
			rmiRegistryPort =  Registry.REGISTRY_PORT;		
		}		
		
		return rmiRegistryPort;
	}
	
	
	private static int getHostPort() {
		if(0 == hostPort)
		{
			// El puerto no ha sido configurado. Le asignamos por defecto (1099)
			hostPort = Registry.REGISTRY_PORT;		
		}		
		
		return hostPort;
		}
	
	private static String getHostaddress() throws UnknownHostException {
		
		return "rmi://" + java.net.InetAddress.getLocalHost().getHostAddress() + ":" + getHostPort() + "/" + "ServicioDatos";

		
	}
	
	
	

}
