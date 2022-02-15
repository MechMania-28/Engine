package mech.mania.engine.networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mech.mania.engine.GameTurn;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TurnServerSocket {
  private final int portNumber;
  private final ServerSocket serverSocket;
  private final Socket clientSocket;

  public TurnServerSocket(int portNumber) throws IOException {
    this.portNumber = portNumber;
    this.serverSocket = new ServerSocket(portNumber);
    this.clientSocket = serverSocket.accept();
  }

  public void write(GameTurn turn) {
    try {
      write(new ObjectMapper().writeValueAsString(turn));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public void write(String string) {
    try {
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      out.println(string);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
