import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class ControlClimatico{

	static String buferRecepcion;
	static String recibido = "";
	static String host = "192.168.0.172";
	static int port = 80;
	static Socket socketServicio = null;
	static String password = "Authorization: Basic cHJhY3RpY2E6ZnI=";

	static String web = "GET / HTTP/1.1\nHost: 192.168.0.172\nUser-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\nAccept-Language: en-US,en;q=0.5\nAccept-Encoding: gzip, deflate\n"+password+"\nConnection: keep-alive\nUpgrade-Insecure-Requests: 1\nCache-Control: max-age=0\n";
	static String enciendeRiego = "GET /?R=ON HTTP/1.1\nHost: 192.168.0.172\nUser-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\nAccept-Language: en-US,en;q=0.5\nAccept-Encoding: gzip, deflate\n"+password+"\nConnection: keep-alive\nUpgrade-Insecure-Requests: 1\nCache-Control: max-age=0\n";
	static String apagaRiego = "GET /?R=OFF HTTP/1.1\nHost: 192.168.0.172\nUser-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\nAccept-Language: en-US,en;q=0.5\nAccept-Encoding: gzip, deflate\n"+password+"\nConnection: keep-alive\nUpgrade-Insecure-Requests: 1\nCache-Control: max-age=0\n";
	static String enciendeCalefaccion = "GET /?C=ON HTTP/1.1\nHost: 192.168.0.172\nUser-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\nAccept-Language: en-US,en;q=0.5\nAccept-Encoding: gzip, deflate\n"+password+"\nConnection: keep-alive\nUpgrade-Insecure-Requests: 1\nCache-Control: max-age=0\n";
	static String apagaCalefaccion = "GET /?C=OFF HTTP/1.1\nHost: 192.168.0.172\nUser-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\nAccept-Language: en-US,en;q=0.5\nAccept-Encoding: gzip, deflate\n"+password+"\nConnection: keep-alive\nUpgrade-Insecure-Requests: 1\nCache-Control: max-age=0\n";
	
	public static void main(String[] args){
		Scanner entrada = new Scanner(System.in);
		int opcion;

		do{
			datos();
			System.out.println("1) Encender Riego");
			System.out.println("2) Apagar Riego");
			System.out.println("3) Encender Calefaccion");
			System.out.println("4) Apagar Calefaccion");
			System.out.println("5) Refrescar informacion");
			System.out.println("6) Salir");
			System.out.println("Opcion elegida:");

			opcion = entrada.nextInt();

			switch (opcion){
	            case 1:  enviaInformacion(enciendeRiego);
	                     break;
	            case 2:  enviaInformacion(apagaRiego);
	                     break;
	            case 3:  enviaInformacion(enciendeCalefaccion);
	                     break;
	            case 4:  enviaInformacion(apagaCalefaccion);
	                     break;
	          
        	}
		} while (opcion!=6);
	}

	public static void datos(){
		try{
			PrintWriter outPrinter;
			BufferedReader inReader;
			socketServicio = new Socket(host, port);
			InputStream inputStream = socketServicio.getInputStream();
			OutputStream outputStream = socketServicio.getOutputStream();
			outPrinter = new PrintWriter(outputStream, true);
			outPrinter.println(web);
			outPrinter.flush();
			inReader = new BufferedReader(new InputStreamReader(inputStream));
			buferRecepcion = inReader.readLine();
			recibido += buferRecepcion;
			while (buferRecepcion != null){
				recibido += buferRecepcion;
				buferRecepcion = inReader.readLine();
			}
			//System.out.println(recibido);
			int pos = recibido.indexOf("Temperatura");
			if (pos != -1){
				System.out.println(recibido.substring(pos,pos+20));
			}
			pos = recibido.indexOf("Humedad");
			if (pos != -1){
				System.out.println(recibido.substring(pos,pos+15));
			}
			pos = recibido.indexOf("Riego <font color=");
			if (pos != -1){
				System.out.println("Riego " + recibido.substring(pos+27,pos+35));
			}
			pos = recibido.indexOf("Calefaccion <font color=");
			if (pos != -1){
				pos += 6;
				System.out.println("Calefaccion " + recibido.substring(pos+27,pos+35));
			}
			socketServicio.close();
		} catch (UnknownHostException e){
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e){
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
		recibido = "";
	}

	public static void  enviaInformacion(String mensaje){
		try{
			PrintWriter outPrinter;
			BufferedReader inReader;
			socketServicio = new Socket(host, port);
			InputStream inputStream = socketServicio.getInputStream();
			OutputStream outputStream = socketServicio.getOutputStream();
			outPrinter = new PrintWriter(outputStream, true);
			outPrinter.println(mensaje);
			outPrinter.flush();
			inReader = new BufferedReader(new InputStreamReader(inputStream));
			buferRecepcion = inReader.readLine();
			recibido += buferRecepcion;
			while (buferRecepcion != null){
				recibido += buferRecepcion;
				buferRecepcion = inReader.readLine();
			}

			socketServicio.close();
		} catch (UnknownHostException e){
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e){
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
		recibido = "";
	}
}