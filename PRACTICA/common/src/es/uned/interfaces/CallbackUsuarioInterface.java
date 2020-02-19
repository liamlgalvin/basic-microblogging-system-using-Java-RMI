package es.uned.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import es.uned.common.Trino;

public interface CallbackUsuarioInterface extends Remote {
	
	public void displayTrinos(List<Trino> trinos) throws RemoteException; ;
	public void displayTrino(Trino trino) throws RemoteException; ;

}
