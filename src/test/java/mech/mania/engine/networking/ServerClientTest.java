package mech.mania.engine.networking;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ServerClientTest {
  private void runAllCallables(List<Callable<String>> callables) {
    ExecutorService executor = Executors.newFixedThreadPool(callables.size());
    try {
      executor.invokeAll(callables);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    executor.shutdown();
  }

  @Disabled("Old version")
  @Test
  @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
  public void testLifeCycle() {
    Callable<String> server =
        () -> {
          Server turnServer = new Server();
          while (!turnServer.isOpen()) turnServer.open();
          turnServer.close();
          assertFalse(turnServer.isOpen());
          return "Task's execution";
        };

    Callable<String> client =
        () -> {
          Client testClient = new Client(27);
          while (!testClient.isConnected()) testClient.connect();
          testClient.disconnect();
          assertFalse(testClient.isConnected());
          return "Task's execution";
        };

    runAllCallables(Arrays.asList(server, client));
  }

  @Disabled("Old version")
  @Test
  @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
  public void testWriteString() {
    Callable<String> server =
        () -> {
          Server turnServer = new Server();
          while (!turnServer.isOpen()) turnServer.open();
          turnServer.writeAll("Hello.");
          turnServer.close();
          return "Task's execution";
        };

    Callable<String> client =
        () -> {
          Client testClient = new Client(27);
          while (!testClient.isConnected()) testClient.connect();
          assertEquals(testClient.read(), "Hello.");
          testClient.disconnect();
          return "Task's execution";
        };

    runAllCallables(Arrays.asList(server, client));
  }
}
