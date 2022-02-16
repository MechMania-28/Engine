package mech.mania.engine.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
  private final int portNumber;
  private Socket socket;
  private BufferedReader in;
  private boolean connected = false;

  public Client(int portNumber) {
    this.portNumber = portNumber;
  }

  /**
   * Connects to the server at the port number passed into the constructor.
   */
  public void connect(){
      try {
        this.socket = new Socket("localhost", portNumber);
        this.in =
            new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.connected = true;
      } catch (IOException e) {
        e.printStackTrace();
      }
  }

  /**
   * Reads a single line from a server at the port number passed into the constructor.
   * @return Line read from server.
   */
  public String read() {
    try {
      return in.readLine();
      } catch (IOException e) {
        e.printStackTrace();
      }
    return null;
  }

  public boolean isConnected() {
    return connected;
  }
}
