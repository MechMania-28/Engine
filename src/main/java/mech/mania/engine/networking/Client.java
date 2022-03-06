package mech.mania.engine.networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
  private final int portNumber;
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private boolean connected = false;

  public Client(int portNumber) {
    this.portNumber = portNumber;
  }

  /** Connects to the server at the port number passed into the constructor. */
  public void connect() {
    try {
      this.socket = new Socket("localhost", portNumber);
      this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.out = new PrintWriter(socket.getOutputStream(), true);
      this.connected = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Reads a single line from a server at the port number passed into the constructor.
   *
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

  /**
   * Writes an object as JSON into the server's output stream.
   *
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
   *
   * @param string String to be written.
   */
  public void write(String string) {
    out.println(string);
  }

  public void disconnect() {
    if (!socket.isClosed()) {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }
    }
    this.connected = false;
  }

  public boolean isConnected() {
    return connected;
  }
}
