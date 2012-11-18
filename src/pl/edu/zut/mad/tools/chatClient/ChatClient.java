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
 * zadanie spe�ania. Aby zako�czy� nadawawnie nale�y albo wyj�� z aplikacji,
 * albo wys�a� wiadomo�� CLOSE.
 * 
 * Nie wiem dlaczego w niekt�rych miejscach musz�, a w innych nie musz�
 * konfigurowa� strumienia wychodz�cego. Wydaje mi sie, �e konfiguracja w
 * onStop() jest zb�dna, ale nie mia�em mo�liwo�ci tego sprawdzi�.
 * 
 * @author Sebastian Peryt
 * 
 */
public class ChatClient extends Activity {

	/*
	 * Zmienne
	 */
	/**
	 * Adres serwera IP nale�y poda� razem z portem czyli na przyk�ad:
	 * <b>192.35.28.88:8080</b> 
	 * W momencie po��czenia ta warto�� jest rodzielana na
	 * adres IP i port prze aplikacj�
	 */
	private EditText serverIp;

	private Button connectPhones;
	private Button sendMsg;

	// wa�na rzecz - bufor do pisania wiadomo�ci wychodz�cych
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
		 * Konfiguracja r�nych element�w layoutu
		 */
		// adres IP serwera
		serverIp = (EditText) findViewById(R.id.server_ip);
		connectPhones = (Button) findViewById(R.id.connect_phones);

		// wiadomo�� do wys�ania
		sendField = (EditText) findViewById(R.id.send_field);
		sendMsg = (Button) findViewById(R.id.msg_send);

		connectPhones.setOnClickListener(connectListener);
		sendMsg.setOnClickListener(sendMessage);
		/*
		 * KONIEC - Konfiguracja r�nych element�w layoutu
		 */
	}

	@Override
	protected void onStop() {
		super.onStop();
		/*
		 * Operacja poni�ej wymagana, inaczej wszystko b�dzie ca�y czas dzia�a�,
		 * nawet po wyj�ciu z aplikacji
		 */
		try {
			out = new BufferedWriter(
					new OutputStreamWriter(s.getOutputStream()));
			/*
			 * wyslanie sygnalu konca, tylko na tak� wiadomo�� (wa�ne du�e
			 * litery) serwer si� zatrzyma
			 */
			String outMsg = "CLOSE";
			// wpisanie wiadomo�ci do strumienia
			out.write(outMsg);
			// wyczyszczenie strumienia, czyli wys�anie wiadomo�ci
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

			// wyci�gni�cie IP i portu serwera z adresu podanego przez
			// u�ytkownika
			String[] serverTmp = serverIp.getText().toString().split(":");

			serverIpAddress = serverTmp[0];
			serverPort = Integer.parseInt(serverTmp[1]);
			// po��czenie si� z serwerem na bazie posiadanych danych
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
	 * Funkcja, kt�rej zadaniem jest nawi�zanie pol�czenia z serwerem po
	 * wprowadzeniu jego adresu IP i portu
	 */
	private void runTcpConnection() {
		try {
			// Skonfigurowanie socketa
			s = new Socket(serverIpAddress, serverPort);
			out = new BufferedWriter(
					new OutputStreamWriter(s.getOutputStream()));

			// Pierwsza wiadomo��, kt�r� otrzymuje serwer
			String outMsg = "TCP po��czono z " + serverIpAddress + ":"
					+ serverPort + System.getProperty("line.separator");

			// wpisanie wiadomo�ci do strumienia
			out.write(outMsg);
			// wyczyszczenie struminia i wys�anie wiadomo�ci
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
	 * G��wna funkcja wysy�aj�ca wiadomo�� na serwer.
	 * 
	 * @param str
	 *            wiadomo��, kt�ra ma by� wys�ana na serwer. Nie zaobserwowa�em
	 *            (ale te� specjalnie nie testowa�em), czy jest jaka� minimalna
	 *            ilo�� znak�w
	 */
	public void sendMessageToServer(String str) {
		try {
			/*
			 * przygotowanie wiadomo�ci wychodz�cej napisanej przez
			 * u�ytkownikaDodanie nowej linii do wiadomo�� wychodz�cej jest
			 * konieczne, bo inaczej wiadomo�ci nie b�d� umieszczane pod sob�
			 */
			String outMsg = str + System.getProperty("line.separator");
			// wpiasnie wiadomo�ci do strumienia wychodz�cego
			out.write(outMsg);
			// wyczyszczenie strumienia wychodz�cego
			out.flush();
			Log.i(TAG, "wyslano: " + outMsg);
			/*
			 * sprawdzenie, czy wiadomo�� to nie CLOSE i ew. zamkni�cie klienta
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