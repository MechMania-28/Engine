package mech.mania.engine.networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {
  private final int portNumber;
  private final List<Socket> clientSockets;
  private ServerSocket serverSocket;
  private boolean open;

  private final int timeoutMilis = 5000;

  public Server(int portNumber, int clientCount) {
    this.portNumber = portNumber;
    this.open = false;
    this.clientSockets = Arrays.asList(new Socket[clientCount]);
  }

  public void terminateClient(int index) {
    try {
      if (!clientSockets.get(index).isClosed())
        clientSockets.get(index).close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** Starts a server at the port number passed into the constructor. */
  public void open() {
    try {
      this.serverSocket = new ServerSocket(portNumber);
      for (int i = 0; i < clientSockets.size(); i++) {
        Socket clientSocket = serverSocket.accept();
        clientSocket.setSoTimeout(timeoutMilis);
        clientSockets.set(i, clientSocket);

      }
      this.open = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Reads a single line from a server at the port number passed into the constructor.
   *
   * @return Line read from server.
   */
  public List<String> readAll() {
    try {
      List<String> reads = new ArrayList<>();
      for (Socket socket : clientSockets) {
        /* Handle possible closed sockets */
        if (socket.isClosed()) {
          reads.add("null");
          continue;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        reads.add(in.readLine());
      }
      System.out.println("received " + reads);
      return reads;
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
  public void writeAll(Object obj) {
    try {
      writeAll(new ObjectMapper().writeValueAsString(obj));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  /**
   * Writes a string into the server's output stream.
   *
   * @param string String to be written.
   */
  public void writeAll(String string) {
    try {
      for (Socket socket : clientSockets) {
        /* Handle possible closed sockets */
        if (socket.isClosed()) {
          continue;
        }
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter out = new PrintWriter(outputStream, true);
        out.println(string);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Writes a string into the server's output stream.
   *
   * @param string String to be written.
   */
  public void write(String string, int i) {
    if (clientSockets.get(i).isClosed()) {
      return;
    }
    try {
      PrintWriter out = new PrintWriter(clientSockets.get(i).getOutputStream(), true);
      out.println(string);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean isOpen() {
    return open;
  }

  public void close() {
    try {
      serverSocket.close();
      this.open = false;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
