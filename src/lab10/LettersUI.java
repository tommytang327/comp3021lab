package lab10;

import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class LettersUI extends Application {

	final static int SCENE_WIDTH = 300;
	final static int SCENE_HEIGHT = 150;
	
	private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private static boolean mutex = true;

	public static void main(String[] args) {
		launch(LettersUI.class, args);
	}

	@Override
	public void start(Stage stage) {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes

		String[] string_thread = { "A", "B", "C" };
		for (String s : string_thread) {
			Text text = new Text(s);
			text.setFont(new Font(50));
			text.setVisible(false);
			hbox.getChildren().addAll(text);
		}
		
		Scene scene = new Scene(hbox, SCENE_WIDTH, SCENE_HEIGHT);
		stage.setScene(scene);
		stage.setTitle("Example");
		stage.show();

		// Since now you have to add 3 more Threads, Can we use a for loop instead ?
		/*Thread t1 = new Thread(new MyTask((Text) hbox.getChildren().get(0), this));
		Thread t2 = new Thread(new MyTask((Text) hbox.getChildren().get(1), this));
		Thread t3 = new Thread(new MyTask((Text) hbox.getChildren().get(2), this));
		t1.setDaemon(true);
		t2.setDaemon(true);
		t3.setDaemon(true);
		t1.start();
		t2.start();
		t3.start();*/
		Thread[] threads = new Thread[string_thread.length];
		
		for (int i = 0; i < string_thread.length; i++) {
            threads[i] = new Thread(new MyTask((Text) hbox.getChildren().get(i), this));
            threads[i].setDaemon(true);
            threads[i].start();
        }
		
	}

	public void showText(Text text, boolean show) {
		// the parameter show tells if the text has to appear o disappear
		/*if (show) {
			text.setVisible(true);
		} else {
			text.setVisible(false);
		}*/
		
		if (show) {
            lock.lock();
            try {
                while(!mutex) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                text.setVisible(true);
            }finally {
                mutex = !mutex;
                lock.unlock();
            }
        }else {
            lock.lock();
            try {
                text.setVisible(false);
                condition.signal();
            }finally {
                mutex = !mutex;
                lock.unlock();
            }
        }
	}
	class MyTask implements Runnable {

		int timeBudgetms = 5 * 60 * 1000;
		LettersUI instance;
		Text text;

		public MyTask(Text text, LettersUI zeroOROne) {
			this.text = text;
			this.instance = zeroOROne;
		}

		@Override
		public void run() {
			boolean show = true;
			long startTime = System.currentTimeMillis();
			while ((startTime + timeBudgetms) > System.currentTimeMillis()) {
				instance.showText(text, show);
				show = !show;
				try {
					Random rn = new Random();
					int range = 3000 - 1000 + 1;
					int randomNum = rn.nextInt(range) + 1000;
					Thread.yield();
					Thread.sleep(randomNum);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

