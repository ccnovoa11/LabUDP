package Client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) {

		DatagramSocket socket = null;
		DatagramPacket inPacket = null;
		DatagramPacket outPacket = null;
		byte[] inBuf, outBuf;
		final int PORT = 50000;
		String msg = null;
		Scanner src = new Scanner(System.in);


		try
		{
			InetAddress address = InetAddress.getByName("localhost");
			socket = new DatagramSocket();

			msg = "";
			outBuf =msg.getBytes();
			outPacket = new DatagramPacket(outBuf, 0, outBuf.length,address,PORT);
			socket.send(outPacket);

			inBuf = new byte[65535];
			inPacket = new DatagramPacket(inBuf, inBuf.length);
			socket.receive(inPacket);

			String data = new String(inPacket.getData(), 0, inPacket.getLength());
			//Print file list
			System.out.println(data);

			//Send file name
			String filename = src.nextLine();
			outBuf = filename.getBytes();
			outPacket = new DatagramPacket(outBuf, 0, outBuf.length, address, PORT);
			socket.send(outPacket);

			//Receive file
			inBuf = new byte[100000000];
			inPacket = new DatagramPacket(inBuf, inBuf.length);
			socket.receive(inPacket);

			data = new String(inPacket.getData(), 0, inPacket.getLength());
			if(data.endsWith("ERROR"))
			{
				System.out.println("File doesn't exists.\n");
				socket.close();
			}

			else
			{
				try
				{
					BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
					pw.write(data);
					//Force write buffer to Fİle
					pw.close();

					System.out.println("Archivo recibido. Closing Socket...");
					int buffer = socket.getSoTimeout();
					socket.close();					
					System.out.println("Socket close.");
					System.out.println(buffer);
					

				} catch (Exception ioe) {
					System.out.println("File Error\n");
					socket.close();
				}
			}

		} catch (Exception e) {
			System.out.println("\nNetwork Error. Try Again Later. \n");
		}

	}
}