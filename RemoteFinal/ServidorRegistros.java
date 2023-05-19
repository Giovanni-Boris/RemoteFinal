package RemoteFinal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServidorRegistros {
  private Map<String, Object> registry;

  public ServidorRegistros() {
    registry = new HashMap<>();
  }

  public void bind(String name, Object obj) {
    System.out.println("Servicio: " + name + " registrado");
    registry.put(name, obj);
  }

  public Object lookup(String name) {
    return registry.get(name);
  }

  public void start(int port) {
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      System.out.println("Servidor DNS en el servidor de puerto " + port);

      while (true) {
        Socket clientSocket = serverSocket.accept();
        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        String methodName = (String) in.readObject();
        String objName = (String) in.readObject();
        if (methodName.equals("lookup")) {
          Object obj = lookup(objName);
          out.writeObject(obj);
        } else if (methodName.equals("bind")) {
          Object obj = in.readObject();
          bind(objName, obj);
        }

        out.close();
        in.close();
        clientSocket.close();
      }
    }

    catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    ServidorRegistros registryServer = new ServidorRegistros();
    registryServer.start(8888);
  }
}