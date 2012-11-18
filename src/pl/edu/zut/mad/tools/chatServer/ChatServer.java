package pl.edu.zut.mad.tools.chatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import pl.edu.zut.mad.tools.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

/**
 * Klasa serwera czatu tekstowego. Odbiór wiadomosci odbywa siê w wiecznej pêtli
 * wewn¹trz drugiego w¹tku, ¿eby nie zwiesiæ GUI. Serwer dzia³a przy po³¹czeniu
 * z jednym urz¹dzeniem, nie mia³em okazji sprawdzic dzia³ania przy próbie
 * pod³¹czenia kolejnego urz¹dzenia, jednak s¹dzê, ¿e socket mo¿e byc tylko
 * jeden, wiêc obowi¹zuje najprawdopodonbniej zasada - kto pierwszy ten lepszy.
 * Port serwera jest ustalany na sztywno w kodzie, bo nie widzia³em sensu ¿eby
 * go zmieniaæ.
 * 
 * @author Sebastian Peryt
 * 
 */
public class ChatServer extends Activity {
	/*
	 * Zmienne
	 */
	private TextView serverStatus;
	private TextView recivedMsg;
	String line = null;

	// zmienna sta³a odpowiadaj¹ca za ci¹g³e sluchanie po³¹czenia
	private boolean connected = true;

	Socket client = null;

	public String error = null;

	public static String SERVERIP = null;

	private static final String TAG = "AndChatServer";

	// odgórnie ustalony port serwera
	public static final int SERVERPORT = 8080;

	// handler do obs³ugi w¹tku nas³uchiwania wiadomoœci przez serwer
	private Handler handler = new Handler();

	private ServerSocket serverSocket;

	/*
	 * KONIEC - Zmienne
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_server);
		/*
		 * Konfiguracja ró¿nych elementów layoutu
		 */
		serverStatus = (TextView) findViewById(R.id.server_status);
		recivedMsg = (TextView) findViewById(R.id.rec_msg);
		/*
		 * KONIEC -Konfiguracja ró¿nych elementów layoutu
		 */

		// pobranie lokalnego adresu IP
		SERVERIP = getLocalIpAddress();

		/*
		 * utworzenie i uruchomienie nowego w¹tku, poniewa¿ odbieranie
		 * wiadomoœci musi ci¹gle dzia³aæ w oddzielnym w¹tku, inaczej wszystko
		 * siê zwiesi.
		 */
		Thread fst = new Thread(new ServerThread());
		fst.start();
	}

	/**
	 * Zadanie tej klasy to ci¹gle utzrymywanie i nas³uchiwanie po³aczeñ
	 * przychodz¹cych od klienta.
	 */
	public class ServerThread implements Runnable {

		public void run() {
			try {
				if (SERVERIP != null) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							serverStatus.setText("Nas³uchiwanie na adresie: "
									+ SERVERIP + ":" + SERVERPORT);
						}
					});
					serverSocket = new ServerSocket(SERVERPORT);
					// ci¹g³e nasluchiwanie po³¹czeñ
					while (true) {
						client = serverSocket.accept();
						handler.post(new Runnable() {
							@Override
							public void run() {
								serverStatus.setText("Po³¹czono."
										+ System.getProperty("line.separator"));
							}
						});

						try {
							line = null;
							while (connected) {
								/*
								 * bufor struminia przychodz¹cego
								 * odpowiedzialnego za otrzymywanie wiadomoœci
								 */
								BufferedReader in = new BufferedReader(
										new InputStreamReader(
												client.getInputStream()));
								/*
								 * sprawdzenie, czy wiadomoœæ przychodz¹ca
								 * zawiera tekst czy nie.
								 */
								if ((line = in.readLine()) != null) {
									Log.d(TAG, line);
									handler.post(new Runnable() {
										@Override
										public void run() {
											/*
											 * sprawdzenie otrzymanej
											 * wiadomoœci. Jeœli jest to CLOSE
											 * nastepuje zamkniêcie socketów.
											 * Jeœli nie nastêpuje wyœwietlenie
											 * nowo przychodz¹cej wiadomoœci
											 */
											if (line.equals("CLOSE")) {
												recivedMsg
														.append("CLOSE socket");
												connected = false;
												try {
													serverSocket.close();
													client.close();
												} catch (IOException e) {
													// catch dodany przez SDK
													e.printStackTrace();
												}
											}
											/*
											 * wiadomoœæ nie zawiera CLOSE
											 */
											else {
												recivedMsg.append("Wiadomoœæ: "
														+ line
														+ System.getProperty("line.separator"));
											}
										}
									});
								}
								/*
								 * wiadomoœæ przychodz¹ca nie zawiera tekstu
								 */
								else {
									recivedMsg
											.append("pusto"
													+ System.getProperty("line.separator"));
								}
							}
							break;
						}
						// b³¹d po³¹czenia urz¹dzeñ
						catch (Exception e) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									serverStatus
											.setText("Wyst¹pi³ b³¹d po³¹czenia. Proszê po³¹czyæ ponownie urz¹dzenia");
								}
							});
							e.printStackTrace();
						}
					}
				}
				/*
				 * nie mo¿na nawi¹zaæ pol¹czeni, poniewa¿ nie ma Internetu i nie
				 * mo¿na pobrac adresu IP serwera.
				 */
				else {
					handler.post(new Runnable() {
						@Override
						public void run() {
							serverStatus
									.setText("Brak po³¹czenia z Internetem");
						}
					});
				}
			} catch (SocketException e) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						serverStatus.setText("Zamkniêto Socket");
					}
				});
				e.printStackTrace();
			} catch (Exception e) {
				error = e.toString();
				handler.post(new Runnable() {
					@Override
					public void run() {
						serverStatus.setText("B³¹d " + error);
					}
				});
				e.printStackTrace();
			}
		}
	}

	/**
	 * Funkcja pobiera lokalny adres IP urz¹dzenia w sieci. Dok³adnie nie wiem
	 * jak ona dzia³a, poniewa¿ zosta³a skopiowana z gotowego przyk³adu z
	 * internetu. 
	 * 
	 * Z tego co wyczyta³em chodzi tutaj o przejœcie wszystkich
	 * mo¿liwych interfejsów internetowych i urzywaj¹c tego który dzia³a
	 * pobranie i przypisanie adresu IP serwera.
	 */
	private String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e(TAG, ex.toString());
		}
		return null;
	}

	@Override
	protected void onStop() {
		super.onStop();
		/*
		 * konieczne zamkniêcie obu socketów, poniewa¿ inaczej bêd¹ dzia³a³y
		 * ca³y czas.
		 */
		try {
			serverSocket.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}