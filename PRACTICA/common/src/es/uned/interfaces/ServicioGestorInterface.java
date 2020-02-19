package es.uned.interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import es.uned.common.Trino;
import es.uned.common.User;

public interface ServicioGestorInterface extends Remote {
	
	public void connect(String nick, CallbackUsuarioInterface callback) throws RemoteException; 
	public void disconnect(String nick) throws RemoteException; 
	
	//followers
	
	public boolean follow(String nickUser, String nickFollow) throws RemoteException;
	public boolean unFollow(String nickUser, String nickFollow) throws RemoteException;
	public List<String> getFollowers(String nickUser) throws RemoteException;
	public List<String> getFollowing(String nickUser) throws RemoteException;
	
	//trinos
	
	public Trino makeTrino (String nickSender, String body) throws RemoteException;
	public void sendTrino(String nickSender, String body) throws RemoteException;
	public void deleteTrino(String nickUser, Trino trino) throws RemoteException;
	public List<Trino> getOfflineTrinos(String nick) throws RemoteException;
	public List<Trino> getTrinos(String nick) throws RemoteException;
	
	

}
