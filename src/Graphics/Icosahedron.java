package Graphics;
import java.awt.Color;

import Geometry.Vector;


/**
 * Creates an icosahedron object and adds it to the objects to be displayed on screen
 * @author Dominic
 *
 */
public class Icosahedron{
	
	private double x, y, z, size;
	private Point[] vertices = new Point[12];
	private int[][] sides = {{0, 8, 4},
								{0, 5, 10},
								{2, 4, 9},
								{2 ,11 ,5},
								{1, 6, 8},
								{1, 10, 7},
								{3, 9, 6},
								{3, 7, 11},
								{0, 10, 8},
								{1, 8, 10},
								{2, 9, 11},
								{3, 11, 9},
								{4, 2, 0},
								{5, 0, 2},
								{6, 1, 3},
								{7, 3, 1},
								{8, 6, 4},
								{9, 4, 6},
								{10, 5, 7},
								{11, 7, 5}};
	private Color[] colors = new Color[20];
	private double gr = 1.618;
	private Vector xVec, yVec, zVec;
	
	/**
	 * Creates the new icosahedron, and adds it to the screen
	 * @param v x, y and z coordinates of its position
	 * @param size Size of the object
	 */
	public Icosahedron(Vector v, double size, Vector xVec, Vector yVec, Vector zVec){
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
		this.size = size;
		gr *= size;
		this.xVec = xVec;
		this.yVec = yVec;
		this.zVec = zVec;
		
		createPoints();
		
		for(int i = 0; i < 20; i++){
			colors[i] = Color.BLUE;
		}
		
		GraphicalModel g = new GraphicalModel(vertices, sides, colors);
		g.create();
	}
	
	/**
	 * Creates the vertices of the object
	 */
	private void createPoints(){
		vertices[0] = new Point(gr, size, 0);
		vertices[1] = new Point(-gr, size, 0);
		vertices[2] = new Point(gr, -size, 0);
		vertices[3] = new Point(-gr, -size, 0);
		vertices[4] = new Point(size, 0, gr);
		vertices[5] = new Point(size, 0, -gr);
		vertices[6] = new Point(-size, 0, gr);
		vertices[7] = new Point(-size, 0, -gr);
		vertices[8] = new Point(0, gr, size);
		vertices[9] = new Point(0, -gr, size);
		vertices[10] = new Point(0, gr, -size);
		vertices[11] = new Point(0, -gr, -size);
		
		for(int i = 0; i < vertices.length; i++){
			vertices[i] = Matrix.multiplyPoint(Matrix.getR(xVec, yVec, zVec), vertices[i]);
			vertices[i] = new Point(vertices[i].x + x, vertices[i].y + y, vertices[i].z + z);
		}
	}
	
}
