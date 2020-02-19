package es.uned.usuario;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import es.uned.common.Trino;
import es.uned.interfaces.CallbackUsuarioInterface;

public class CallbackUsuarioImpl extends UnicastRemoteObject implements CallbackUsuarioInterface{

	private static final long serialVersionUID = 8075802665472357512L;

	protected CallbackUsuarioImpl() throws RemoteException {
		super();
			}

	@Override
	public void displayTrinos(List<Trino> trinos) throws RemoteException {
		for (Trino trino: trinos) {
			System.out.println("> " + trino.ObtenerNickPropietario()+"# "+ trino.ObtenerTrino());
		}
		
	}
	public void displayTrino(Trino trino) throws RemoteException  {
		
			System.out.println("> " + trino.ObtenerNickPropietario()+"# "+ trino.ObtenerTrino());
		
		
	}

}


