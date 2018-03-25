package Client;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Client {

private int idClient;
private String ipNumber;
private int portNumber;
private int bufferSize;
private int objectsNumber;
private Object object;
private DatagramSocket socket;
private String clientFilePath = "./data/ClientId"+idClient+"/";

    public Client( String ipNumber, int portNumber, int bufferSize, int objectsNumber) {

            this.ipNumber = ipNumber;
            this.portNumber = portNumber;
            this.bufferSize = bufferSize;
            this.objectsNumber = objectsNumber;
            System.out.println("id " + this.idClient);
        System.out.println("ipnumber " + this.ipNumber);
        System.out.println("PORTnumber " + this.portNumber);
        System.out.println("bufferZice " + this.bufferSize);
        System.out.println("objects " + this.objectsNumber);
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getIpNumber() {
        return ipNumber;
    }

    public void setIpNumber(String ipNumber) {
        this.ipNumber = ipNumber;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getObjectsNumber() {
        return objectsNumber;
    }

    public void setObjectsNumber(int objectsNumber) {
        this.objectsNumber = objectsNumber;
    }

    public void start(){
        try {
            socket=new DatagramSocket();
            socket.setSendBufferSize(bufferSize);
            InetAddress ipServer= InetAddress.getByName(ipNumber);

            for (int i = 0; i < objectsNumber ; i++) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(6400);
                ObjectOutputStream oos = new ObjectOutputStream(outputStream);

                object = new Object(i, LocalDateTime.now().atZone(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME) ,objectsNumber);
                oos.writeObject(object);

                byte[] dataByte = outputStream.toByteArray();

                DatagramPacket datagram = new DatagramPacket(dataByte,dataByte.length, ipServer,portNumber);
                socket.send(datagram);
            }
            JOptionPane.showMessageDialog(null,"Objetos enviados","OK",JOptionPane.INFORMATION_MESSAGE);

        }catch (SocketException e){
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
