// Message containing a count of the number of files
public class FileCount {

	// The file count
	private final int count;
	
	// Create the message with the given count
	public FileCount( int count ) {
		this.count = count;
	}
	
	// Get the count
	public int getCount() {
		return count;
	}
}