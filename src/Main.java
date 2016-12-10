import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Anastasia on 09.10.16.
 */
public class Main {

    private static final int PORT = 40404;
    private static final int TIME = 1000;
    private static final int TIMEOUT = 5000;


    private static void checkTimes(Map<InetAddress, Long> map){

        Iterator<Map.Entry<InetAddress, Long>> it = map.entrySet().iterator();
        long time = System.currentTimeMillis();
        while (it.hasNext()){
            Map.Entry<InetAddress, Long> entry = it.next();

            if (time - entry.getValue() > TIMEOUT){
                System.out.println(entry.getKey() + " упал");
                it.remove();
            }
        }
    }

    public static void main(String[] args) throws IOException {

        DatagramSocket datagramSocket = new DatagramSocket(PORT);
        DatagramPacket datagramPacket = new DatagramPacket(new byte[0], 0, InetAddress.getByName("255.255.255.255"), PORT);
        DatagramPacket datagramPacket1 = new DatagramPacket(new byte[1], 1);
        Map<InetAddress, Long> map = new HashMap<>();
        long last = 0;

        datagramSocket.setSoTimeout(TIME);

        for(;;){

            if(System.currentTimeMillis() - last > TIME) {
                datagramSocket.send(datagramPacket);
                last = System.currentTimeMillis();
                checkTimes(map);
            }

            try {

                datagramSocket.receive(datagramPacket1);

                if(!map.containsKey(datagramPacket1.getAddress())){
                    System.out.println(datagramPacket1.getAddress() + " поднялся");
                }

                map.put(datagramPacket1.getAddress(), System.currentTimeMillis());

            }
            catch (SocketTimeoutException e){

            }

        }

    }

}
