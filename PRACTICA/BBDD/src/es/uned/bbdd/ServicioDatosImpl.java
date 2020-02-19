package es.uned.bbdd;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import es.uned.common.*;
import es.uned.interfaces.*;

public class ServicioDatosImpl extends UnicastRemoteObject implements ServicioDatosInterface{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2507022499753930261L;

	protected ServicioDatosImpl() throws RemoteException {
		super();
		
	}
	private Map<String, List<String>> followersTable = new HashMap<String, List<String>>();
	private Map<String, List<String>> followingTable = new HashMap<String, List<String>>();
	private Map<String, List<Trino>> trinosTable = new HashMap<String, List<Trino>>();
	private List<Trino> trinoslist = new LinkedList<Trino>(); // todos los trinos del sistema
	private Map<String, User> registeredUsers = new HashMap<String, User>();
	private Map<String, User> loggedInUsers = new HashMap<String, User>();
	private Map<String, List<Trino>> offlineTrinosTable = new HashMap<String, List<Trino>>();
	
	// users
	
	@Override
	public synchronized void registerUser(User user) throws RemoteException {	
		registeredUsers.put(user.getNick(), user);
	}
	
	@Override
	public synchronized boolean loginUser(User user) throws RemoteException {
	
		loggedInUsers.put(user.getNick(), user);
		user.setLastLogin();
		
		
		if(loggedInUsers.containsKey(user.getNick())) {
			return true;
			}else {
			return false;
			}
		
		
	}
	
	@Override
	public synchronized void logoutUser(String nick) throws RemoteException {
		
		loggedInUsers.remove(nick);
		
	}
	@Override
	public Map<String, User> getRegisteredUsers() throws RemoteException {
		return this.registeredUsers;
	}
	@Override
	public Map<String, User> getLoggedinUsers() throws RemoteException {
		return this.loggedInUsers;
	}


	@Override
	public User getUser(String nick) throws RemoteException {
		return registeredUsers.get(nick);
		 
	}

	
	
	
	
	@Override
	public synchronized void follow(User nickUser, User userToFollow) throws RemoteException {
		
		
		if(followingTable.get(nickUser.getNick()) == null) {
			
			List<String> following = new LinkedList<String>();
			followingTable.put(nickUser.getNick(), following);
		}
		
		followingTable.get(nickUser.getNick()).add(userToFollow.getNick());
		
		
		if(followersTable.get(userToFollow.getNick()) == null) {
			List<String> followers = new LinkedList<String>();
			followersTable.put(userToFollow.getNick(), followers);
		}
		followersTable.get(userToFollow.getNick()).add(nickUser.getNick()); 
		
	
	}
	
	@Override
	public synchronized void unFollow(User nickUser, User userToUnfollow) throws RemoteException {
		
		if(followingTable.get(nickUser.getNick()) != null) {
			followingTable.get(nickUser.getNick()).remove(userToUnfollow.getNick()); 
		}
		
		if(followersTable.get(userToUnfollow.getNick()) != null) {
			followersTable.get(userToUnfollow.getNick()).remove(nickUser.getNick()); 
		}
	}
	
	@Override
	public List<String> getFollowers(String nickUser) throws RemoteException {
		List<String> followers = new LinkedList<String>();
		
		if(followersTable.get(nickUser) != null) {
						
			followers = followersTable.get(nickUser);
		}
		return followers;
	}
	@Override
	public List<String> getFollowing(String nickUser) throws RemoteException {
		List<String> following = new LinkedList<>();
		
		if(followingTable.get(nickUser)  != null) {
			following = followingTable.get(nickUser);
		}
		return following;
	}
	@Override
	public void publishTrino(String nickUser, String trino) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public synchronized void addTrinoToList(String nickUser, Trino trino) throws RemoteException {
		// adds trino to users trino table
		if(trinosTable.get(nickUser) == null) {
			trinosTable.put(nickUser, new LinkedList<Trino>());
		}
		trinosTable.get(nickUser).add(trino);

		
	}
	@Override
	public synchronized void deleteTrino(String nickUser, Trino trino) throws RemoteException {
		// deletes the trino from the offline list for given user
		trinosTable.get(nickUser).remove(trino);
		trinoslist.remove(trino);
		
	}

	@Override
	public List<Trino> getOfflineTrinos(String nickUser) throws RemoteException {
		if(offlineTrinosTable.containsKey(nickUser)) {
		return offlineTrinosTable.get(nickUser);
		}else {return null;}
	}

	@Override
	public synchronized void addTrinosOffline(Trino trino, String nickReceiver) throws RemoteException {
		if(offlineTrinosTable.get(nickReceiver)==null) {
			
			offlineTrinosTable.put(nickReceiver, new LinkedList<Trino>() );
			
		}
		offlineTrinosTable.get(nickReceiver).add(trino);
		
	}
	@Override
	public synchronized void deleteTrinoOffline(String nickfollower, Trino trino) throws RemoteException {
		// deletes the trino from the offline list for given user
		offlineTrinosTable.get(nickfollower).remove(trino);

		long time = trino.ObtenerTimestamp();
				
		ListIterator<Trino> it = offlineTrinosTable.get(nickfollower).listIterator();
		while(it.hasNext()) {
			
			if(it.next().ObtenerTimestamp() == time) {
					it.remove();	
				
			}
		}
	}

	@Override
	public List<Trino> getAllTrinos() throws RemoteException {
			if(trinoslist == null) {
				this.trinoslist = new LinkedList<Trino>();
			}
		return trinoslist;
	}

	@Override
	public void addAllTrinos(Trino trino) throws RemoteException { // redundant but will sve time in the end
		trinoslist.add(trino);
		
	}

	@Override
	public List<Trino> getUsersTrinos(String nickUser) throws RemoteException {
		
		List<Trino> userTrinos = new LinkedList<Trino>();
		if(trinosTable.get(nickUser) != null) {
			userTrinos = trinosTable.get(nickUser);
		}
		return userTrinos;
	}

	@Override
	public void setOfflineTrinos(String nickUser) throws RemoteException {
		offlineTrinosTable.get(nickUser).clear();
		
	}
	
	




	


	

	

}
