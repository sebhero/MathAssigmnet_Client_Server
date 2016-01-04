package com.seb.math.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.seb.math.shared.MathAssigment;

public class MathClientHandler extends Thread
{
	// communication Socket
	Socket theSocket;

	// client IP
	String address;

	/***
	 * Client handler on Server. Creates a client handler, takes the socket the
	 * client is connected from and pulls out the address client is connected from
	 * 
	 * @param clientsSocket
	 *          the clients socket
	 */
	public MathClientHandler(Socket clientsSocket)
	{
		this.theSocket = clientsSocket;
		address = theSocket.getInetAddress().getHostAddress();
	}

	/***
	 * Run method is called the the thread is created. Here I handle a the
	 * intreaction from the client.
	 */
	@Override
	public void run()
	{
		// Im going to stream objects between client and server
		// these are the 2 streams i need then.
		ObjectOutputStream out = null;
		ObjectInputStream in = null;

		/*
		 * Connect the socket with the output stream. So thta i can send data to
		 * client.
		 */
		try
		{
			out = new ObjectOutputStream(theSocket.getOutputStream());
			in = new ObjectInputStream(theSocket.getInputStream());

		}
		catch (final IOException e)
		{
			e.printStackTrace();
			return; // should close the streams
		}

		MathAssigment assigmentAnsweard = null;
		boolean isNotExitRequested = true;
		boolean isCorrect = false;

		while (isNotExitRequested)
		{
			try
			{
				// client disconnected
				if (!theSocket.isConnected())
				{
					break;
				}
				// send a assigment to client
				sendAssigment(out);
				assigmentAnsweard = readInput(in);
				isCorrect = MathAssigmentHandler.check(assigmentAnsweard);
				// send return msg boolean
				sendAnswear(isCorrect, out);

			}
			catch (ClassNotFoundException | IOException e)
			{
				isNotExitRequested = false;
			}
		}

		/***
		 * Handle if clients exits
		 */
		try
		{
			handleExit(in, out);
		}
		catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/***
	 * Handle disconnect from client. closes all streams
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	private void handleExit(ObjectInputStream in, ObjectOutputStream out) throws IOException
	{
		in.close();
		out.close();
		theSocket.close();
		System.out.println(address + ": disconnected");

	}

	/***
	 * Send answear object to client
	 * 
	 * @param isCorrect
	 * @param out
	 * @throws IOException
	 */
	private void sendAnswear(boolean isCorrect, ObjectOutputStream out) throws IOException
	{
		out.writeObject(isCorrect);

	}

	/***
	 * Send a assigmnet to client
	 * 
	 * @param out
	 *          output stream to client
	 * @throws IOException
	 */
	private void sendAssigment(ObjectOutputStream out) throws IOException
	{
		out.writeObject(new MathAssigment());
		out.flush();
	}

	/***
	 * Reads the input sent from client and creats a mathassigment from it.
	 * 
	 * @param in
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private MathAssigment readInput(ObjectInputStream in) throws ClassNotFoundException, IOException
	{
		final Object input = in.readObject();

		if (input instanceof MathAssigment)
		{
			return (MathAssigment) input;
		}
		else
		{
			throw new ClassNotFoundException("this is not a MathAssigment");
		}
	}

}
