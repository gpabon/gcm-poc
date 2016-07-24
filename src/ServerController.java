package test.gpabon.gcmpoc;

import org.objectweb.proactive.extensions.annotation.ActiveObject;
import org.objectweb.proactive.EndActive;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.api.PAActiveObject;
import org.objectweb.proactive.api.PALifeCycle;

import java.io.Serializable;


@ActiveObject
public class ServerController implements EndActive, Serializable {
    
    private static final int MAX_QUEUE_SIZE = 3; 
    private ServerController next;  // Next Server in the ring 
    private boolean isLast;         // identificator of last server in the ring
    private int queueSize;          // Number of active requests 
    private ServerProvider provider;// ActiveObject that calculates result

    // empty constructor is required by Proactive
    public ServerController() {}

    // initialization of the next server
    public void serverInitialization(ServerController next, boolean isLast,
        ServerController thisController) {
        this.next = next;
        this.isLast = isLast;
        queueSize = 0;
        try {
            provider = (ServerProvider) PAActiveObject.newActive(
                ServerProvider.class.getName(), null);
            provider.serverInitialization(thisController);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // result calculation
    public void calculateResult(String input) {
        if (queueSize < MAX_QUEUE_SIZE) {
            provider.calculateResult(input);
            queueSize++;
            return;
        }
        next.calculateResult(input);
    }
    
    public void resultCalculated() {
        queueSize--;
    }
    
    public void terminate() {
        PAActiveObject.terminateActiveObject(provider,false);
    }
    
    public void endActivity(Body body) {
        if (isLast) {
            PALifeCycle.exitSuccess();
            return;
        }
        next.terminate();
    }
}