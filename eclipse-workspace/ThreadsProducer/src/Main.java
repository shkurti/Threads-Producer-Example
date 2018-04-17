import java.io.IOException;
import java.util.Random;

public class Main {

	public static void main(String[] args) {

		Message message = new Message();
		(new Thread(new Writer(message))).start();
		(new Thread(new Reader(message))).start();
		
	}
}

	class Message {
		private String message;
		private boolean empty = true;

		public synchronized String read() {
			while (empty) {

				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			empty = true;
			notifyAll();
			return message;
		}

		public synchronized void write(String message) {
			while (!empty) {
				
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			empty = false;
			this.message = message;
			notifyAll();
		}
	}

	class Writer implements Runnable {
		private Message message;

		public Writer(Message message) {
			this.message = message;
		}

		@Override
		public void run() {
			String messages[] = { "Hello this is a test for practicing threads",
					"Today is a ugly weather because it is raining",
					"The office is empty not to many people are working",
					"The stock market is very bad and is not doing well" };

			Random random = new Random();

			for (int i = 0; i < messages.length; i++) {
				message.write(messages[i]);
				try {
					Thread.sleep(random.nextInt(2000));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			message.write("Finished");

		}

	}

	class Reader implements Runnable {
		private Message message;

		public Reader(Message message) {
			this.message = message;
		}

		@Override
		public void run() {
			Random random = new Random();

			for (String latestMessage = message.read(); !latestMessage.equals("Finished"); latestMessage = message
					.read()) {
				System.out.println(latestMessage);

				try {
					Thread.sleep(random.nextInt(2000));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}


