package com.section15;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Message message = new Message();
        (new Thread(new Writer(message))).start();
        (new Thread(new Reader(message))).start();
    }
}

class Message {
    // consumer to read the message
    private String message;
    private boolean empty = true;

    public synchronized String read() {
        while (empty){ // loop until there is a message to read
            try {
                wait(); // called to test the condition that is waiting on or sleeping
            } catch (InterruptedException e){

            }
        }
        empty = true;
        notifyAll(); // responding to one thread at a time to give proper response
        return message;
    }

    // producer to write the message
    public synchronized void write(String message){
        while(!empty){
            try {
                wait();
            } catch (InterruptedException e){

            }
        }
        empty= false;
        this.message = message;
        notifyAll();
    }
}

class Writer implements Runnable{
    private Message message;

    public Writer(Message message){
        this.message = message;
    }

    public void run(){
        String messages[] = {
                "Houston Rockets SUCK!",
                "James flop happy Harden is trash",
                "Golden State will win in 6",
                "KD, Klay, and Curry will show up and show out",
                "Draymond need to lay those hands on those MITCHES!!!!"
        };

        // instance of a random class
        Random random = new Random();

        for (int i=0; i<messages.length; i++){
            message.write(messages[i]);
            try {
                Thread.sleep(random.nextInt(2000));
            } catch (InterruptedException e){

            }
        }
        message.write("Finished");
    }
}

class Reader implements Runnable{
    private Message message;

    public Reader(Message message){
        this.message = message;
    }

    public void run(){
        Random random = new Random();
        for (String latestMessage = message.read(); !latestMessage.equals("Finished");
             latestMessage = message.read()){
            System.out.println(latestMessage);
            try {
                Thread.sleep(random.nextInt(2000));
            } catch (InterruptedException e){

            }
        }
    }
}
