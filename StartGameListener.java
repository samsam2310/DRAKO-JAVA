package DRAKO.Listener;

import DRAKO.talk.*;

public interface StartGameListener{
	void BuildGame(StreamData _talk,StreamData _chat);
    void end_game();
}