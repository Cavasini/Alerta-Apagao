package br.com.alertaApagao;

import javax.xml.ws.Endpoint;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        AlertServiceImplementation alertService = new AlertServiceImplementation();
        Endpoint.publish("http://localhost:8085/AlertService", alertService);
        System.out.println("AlertService ON!");
    }
}
