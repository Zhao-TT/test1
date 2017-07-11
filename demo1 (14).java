
package tank_zhaotengteng;
import java.awt.*;
import java.io.*;
//import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
//import java.io.File;
//import java.io.IOException;
import java.util.*;

public class demo1 extends JFrame implements ActionListener{
	
	MyPanel4 mp=null;
	MyStartPanel msp=null;
	
	JMenuBar jmb=null;
	JMenu jm1=null;
	JMenuItem jmi1=null;
	JMenuItem jmi2=null;
	JMenuItem jmi3=null;
	JMenuItem jmi4=null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		demo1 mtg=new demo1();
	}
	public demo1()
	{
		//mp=new MyPanel4();
		//Thread t =new Thread(mp);
		//t.start();
		
		//this.add(mp); 
		
		//this.addKeyListener(mp);
		
		jmb=new JMenuBar();
		jm1=new JMenu("游戏(G)");
		jm1.setMnemonic('G');
		jmi1=new JMenuItem("开始新游戏(N)");
		jmi2=new JMenuItem("退出游戏(E)");
		jmi3=new JMenuItem("存盘退出游戏(C)");
		jmi4=new JMenuItem("继续上局游戏(S)");
		jmi1.setMnemonic('N');
		jmi2.setMnemonic('E');
		jmi3.setMnemonic('C');
		jmi4.setMnemonic('S');
		
		jmi1.addActionListener(this);
		jmi1.setActionCommand("newgame");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("exit");
		jmi3.addActionListener(this);
		jmi3.setActionCommand("saveExit");
		jmi4.addActionListener(this);
		jmi4.setActionCommand("conGame");
		
		jm1.add(jmi1);
		jm1.add(jmi2);
		jm1.add(jmi3);
		jm1.add(jmi4);
		jmb.add(jm1);
		
		msp=new MyStartPanel();
		Thread t=new Thread(msp);
		t.start();
		
		this.setJMenuBar(jmb);
		this.add(msp);
		this.setSize(600,500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getActionCommand().equals("newgame"))
		{
			mp=new MyPanel4("newGame");
			Thread t =new Thread(mp);
			t.start();
			
			this.remove(msp);
			
			this.add(mp); 
			
			this.addKeyListener(mp);
			
			this.setVisible(true);
			
		}else if(arg0.getActionCommand().equals("exit"))
		{
			Recorder.keepRecording();
			
			System.exit(0);
		}
		else if(arg0.getActionCommand().equals("saveExit"))
		{
			Recorder rd=new Recorder();
		     rd.setEts(mp.ets);
		     rd.keepRecAndEnemyTank();
			 System.exit(0);
		}else if(arg0.getActionCommand().equals("conGame"))
		{
			mp=new MyPanel4("con");
			
			Thread t =new Thread(mp);
			t.start();
			
			this.remove(msp);
			
			this.add(mp); 
			
			this.addKeyListener(mp);
			
			this.setVisible(true);
		}
	}

}

class MyStartPanel extends JPanel implements Runnable
{
	int times=0;
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		if(times%2==0)
		{g.setColor(Color.yellow );
		Font myFont =new Font("华文新魏",Font.BOLD,30);
		g.setFont(myFont);
		g.drawString("stage: 1", 150, 150);}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			times++;
			this.repaint();
		}
	}
}



class MyPanel4 extends JPanel implements KeyListener,Runnable
{	
	Hero hero=null;
	Vector<EnemyTank>ets=new Vector<EnemyTank>();
	Vector<Bomb>bomb=new Vector<Bomb>();
	
	Vector<Node>nodes=new Vector<Node>();
	
	
	int enSize=6;
	
