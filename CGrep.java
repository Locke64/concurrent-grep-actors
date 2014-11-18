import static akka.actor.Actors.*;
import akka.actor.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

public class CGrep {
	public static void main( String[] args ) {
		
		if( args.length < 1 )
			System.out.println( "Usage: java CGrep pattern [file...]" );
		else if( args.length == 1 )
			find(Pattern.compile(args[0]));
		else {
			Collection<String> files = new ArrayList<String>();
			for( int a = 1; a < args.length; a++ )
				files.add(args[a]);
			find(Pattern.compile(args[0]), files);
		}
	}
	
	// find the given pattern in the given input by creating an executing a callable Finder
	public static void find( final Pattern pattern ) {
		ActorRef collection = actorOf( CollectionActor.class );
		collection.start();
		collection.tell( new FileCount( 1 ) );
		ActorRef scanner = actorOf( new UntypedActorFactory() {
				public UntypedActor create() {
					return new ScanActor( pattern );
				}
			} );
		scanner.start();
		scanner.tell( new Configure( null, collection ) );
		scanner.tell( poisonPill() );
	}

	// find the given pattern in the given files by creating and executing a callable Finder for each file
	public static void find( final Pattern pattern, Collection<String> files ) {
		ActorRef collection = actorOf( CollectionActor.class );
		collection.start();
		collection.tell( new FileCount( files.size() ) );
		for( String f : files ) {
		ActorRef scanner = actorOf( new UntypedActorFactory() {
				public UntypedActor create() {
					return new ScanActor( pattern );
				}
			} );
			scanner.start();
			scanner.tell( new Configure( f, collection ) );
			scanner.tell( poisonPill() );
		}
	}
}
