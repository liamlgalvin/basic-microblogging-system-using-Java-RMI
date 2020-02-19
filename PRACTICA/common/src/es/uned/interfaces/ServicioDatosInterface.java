package es.uned.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import es.uned.common.*;


public interface ServicioDatosInterface extends Remote {
	

	
	//users
	
	public void registerUser(User user) throws RemoteException;
	public boolean loginUser(User user) throws RemoteException;
	public void logoutUser(String nick) throws RemoteException;
	public Map<String, User> getRegisteredUsers() throws RemoteException;
	public Map<String, User> getLoggedinUsers() throws RemoteException;
	public User getUser(String nick) throws RemoteException;
	
	
	
	//followers
	
	public void follow(User nickUser, User nickToFollow) throws RemoteException;
	public void unFollow(User nickUser, User userToUnfollow) throws RemoteException;
	public List<String> getFollowers(String nickUser) throws RemoteException;
	public List<String> getFollowing(String nickUser) throws RemoteException;
	
	//trinos
	
	public void publishTrino(String nickUser, String trino) throws RemoteException;
	public void addTrinoToList(String nickUser, Trino trino) throws RemoteException;
	public void deleteTrino(String nickUser, Trino trino) throws RemoteException;
	
	public List<Trino> getOfflineTrinos(String nickUser) throws RemoteException;
	public void setOfflineTrinos(String nickUser) throws RemoteException;

	public void addTrinosOffline(Trino trino, String nickReceiver) throws RemoteException;

	public void deleteTrinoOffline(String nickUser, Trino trino) throws RemoteException;
	public List<Trino> getUsersTrinos(String nickUser) throws RemoteException;
	public List<Trino> getAllTrinos() throws RemoteException;
	public void addAllTrinos(Trino trino) throws RemoteException;
	
	


	
	
	
	
	

}
