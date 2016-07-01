package test.gpabon.gcmpoc;

import org.objectweb.proactive.extensions.annotation.ActiveObject;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.InitActive;
import org.objectweb.proactive.RunActive;
import org.objectweb.proactive.EndActive;
import org.objectweb.proactive.Service;
import org.objectweb.proactive.ActiveObjectCreationException;
import org.objectweb.proactive.core.node.NodeException;
import org.objectweb.proactive.api.PAActiveObject;
import org.objectweb.proactive.core.body.AttachedCallback;
import org.objectweb.proactive.api.PALifeCycle;
import org.objectweb.proactive.core.body.request.Request;


@ActiveObject
public class Server implements InitActive, RunActive, EndActive {
    
    private Server next;
    private String id;      // Unique ID of "this" ActiveObject
    private boolean isLast; // identificator of last server in the ring
    private NewRequestEventListener listener;
    
    // empty constructor is required by Proactive
    public Server() {}

    // initialization of the id field
    public void initActivity(Body body) {
        id = body.getID().shortString();
        System.out.println((System.currentTimeMillis() % 1000000) + ": ActiveObject created " + id);
    }

    // initialization of the next server
    public int setNextServer(Server next, boolean isLast) {
        this.next = next;
        this.isLast = isLast;
        listener.setNext(this.next);
        return 0;
    }
    
    public void runActivity(Body body) {
        Service service = new Service(body);
        listener = new NewRequestEventListener(body, id);
        body.attach(listener);
        while (body.isActive()) {
            Request request = service.blockingRemoveOldest();
	        if (!request.ignoreIt()) {
                body.serve(request);
	        }
	        if (request.hasBeenForwarded()) {
	            request.setIgnoreIt(true);
	        }
        }
    }
    
    public void endActivity(Body body) {
        System.out.println((System.currentTimeMillis() % 1000000) + ": "+ id + " terminated");
        PAActiveObject.terminateActiveObject(next,false);
        if (isLast) {
            PALifeCycle.exitSuccess();
        }
    }
    // result calculation
    public void calculateResult(String input) {
        String result = input + " not served";
        try {
            Thread.sleep(4000);
            result = input + " served by " + id;
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println((System.currentTimeMillis() % 1000000) + ": " +result);
    }
    
}