package Graphics;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class Screen extends JPanel implements KeyListener{
	
	private double sleepTime = 1000/60, lastRefresh = 0;
	public static Point viewFrom;
	public static Point viewTo;
	public static Vector lightDir;
	public static double[][] cameraSystem, worldToCamera, CM;
	public static Vector N, U, V;
	private Vector moveVec;
	
	private double lightPosition, mapSize = 10;
	private static double moveSpeed = 1, verticalLook = 0, horizontalLook = 0;
	private double verticalLookSpeed = 0.00001, horizontalLookSpeed = 0.01;
	private double r;
	
	public static int nPoly = 0, nPoly3D = 0;
//	public static PolygonObj[] drawablePolygons = new PolygonObj[100];
	public static ArrayList<Poly3D> poly3Ds = new ArrayList<Poly3D>();
	int drawOrder[];
	boolean w, a, s, d, e, q;
	
	public Screen(){
		
		viewFrom = new Point(0, 0, 0);
		viewTo = new Point(1, 0, 0);
		lightDir = new Vector(1, 1, 1);
		lightDir.normalise();
		
		N = viewTo.pointMinusPoint(viewFrom);
		N.normalise();
		U = new Vector(0, 1, 0);
		U.normalise();
		V = U.crossProduct(N);
		V.normalise();
		U = N.crossProduct(V);
		U.normalise();
		
		cameraSystem = new double[][] { {U.x,  U.y,  U.z,  0},
										{V.x,  V.y,  V.z,  0},
										{N.x, N.y, N.z, 0},
										{0,    0,    0,    1}};
										
		CM = Matrix.getCM(viewFrom, V, U, N, 100);
		Matrix.printMatrix(CM);
						
		Matrix.printMatrix(cameraSystem);
										
//		worldToCamera = Matrix.inverse(cameraSystem);
//		
//		Matrix.printMatrix(cameraSystem);
		
		Poly3D poly1 = new Poly3D(new double[]{0, 0.3, 0.6}, new double[]{0, 0.4, 0}, new double[]{0, 0, 0}, Color.RED);
		Screen.poly3Ds.add(poly1);
		Poly3D poly2 = new Poly3D(new double[]{0, 0.3, 0.3}, new double[]{0, 0.4, 0.2}, new double[]{0, 0, 0.4}, Color.RED);
		Screen.poly3Ds.add(poly2);
		Poly3D poly3 = new Poly3D(new double[]{0, 0.6, 0.3}, new double[]{0, 0, 0.2}, new double[]{0, 0, 0.4}, Color.RED);
		Screen.poly3Ds.add(poly3);
		Poly3D poly4 = new Poly3D(new double[]{0.3, 0.6, 0.3}, new double[]{0.4, 0, 0.2}, new double[]{0, 0, 0.4}, Color.RED);
		Screen.poly3Ds.add(poly4);
			
		Random r = new Random();
		for(int i = 0; i < 100; i++){
			Asteroid.createAsteroid(r.nextInt((int)mapSize), r.nextInt((int)mapSize), r.nextInt((int)mapSize));
		}
		addKeyListener(this);
		setFocusable(true);
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int)GameEngine.screenSize.getWidth(), (int)GameEngine.screenSize.getHeight());
		
		camera();
		Calculations.setInfo();
		setLight();
		nPoly = poly3Ds.size();
