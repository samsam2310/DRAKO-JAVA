package DRAKO.Listener;

public interface GameListener{
	void set_hand_id(int x);
	void showMenu(boolean x);
	void addStack(int x);
	void finish();
	void ask_rival_defense(int id,int x);
	void ask_self_defense();
	int set_round(int x);
	void wait_for_show();
	void clean();
	void stopAll();
}