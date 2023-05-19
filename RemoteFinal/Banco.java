package RemoteFinal;

import java.util.*;

public class Banco {

  private Hashtable<String, Integer> cuentas;

  public Banco() {
    cuentas = new Hashtable<String, Integer>();
    // Creando 10 cuentas de bancos entre 100 y 200
    for (int i = 1; i < 5; i++) {
      cuentas.put("Cliente" + i, (int) (Math.random() * 100 + 100));
    }
  }

  public String depositar(String nombre, int depositar) {
    System.out.println("Cuenta " + nombre + "  depositando " + depositar);
    int nuevoDeposito = cuentas.get(nombre) + depositar;
    cuentas.put(nombre, nuevoDeposito);
    mostrarCuentas();
    return "Nuevo saldo de " + nombre + " :  " + nuevoDeposito;
  }

  public String sacar(String nombre, int monto) {
    System.out.println("Cuenta " + nombre + "  sacando " + monto);
    int saldo = cuentas.get(nombre) - monto;
    if (saldo < 0)
      return "No se pudo realizar la operacion resultado negativo";
    cuentas.put(nombre, +saldo);
    mostrarCuentas();
    return "Nuevo saldo de " + nombre + " :  " + saldo;
  }

  public String mostrarCuentas() {
    Set<String> setOfKeys = cuentas.keySet();
    Iterator<String> itr = setOfKeys.iterator();
    String resp = "\nNombre\t\t\tCuenta\n";
    while (itr.hasNext()) {
      String key = itr.next();
      resp += (key + "\t\t " + cuentas.get(key)) + "\n";
    }
    return resp;

  }

  /*
   * public static void main(String[] args) {
   * Banco bk = new Banco();
   * bk.mostrarCuentas();
   * }
   */

}
