import akka.actor.ActorRef;

public class Configure {
    private final ActorRef collection_actor;
    private final String file_name;

    public Configure(String name, ActorRef reference) {
        file_name = name;
	collection_actor = reference;
    }

    public ActorRef getReference() {
	return collection_actor;
    }

    public String getFilename() {
	return file_name;
    }
}
