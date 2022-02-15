package mech.mania.engine.networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mech.mania.engine.GameTurn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestClient {
  private final int portNumber;

  public TestClient(int portNumber) {
    this.portNumber = portNumber;
  }

  public void read() {
    try (
        Socket kkSocket = new Socket("localhost", portNumber);
        PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(kkSocket.getInputStream()))
    ) {
      String fromServer;
      while ((fromServer = in.readLine()) != null) {
        System.out.println("Server: " + fromServer);
        if (fromServer.equals("Bye."))
          break;

        String fromUser = "copy.";
        System.out.println("Client: " + fromUser);
        out.println(fromUser);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
