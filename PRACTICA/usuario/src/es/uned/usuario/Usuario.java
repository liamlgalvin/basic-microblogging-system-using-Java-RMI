package es.uned.usuario;


import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;


import es.uned.common.Gui;
import es.uned.common.Trino;
import es.uned.common.User;
import es.uned.interfaces.ServicioAutenticacionInterface;
import es.uned.interfaces.ServicioGestorInterface;




public class Usuario {

		
		private static User miUser = null; 
		private static String miNick = null; 
		private static int hostPort;
		
		private static ServicioAutenticacionInterface servidor;
		private static ServicioGestorInterface servGest;
		private static CallbackUsuarioImpl callback;

		
		
		public static void main(String[] args) throws Exception{
					
			
			try {
			Registry registry = LocateRegistry.getRegistry(1099);
		
			
			servidor = (ServicioAutenticacionInterface) registry.lookup(getHostAddressAuth());
			servGest = (ServicioGestorInterface) registry.lookup(getHostAddressGest());
			

			callback = new CallbackUsuarioImpl();
			registry.rebind(getURLCallback(), callback);

			
			guiMain();
			
			
			}catch(RemoteException | NotBoundException e){
				Gui.outMessage("Error", "Error conectando al servidor. No se ha podido conectar.");
			}

					
		}
		

	
	private static void guiMain() throws RemoteException {
		
		int opt;
			
				opt = Gui.menu("Menu Principal", 
						new String[] {"Registrar (nuevo usuario)", 
						"Hacer login",
						"Salir"});
				
				switch(opt) {
					case 0: register(); break;
					case 1: autenticarse(); break;
					case 2: break; //exit();
					
				}
			
	
	}

