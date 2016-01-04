package com.seb.math.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class MathServer
{
	private int port = 10_005;
	private ServerSocket theServerSocket;
	private Socket clientsSocket;
	private SocketChannel fileSocketChannel;
	private Runnable theFileServer;

	public static void main(String[] args)
	{
		final MathServer theServer = new MathServer(null);
	}

	/***
	 * Starts a math server on given port if no port given, is will start on port:
	 * 10_005
	 * 
	 * @param argumentPort
	 */
	public MathServer(String argumentPort)
	{
		try
		{
			// try to set port
			port = Integer.parseInt(argumentPort);
			System.out.println("listening on port:" + port);

		}
		catch (final NumberFormatException e)
		{
			// if couldnt set port set to default port
			port = 10_005;
			System.out.println("listening on port:" + port);
		}

		try
		{
			// start sever
			setupServer();

			// init listning for clients
			multiClientConnectionHandleing();
		}
		catch (final IOException e)
		{
			System.err.println("Fel po server setup");
			e.printStackTrace();
		}

	}

	/***
	 * Handles multi clients, creates a thread for each
	 */
	private void multiClientConnectionHandleing()
	{
		new Thread(theFileServer).start();
		System.out.println("listning for clients");
		while (true)
		{
			try
			{

				clientsSocket = theServerSocket.accept();
				System.out.println("Client connected: " + clientsSocket.getInetAddress().getHostAddress());
				// when client has connected a new mathclienthandler is created for
				// handling communication with the client.
				new MathClientHandler(clientsSocket).start();

			}
			catch (final IOException e)
			{
				System.err.println("Fel po client kopplingen");
				e.printStackTrace();
			}

		}
	}

	/***
	 * Sets up the math server and the math file server for communication
	 * 
	 * @throws IOException
	 */
	private void setupServer() throws IOException
	{
		// connect port with server
		theServerSocket = new ServerSocket(port);
		System.out.println("Started Server");
		theFileServer = new MathFileServer(port);
		System.out.println("Started FileServer");
		System.out.println("lisning on port: " + ((MathFileServer) theFileServer).getLocalAddress());

	}

}
