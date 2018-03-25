package Server;

import java.io.Serializable;

public class ClientInformation{
    private int idClient;
    private double average;
    private int numberPacketReceived;
    private int totalObjects;

    public ClientInformation(int idClient, int totalObjects) {
        this.totalObjects = totalObjects;
        this.idClient=idClient;
        average=0;
    }

    public int getNumberPacketReceived() {
        return numberPacketReceived;
    }

    public void setNumberPacketReceived() {
        this.numberPacketReceived = this.numberPacketReceived+1;
    }

    public double setAverage(int lastTime, int firstTime) {
        int time =(lastTime-firstTime)/1000000;
        average = (average+time)/numberPacketReceived;
        return average;
    }

    public double getAverage() {
        return average;
    }

    public int numberLostPackets(){
        return totalObjects-numberPacketReceived;
    }

}