	Image image1=null;
	Image image2=null;
	Image image3=null;
	
	
	public MyPanel4(String flag)
	{
		Recorder.getRecoring();
		
		hero=new Hero(180,110);
		hero.setDirect(0);
	if(flag.equals("newGame"))	
	{	for(int i=0;i<enSize;i++)
		{
			EnemyTank et=new EnemyTank((i+1)*50,0);
			et.setColor(0);
			et.setDirect(2);
			
			et.setEts(ets);
			  
			Thread t=new Thread(et);
			t.start();
			Shot s=new Shot(et.x+10,et.y+30,2);
			et.ss.add(s);
			Thread t2=new Thread(s);
			t2.start();
			ets.add(et);
		}
	}else
	{
		nodes=new Recorder().getNodesAndEnNums();
		for(int i=0;i<nodes.size();i++)
		{
			Node node=nodes.get(i);
			EnemyTank et=new EnemyTank(node.x,node.y);
			et.setColor(0);
			et.setDirect(node.direct);
			
			et.setEts(ets);
			  
			Thread t=new Thread(et);
			t.start();
			Shot s=new Shot(et.x+10,et.y+30,2);
			et.ss.add(s);
			Thread t2=new Thread(s);
			t2.start();
			ets.add(et);
		}
		
		
	}
//		try {
//			image1=ImageIO.read(new File("bom1.jpg"));
//			image2=ImageIO.read(new File("bom2.jpg"));
//			image3=ImageIO.read(new File("bom3.jpg"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bom1.jpg"));
	    image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bom2.jpg"));		
	    image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bom3.jpg"));
	}
	
	public void showInfo(Graphics g)
	{
		this.drawTank(80, 330, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getEnNum()+"", 110, 350);
		this.drawTank(130, 330, g, 0, 1);
		g.setColor(Color.black);
		g.drawString(Recorder.getMyLife()+"", 160, 350);
		
		g.setColor(Color.black);
		Font f=new Font("宋体",Font.BOLD,20);
		g.setFont(f);
		g.drawString("您的总成绩", 420, 20);
		
		this.drawTank(420, 60, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getAllEnNum()+"", 460, 80);
	}
	
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		g.setColor(Color.CYAN);
		
		this.showInfo(g);
		
		if(hero.isLive)
		{this.drawTank(hero.getX(), hero.getY(), g, this.hero.direct, 1);}
		
		for(int i=0;i<hero.ss.size();i++)
		{
			Shot myShot=hero.ss.get(i);
			if(myShot!=null&&myShot.isLive==true)
			{
				g.draw3DRect(myShot.x, myShot.y, 1, 1, false);
			}
			if(myShot.isLive==false)
			{
				hero.ss.remove(myShot);
			}
		}
		
		for(int i=0;i<bomb.size();i++)
		{
			Bomb b=bomb.get(i);
			
			if(b.life>6)
			{
				g.drawImage(image1, b.x, b.y, 30,30,this);
			}else if(b.life>3)
			{
				g.drawImage(image2, b.x, b.y, 30,30,this);
			}else
			{
				g.drawImage(image3, b.x, b.y, 30,30,this);
			}  
			b.lifeDown();  
			if(b.life==0)
			{
			     bomb.remove(b);
			}
		}
	    
		
		//只能画出一颗子弹
		/*if(hero.s!=null&&hero.s.isLive==true)
		{
			g.draw3DRect(hero.s.x, hero.s.y, 1, 1, false);
		}*/
			
