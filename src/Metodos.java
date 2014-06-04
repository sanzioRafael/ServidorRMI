import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Metodos extends Remote {
	public void armazenaArquivo(byte[] arquivo, String nome)
			throws RemoteException;

	public byte[] zipar(byte[] arquivo, String nomeArquivo1, byte[] arquivo2,
			String nomeArquivo2) throws RemoteException;

	public byte[] modificaVideo(byte[] video, String nome)
			throws RemoteException;

	public void executaVideo(byte[] video, String nome) throws RemoteException;

	public double[][] matrizInversa(double[][] matriz) throws RemoteException;

	public boolean chatReq() throws RemoteException;

	public void chat(ClientInterf client) throws RemoteException;
}