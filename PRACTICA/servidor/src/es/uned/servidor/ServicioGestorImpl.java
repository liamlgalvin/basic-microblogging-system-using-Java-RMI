package es.uned.servidor;

import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import es.uned.common.Trino;
import es.uned.common.User;
import es.uned.interfaces.*;

public class ServicioGestorImpl extends UnicastRemoteObject implements ServicioGestorInterface{
	
	
	Map<String, CallbackUsuarioInterface> callbackList = new HashMap<String, CallbackUsuarioInterface>();
	

	
	private static final long serialVersionUID = 6141700621140111647L;

	protected ServicioGestorImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	// class variables
	private String hostAddress;
	private int hostPort;
	
	// metodos de rmi
	public void setHostAddress(String hostAddress) {
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
		Registry registry = LocateRegistry.getRegistry();
		String hostUrl = "rmi://" + java.net.InetAddress.getLocalHost().getHostAddress() + ":" + getHostPort() + "/" + "ServicioDatos";
		ServicioDatosInterface servicioDatos = (ServicioDatosInterface) registry.lookup(hostUrl);
		return servicioDatos;
	}
	
	
	/** The remote method that "does all the work". This won't get
	 * called until the client starts up.
	 */
	@Override
	public void connect(String nick, CallbackUsuarioInterface callback) throws RemoteException {
		callbackList.put(nick, callback);
		
		if( getOfflineTrinos(nick)!= null) {
			
			try {
				 
				ServicioDatosInterface sd = getRemoteReference();
				callback.displayTrinos(getOfflineTrinos(nick));
				sd.setOfflineTrinos(nick);
			} catch (UnknownHostException | NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	@Override
	public void disconnect(String nick) throws RemoteException {
		callbackList.remove(nick);
		}
	
	

	@Override
	public boolean follow(String nickUser, String nickFollow) throws RemoteException {
		try {
			
			ServicioDatosInterface sd = getRemoteReference();	

				
				User user = sd.getUser(nickUser);
				User userFollow = sd.getUser(nickFollow);
				
				sd.follow(user , userFollow);
				
				return true;
				
				
		} catch(RemoteException |  NotBoundException | UnknownHostException e) {
			
		}
        return false;
	}

	@Override
	public boolean unFollow(String nickUser, String nickFollow) throws RemoteException {
		
		try {
			ServicioDatosInterface sd = getRemoteReference();
			
			if(sd.getFollowing(nickUser).contains(nickFollow)) {
				
					sd.unFollow(sd.getUser(nickUser),sd.getUser(nickFollow));
				
					return true;
				
				}
		} catch(RemoteException |  NotBoundException | UnknownHostException e) {
			
		}
        return false;
	}

	@Override
	public List<String> getFollowers(String nickUser) throws RemoteException {
		
List<String> followers = new LinkedList<>();
		
		try {
			ServicioDatosInterface sd = getRemoteReference();
			
			if(sd.getRegisteredUsers().containsKey(nickUser)) { 
					
				followers = sd.getFollowers(nickUser);
				
				}
		} catch(RemoteException |  NotBoundException | UnknownHostException e) {
			
		}

		
		return followers;
	}

	@Override
	public List<String> getFollowing(String nickUser) throws RemoteException {
		
		List<String> following = new LinkedList<>();
		
		try {
			ServicioDatosInterface sd = getRemoteReference();
			
			if(sd.getRegisteredUsers().containsKey(nickUser)) { 
					
				following = sd.getFollowing(nickUser);
				
				}
		} catch(RemoteException |  NotBoundException | UnknownHostException e) {
			
		}

		
		return following;
	}

	@Override
	public void sendTrino(String nickSender,String body) throws RemoteException {
		try {
			ServicioDatosInterface sd = getRemoteReference();
					
			Trino trino = makeTrino(nickSender, body);
			
			List<String> followers =  getFollowers(nickSender);
			
			for(String follower: followers) {
				if(sd.getLoggedinUsers().containsKey(follower)) { // significa que usuario esta online. 
					callbackList.get(follower).displayTrino(trino); // el trino se envia directamente con callback
				}else {
					sd.addTrinosOffline( trino , follower );  // si no se guarda en la lista de offline trinos
				}
			}
			
			sd.addTrinoToList(nickSender, trino); // el trino se guarda 
			sd.addAllTrinos(trino);
				
			
		} catch(RemoteException |  NotBoundException | UnknownHostException e) {
			
		}

		
	}

	@Override
	public synchronized void deleteTrino(String nickUser, Trino trino) throws RemoteException {
		List<String> followers = getFollowers(nickUser);
		
		try {
			ServicioDatosInterface sd = getRemoteReference();
			
			// remove from each offline follower
			
			for(String follower: followers) {
				if(sd.getOfflineTrinos(follower) != null) {
					
					sd.deleteTrinoOffline(follower, trino);
				}
			}
			
			// remove from all trinos list and remove from  trinos list
			
			sd.deleteTrino(nickUser, trino);
			
			
			
		} catch (UnknownHostException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

	@Override
	public List<Trino> getOfflineTrinos(String nick) throws RemoteException {
		
		List<Trino> offlineTrinos = new LinkedList<>();
		// check for offline trinos

		try {
			ServicioDatosInterface sd = getRemoteReference();

			offlineTrinos = sd.getOfflineTrinos(nick);

		} catch(RemoteException |  NotBoundException | UnknownHostException e) {
			
		}

		
		return offlineTrinos;
	}

	@Override
	public Trino makeTrino(String nickSender, String body) throws RemoteException {
		Trino trino = new Trino(body, nickSender);
		return trino;
	}

	@Override
	public List<Trino> getTrinos(String nick) throws RemoteException {
		List<Trino> trinos = new LinkedList<Trino>();
		
		try {
			ServicioDatosInterface sd = getRemoteReference();
			
			return sd.getUsersTrinos(nick);
				

		} catch(RemoteException |  NotBoundException | UnknownHostException e) {
			
		}
		
	
		
		return trinos;
	}



}
