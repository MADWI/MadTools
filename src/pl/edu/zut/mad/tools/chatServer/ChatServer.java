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
 * Klasa serwera czatu tekstowego. Odbi�r wiadomosci odbywa si� w wiecznej p�tli
 * wewn�trz drugiego w�tku, �eby nie zwiesi� GUI. Serwer dzia�a przy po��czeniu
 * z jednym urz�dzeniem, nie mia�em okazji sprawdzic dzia�ania przy pr�bie
 * pod��czenia kolejnego urz�dzenia, jednak s�dz�, �e socket mo�e byc tylko
 * jeden, wi�c obowi�zuje najprawdopodonbniej zasada - kto pierwszy ten lepszy.
 * Port serwera jest ustalany na sztywno w kodzie, bo nie widzia�em sensu �eby
 * go zmienia�.
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

	// zmienna sta�a odpowiadaj�ca za ci�g�e sluchanie po��czenia
	private boolean connected = true;

	Socket client = null;

	public String error = null;

	public static String SERVERIP = null;

	private static final String TAG = "AndChatServer";

	// odg�rnie ustalony port serwera
	public static final int SERVERPORT = 8080;

	// handler do obs�ugi w�tku nas�uchiwania wiadomo�ci przez serwer
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
		 * Konfiguracja r�nych element�w layoutu
		 */
		serverStatus = (TextView) findViewById(R.id.server_status);
		recivedMsg = (TextView) findViewById(R.id.rec_msg);
		/*
		 * KONIEC -Konfiguracja r�nych element�w layoutu
		 */

		// pobranie lokalnego adresu IP
		SERVERIP = getLocalIpAddress();

		/*
		 * utworzenie i uruchomienie nowego w�tku, poniewa� odbieranie
		 * wiadomo�ci musi ci�gle dzia�a� w oddzielnym w�tku, inaczej wszystko
		 * si� zwiesi.
		 */
		Thread fst = new Thread(new ServerThread());
		fst.start();
	}

	/**
	 * Zadanie tej klasy to ci�gle utzrymywanie i nas�uchiwanie po�acze�
	 * przychodz�cych od klienta.
	 */
	public class ServerThread implements Runnable {

		public void run() {
			try {
				if (SERVERIP != null) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							serverStatus.setText("Nas�uchiwanie na adresie: "
									+ SERVERIP + ":" + SERVERPORT);
						}
					});
					serverSocket = new ServerSocket(SERVERPORT);
					// ci�g�e nasluchiwanie po��cze�
					while (true) {
						client = serverSocket.accept();
						handler.post(new Runnable() {
							@Override
							public void run() {
								serverStatus.setText("Po��czono."
										+ System.getProperty("line.separator"));
							}
						});

						try {
							line = null;
							while (connected) {
								/*
								 * bufor struminia przychodz�cego
								 * odpowiedzialnego za otrzymywanie wiadomo�ci
								 */
								BufferedReader in = new BufferedReader(
										new InputStreamReader(
												client.getInputStream()));
								/*
								 * sprawdzenie, czy wiadomo�� przychodz�ca
								 * zawiera tekst czy nie.
								 */
								if ((line = in.readLine()) != null) {
									Log.d(TAG, line);
									handler.post(new Runnable() {
										@Override
										public void run() {
											/*
											 * sprawdzenie otrzymanej
											 * wiadomo�ci. Je�li jest to CLOSE
											 * nastepuje zamkni�cie socket�w.
											 * Je�li nie nast�puje wy�wietlenie
											 * nowo przychodz�cej wiadomo�ci
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
											 * wiadomo�� nie zawiera CLOSE
											 */
											else {
												recivedMsg.append("Wiadomo��: "
														+ line
														+ System.getProperty("line.separator"));
											}
										}
									});
								}
								/*
								 * wiadomo�� przychodz�ca nie zawiera tekstu
								 */
								else {
									recivedMsg
											.append("pusto"
													+ System.getProperty("line.separator"));
								}
							}
							break;
						}
						// b��d po��czenia urz�dze�
						catch (Exception e) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									serverStatus
											.setText("Wyst�pi� b��d po��czenia. Prosz� po��czy� ponownie urz�dzenia");
								}
							});
							e.printStackTrace();
						}
					}
				}
				/*
				 * nie mo�na nawi�za� pol�czeni, poniewa� nie ma Internetu i nie
				 * mo�na pobrac adresu IP serwera.
				 */
				else {
					handler.post(new Runnable() {
						@Override
						public void run() {
							serverStatus
									.setText("Brak po��czenia z Internetem");
						}
					});
				}
			} catch (SocketException e) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						serverStatus.setText("Zamkni�to Socket");
					}
				});
				e.printStackTrace();
			} catch (Exception e) {
				error = e.toString();
				handler.post(new Runnable() {
					@Override
					public void run() {
						serverStatus.setText("B��d " + error);
					}
				});
				e.printStackTrace();
			}
		}
	}

	/**
	 * Funkcja pobiera lokalny adres IP urz�dzenia w sieci. Dok�adnie nie wiem
	 * jak ona dzia�a, poniewa� zosta�a skopiowana z gotowego przyk�adu z
	 * internetu. 
	 * 
	 * Z tego co wyczyta�em chodzi tutaj o przej�cie wszystkich
	 * mo�liwych interfejs�w internetowych i urzywaj�c tego kt�ry dzia�a
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
		 * konieczne zamkni�cie obu socket�w, poniewa� inaczej b�d� dzia�a�y
		 * ca�y czas.
		 */
		try {
			serverSocket.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}