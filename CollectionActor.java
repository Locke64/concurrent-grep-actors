import akka.actor.UntypedActor;

public class CollectionActor extends UntypedActor {
 
  public void onReceive(Object message) throws Exception {
    if( message instanceof FileCount ) {
	  //TODO
    } else if( message instanceof Found ) {
      System.out.println(String.format("Received String message: %s", message));
      //getSender().tell(message, getSelf());
	  //TODO
    } else
      unhandled(message);
  }
}