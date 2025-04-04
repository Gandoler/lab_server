import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
 
// ��������� ��������� Runnable, ������� ��������� �������� � ��������
public class ClientHandler implements Runnable {
  // ��������� ������ �������
  private Server server;
  // ��������� ���������
  private PrintWriter outMessage;
  // �������� ��������
  private Scanner inMessage;
  private static final String HOST = "localhost";
  private static final int PORT = 3443;
  // ���������� �����
  private Socket clientSocket = null;
  // ���������� ������� � ����, ��������� ����
  private static int clients_count = 0;
 
  // �����������, ������� ��������� ���������� ����� � ������
  public ClientHandler(Socket socket, Server server) {
    try {
      clients_count++;
      this.server = server;
      this.clientSocket = socket;
      this.outMessage = new PrintWriter(socket.getOutputStream());
      this.inMessage = new Scanner(socket.getInputStream());
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  // �������������� ����� run(), ������� ���������� �����
  // �� �������� new Thread(client).start();
  @Override
  public void run() {
    try {
      while (true) {
        // ������ ���������� ���������
        server.sendMessageToAllClients("����� �������� ����� � ���!");
        server.sendMessageToAllClients("�������� � ���� = " + clients_count);
        break;
      }
 
      while (true) {
        // ���� �� ������� ������ ���������
        if (inMessage.hasNext()) {
        String clientMessage = inMessage.nextLine();
    // ���� ������ ���������� ������ ���������, �� ���� ����������� � 
    // ������ ������� �� ����
        if (clientMessage.equalsIgnoreCase("##session##end##")) {
          break;
        }
    // ������� � ������� ��������� (��� �����)
        System.out.println(clientMessage);
    // ���������� ������ ��������� ���� ��������
        server.sendMessageToAllClients(clientMessage);
        server.sendMessageToAllClients("�������� � ���� = " + clients_count);
      }
      // ������������� ���������� ������ �� 100 ��
      Thread.sleep(100);
    }
  }
  catch (InterruptedException ex) {
    ex.printStackTrace();
  }
  finally {
    this.close();
  }
}
  // ���������� ���������
  public void sendMsg(String msg) {
    try {
      outMessage.println(msg);
      outMessage.flush();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  // ������ ������� �� ����
  public void close() {
    // ������� ������� �� ������
    server.removeClient(this);
    clients_count--;
    server.sendMessageToAllClients("�������� � ���� = " + clients_count);
  }
}