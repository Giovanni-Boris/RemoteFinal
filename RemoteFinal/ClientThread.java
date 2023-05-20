package RemoteFinal;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.IOException;

public class ClientThread implements Runnable {
  private Socket conexion;
  private EnlaceRegistros registry;

  public ClientThread(Socket conexion, EnlaceRegistros service) {
    this.conexion = conexion;
    this.registry = service;
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

  public void run() {
    try {
      System.out.println("Nueva conexion");

      ObjectOutputStream out = new ObjectOutputStream(conexion.getOutputStream());
      ObjectInputStream in = new ObjectInputStream(conexion.getInputStream());

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
      conexion.close();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
