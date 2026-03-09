package service;

import client.Client;
import travel.Accommodation;
import travel.Transportation;
import travel.Trip;

import java.io.PrintWriter;

//WSP
public class SmartTravelService {

    Client[] clients = new Client[100];
    Trip[] trips = new Trip[200];
    Accommodation[] accommodation = new Accommodation[50];
    Transportation [] transportation = new Transportation[50];

    public Client addClient(Client client) {
        return client;
    }

    public Trip createTrip(Trip trip) {
        return trip;
    }

    public void loadAllData(){
        PrintWriter pw = new PrintWriter(System.out);
    }

    public void saveAllData(){
        PrintWriter pw = new PrintWriter(System.out);
    }

    public void calculateTripTotal(){
        return;
    }



}
