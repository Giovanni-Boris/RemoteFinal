package RemoteFinal;

import java.io.Serializable;

public interface Interfaz extends Serializable {
  // Define los métodos remotos que se pueden invocar
  void depositar(int monto);

  void retirar(int monto);

  void registerClient(ClientInterface client);

}