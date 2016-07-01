package test.gpabon.gcmpoc;

import org.objectweb.proactive.Body;
import org.objectweb.proactive.core.body.AttachedCallback;
import org.objectweb.proactive.core.body.reply.Reply;
import org.objectweb.proactive.core.body.request.Request;
import org.objectweb.proactive.core.mop.StubObject;
import org.objectweb.proactive.core.body.proxy.BodyProxy;


public class NewRequestEventListener extends AttachedCallback {

    private final int THRESHOLD = 2;
    
    private Body body;
    private String id;
    private Server next;
    
    public NewRequestEventListener(Body body, String id) {
        super(body);
        this.body = body;
        this.id = id;
    }
    
    public void setNext(Server next) {
        this.next = next;
    }

	@Override
	public int onReceiveReply(Reply reply) {
		return 0;
	}

	@Override
	public synchronized int onReceiveRequest(Request request) {
	    int queueSize = body.isActive()? body.getRequestQueue().size(): -1;
	    if (request.getMethodCall().getName().equals("calculateResult")) {
        	System.out.print((System.currentTimeMillis() % 1000000) + ": "+ id + 
            	" onReceiveRequest size: " + queueSize +
            	", request parameter: " + request.getParameter(0));
	        if (queueSize >= THRESHOLD) {
            	System.out.print(" FORWARDED\n");
                try {
	                request.send(((BodyProxy)(((StubObject) next).getProxy())).getBody().getRemoteAdapter());
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        } else {
	        	System.out.print("\n");
	        }
	        
	    }
		return 0;
	}

	@Override
	public int onDeliverReply(Reply reply) {
		return 0;
	}

	@Override
	public int onDeliverRequest(Request request) {
		return 0;
	}

	@Override
	public int onSendReplyBefore(Reply reply) {
		return 0;
	}

	@Override
	public int onSendReplyAfter(Reply reply) {
		return 0;
	}

	@Override
	public int onSendRequestBefore(Request request) {
		return 0;
	}

	@Override
	public int onSendRequestAfter(Request request) {
		return 0;
	}

	@Override
	public int onServeRequestBefore(Request request) {
		return 0;
	}

	@Override
	public int onServeRequestAfter(Request request) {
		return 0;
	}

}