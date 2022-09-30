package mech.mania.engine.networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mech.mania.engine.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Server {
  private final List<Socket> clientSockets;
  private final List<ServerSocket> serverSockets;
  private boolean open;
  private final int TIMEOUT_MILIS_INIT = 2 * 1000;
  private final int TIMEOUT_MILIS_TURN = 2 * 1000;

  private static final Logger LOGGER = LogManager.getLogger(Server.class.getName());

  public Server() {


    this.open = false;
    this.clientSockets = Arrays.asList(new Socket[Config.MAX_PLAYERS]);
    this.serverSockets = Arrays.asList(new ServerSocket[Config.MAX_PLAYERS]);

  }



  /** Starts a server at the port number passed into the constructor. */
  public void open() {
    try {
      for (int i = 0; i < clientSockets.size(); i++) {
        ServerSocket serverSocket = new ServerSocket(Config.PORTS[i]);
        serverSocket.setSoTimeout(TIMEOUT_MILIS_INIT);

        Socket clientSocket;
        try {
          clientSocket = serverSocket.accept();
        } catch (SocketTimeoutException e) {
          LOGGER.debug("Accept timeout for client socket" + i);
          continue;
        }
        if (clientSocket != null) {
          clientSocket.setSoTimeout(TIMEOUT_MILIS_TURN);
          clientSockets.set(i, clientSocket);
        }

      }
      this.open = true;
    } catch (IOException e) {
      LOGGER.debug(e);
    }
  }

  public void terminateClient(int index, int turnCount) {
    if (clientSockets.get(index) == null) {
      return;
    }
    try {
      if (!clientSockets.get(index).isClosed()) {
        clientSockets.get(index).close();
        LOGGER.warn(String.format("Client crashed. terminating socket at index %d, @turn %d", index, turnCount));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    clientSockets.set(index, null);
  }

  /**
   * Reads a single line from a server at the port number passed into the constructor.
   * The reason for checking null twice is that the socket closes
   * the local END of a socket AFTER reading from it and getting NULL, which is
   * the only way of detecting an eof or the other end
   * disconnects.
   *
   * @return Line read from server.
   */
  public List<String> readAll() {
      List<String> reads = new ArrayList<>();
      for (Socket socket : clientSockets) {
        /* Handle possible closed sockets. Mock input from socket */
        if (socket == null || socket.isClosed()) {
          reads.add("null");
          continue;
        }

        String readLine = null;

        /* try read from socket. Handle timeout and null input. */
        try {
          BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          readLine = in.readLine();

          LOGGER.debug(readLine);
        } catch (SocketTimeoutException e) {
          readLine = null;
        } catch (IOException e) {
          LOGGER.debug(e);
        }

        reads.add(readLine == null ? "null" : readLine);


      }
      return reads;

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
      LOGGER.debug(e);
    }
  }

  /**
   * Writes a string into the server's output stream.
   *
   * @param string String to be written.
   */
  public void writeAll(String string) {
    LOGGER.debug("Sending " + string);
    try {
      for (Socket socket : clientSockets) {
        if (socket == null || socket.isClosed()) {
          continue;
        }
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter out = new PrintWriter(outputStream, true);
        out.println(string);
      }
    } catch (IOException e) {
      LOGGER.debug(e);
    }
  }

  /**
   * Writes a string into the server's output stream.
   *
   * @param string String to be written.
   */
  public void write(String string, int i) {
    if (clientSockets.get(i) == null || clientSockets.get(i).isClosed()) {
      return;
    }
    try {
      PrintWriter out = new PrintWriter(clientSockets.get(i).getOutputStream(), true);
      out.println(string);
    } catch (IOException e) {
      LOGGER.debug(e);
    }
  }

  public boolean isOpen() {
    return open;
  }

  public void close() {
      for (ServerSocket serverSocket : serverSockets) {
      try {
        serverSocket.close();
        LOGGER.debug("Closing socket");
        } catch (IOException e) {
          LOGGER.debug(e);
        } catch (NullPointerException e) {
          LOGGER.debug("Closing null socket");
        }
      }
    this.open = false;
  }
}
