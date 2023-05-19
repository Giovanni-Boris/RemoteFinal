package RemoteFinal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class EnlaceRegistros {
  private String host;
  private int port;

  public EnlaceRegistros(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public void bind(String name, Object obj) {
    try {
      Socket socket = new Socket(host, port);
      System.out.println("Connectado al servidor DNS... para registrar mi servicio " + name);

      ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
      ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

      out.writeObject("bind");
      out.writeObject(name);
      out.writeObject(obj);

      out.close();
      in.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Object lookup(String name) {
    try {
      Socket socket = new Socket(host, port);
      System.out.println("Connectado al servidor DNS... para buscar mi servicio " + name);

      ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
      ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
      out.writeObject("lookup");
      out.writeObject(name);

      Object obj = in.readObject();

      out.close();
      in.close();
      socket.close();

      return obj;
    }

    catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    return null;
  }
}