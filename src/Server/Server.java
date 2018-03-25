package Server;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import Client.Object;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Server {

    private int port;
    private DatagramSocket datagramSocket;
    private String [] clientsList;
    private ClientInformation [] clientsInformation;
    private ArrayList<String> [] infoTemporal;
    private static int ID_GENERATOR = 0;

    public Server(int port) {
        this.port = port;
        this.clientsList = new String[100];
        this.clientsInformation = new ClientInformation[100];
        this.infoTemporal = new ArrayList[100];
        System.out.printf("*************************************"+"\n");
        System.out.println("Corriendo en el puerto: " + port);
        System.out.printf("*************************************" +"\n");
    }

    public void start(){

        try{
            DatagramSocket datagramSocket = new DatagramSocket(port);
            datagramSocket.setSendBufferSize(128000);
            byte[] buffer = new byte[200];
            DatagramPacket datagramReceived = new DatagramPacket(buffer, buffer.length);
            int n=0;

            CompletableFuture.runAsync(()->{
               while(true){
                   try {
                       Thread.sleep(11000);
                       writeRecords();
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
            });
            while(true){
                n++;
                datagramSocket.receive(datagramReceived);
                byte[] data = datagramReceived.getData();
                String message = new String(data, 0, datagramReceived.getLength());
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais);
                Object object = (Object) ois.readObject();
                ZonedDateTime timeObject = ZonedDateTime.now(ZoneId.of("GMT"));
                int idClient = existClient(datagramReceived.getAddress().toString(), datagramReceived.getPort(),object.getTotalSentObjects());
                ClientInformation info = clientsInformation[idClient];
                int time = ZonedDateTime.parse( object.getTimeMark(), DateTimeFormatter.RFC_1123_DATE_TIME).getNano();
                info.setNumberPacketReceived();
                info.setAverage(timeObject.getNano(), time);
                insertRecord(timeObject,object,idClient);

            }

        }catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int existClient (String address, int port, int totalObjects){
        boolean exist =false;
        String id=address+port;
        for (int i = 0; i < clientsList.length&&!exist; i++) {
            if (clientsList[i] != null && clientsList[i].equalsIgnoreCase(id))
                return i;
        }
        if(!exist) {
            ClientInformation info = new ClientInformation(ID_GENERATOR, totalObjects);
            clientsInformation[ID_GENERATOR] = info;
            infoTemporal[ID_GENERATOR] = new ArrayList();
            clientsList[ID_GENERATOR++]=address+port;
        }
        return ID_GENERATOR-1;
    }

    public void insertRecord(ZonedDateTime arrivalTime, Object object, int idClient) throws IOException {
        String fileName= "client"+idClient+".txt";
        int time = ZonedDateTime.parse( object.getTimeMark(), DateTimeFormatter.RFC_1123_DATE_TIME).getNano();
        int timeDifference = (arrivalTime.getNano() - time)/1000000;
        String t = object.getSequenceNumer() + ": "+timeDifference + "ms";
        t += "\r\n";
        infoTemporal[idClient].add(t);
    }

    public void writeRecords() throws IOException {
        for (int i = 0; i < 100; i++) {
            if (infoTemporal[i] == null) return;
            String fileName= "client"+i+".txt";
            FileWriter fileWriter = new FileWriter(fileName, true);
            for (int j = 0; j < infoTemporal[i].size(); j++) {
                fileWriter.write(infoTemporal[i].get(j));
            }
            infoTemporal[i].clear();
            fileWriter.close();
            System.out.printf("¿deseas conocer estadisticas? (y/n)"+"\n");
            Scanner scr = new Scanner(System.in);
            String character = scr.nextLine();
            stadistics(character);
        }

    }

    public void stadistics(String n){

        if(n.equalsIgnoreCase("y")){
            System.out.printf("¿cual cliente deseas conocer?"+"\n");
            Scanner scr2 = new Scanner(System.in);
            String numClient = scr2.nextLine();
            ClientInformation tmp = clientsInformation[Integer.parseInt(numClient)];
            double average = tmp.getAverage();
            int actualObjectsNumber= tmp.getNumberPacketReceived();
            int lostObjects = tmp.numberLostPackets();
            System.out.printf("actuales: " + actualObjectsNumber+"\n");
            System.out.printf("perdidos: " + lostObjects+"\n");
            System.out.printf("promedio: " + average+"\n");
        }
    }

    public static void main(String[] args){
        Server server = new Server(65000);
        server.start();
    }
}
