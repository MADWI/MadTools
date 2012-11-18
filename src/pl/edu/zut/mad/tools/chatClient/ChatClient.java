package pl.edu.zut.mad.tools.chatClient;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * Klasa klienta czatu tekstowego. Nie jest ona bardzo zaawansowana, ale swoje
 * zadanie spe³ania. Aby zakoñczyæ nadawawnie nale¿y albo wyjœæ z aplikacji,
 * albo wys³aæ wiadomoœæ CLOSE.
 * 
 * Nie wiem dlaczego w niektórych miejscach muszê, a w innych nie muszê
 * konfigurowaæ strumienia wychodz¹cego. Wydaje mi sie, ¿e konfiguracja w
 * onStop() jest zbêdna, ale nie mia³em mo¿liwoœci tego sprawdziæ.
 * 
 * @author Sebastian Peryt
 * 
 */
public class ChatClient extends Activity {

	/*
	 * Zmienne
	 */
	/**
	 * Adres serwera IP nale¿y podaæ razem z portem czyli na przyk³ad:
	 * <b>192.35.28.88:8080</b> 
	 * W momencie po³¹czenia ta wartoœæ jest rodzielana na
	 * adres IP i port prze aplikacjê
	 */
	private EditText serverIp;

	private Button connectPhones;
	private Button sendMsg;

	// wa¿na rzecz - bufor do pisania wiadomoœci wychodz¹cych
	BufferedWriter out = null;

	private EditText sendField;

	private String serverIpAddress = "";
	// port serwera
	private int serverPort = 0;

	// TAG
	private static final String TAG = "AndChatClient";

	/*
	 * socket klienta
	 */
	private Socket s = null;

	/*
	 * KONIEC - Zmienne
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/*
		 * Konfiguracja ró¿nych elementów layoutu
		 */
		// adres IP serwera
		serverIp = (EditText) findViewById(R.id.server_ip);
		connectPhones = (Button) findViewById(R.id.connect_phones);

		// wiadomoœæ do wys³ania
		sendField = (EditText) findViewById(R.id.send_field);
		sendMsg = (Button) findViewById(R.id.msg_send);

		connectPhones.setOnClickListener(connectListener);
		sendMsg.setOnClickListener(sendMessage);
		/*
		 * KONIEC - Konfiguracja ró¿nych elementów layoutu
		 */
	}

	@Override
	protected void onStop() {
		super.onStop();
		/*
		 * Operacja poni¿ej wymagana, inaczej wszystko bêdzie ca³y czas dzia³aæ,
		 * nawet po wyjœciu z aplikacji
		 */
		try {
			out = new BufferedWriter(
					new OutputStreamWriter(s.getOutputStream()));
			/*
			 * wyslanie sygnalu konca, tylko na tak¹ wiadomoœæ (wa¿ne du¿e
			 * litery) serwer siê zatrzyma
			 */
			String outMsg = "CLOSE";
			// wpisanie wiadomoœci do strumienia
			out.write(outMsg);
			// wyczyszczenie strumienia, czyli wys³anie wiadomoœci
			out.flush();
			// zamykamy socket
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private OnClickListener connectListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			// wyci¹gniêcie IP i portu serwera z adresu podanego przez
			// u¿ytkownika
			String[] serverTmp = serverIp.getText().toString().split(":");

			serverIpAddress = serverTmp[0];
			serverPort = Integer.parseInt(serverTmp[1]);
			// po³¹czenie siê z serwerem na bazie posiadanych danych
			runTcpConnection();
		}
	};

	private OnClickListener sendMessage = new OnClickListener() {

		@Override
		public void onClick(View v) {
			sendMessageToServer(sendField.getText().toString());
		}
	};

	/**
	 * Funkcja, której zadaniem jest nawi¹zanie pol¹czenia z serwerem po
	 * wprowadzeniu jego adresu IP i portu
	 */
	private void runTcpConnection() {
		try {
			// Skonfigurowanie socketa
			s = new Socket(serverIpAddress, serverPort);
			out = new BufferedWriter(
					new OutputStreamWriter(s.getOutputStream()));

			// Pierwsza wiadomoœæ, któr¹ otrzymuje serwer
			String outMsg = "TCP po³¹czono z " + serverIpAddress + ":"
					+ serverPort + System.getProperty("line.separator");

			// wpisanie wiadomoœci do strumienia
			out.write(outMsg);
			// wyczyszczenie struminia i wys³anie wiadomoœci
			out.flush();
			Log.i(TAG, "wyslano: " + outMsg);
			// catch dodane przez SDK
		} catch (UnknownHostException e) {
			Log.e(TAG, "RTC Blad - UnknownHostException: " + e.toString());
		} catch (IOException e) {
			Log.e(TAG, "RTC Blad - IOException: " + e.toString());
		}
	};

	/**
	 * G³ówna funkcja wysy³aj¹ca wiadomoœæ na serwer.
	 * 
	 * @param str
	 *            wiadomoœæ, która ma byæ wys³ana na serwer. Nie zaobserwowa³em
	 *            (ale te¿ specjalnie nie testowa³em), czy jest jakaœ minimalna
	 *            iloœæ znaków
	 */
	public void sendMessageToServer(String str) {
		try {
			/*
			 * przygotowanie wiadomoœci wychodz¹cej napisanej przez
			 * u¿ytkownikaDodanie nowej linii do wiadomoœæ wychodz¹cej jest
			 * konieczne, bo inaczej wiadomoœci nie bêd¹ umieszczane pod sob¹
			 */
			String outMsg = str + System.getProperty("line.separator");
			// wpiasnie wiadomoœci do strumienia wychodz¹cego
			out.write(outMsg);
			// wyczyszczenie strumienia wychodz¹cego
			out.flush();
			Log.i(TAG, "wyslano: " + outMsg);
			/*
			 * sprawdzenie, czy wiadomoœæ to nie CLOSE i ew. zamkniêcie klienta
			 * w takim wypadku
			 */
			if (out.equals("CLOSE")) {
				s.close();
				this.finish();
			}
			// catch dodane autmatycznie przez SDK
		} catch (UnknownHostException e) {
			Log.e(TAG, "SMTS Blad - UnknownHostException: " + e.toString());
		} catch (IOException e) {
			Log.e(TAG, "SMTS Blad - IOException: " + e.toString());
		}

	}
}