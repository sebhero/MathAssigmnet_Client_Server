package com.seb.math.server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.seb.math.shared.Communicator;
import com.seb.math.shared.MathAssigment;

/***
 * A Server for handling file transfer with math assigments.
 * 
 * @author seb
 * 
 */
public class MathFileServer implements Runnable
{

	private final int port;
	private final ServerSocketChannel theServerSocket;
	private SocketChannel socketChannel;
	private final File theFile;
	private Communicator theCom;
	private static final int BSIZE = 1024;

	public MathFileServer(int port) throws IOException
	{
		this.port = port;
		theServerSocket = ServerSocketChannel.open();
		theServerSocket.socket().bind(new InetSocketAddress(port + 10));
		theFile = new File(getClass().getClassLoader().getResource(".").getPath() + "serverFile.txt");
		// theFile = new
		// File("D:/programming/skola_workspace/05_DT008G_jobo1000/server/test3.txt");
	}

	/***
	 * Main thread for running
	 */
	@Override
	public void run()
	{
		System.out.println("file server listening");
		// final FileChannel fcOut = null;
		while (true)
		{
			try
			{
				// maybe set before loop
				socketChannel = theServerSocket.accept();
				theCom = new Communicator(socketChannel);
				theCom.setOutFile(theFile);
				theCom.setInFile(theFile);

				// listen for client sending file
				theCom.listenForFile();
				// update/correct the recived file
				updateFile(theFile);

				// return the corrected file
				theCom.sendFile();
				System.out.println("Corrected file return to client");
			}
			catch (final IOException | InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/***
	 * Updates the recived file. correct all the assigments. loads the recived
	 * file into a list and then corrects it and updates the file.
	 * 
	 * @param theFile
	 *          the file of assigments
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void updateFile(File theFile) throws IOException, InterruptedException
	{
		// read in the file. to a list
		final List<String> mylist = Files.readAllLines(theFile.toPath(), StandardCharsets.ISO_8859_1);
		System.out.println("processing file");

		// final StringTokenizer tokenizer = new StringTokenizer(null,);
		boolean isCorrect = false;
		for (int i = 0; i < mylist.size(); i++)
		{
			final String assigmentText = mylist.get(i);
			// creates the assigment object
			final MathAssigment assigmentAnsweared = MathAssigmentHandler.create(assigmentText);
			isCorrect = MathAssigmentHandler.check(assigmentAnsweared);
			if (isCorrect)
			{
				mylist.set(i, assigmentText + " (rätt)");
			}
			else
			{
				mylist.set(i, assigmentText + " (fel)");
			}
			Thread.sleep(500);

		}

		Files.write(theFile.toPath(), mylist, StandardCharsets.ISO_8859_1, StandardOpenOption.TRUNCATE_EXISTING);
		System.out.println("done updating file");

	}

	/***
	 * Get the address where the file server is on.
	 * 
	 * @return returns the files servers address with port
	 * @throws IOException
	 */
	public String getLocalAddress() throws IOException
	{
		return theServerSocket.getLocalAddress().toString();
	}

}
