package RemoteFinal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.Serializable;

public class Cliente implements ClientInterface, Serializable, Runnable {
  private String host;
  private int port;
  private String name = "";

  public Cliente(String name, String host, int port) {
    this.host = host;
    this.port = port;
    this.name = name;
  }

  @Override
  public void receiveMessage(String message) {
    System.out.println("Received message: para " + name + " el mensaje del servidor es" + message);
  }

  public Object invokarMethodoRemota(String servicio, String methodName, Object[] arguments) {
    try {
      Socket socket = new Socket(host, port);
      System.out.println("Conectado al servidor remoto " + name);

      ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
      ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
      out.writeObject(methodName);
      out.writeObject(servicio);
      out.writeObject(arguments);

      Object result = in.readObject();

      out.close();
      in.close();
      socket.close();

      return result;
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public void run() {
    Object result = this.invokarMethodoRemota("Banco", "registrar", new Object[] { this });

    System.out.println("Registro: " + (String) result + "  " + name);
    result = this.invokarMethodoRemota("Banco", "depositar", new Object[] { "500" });
    System.out.println("Deposito: " + (String) result + "  " + name);
    result = this.invokarMethodoRemota("Banco", "sacar", new Object[] { "500" });
    System.out.println("Retiro: " + (String) result + "  " + name);
  }

  public static void main(String[] args) {
    new Thread(new Cliente("Mateo", "localhost", 8700)).start();
    new Thread(new Cliente("Lucas", "localhost", 8700)).start();
  }
}