package mech.mania.engine.networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  private final int portNumber;
  private ServerSocket serverSocket;
  private Socket clientSocket;
  private boolean started;

  public Server(int portNumber)  {
    this.portNumber = portNumber;
    this.started = false;
  }

  /**
   * Starts a server at the port number passed into the constructor.
   */
  public void start(){
    try {
      this.serverSocket = new ServerSocket(portNumber);
      this.clientSocket = serverSocket.accept();
      this.started = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Writes an object as JSON into the server's output stream.
   * @param obj Object to be written.
   */
  public void write(Object obj) {
    try {
      write(new ObjectMapper().writeValueAsString(obj));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  /**
   * Writes a string into the server's output stream.
   * @param string String to be written.
   */
  public void write(String string) {
    try {
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      out.println(string);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean isStarted() {
    return started;
  }
}
