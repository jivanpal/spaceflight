//Server Class
package ClientNetworking.GameHost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import GeneralNetworking.Lobby;
import GeneralNetworking.Player;
import Geometry.Vector;
import Physics.Body;
import GameLogic.*;

public class MapServer extends Thread
{
	private final int PORT = 1273;
	private final Lobby lobby;
	private ServerSocket serverSocket = null;
	private Map gameMap = new Map(10000, 10000, 10000);

	public MapServer(Lobby l)
	{
		lobby = l;

		// Open a server socket:
		try
		{
			serverSocket = new ServerSocket(PORT);
		}
		catch (IOException e)
		{
			System.err.println("Couldn't listen on port " + PORT);
		}
	}

	public void run()
	{
		try
		{
			LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();

			while (true)
			{
				// Listen to the socket, accepting connections from new clients:
				Socket socket = serverSocket.accept();
				InetAddress address = socket.getInetAddress();
				boolean flag = false;		
				int pos = 0;
				String name = "";
				Player[] players = lobby.getPlayers();
				for (pos=0;pos<players.length;pos++)
				{
					if (players[pos]!= null && players[pos].address == address)
					{
						name = players[pos].nickname;
						flag = true;
						break;
					}
				}
				if (!flag)
				{
					socket.close();
				}
				else
				{

					// If the player added is the pilot, put a new ship on the
					// map in a sensible position.
					if (pos % 2 == 0)
					{
						Ship ship = (new Ship(name));
						ship.setPosition(new Vector(
								pos % 4 == 0 ? 0 : gameMap.getDimensions().getX() / 2,
								pos < 4 ? 0 : gameMap.getDimensions().getY() / 2,
								0
						));
						gameMap.add(ship);
					}
					Random r = new Random();
					for (int i = 0; i < 100; i++)
					{
						Asteroid a = new Asteroid();
						a.setPosition(new Vector(r.nextDouble()* gameMap.getDimensions().getX(), 
												r.nextDouble()* gameMap.getDimensions().getY(),
												r.nextDouble()* gameMap.getDimensions().getZ()));
						boolean overlaps = false;
						for(Body b : gameMap) {
							if (a.isTouching(b)) {
								overlaps=true;
								break;
							}
						}
						
						if(!overlaps) {
							gameMap.add(a);
						} else {
							i--;
						}
					}

					// This is so that we can use readLine():
					ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());

					// This is to print o the server
					ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());

					// We create and start new threads to read from the
					// client(this one executes the commands):

					GameHostReceiver clientInput = new GameHostReceiver(fromClient, gameMap);
					clientInput.start();

					// We create and start a new thread to write to the client:
					GameHostSender clientOutput = new GameHostSender(toClient, queue);
					clientOutput.start();
				}

			}
		}
		catch (IOException e)
		{
			System.err.println("IO error " + e.getMessage());
		}
	}
}
