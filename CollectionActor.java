import static akka.actor.Actors.*;
import akka.actor.UntypedActor;
import java.util.List;

public class CollectionActor extends UntypedActor {

	private int totalFiles = 0;
	private int receivedFiles = 0;

	public void onReceive(Object message) throws Exception {
		if( message instanceof FileCount ) {
			totalFiles = ((FileCount) message).getCount();
		} else if( message instanceof Found ) {
			++receivedFiles;
			Found found = (Found) message;
			String filename = found.getFileName();

			if ( filename != null )
				System.out.println( filename );
			else
				System.out.println( "-" );

			List<String> matches = found.getMatches();
			if( matches.size() == 0 )
				System.out.println("No matches found");
			else
				for( int a = 0; a < matches.size(); a++ )
				System.out.println( matches.get( a ) );
				
			if( receivedFiles == totalFiles ) {
				getContext().stop();
			}
		} else
			unhandled( message );
	}
}