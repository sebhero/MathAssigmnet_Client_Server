package com.seb.math.client;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.StringTokenizer;

import com.seb.math.shared.Communicator;
import com.seb.math.shared.MathAssigment;

/***
 * Mathclient. this is the math client wich the gui uses for communicating with
 * the server.
 * 
 * @author seb
 * 
 */
public class MathClient
{
	private int port = 10_005;
	private String address = "localhost";
	private Socket socket;
	private MathAssigment currentAssigmnet;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	private SocketChannel fileSocketChannel;
	private ServerSocketChannel theServerSocket;
	private static final int BSIZE = 1024;

	public static void main(String[] args)
	{
		final MathClient theClient = new MathClient();
	}

	public MathClient()
	{

	}

	/***
	 * Send a file to client and then listens for the file. closes stream
	 * afterwards
	 * 
	 * @param infile
	 * @return
	 * 
	 * @throws IOException
	 */
	public String sendFile(File infile) throws IOException
	{
		fileSocketChannel = SocketChannel.open(new InetSocketAddress("localhost", port + 10));

		final Communicator theCom = new Communicator(fileSocketChannel);

		System.out.println("CLient started");

		System.out.println("parent:" + infile.getParent());
		System.out.println("name:" + infile.getName());
		final StringTokenizer token = new StringTokenizer(infile.getName(), ".");

		theCom.setInFile(infile);
		theCom.setOutFile(new File(infile.getParent() + "\\" + token.nextToken() + "_answeared.txt"));

		// theCom.initialize();
		theCom.sendFile();

		System.out.println("File Transfered...");
		System.out.println("waiting for results");
		theCom.listenForFile();
		fileSocketChannel.close();
		return theCom.getOutFile().getAbsolutePath();
	}

	/***
	 * Setup client for socket communications
	 */
	private void setupClient()
	{
		try
		{
			// connect client to ther server
			socket = new Socket(address, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		}
		catch (final Exception e)
		{
			System.err.println(e.getMessage());
			// if error exit
			System.exit(0);
		}
	}

	/***
	 * Connect to server
	 * 
	 * @param guiAddress
	 *          address of server
	 * @param guiPort
	 *          port using for communication
	 */
	public void connect(String guiAddress, String guiPort)
	{
		this.port = Integer.parseInt(guiPort);
		this.address = guiAddress;
		setupClient();

	}

	/***
	 * Get the new math assigment
	 * 
	 * @return
	 */
	public MathAssigment getAssigment()
	{
		// receive object from server
		Object input;
		try
		{
			input = in.readObject();
			// if read is on recieving assigmnet
			if (input instanceof MathAssigment)
			{
				// do assigment stuff.
				currentAssigmnet = (MathAssigment) input;
				return currentAssigmnet;
			}
		}
		catch (ClassNotFoundException | IOException e)
		{
			System.err.println("fel vid ta emot assigment");
			e.printStackTrace();
		}
		// on fail
		return null;

	}

	/***
	 * Disconnect from the server
	 */
	public void disconnect()
	{
		System.out.println("shuting down gracefully");
		try
		{
			in.close();
			out.close();
			socket.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}

	}

	/***
	 * Send answear to server for correcting.
	 * 
	 * @param sendAnswear
	 * @return
	 */
	public boolean sendAnswear(String sendAnswear)
	{
		currentAssigmnet.setAnswear(String.valueOf(sendAnswear));
		try
		{
			out.writeObject(currentAssigmnet);
			// receive if its correct
			final Object input = in.readObject();
			if (input instanceof Boolean)
			{
				final boolean corrected = (boolean) input;
				System.out.println("answear is: " + corrected);
				return corrected;
			}
		}
		catch (final IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return false;

	}

}