//		poly3Ds.remove(nPoly-1);
//		poly3Ds.remove(nPoly-2);
//		poly3Ds.remove(nPoly-3);
//		poly3Ds.remove(nPoly-4);
//		
//		Asteroid.createAsteroid(viewTo.x, viewTo.y, viewTo.z);
		
		for(int i = 0; i < nPoly; i++){
			poly3Ds.get(i).update();
		}
		
		setDrawOrder();
		
		for(int i = 0; i < nPoly; i++){
//			System.out.println("Drawing Polygon " + i);
			poly3Ds.get(drawOrder[i]).poly.drawPoly(g);
		}
		
		Vector camCoords = Matrix.multiplyVector2(cameraSystem, new Vector(0, 0, 0));
		
		g.setColor(Color.WHITE);
		g.drawString("x: " + viewFrom.x + ", y: " + viewFrom.y + ", z: " + viewFrom.z + " x: " + camCoords.x + ", y: " + camCoords.y + ", z: " + camCoords.z, 40, 40);
		g.drawString("x: " + viewTo.x + ", y: " + viewTo.y + ", z: " + viewTo.z, 40, 60);
		g.drawString("r: " + r + " vert: " + verticalLook, 40, 80);
		g.drawLine((int)GameEngine.screenSize.getWidth()/2 - 5, (int)GameEngine.screenSize.getHeight()/2, (int)GameEngine.screenSize.getWidth()/2 + 5, (int)GameEngine.screenSize.getHeight()/2);
		g.drawLine((int)GameEngine.screenSize.getWidth()/2, (int)GameEngine.screenSize.getHeight()/2 - 5, (int)GameEngine.screenSize.getWidth()/2, (int)GameEngine.screenSize.getHeight()/2 + 5);
		
		sleepAndRefresh();
	}
	
	public void sleepAndRefresh(){
		while(true){
			if(System.currentTimeMillis() - lastRefresh > sleepTime){
				lastRefresh = System.currentTimeMillis();
				repaint();
				break;
			}
			else{
				try{
					Thread.sleep((long)(sleepTime - (System.currentTimeMillis() - lastRefresh)));
				}
				catch(Exception e){
					
				}
			}
		}
	}
	
	private void setDrawOrder(){
		double[] k = new double[nPoly];
		drawOrder = new int[nPoly];
		nPoly = poly3Ds.size();
		
		for(int i = 0; i < nPoly; i++){
			k[i] = poly3Ds.get(i).avgDistance;
			drawOrder[i] = i;
		}
		
		double temp;
		int tempr;
		for(int a = 0; a < k.length; a++){
			for(int b = 0; b < k.length - 1; b++){
				if(k[b] < k[b+1]){
					temp = k[b];
					tempr = drawOrder[b];
					drawOrder[b] = drawOrder[b+1];
					k[b] = k[b+1];
					
					drawOrder[b+1] = tempr;
					k[b+1] = temp;
				}
			}
		}
	}
	
	private void setLight(){
		lightDir.x = mapSize/2 - (mapSize/2 + Math.cos(lightPosition) * mapSize * 10);
		lightDir.y = mapSize/2 - (mapSize/2 + Math.sin(lightPosition) * mapSize * 10);
		lightDir.z = -200;
	}
	
	private void camera(){
//		Vector viewVec = viewTo.pointMinusPoint(viewFrom);
		
		double xMove = 0, yMove = 0, zMove = 0;
		
		if(e){
			Vector nScale = Matrix.multiplyVector2(Matrix.getScalingMatrix(moveSpeed, moveSpeed, moveSpeed), N);
			viewFrom = viewFrom.pointPlusVector(nScale);
			cameraSystem[0][3] = -U.dotProduct(viewFrom);
			cameraSystem[1][3] = -V.dotProduct(viewFrom);
			cameraSystem[2][3] = -N.dotProduct(viewFrom);
		}
		if(q){
			Vector scale = new Vector(-moveSpeed, -moveSpeed, -moveSpeed);
			viewFrom = viewFrom.pointPlusVector(scale);
			cameraSystem[0][3] = -U.dotProduct(viewFrom);
			cameraSystem[1][3] = -V.dotProduct(viewFrom);
			cameraSystem[2][3] = -N.dotProduct(viewFrom);
		}
		if(a){
//			horizontalLook -= horizontalLookSpeed;
			U = Matrix.multiplyVector2(Matrix.getRotationMatrix(-horizontalLookSpeed, N), U);
			V = Matrix.multiplyVector2(Matrix.getRotationMatrix(-horizontalLookSpeed, N), V);
			
			U.normalise();
			V.normalise();
			
			cameraSystem[0][0] = U.x;
			cameraSystem[0][1] = U.y;
			cameraSystem[0][2] = U.z;
			cameraSystem[1][0] = V.x;
			cameraSystem[1][1] = V.y;
			cameraSystem[1][2] = V.z;
			
			cameraSystem[0][3] = -U.dotProduct(viewFrom);
			cameraSystem[1][3] = -V.dotProduct(viewFrom);
		}
		if(d){
//			horizontalLook += horizontalLookSpeed;
			U = Matrix.multiplyVector2(Matrix.getRotationMatrix(horizontalLookSpeed, N), U);
			V = Matrix.multiplyVector2(Matrix.getRotationMatrix(horizontalLookSpeed, N), V);
			
			U.normalise();
			V.normalise();
			
			cameraSystem[0][0] = U.x;
			cameraSystem[0][1] = U.y;
			cameraSystem[0][2] = U.z;
			cameraSystem[1][0] = V.x;
			cameraSystem[1][1] = V.y;
			cameraSystem[1][2] = V.z;
			
			cameraSystem[0][3] = -U.dotProduct(viewFrom);
			cameraSystem[1][3] = -V.dotProduct(viewFrom);
		}
		if(s){
//			verticalLook += verticalLookSpeed;
			U = Matrix.multiplyVector2(Matrix.getRotationMatrix(verticalLookSpeed, V), U);
			N = Matrix.multiplyVector2(Matrix.getRotationMatrix(verticalLookSpeed, V), N);
			
			U.normalise();
			N.normalise();
			
			cameraSystem[2][0] = N.x;
			cameraSystem[2][1] = N.y;
			cameraSystem[2][2] = N.z;
			cameraSystem[0][0] = U.x;
			cameraSystem[0][1] = U.y;
			cameraSystem[0][2] = U.z;
			
			cameraSystem[1][3] = -V.dotProduct(viewFrom);
			cameraSystem[2][3] = -N.dotProduct(viewFrom);
		}
		if(w){
//			verticalLook -= verticalLookSpeed;
			U = Matrix.multiplyVector2(Matrix.getRotationMatrix(-verticalLookSpeed, V), U);
			N = Matrix.multiplyVector2(Matrix.getRotationMatrix(-verticalLookSpeed, V), N);
			
			U.normalise();
			N.normalise();
			
			cameraSystem[2][0] = N.x;
			cameraSystem[2][1] = N.y;
			cameraSystem[2][2] = N.z;
			cameraSystem[0][0] = U.x;
			cameraSystem[0][1] = U.y;
			cameraSystem[0][2] = U.z;
			
			cameraSystem[1][3] = -V.dotProduct(viewFrom);
			cameraSystem[2][3] = -N.dotProduct(viewFrom);
		}
		
//		Matrix.printMatrix(cameraSystem);
		
		moveVec = new Vector(xMove * moveSpeed, yMove * moveSpeed, zMove * moveSpeed);
		viewFrom = viewFrom.pointPlusVector(moveVec);
		viewTo = viewTo.pointPlusVector(moveVec);
		updateVectors();
		
		CM = Matrix.getCM(viewFrom, V, U, N, 2);
//		Matrix.printMatrix(CM);	
	}
	
	private void updateVectors(){
		U = new Vector(cameraSystem[0][0], cameraSystem[0][1], cameraSystem[0][2]);
		U.normalise();
		V = new Vector(cameraSystem[1][0], cameraSystem[1][1], cameraSystem[1][2]);
		V.normalise();
		N = new Vector(cameraSystem[2][0], cameraSystem[2][1], cameraSystem[2][2]);
		N.normalise();
	}

	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent ev) {
		if(ev.getKeyCode() == KeyEvent.VK_W){
			w = true;
		}
		if(ev.getKeyCode() == KeyEvent.VK_A){
			a = true;
		}
		if(ev.getKeyCode() == KeyEvent.VK_S){
			s = true;
		}
		if(ev.getKeyCode() == KeyEvent.VK_D){
			d = true;
		}
		if(ev.getKeyCode() == KeyEvent.VK_E){
			if(q){
				q = false;
			}
			else{
				e = true;
			}
		}
		if(ev.getKeyCode() == KeyEvent.VK_Q){
			if(e){
				e = false;
			}
			else{
				q = true;
			}
		}
	}

	public void keyReleased(KeyEvent ev) {
		if(ev.getKeyCode() == KeyEvent.VK_W){
			w = false;
		}
		if(ev.getKeyCode() == KeyEvent.VK_A){
			a = false;
		}
		if(ev.getKeyCode() == KeyEvent.VK_S){
			s = false;
		}
		if(ev.getKeyCode() == KeyEvent.VK_D){
			d = false;
		}
	}
}