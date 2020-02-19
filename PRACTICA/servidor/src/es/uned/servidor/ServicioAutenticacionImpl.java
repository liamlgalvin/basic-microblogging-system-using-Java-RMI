package es.uned.servidor;

import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import es.uned.common.Gui;
import es.uned.common.User;
import es.uned.interfaces.ServicioAutenticacionInterface;
import es.uned.interfaces.ServicioDatosInterface;

public class ServicioAutenticacionImpl extends UnicastRemoteObject implements ServicioAutenticacionInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7458889901022033031L;



	protected ServicioAutenticacionImpl() throws RemoteException {
		super();
	}

	// class variables
	private String hostAddress = null;
	private int hostPort = 0;
	
	
	
	// metodos de rmi
	
	public void setHostAddress(String hostAddress)  {
		this.hostAddress = hostAddress;
	}
	
	public String getHostAddress() {
		return this.hostAddress;
	}
	
	public void setHostPort(int hostPort) {
		this.hostPort = hostPort;
	}
	
	
	public int getHostPort() {
		return this.hostPort;
	}
	
	private ServicioDatosInterface getRemoteReference() throws RemoteException, UnknownHostException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(); //hostAddress, hostPort
		String hostUrl = "rmi://" + java.net.InetAddress.getLocalHost().getHostAddress() + ":" + getHostPort() + "/" + "ServicioDatos";
		ServicioDatosInterface servicioDatos = (ServicioDatosInterface) registry.lookup(hostUrl);
		return servicioDatos;
	}
	
	
	
	// public methods
	
	@Override
	public boolean register(String name, String nick, String password) throws RemoteException {		
			
			try 
			{		
				ServicioDatosInterface sd = getRemoteReference();
				User user = new User(name, nick, password);// if successful makes a new user
				sd.registerUser(user);
				return true;
				
			} 
			catch (RemoteException | UnknownHostException | NotBoundException e) 
			{
				
				return false;
			}
					
	}
	


	@Override
	public User login(String nick, String password) throws RemoteException {
		try {
			
			ServicioDatosInterface sd = getRemoteReference();
			User user = sd.getUser(nick);

			if (user.getPassword().compareTo(password)==0) { // final check
					if(sd.loginUser(user)) {
						return user;
					}
			}
		}catch(RemoteException | NotBoundException | UnknownHostException e){
			return null;
		}
		return null;
	}

	@Override
	public boolean logout(String nick) throws RemoteException {
		try {
			ServicioDatosInterface sd = getRemoteReference();
			if(sd.getLoggedinUsers().containsKey(nick)) { 
				sd.logoutUser(nick);
				return true;
				}
		} catch(RemoteException |  NotBoundException | UnknownHostException e) {
			
		}
		return false;

		
	}

	@Override
	public List<User> registeredUsers() throws RemoteException { // put contents of map into Linkedlist
		
		try {
			ServicioDatosInterface sd = getRemoteReference();
			
			Map<String, User> userMap = sd.getRegisteredUsers();
			List<User> registeredUsers =  new LinkedList<User>();;
			
			for (User user: userMap.values()) {
				registeredUsers.add(user);
			}
			
			return registeredUsers;
			
		
		} catch(RemoteException |  NotBoundException | UnknownHostException e) {
			
		}

		
		
		return null;
	}

	@Override
	public List<User> loggedInUsers() throws RemoteException { // put contents of map into list i guess
		List<User> loggedInUsers = new LinkedList<>();

		try {
			ServicioDatosInterface sd = getRemoteReference();
			
			loggedInUsers.addAll(sd.getLoggedinUsers().values());
			
			
		} catch (UnknownHostException | NotBoundException e) {

			e.printStackTrace();
		}
		
		
		return loggedInUsers;
	}
	
	@Override
	public User getUser(String nick) throws RemoteException {
		
		try 
		{		
			ServicioDatosInterface sd = getRemoteReference();
			User user = sd.getUser(nick);
			if(user != null) {
				return user;
			}
		} 
		catch (RemoteException | UnknownHostException | NotBoundException e) 
		{
			return null;
		}
		
		return null;
	}


	@Override
	public boolean checkNick(String nick) throws RemoteException {
		
		
		try 
		{		
			ServicioDatosInterface sd = getRemoteReference();
			
			if (sd.getRegisteredUsers().containsKey(nick)) {
				return false;
			}else {
				return true;
			}			
			
		} 
		catch (RemoteException | UnknownHostException | NotBoundException e) 
		{
			return false;
		}
	}

	@Override
	public boolean checkpassword(String nick, String password) throws RemoteException {
		try {
			ServicioDatosInterface sd = getRemoteReference();
			if(sd.getRegisteredUsers().containsKey(nick)) { 
				User user = sd.getUser(nick);
				
				if (user.getPassword().compareTo(password) == 0) {
					return true;
					}
				}
		} catch(RemoteException |  NotBoundException | UnknownHostException e) {
			
		}
		return false;
	}

	@Override
	public boolean checkUserOnline(String nick) throws RemoteException {
	
		try {
			ServicioDatosInterface sd = getRemoteReference();
			if(sd.getLoggedinUsers().containsKey(nick)) { 
				return true;
				}
		} catch(RemoteException |  NotBoundException | UnknownHostException e) {
			
		}
		return false;
	}

	@Override
	public String hellomate() throws RemoteException {
		// TODO Auto-generated method stub
		return "hello from auth";
	}





}
