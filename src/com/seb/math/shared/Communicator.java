package com.seb.math.shared;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

/***
 * Handle sending and reciving files Max size for file is 8mb
 * 
 * @author seb
 * 
 */
public class Communicator
{

	private final SocketChannel theSocketChannel;
	private File inFile; // save to
	private File outFile;// send file
	FileChannel theFileChannel;

	public Communicator(SocketChannel theSocketChannel)
	{
		this.theSocketChannel = theSocketChannel;
	}

	public File getInFile()
	{
		return inFile;
	}

	public void setInFile(File inFile)
	{
		this.inFile = inFile;

	}

	public File getOutFile()
	{
		return outFile;
	}

	public void setOutFile(File outFile)
	{
		this.outFile = outFile;
	}

	/***
	 * Send file to partner.
	 * 
	 * @throws IOException
	 */
	public void sendFile() throws IOException
	{

		// get the channel for transfer

		theFileChannel = (FileChannel.open(this.inFile.toPath(), StandardOpenOption.READ));
		// sending file
		System.out.println("Sending file");
		// start the transfer
		final long sent = theFileChannel.transferTo(0, theFileChannel.size(), theSocketChannel);
		System.out.println("Sending Done sent: " + sent);
		// finish with closing up everything.. maybe use try catch finally
		theFileChannel.close();
		// need to be closed due to the 8-loop listning on reciver
		theSocketChannel.shutdownOutput();
		// dont know if needed
		theSocketChannel.open();

	}

	/***
	 * Listen for file sent from sender If target file doesnt exist it Creates it
	 * otherwise update
	 * 
	 * @throws IOException
	 */
	public void listenForFile() throws IOException
	{

		// set channel to output.
		theFileChannel = (FileChannel.open(this.outFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING));
		System.out.println("Lisning for File transfer");
		// listen for file transfer until socketchannel closes. (long.max_value)
		final long recived = theFileChannel.transferFrom(theSocketChannel, 0, Long.MAX_VALUE);
		System.out.println("Recived Done: " + recived);
		// closing channels
		theFileChannel.close();
		// TODO EXTRA
		final List<String> mylist = Files.readAllLines(outFile.toPath(), StandardCharsets.ISO_8859_1);
		for (final String txt : mylist)
		{
			System.out.println(txt);
		}
	}

}
