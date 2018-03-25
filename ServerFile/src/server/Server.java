package server;

import java.io.*;
import java.net.*;

public class Server {
	public static void main(String[] args) 
	{

		DatagramSocket socket = null;
		DatagramPacket inPacket = null;
		DatagramPacket outPacket = null;
		byte[] inBuf, outBuf;
		String msg;
		final int PORT = 50000;

		try
		{
			socket = new DatagramSocket(PORT);
			while(true) 
			{
				System.out.println("\nEscuchando peticiones...\n");

				inBuf = new byte[100000000];
				inPacket = new DatagramPacket(inBuf, inBuf.length);
				socket.receive(inPacket);

				int source_port=inPacket.getPort();
				InetAddress source_address = inPacket.getAddress();
				msg = new String(inPacket.getData(), 0, inPacket.getLength());
				System.out.println("CLient: " + source_address + ":" + source_port);

				String dirname  = "C:\\Users\\Novoa Avellaneda\\Desktop\\Data";
				File f1 = new File(dirname);
				File fl[] = f1.listFiles();

				StringBuilder sb = new StringBuilder("\n");
				int c=0;

				for(int i = 0;i<fl.length;i++)
				{
					if(fl[i].canRead())
						c++;
				}

				sb.append(c+ " archivos encontrados.\n\n");

				for(int i=0; i<fl.length;i++)
					sb.append(fl[i].getName()+ " " + fl[i].length()+ " Bytes\n");

				sb.append("\nIngrese el nombre del archivo junto a su formato (Ej: archivo.txt): ");
				outBuf = (sb.toString()).getBytes();
				outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address, source_port);
				socket.send(outPacket);

				inBuf = new byte[100000000];
				inPacket = new DatagramPacket(inBuf, inBuf.length);
				socket.receive(inPacket);
				String filename = new String(inPacket.getData(), 0, inPacket.getLength());

				System.out.println("Archivo solicitado: " + filename);

				boolean flis = false;
				int index =-1;
				sb = new StringBuilder("");
				for(int i=0;i<fl.length;i++) 
				{
					if(((fl[i].getName()).toString()).equalsIgnoreCase(filename))
					{
						index = i;
						flis = true;

					}
				}

				if(!flis) 
				{
					System.out.println("ERROR");
					sb.append("ERROR");
					outBuf = (sb.toString()).getBytes();
					outPacket = new DatagramPacket(outBuf, 0, outBuf.length, source_address, source_port);
					socket.send(outPacket);
				}
				else
				{
					try
					{
						//File send
						File ff=new File(fl[index].getAbsolutePath());
						FileReader fr = new FileReader(ff);
						BufferedReader brf = new BufferedReader(fr);
						String s = null;
						sb=new StringBuilder();

						while((s=brf.readLine())!=null)
						{
							sb.append(s);
						}

						if(brf.readLine()==null)
							System.out.println("Entrega exitosa, CLosing Socket");

						outBuf=new byte[58000000];
						outBuf = (sb.toString()).getBytes();
						outPacket = new DatagramPacket(outBuf, 0, outBuf.length,source_address, source_port);
						socket.send(outPacket);

					} catch (Exception ioe) {
						System.out.println(ioe);
						System.out.println("No es posible enviar archivos de más de 65000 bytes");
					}

				}
			}


		} catch (Exception e){
			System.out.println("Error\n");
		}

	}
}