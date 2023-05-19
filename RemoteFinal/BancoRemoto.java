/*
 * package RemoteFinal;
 * 
 * public class BancoRemoto implements Interfaz {
 * public BancoRemoto(){
 * 
 * }
 * 
 * @Override
 * public synchronized void depositar(int monto) {
 * cuentas.depositar(USUARIO_CUENTA, monto);
 * sendMessageToAll(cuentas.mostrarCuentas());
 * };
 * 
 * @Override
 * public synchronized void retirar(int monto) {
 * cuentas.sacar(USUARIO_CUENTA, monto);
 * sendMessageToAll(cuentas.mostrarCuentas());
 * };
 * 
 * @Override
 * public void registerClient(ClientInterface client) {
 * clients.add(client);
 * System.out.println("New client registered.");
 * }
 * }
 */