	     for(int i=0;i<ets.size();i++)
		{
	    	 EnemyTank et=ets.get(i);
	    	 if(et.isLive)
	    	 {
			this.drawTank(et.getX(),et.getY(),g,et.getDirect(),0);
			//else{
			//	ets.remove(et);
			//}
			
			for(int j=0;j<et.ss.size();j++)
			{
				Shot enemyShot=et.ss.get(j);
				if(enemyShot.isLive)
				{
					g.draw3DRect(enemyShot.x, enemyShot.y, 1, 1, false);
				}else{
					et.ss.remove(enemyShot);
				}
			}
	    	 }
	    	 
		}
		
	}	
	
	public void hitMe()
	{
		for(int i=0;i<this.ets.size();i++)
		{
			EnemyTank et=ets.get(i);
			
			for(int j=0;j<et.ss.size();j++)
			{
				Shot enemyShot=et.ss.get(j);
				if(hero.isLive)
				{
					this.hitTank(enemyShot, hero);
					//Recorder.reducemyLife();
				}
			}
		}
		
	}
	
	
	
	public void hitEnemyTank()
	{
		for(int i=0;i<hero.ss.size();i++)
		{
			Shot myShot=hero.ss.get(i);
			if(myShot.isLive)
			{
				for(int j=0;j<ets.size();j++)
				{
					EnemyTank et=ets.get(j);
					if(et.isLive)
					{
						this.hitTank(myShot,et);
						//Recorder.reduceEnNum();
						//Recorder.addEnNumRec();
					}
				}
					
			}
		}
		
	}
	
	
	public void hitTank(Shot s,Tank iet)
	{
		switch(iet.direct)
		{
		case 0:
		case 2:
			if(s.x>=iet.x&&s.x<=iet.x+20&&s.y>=iet.y&&s.y<=iet.y+30)
			{
				//击中
				//子弹死亡
				s.isLive=false;
				//敌人坦克死亡
				iet.isLive=false;
				
				if(iet==hero)
				{
					Recorder.reducemyLife();
				} else {
					Recorder.reduceEnNum();
					Recorder.addEnNumRec();
				}
				//Recorder.reduceEnNum();
				//Recorder.reducemyLife();
				//Recorder.addEnNumRec();
				
				Bomb b=new Bomb(iet.x,iet.y);
				bomb.add(b);
				
			}
			break;
		case 1:
		case 3:
			if(s.x>=iet.x&&s.x<=iet.x+30&&s.y>=iet.y&&s.y<=iet.y+20)
			{
				//击中
				//子弹死亡
				s.isLive=false;
				//敌人坦克死亡
				iet.isLive=false;
				if(iet==hero)
				{
					Recorder.reducemyLife();
				}else {
					Recorder.reduceEnNum();
					Recorder.addEnNumRec();
				}
				
				//Recorder.reduceEnNum();
				//Recorder.reducemyLife();
				//Recorder.addEnNumRec();
				
				Bomb b=new Bomb(iet.x,iet.y);
				bomb.add(b);
			}
			break;
		}
	}
	
	
	public void drawTank(int x,int y,Graphics g,int direct,int type)
	{ 
		switch(type)
		{
		case 0: 
			g.setColor(Color.cyan);
			break;
		case 1:
			g.setColor(Color.yellow);
			break;
		}
		switch(direct)
		{
		case 0:
			g.fill3DRect(x, y, 5, 30,false);
			g.fill3DRect(x+15, y, 5, 30,false);
			g.fill3DRect(x+5, y+5, 10, 20,false);
			g.fillOval(x+4,y+9,10,10);
			g.drawLine(x+9, y+15, x+9,y);
			break;
			
		case 1: 
			g.fill3DRect(x, y, 30, 5, false);
			g.fill3DRect(x, y+15, 30, 5, false);
			g.fill3DRect(x+5, y+5, 20, 10, false);
			g.fillOval(x+10, y+5, 10, 10);
			g.drawLine(x+15,y+10, x+30, y+10);
			break;
		
		case 2:
			g.fill3DRect(x, y, 5, 30,false);
			g.fill3DRect(x+15, y, 5, 30,false);
			g.fill3DRect(x+5, y+5, 10, 20, false);
			g.fillOval(x+4,y+9,10,10);
			g.drawLine(x+9, y+15,x+9,y+30);
			break;
				
		case 3:
		    g.fill3DRect(x, y, 30, 5, false);
		    g.fill3DRect(x, y+15, 30, 5, false);
		    g.fill3DRect(x+5, y+5, 20, 10, false);
		    g.fillOval(x+10, y+5, 10, 10);
		    g.drawLine(x+15,y+10, x, y+10);
		    break;
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode()==KeyEvent.VK_W)
		{
			this.hero.setDirect(0);
			this.hero.moveUp();
		}else if(arg0.getKeyCode()==KeyEvent.VK_D)
		{
			this.hero.setDirect(1);
			this.hero.moveRight();
		}else if(arg0.getKeyCode()==KeyEvent.VK_S)
		{
			this.hero.setDirect(2);
			this.hero.moveDown();
		}else if(arg0.getKeyCode()==KeyEvent.VK_A)
		{
			this.hero.setDirect(3);
			this.hero.moveLeft();
		}
		
		if(arg0.getKeyCode()==KeyEvent.VK_J)
		{
			if(this.hero.ss.size()<=4)
			{	
			this.hero.shotEnemy();
			}
		}
		
		this.repaint();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.hitEnemyTank();
			this.hitMe();
			
			this.repaint();
		}
	}
}

