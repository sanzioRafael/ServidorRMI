import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterf extends Remote {
	public void atualizarMensagem(String mensagem, String nome)
			throws RemoteException;

	public void definirStub(ClientInterf chat) throws RemoteException;

	public void sairChat() throws RemoteException;
}