	private static void guiLoggedIn() throws RemoteException {
		
		int opt;
		
		do {

			
			opt = Gui.menu("Menu Principal - @" + miNick, 
					new String[] {"Información del Usuario", 
					"Enviar Trino",
					"Listar Usuarios del Sistema",
					"Seguir a",
					"Dejar de seguir a",
					"Borrar trino a los usuarios que todavía no lo han recibido",
					"Salir (logout)"});
			
			
			switch(opt) {
			
				case 0: userInfo(); break;
				case 1: enviarTrino(); break;
				case 2: listUsers(); break;
				case 3: follow(); break;
				case 4: unfollow(); break;
				case 5: deleteTrino(); break;
				case 6: logout(); break;
				
			}
		}
		while(opt != 6);
	
	}

private static void deleteTrino() throws RemoteException {
	
	if(servGest.getTrinos(miNick).size()>0) {
		int opt = Gui.trinoMenu("Borrar Trino", servGest.getTrinos(miNick));
		
		
		servGest.deleteTrino(miNick, servGest.getTrinos(miNick).get(opt));
		
		System.out.println("\"" +servGest.getTrinos(miNick).get(opt).ObtenerTrino() + "\" borrado"); 
		
		guiLoggedIn();
		
	}else {
		Gui.outLn("No tiene trinos.");
	}
	
		
		
	}

private static void unfollow() throws RemoteException {
	String nickUnFollow = Gui.input("Dejar de seguir a","Ingrese el trino del usuario: @");
	
	if (!servidor.checkNick(nickUnFollow)) {
		
		if((servGest.getFollowing(miNick).contains(nickUnFollow))) {
			
			if(servGest.unFollow(miNick, nickUnFollow)) {
			
			Gui.outMessage("Exito", "Ha dejado de seguir a " + nickUnFollow);
			guiLoggedIn();
			
			}else {
				Gui.outMessage("Error", "Error en dejar de seguir");
				guiLoggedIn();
			}
			
		}else {
			Gui.outMessage("Error", "No sigue a " + nickUnFollow);
			guiLoggedIn();
		}
	}else{
		Gui.outMessage("Error", nickUnFollow + " no existe");
		guiLoggedIn();
	}

		
	}
		
	
private static void follow() throws RemoteException {
	String nickFollow = Gui.input("Seguir a","ingrese el trino del usuario: @");
	
	if (!servidor.checkNick(nickFollow)) {
		
		if(!(servGest.getFollowing(miNick).contains(nickFollow))) {
			
				if(servGest.follow(miNick, nickFollow)) {
					Gui.outMessage("Exito", "sigue a " + nickFollow);
					guiLoggedIn();
					
				}else {						
						Gui.outMessage("Error", "Error en seguir");
						guiLoggedIn();
					}
					

				}else{
				Gui.outMessage("Error", "Ya sigue este usuario");
				guiLoggedIn();
			}
	} else {
		Gui.outMessage("Error", "Usuario no existe");
		guiLoggedIn();
		
			}
		
	}

private static void listUsers() throws RemoteException {
	
		
		Gui.outUserList("Usuarios del sistema", servidor.registeredUsers());
		
		Gui.input("Pulse una tecla para volver al menu principal: ");
		guiLoggedIn();
	}

private static void enviarTrino() throws RemoteException {
	
	
	String body = Gui.input("Enviar Trino","Escriba su trino: "); 
		servGest.sendTrino(miNick, body);
	}



public static void  register() throws RemoteException {
	try {
		
		boolean nickOK = false;
		String nick;
		
		do {
			nick = Gui.input("Registrarse", "Ingrese su nick: @");
			
			nickOK = servidor.checkNick(nick); 
				
			if (!nickOK) { 
				Gui.outLn("nick no disponible. Intente con otro.");
				}
			} while ( nickOK == false);
		
		String nombre = Gui.input("Ingrese su nombre: ");
		String password = Gui.input("Ingrese su contraseña: ");
		
		if(servidor.register(nombre, nick, password)) {
			Gui.outMessage("Exito", "Está registrado. Puede hacer el login");
			guiMain();
	
		}
		
	
	
	}catch(RemoteException e) {
		System.out.println("server down");
	}
	
	}	
	
public static void autenticarse() throws RemoteException {
	
	String nick = Gui.input("Autenticarse", "Ingrese su nick: @");
	
	
	if (nick != null && !nick.isEmpty()) {
		
		try {
		if(!servidor.checkNick(nick)){
			
			servidor.getUser(nick);

			if(!servidor.checkUserOnline(nick)) {
				String password = Gui.input("Ingrese su contraseña: ");	
				if(servidor.checkpassword(nick, password)){

					User newUser = servidor.login(nick, password);
					if( newUser != null) {
						
						miNick = newUser.getNick();
						miUser = newUser;
						
						Gui.outMessage("Exito", "Se ha autenticado con exito");
						
						servGest.connect(miNick, callback);
						
						guiLoggedIn();
						
						
					}else {
						Gui.outMessage("Error", "Error con el login");
						guiMain();
					}
										
				}else {
					Gui.outMessage("Error", "Contraseña incorrecta");
					guiMain();

				}
		} else {
			Gui.outMessage("Error", "Ya tiene una sesion abierta");
			guiMain();

		}
		}else {
			Gui.outMessage("Error", "nick no encontrado");
			guiMain();

		}
	}catch (RemoteException e) {
		Gui.outMessage("Error", "RemoteException");
	}
			
	}
}
			
			


public static void logout() throws RemoteException {

	if(servidor.logout(miNick)) {
		miUser = null;
		miNick = null;
		servGest.disconnect(miNick);
		Gui.outMessage("Exito", "el logout se ha completado con exito");
		System.exit(0);
		
	}else {
		Gui.outMessage("Error", "error con el logout");
	}
	
}


private static void userInfo() {
	
	
	Gui.outLn("Nombre: " + miUser.getName());
	Gui.outLn("Nick: " + miUser.getNick());
	Gui.outLn("Fecha de registro: " + new Date(miUser.getRegisterDate() * 1000));
	Gui.outLn("Ultima login: " + new Date(miUser.getLastLogin() * 1000));
	
	System.out.println();
	Gui.input("Pulse retorno para volver al munu principal:");
	
}



private static int getHostPort() {
	if(0 == hostPort)
	{
		// El puerto no ha sido configurado. Le asignamos por defecto (1099)
		hostPort =  Registry.REGISTRY_PORT;		
	}		
	
	return hostPort;
	}

private static String getHostAddressAuth() throws UnknownHostException {
	
	return "rmi://" + java.net.InetAddress.getLocalHost().getHostAddress() + ":" + getHostPort() + "/" + "ServicioAutenticacion";

	
}
private static String getHostAddressGest() throws UnknownHostException {
	
	return "rmi://" + java.net.InetAddress.getLocalHost().getHostAddress() + ":" + getHostPort() + "/" + "ServicioGestor";

	
}

private static String getURLCallback() throws UnknownHostException {
	
	return "rmi://" + java.net.InetAddress.getLocalHost().getHostAddress() + ":" + getHostPort() + "/" + "CallbackUsuario";

	
}

	
	
}