class Node
{
	int x;
	int y;
	int direct;
	public Node(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
		
	}
}


class Recorder
{
	private static int enNum=20;
	private static int myLife=3;
	private static int allEnNum=0;
	
	 static Vector<Node> nodes=new Vector<Node>();
	
	private static FileWriter fw=null;
	private static BufferedWriter bw=null;
	private static FileReader fr=null;
	private static BufferedReader br=null;
	
	private  Vector<EnemyTank> ets=new Vector<EnemyTank>();
	
	public Vector<Node> getNodesAndEnNums()
	{
		try {
			fr=new FileReader("d:\\myRecording.txt");
			br=new BufferedReader(fr);
			String n="";
			n=br.readLine();
			allEnNum=Integer.parseInt(n);
			while((n=br.readLine())!=null)
			{
				String []xyz=n.split(" ");
				
					Node node=new Node(Integer.parseInt(xyz[0]),Integer.parseInt(xyz[1]),Integer.parseInt(xyz[2]));
				    nodes.add(node);
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return nodes;
	}
	
	
	public  void keepRecAndEnemyTank()
	{
		try {
			fw=new FileWriter("d:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			
			bw.write(allEnNum+"\r\n");
			
			for(int i=0;i<ets.size();i++)
			{
				 EnemyTank et=ets.get(i);
				 if(et.isLive)
				 {
					 String recode=et.x+" "+et.y+" "+et.direct;
					 bw.write(recode+"\r\n");
					  
				 }
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void getRecoring()
	{
		try {
			fr=new FileReader("d:\\myRecording.txt");
			br=new BufferedReader(fr);
			String n=br.readLine();
			allEnNum=Integer.parseInt(n);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void keepRecording()
	{
		try {
			fw=new FileWriter("d:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			
			bw.write(allEnNum+"\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static int getEnNum() 
	{
		return enNum;
	}
	public static void setEnNum(int enNum)
	{
		Recorder.enNum = enNum;
	}
	public static int getMyLife()
	{
		return myLife;
	}
	public static void setMyLife(int myLife)
	{
		Recorder.myLife = myLife;
	}
	public static void reduceEnNum()
	{
		enNum--;
	}
	public static void reducemyLife()
	{
		myLife--;
	}
	public static void addEnNumRec()
	{
		allEnNum++;
	}
	public static int getAllEnNum() {
		return allEnNum;
	}
	public static void setAllEnNum(int allEnNum) {
		Recorder.allEnNum = allEnNum;
	}

	public  Vector<EnemyTank> getEts() {
		return ets;
	}

	public  void setEts(Vector<EnemyTank> ets) {
		this.ets = ets;
	}
}

class Tank
{
	int x=0;
	int y=0;
	int direct;
	int speed=1;
	int color;
	boolean isLive=true;
	public Tank(int x,int y)
	{
		this.x=x;
		this.y=y;}
	
	public int getX() 
	{
		return x;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public int getY() 
	{
		return y;
	}
	public void setY(int y) 
    {
		this.y = y;
	}

	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) 
	{
		this.direct = direct;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed)
	{
		this.speed = speed;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
}

class EnemyTank extends Tank implements Runnable
{
	//boolean isLive=true;
	int times=0;
	Vector<EnemyTank>ets=new Vector<EnemyTank>();
	
	Vector<Shot>ss=new Vector<Shot>();
	
	public EnemyTank(int x,int y)
	{
		super(x,y);
	}
	//@Override
	public void setEts(Vector<EnemyTank>vv)
	{
		this.ets=vv;
	}
	
	public boolean isTouchOtherEnemy()
	{
	
	   boolean b=false;
	   switch(this.direct)
	   {
	   case 0:
		   for(int i=0;i<ets.size();i++)
		   {
			   EnemyTank et=ets.get(i);
			   if(et!=this)
			   {
				   if(et.direct==0||et.direct==2)
				   {
					   if(this.x>=et.x&&this.x<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
					   {
						   return true;
					   }
					   if(this.x+20>=et.x&&this.x+20<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
					   {
						   return true;
					   }
				   }
				   if(et.direct==3||et.direct==1)
				   {
					   if(this.x>=et.x&&this.x<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
					   {
						   return true;
					   }
					   if(this.x+20>=et.x&&this.x+20<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
					   {
						   return true;
					   }
				   }
			   }
		   }
		   break;
	   case 1:
		   for(int i=0;i<ets.size();i++)
		   {
			   EnemyTank et=ets.get(i);
			   if(et!=this)
			   {
				   if(et.direct==0||et.direct==2)
				   {
					   if(this.x+30>=et.x&&this.x+30<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
					   {
						   return true;
					   }
					   if(this.x+30>=et.x&&this.x+30<=et.x+20&&this.y+20>=et.y&&this.y+20<=et.y+30)
					   {
						   return true;
					   }
				   }
				   if(et.direct==3||et.direct==1)
				   {
					   if(this.x+30>=et.x&&this.x+30<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
					   {
						   return true;
					   }
					   if(this.x+30>=et.x&&this.x+30<=et.x+30&&this.y+20>=et.y&&this.y+20<=et.y+20)
					   {
						   return true;
					   }
				   }
			   }
		   }
		   break;
	   case 2:
		   for(int i=0;i<ets.size();i++)
		   {
			   EnemyTank et=ets.get(i);
			   if(et!=this)
			   {
				   if(et.direct==0||et.direct==2)
				   {
					   if(this.x>=et.x&&this.x<=et.x+20&&this.y+30>=et.y&&this.y+30<=et.y+30)
					   {
						   return true;
					   }
					   if(this.x+20>=et.x&&this.x+20<=et.x+20&&this.y+30>=et.y&&this.y+30<=et.y+30)
					   {
						   return true;
					   }
				   }
				   if(et.direct==3||et.direct==1)
				   {
					   if(this.x>=et.x&&this.x<=et.x+30&&this.y+30>=et.y&&this.y+30<=et.y+20)
					   {
						   return true;
					   }
					   if(this.x+20>=et.x&&this.x+20<=et.x+30&&this.y+30>=et.y&&this.y+30<=et.y+20)
					   {
						   return true;
					   }
				   }
			   }
		   }
		   break;
	   case 3:
		   for(int i=0;i<ets.size();i++)
		   {
			   EnemyTank et=ets.get(i);
			   if(et!=this)
			   {
				   if(et.direct==0||et.direct==2)
				   {
					   if(this.x>=et.x&&this.x<=et.x+20&&this.y+30>=et.y&&this.y+30<=et.y+30)
					   {
						   return true;
					   }
					   if(this.x>=et.x&&this.x+30<=et.x+20&&this.y+20>=et.y&&this.y+20<=et.y+30)
					   {
						   return true;
					   }
				   }
				   if(et.direct==3||et.direct==1)
				   {
					   if(this.x>=et.x&&this.x<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
					   {
						   return true;
					   }
					   if(this.x>=et.x&&this.x<=et.x+30&&this.y+20>=et.y&&this.y+20<=et.y+20)
					   {
						   return true;
					   }
				   }
			   }
		   }
		   break;
		   
	   }
		
		
		return b;
	}
	
	public void run () {
		
       while(true)
       {
    	   switch(this.direct)
    	   {
    	   case 0:
    		   for(int i=0;i<30;i++)
    		   { 
    			   if(y>0&&!this.isTouchOtherEnemy())  	   
    		   { y-=speed;}
    		   try {
    				Thread.sleep(50);
    			} catch (InterruptedException e) {
    		   
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		   }
    		   
    		   break;
    	   case 1:
    		  for(int i=0;i<30;i++)
    		  {
    			  if(x<400&&!this.isTouchOtherEnemy())
    			  { x+=speed;}
    			  try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		  }
    		  break;
    		   
    	   case 2:
    		 for(int i=0;i<30;i++)
    		 {
    			 if(y<300&&!this.isTouchOtherEnemy())
    			 {y+=speed;}
    			 try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		 }
    		 break;
    		   
    	   case 3:
    		  for(int i=0;i<30;i++)
    		  {
    			  if(x>0&&!this.isTouchOtherEnemy())
    			  { x-=speed;}
    			  try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		  }
    		  break; 
    	   }
    	   this.times++;
    	   
    	  if(times%2==0)
			{
				if(isLive)
				{
					if(ss.size()<5)
					{
						Shot s=null;
						switch(direct)
						{
						case 0:
							s=new Shot(x+10,y,0);
							ss.add(s);
							break;
						case 1:
							s=new Shot(x+30,y+10,1);
							ss.add(s);
							break;
						case 2:
							s=new Shot(x+10,y+30,2);
							ss.add(s);
							break;
						case 3:
							s=new Shot(x,y+10,3);
							ss.add(s);
							break;
							
						}
						Thread t=new Thread(s);
						t.start();
					}
				}
			}
    	   
    	   
    	   this.direct=(int)(Math.random()*4);
    	   
    	   if(this.isLive==false)
    	   {
    		   break;
    	   }
    	   
    	   
    	   
       }
      
		
}
}

class Hero extends Tank 
{
	//Shot s=null;
	Vector<Shot>ss=new Vector<Shot>();
	Shot s=null;
	public Hero(int x, int y)
	{
		super(x,y);
	}
	
	public void shotEnemy()
	{
		switch(this.direct)
		{
		case 0:
			s=new Shot(x+10,y,0);
			ss.add(s);
			break;
		case 1:
			s=new Shot(x+30,y+10,1);
			ss.add(s);
			break;
		case 2:
			s=new Shot(x+10,y+30,2);
			ss.add(s);
			break;
		case 3:
			s=new Shot(x,y+10,3);
			ss.add(s);
			break;
					
		}
		Thread t=new Thread(s);//启动子弹线程
		t.start();
	}
	
	
	
	public void moveUp()
	{
		y-=speed; //this.y-=speed;
	}
	
	public void moveRight()
	{
		x+=speed;
	}
	
	public void moveDown()
	{
		y+=speed;
	}
	
	public void moveLeft()
	{
		x-=speed;
	}
	
}

class Bomb
{
	int x,y;
	int life=9;
	boolean isLive=true;
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public void lifeDown()
	{
		if(life>0)
		{
			life--;
		}else
		{
			this.isLive=false;
		}
	}
}


class Shot implements Runnable
{
	int x;
	int y;
	int direct;
	int speed=5;
	boolean isLive=true;
	public Shot(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
	public void run()
	{
		while(true)
		{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			
			switch(direct)
			{
			case 0:
				y-=speed;
				break;
			case 1:
				x+=speed;
				break;
			case 2:
				y+=speed;
				break;
			case 3:
				x-=speed;
				break;//残留子弹何时死亡问题
			}
			//System.out.println("子弹坐标x="+x+"y="+y);
			if(x<0||x>400||y<0||y>300)
			{
				this.isLive=false;
				break;
			}
		}
	}
	
	
}




