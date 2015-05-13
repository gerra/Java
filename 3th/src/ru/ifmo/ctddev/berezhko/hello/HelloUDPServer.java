package ru.ifmo.ctddev.berezhko.hello;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by root on 30.04.15.
 */
public class HelloUDPServer {
    private static final int DATA_SIZE = 1024;



    public static void main(String[] args)       {
        // Check the arguments
        if( args.length != 1 )
        {
            System.out.println( "usage: DatagramServer port" ) ;
            return ;
        }

        try
        {
            // Convert the argument to ensure that is it valid
            int port = Integer.parseInt( args[0] ) ;

            // Construct the socket
            DatagramSocket socket = new DatagramSocket( port ) ;

            System.out.println( "The server is ready..." ) ;


            for( ;; )
            {
                // Create a packet
                DatagramPacket packet = new DatagramPacket( new byte[1024], 1024 ) ;

                // Receive a packet (blocking)
                socket.receive( packet ) ;

                // Print the packet
                System.out.println( packet.getAddress() + " " + packet.getPort() + ": " + new String(packet.getData()) ) ;

                // Return the packet to the sender
                socket.send( packet ) ;
            }
        }
        catch( Exception e )
        {
            System.out.println( e ) ;
        }
    }
}
