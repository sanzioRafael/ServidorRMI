/*
 * Falta: Modificar video;
 * 
 * Modificar Video: biblioteca para modificar o video;
 *  
 * 
 * */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;

import javax.swing.JOptionPane;

import Jama.Matrix;

@SuppressWarnings("serial")
public class Servidor extends UnicastRemoteObject implements Metodos {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button btnStartServer, btnPausar;

	@FXML
	public Label lblStatus, lblData;;

	@FXML
	private TextField tfPorta;

	@FXML
	private ComboBox<String> cbPasta;

	@FXML
	private MediaView mvVideo;

	@FXML
	private TextArea txtAChat1;

	private MediaPlayer mediaPlayer;
	private Media media;
	private String opPasta;

	private Matrix matriz, matrizInv;
	private static int port, chatLim;
	private File file;
	private boolean isPaused;

	public Servidor() throws RemoteException {
		super();
	}

	@FXML
	void initialize() throws IOException {
		chatLim = 0;
		isPaused = false;
	}

	@FXML
	void btnStartServerOnAction(ActionEvent event) throws RemoteException,
			IOException {

		port = Integer.parseInt(tfPorta.getText());
		Servidor stub = Servidor.this;
		Registry registro;
		try {

			registro = LocateRegistry.createRegistry(port);
			registro.rebind("RMI", stub);
			lblStatus.setText("Servidor iniciado com sucesso!");
			lblStatus.setTextFill(Color.GREEN);

			opPasta = cbPasta.getValue();
			file = new File(opPasta);
			file.mkdir();

			Calendar data = Calendar.getInstance();
			lblData.setText("Data: " + data.get(Calendar.DAY_OF_MONTH) + "/"
					+ data.get(Calendar.MONTH) + "/" + data.get(Calendar.YEAR)
					+ "                 Hora: "
					+ data.get(Calendar.HOUR_OF_DAY) + ":"
					+ data.get(Calendar.MINUTE));
		} catch (RemoteException e) {
			lblStatus.setText("Erro ao iniciar o servidor");
			lblStatus.setTextFill(Color.RED);
		}
	}

	@Override
	public void armazenaArquivo(byte[] arquivo, String nome)
			throws RemoteException {
		try {
			switch (opPasta) {
			case "arquivos":
				file = new File("arquivos", nome);
				break;
			case "imagem":
				file = new File("imagem", nome);
				break;
			case "video":
				file = new File("video", nome);
				break;
			}

			file.createNewFile();

			FileOutputStream fos = new FileOutputStream(file);
			fos.write(arquivo);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	@Override
	public byte[] zipar(byte[] arquivo, String nomeArquivo1, byte[] arquivo2,
			String nomeArquivo2) throws RemoteException {
		byte[] arqZip = new byte[1024];
		byte[][] arqs = { arquivo, arquivo2 };
		String[] nomes = { nomeArquivo1, nomeArquivo2 };
		FileOutputStream fos;
		try {
			for (int i = 0; i < nomes.length; i++) {
				// escreve os arquivos
				file = new File(nomes[i]);
				file.createNewFile();
				fos = new FileOutputStream(file);
				fos.write(arqs[i]);
				fos.flush();
				fos.close();
			}

			File fileZip = new File("arquivo.zip");
			fos = new FileOutputStream(fileZip);
			ZipOutputStream zos = new ZipOutputStream(fos);
			FileInputStream fis;
			// zipa os arquivos
			for (int j = 0; j < nomes.length; j++) {
				fis = new FileInputStream(nomes[j]);
				zos.putNextEntry(new ZipEntry(nomes[j]));

				int len;
				while ((len = fis.read(arqZip)) > 0) {
					zos.write(arqZip, 0, len);
				}
				zos.closeEntry();
				fis.close();
			}
			zos.close();

			// Transforma o arquivo zip em array de byte e envia de volta ao cliente
			byte[] arquivoZip = new byte[(int) fileZip.length()];
			fis = new FileInputStream(fileZip);
			fis.read(arquivoZip, 0, arquivoZip.length);
			fis.close();
			return arquivoZip;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public byte[] modificaVideo(byte[] video, String nome)
			throws RemoteException {
		file = new File(nome);
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(video);
			fos.flush();
			fos.close();

			byte[] videoMod = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(videoMod, 0, videoMod.length);
			fis.close();

			return videoMod;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	@Override
	public void executaVideo(byte[] video, String nome) throws RemoteException {
		file = new File(nome);
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(video);
			fos.flush();
			fos.close();

			media = new Media(file.toURI().toURL().toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();
			isPaused = false;
			mvVideo.setMediaPlayer(mediaPlayer);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	@FXML
	void btnPausarOnAction(ActionEvent event) {
		if (mediaPlayer != null) {
			if (isPaused) {
				mediaPlayer.play();
				isPaused = false;
				btnPausar.setText("Pausar");
			} else {
				mediaPlayer.pause();
				isPaused = true;
				btnPausar.setText("Continuar");
			}
		}
	}

	@Override
	public double[][] matrizInversa(double[][] matriz) throws RemoteException {
		this.matriz = new Matrix(matriz);
		matrizInv = this.matriz.inverse();
		return matrizInv.getArray();
	}

	@Override
	public boolean chatReq() throws RemoteException {
		if (chatLim < 3) {
			chatLim++;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void chat(final ClientInterf client) throws RemoteException {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Chat chaat;
				try {
					chaat = new Chat();
					chaat.definirStub(client);
					client.definirStub(chaat);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	public static void sairChat() {
		chatLim--;
	}

}