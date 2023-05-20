package RemoteFinal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

public class ServidorRemoto {

  private EnlaceRegistros registry;
  private static final String USUARIO_CUENTA = "Cliente1";
  private static Banco cuentas = new Banco();
  private static List<ClientInterface> clients = new ArrayList<ClientInterface>();

  public ServidorRemoto(EnlaceRegistros registry) {
    this.registry = registry;
  }

  public void start(int port) {
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      System.out.println("Interfaz server started and listening on port " + port);

      while (true) {
        Socket clientSocket = serverSocket.accept();
        Runnable connectionHandler = new ClientThread(clientSocket, registry);
        System.out.println("Server connected to the client");
        new Thread(connectionHandler).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void sendMessageToAll(String message) {
    System.out.println("Sending message to all clients: ");
    for (ClientInterface client : clients) {
      try {
        client.receiveMessage(message);
      } catch (Exception e) {
        // Handle communication error with client if necessary
      }
    }
  }

  public static void main(String[] args) {
    // Server register
    EnlaceRegistros registryClient = new EnlaceRegistros("localhost", 8888);
    registryClient.bind("Banco", new Interfaz() {
      @Override
      public synchronized void depositar(int monto) {
        cuentas.depositar(USUARIO_CUENTA, monto);
        sendMessageToAll(cuentas.mostrarCuentas());
      };

      @Override
      public synchronized void retirar(int monto) {
        cuentas.sacar(USUARIO_CUENTA, monto);
        sendMessageToAll(cuentas.mostrarCuentas());
      };

      @Override
      public void registerClient(ClientInterface client) {
        clients.add(client);
        System.out.println("New client registered.");
      }

    });
    // Configura el objeto remoto
    ServidorRemoto serverRemotos = new ServidorRemoto(registryClient);
    System.out.println(cuentas.mostrarCuentas());
    serverRemotos.start(8700);
  }
}