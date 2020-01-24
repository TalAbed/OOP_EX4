package gameClient;

import javax.swing.JOptionPane;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;

public class SimpleGameClient {

	public static void main(String[] a)
	{
			//Enter ID
			int id=Integer.parseInt(JOptionPane.showInputDialog("Enter your ID"));
			Game_Server.login(id);
			//Choose level
			Object levels[]=new Object[24];
			for(int i=0;i<levels.length;i++)
				levels[i]=i;
			int level=(Integer)JOptionPane.showInputDialog(null,"Choose a level between 0-23","Level", JOptionPane.QUESTION_MESSAGE,null,levels,null);
			game_service game = Game_Server.getServer(level);
			//Choose game type
			Object type[] = new Object[2];
			type[0]="by mouse";
			type[1]="Automatic";
			String typegame=(String)JOptionPane.showInputDialog(null,"Choose type of game","type of game", JOptionPane.QUESTION_MESSAGE,null,type,null);
			//Choose if save as kml
			Object saveAsKml[] = new Object[2];
			saveAsKml[0]="yes";
			saveAsKml[1]="no";
			String kml=(String)JOptionPane.showInputDialog(null,"would you like to save this game as kml?","save as kml", JOptionPane.QUESTION_MESSAGE,null,saveAsKml,null);
			//create graph and algo graph
			DGraph dg=new DGraph();
			dg.init(game.getGraph());
			MyGameGUI gui=new MyGameGUI(game,level,typegame,dg,kml,id);

		}
}
