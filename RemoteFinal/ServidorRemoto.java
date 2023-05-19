package RemoteFinal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

public class ServidorRemoto {

  private EnlaceRegistros registry;
  private static final String USUARIO_CUENTA = "Cliente1";
  private static Banco cuentas = new Banco();
  private static List<ClientInterface> clients = new ArrayList<ClientInterface>();;

  public ServidorRemoto(EnlaceRegistros registry) {
    this.registry = registry;
  }

  public void start(int port) {
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      System.out.println("Interfaz server started and listening on port " + port);

      while (true) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connectado: ");

        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

        String methodName = (String) in.readObject();
        String objName = (String) in.readObject();

        Interfaz remoteObject = (Interfaz) registry.lookup(objName);

        if (remoteObject != null) {
          // Invoca el método remoto en el objeto remoto
          Object[] arguments = (Object[]) in.readObject();
          Object result = invocarMethodo(remoteObject, methodName, arguments);

          out.writeObject(result);
        } else {
          out.writeObject(null);
        }

        out.close();
        in.close();
        clientSocket.close();
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private Object invocarMethodo(Interfaz banco, String methodName, Object[] arguments) {
    System.out.println("Method invocado " + methodName);
    Thread atencion = new Thread(new Runnable() {
      public void run() {
        if (methodName.equals("registrar")) {
          banco.registerClient((Cliente) arguments[0]);
        } else {
          int monto = Integer.parseInt((String) arguments[0]);
          System.out.println("El monto es " + monto);
          if (methodName.equals("depositar"))
            banco.depositar(monto);
          else
            banco.retirar(monto);
        }
      }
    });
    atencion.start();
    try {
      atencion.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return "Operacion exitosa";
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