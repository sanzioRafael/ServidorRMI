import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Chat extends UnicastRemoteObject implements ClientInterf {
	private JFrame janela;
	private JPanel painel;
	private JTextArea jTextArea;
	private JTextField jTextField;
	private JButton jButton;
	private ClientInterf stub;

	public Chat() throws RemoteException {
		janela = new JFrame("Servidor");
		janela.setSize(480, 480);
		janela.setVisible(true);
		janela.setResizable(false);

		painel = new JPanel();
		painel.setSize(600, 260);
		painel.setVisible(true);
		painel.setBackground(java.awt.Color.gray);

		jTextArea = new JTextArea(20, 40);
		jTextArea.setEnabled(true);
		jTextArea.setVisible(true);
		painel.add(jTextArea);

		jTextField = new JTextField(25);
		jTextField.setVisible(true);
		painel.add(jTextField);

		jButton = new JButton("Enviar");
		jButton.setVisible(true);
		jButton.setSize(30, 20);
		painel.add(jButton);

		janela.add(painel);
		janela.validate();

		janela.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				Servidor.sairChat();
				try {
					stub.sairChat();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});

		jButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String mensagem = jTextField.getText();
					Calendar hora = Calendar.getInstance();
					String nome = "Servidor às "
							+ hora.get(Calendar.HOUR_OF_DAY) + ":"
							+ hora.get(Calendar.MINUTE) + " disse: ";
					stub.atualizarMensagem(mensagem, nome);
					jTextArea.append(nome + mensagem + "\n");

					jTextField.setText(null);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	@Override
	public void atualizarMensagem(String mensagem, String nome)
			throws RemoteException {
		Calendar hora = Calendar.getInstance();
		jTextArea.append(nome + " às " + hora.get(Calendar.HOUR_OF_DAY) + ":"
				+ hora.get(Calendar.MINUTE) + " disse: " + mensagem + "\n");
	}

	@Override
	public void definirStub(ClientInterf chat) throws RemoteException {
		this.stub = chat;
	}

	@Override
	public void sairChat() throws RemoteException {
		janela.dispose();
	}

}
