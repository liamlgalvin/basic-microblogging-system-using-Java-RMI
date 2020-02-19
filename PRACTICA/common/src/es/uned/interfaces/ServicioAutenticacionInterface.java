package es.uned.interfaces;

import java.util.List;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import es.uned.common.User;

public interface ServicioAutenticacionInterface extends Remote {
	
	public String hellomate() throws RemoteException;
	
	// users
	
	public boolean register(String name, String nick, String password) throws RemoteException;
	public User login(String nick, String password) throws RemoteException; // returns session id
	public boolean logout(String nick) throws RemoteException;
	public List<User> registeredUsers() throws RemoteException;
	public List<User> loggedInUsers() throws RemoteException;
	public User getUser(String nick) throws RemoteException;
	public boolean checkNick(String nick) throws RemoteException;
	public boolean checkpassword(String nick, String password) throws RemoteException;
	public boolean checkUserOnline(String nick) throws RemoteException;
	
	
	
	
	
}
