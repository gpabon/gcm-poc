package test.gpabon.gcmpoc;

import org.objectweb.proactive.extensions.annotation.ActiveObject;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.InitActive;
import org.objectweb.proactive.EndActive;
import org.objectweb.proactive.api.PAActiveObject;


@ActiveObject
public class ServerProvider implements InitActive, EndActive {
    
    private ServerController controller;    // Next Server in the ring 
    private String id;                      // Unique ID of "this" ActiveObject

    // empty constructor is required by Proactive
    public ServerProvider() {}

    // initialization of the id field
    public void initActivity(Body body) {
        id = body.getID().shortString();
        System.out.println((System.currentTimeMillis() % 1000000) + 
            ": ActiveObject created " + id);
    }

    // initialization of the next server
    public void serverInitialization(ServerController controller) {
        this.controller = controller;
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
        System.out.println((System.currentTimeMillis() % 1000000) + 
            ": " + result);
        controller.resultCalculated();
    }
    
    public void endActivity(Body body) {
        PAActiveObject.terminateActiveObject(controller,false);
    }
}