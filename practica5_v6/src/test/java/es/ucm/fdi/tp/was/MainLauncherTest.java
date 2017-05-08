package es.ucm.fdi.tp.was;

import org.junit.Test;

import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.mainpr4.MainPr4;

public class MainLauncherTest {

	@Test
	public void testJuegoErroneo() {
		try{
			MainPr4.createInitialState("UnDosTresRespondaOtraVez");
			
		}catch(GameError e){
			System.out.println(e);
		}
	}
}
