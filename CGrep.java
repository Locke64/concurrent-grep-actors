import akka.actor.ActorRef;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

public class CGrep {
	public static void main( String[] args ) {
		
		if ( args.length < 2 )
			System.out.println("Please input the correct format.");
		else {
			 if ( args.length == 2 && !args[1].endsWith(".txt") )
				 Finder.find(Pattern.compile(args[0]), args[1]);
			 else {
				 Collection<String> files = new ArrayList<String>();
				 for( int a = 1; a < args.length; a++ )
					 files.add(args[a]);
				 Finder.find(Pattern.compile(args[0]), files);
			 }
		}
	}
	
	// find the given pattern in the given input by creating an executing a callable Finder
	public static void find( Pattern pattern, String input ) {
		ActorRef collection = actorOf( CollectionActor.class );
		collection.start();
		collection.tell( new FileCount( 1 ) );
		ActorRef scanner = actorOf( ScanActor.class );
		scanner.start();
		scanner.tell( new Configure( null, collection ) );
	}

	// find the given pattern in the given files by creating and executing a callable Finder for each file
	public static void find( Pattern pattern, Collection<String> files ) {
		ActorRef collection = actorOf( CollectionActor.class );
		collection.start();
		collection.tell( new FileCount( 1 ) );
		for( String f : files ) {
			ActorRef scanner = actorOf( ScanActor.class );
			scanner.start();
			scanner.tell( new Configure( f, collection ) );
		}
	}
}
