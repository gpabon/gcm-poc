package test.gpabon.gcmpoc;

import org.objectweb.proactive.api.PAActiveObject;


public class Main {
    
    private final static int SERVERS = 3;
    private final static int REQUESTS = 8;
    
    public static void main(String[] args) {
        try {
            
            ServerController[] servers = new ServerController[SERVERS];
            // creation of Active Objects
            for (int i=0; i<SERVERS; i++) {
                servers[i] = (ServerController) PAActiveObject.newActive(
                    ServerController.class.getName(), null);
            }
            // creation of active objects ring
            for (int i=0; i<SERVERS-1; i++) {
                servers[i].serverInitialization(servers[i+1],false,servers[i]);
            }
            servers[SERVERS-1].serverInitialization(servers[0],true,servers[SERVERS-1]);
            // Send all requests to servers[0]
            for (int j=0; j<REQUESTS; j++) {
                servers[0].calculateResult("Request " + j);
            }
            // enqueue terminate message 
            servers[0].terminate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

