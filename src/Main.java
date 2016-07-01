package test.gpabon.gcmpoc;

import org.objectweb.proactive.ActiveObjectCreationException;
import org.objectweb.proactive.core.node.NodeException;
import org.objectweb.proactive.api.PAActiveObject;


public class Main {
    
    private final static int SERVERS = 3;
    private final static int REQUESTS = 8;
    
    public static void main(String[] args) {
        try {
            
            Server[] servers = new Server[SERVERS];
            // creation of Active Objects
            for (int i=0; i<SERVERS; i++) {
                servers[i] = (Server) PAActiveObject.newActive(Server.class.getName(), null);
            }
            // creation of active objects ring
            for (int i=0; i<SERVERS-1; i++) {
                servers[i].setNextServer(servers[i+1],false);
            }
            servers[SERVERS-1].setNextServer(servers[0],true);
            // Send all requests to servers[0]
            for (int j=0; j<REQUESTS; j++) {
                servers[0].calculateResult("Request " + j);
            }
            // enqueue terminate message 
            PAActiveObject.terminateActiveObject(servers[0],false);
        } catch (NodeException nodeExcep) {
            System.err.println(nodeExcep.getMessage());
        } catch (ActiveObjectCreationException aoExcep) {
            System.err.println(aoExcep.getMessage());
        }
    }
}

