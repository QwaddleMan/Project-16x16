/*
 * This Raycaster object uses marching rays to determine distance from caster to a hit object
 */

package project_16x16.components;
import processing.core.PVector;
import processing.core.PApplet;
import project_16x16.PClass;
import project_16x16.SideScroller;
import project_16x16.objects.EditableObject;
import project_16x16.scene.GameplayScene;
import processing.core.*;
import java.util.ArrayList;


public class RayCaster extends PClass{
	
	private GameplayScene scene;
	private PVector hitPoint;
	private PVector currentPoint;
	private float currentRadius;
	private ArrayList<PVector> points = new ArrayList<PVector>();
	private ArrayList<Float> rads = new ArrayList<Float>();
	private boolean debug = true;
	
	private PVector hit;
	
	public RayCaster(SideScroller game, GameplayScene scene) {
		super(game);
		this.scene = scene;
	}
	
	//returns distance
	public float Cast(EditableObject castObject, float angle) {
		currentPoint = castObject.pos;
		currentPoint = getNextValidPoint(angle);
		
		
		//keep on marching until the current radius is less that .5
		while(currentRadius > 0.5f) {
			currentPoint = getNextValidPoint(angle);
		}
		
		//hit
		hit = currentPoint;
		float hitDistance = PVector.dist(castObject.pos, hit);
		return hitDistance;
	}
	
	public PVector getNextValidPoint(float angle) {
		/*
		 * determine where the next safe location to set
		 * a point is along the path. point can not be inside a object. 
		 */
		EditableObject closestObject = getClosestObject();
		float closestDist = PVector.dist(currentPoint, closestObject.pos);
		float nextStep = closestDist/2;
		PVector nextPoint = new PVector();
		nextPoint.x = this.currentPoint.x + (nextStep * PApplet.cos(angle));
		nextPoint.y = this.currentPoint.y + (nextStep * PApplet.sin(angle));
		
		this.currentRadius = PVector.dist(nextPoint, closestObject.pos);
		if(this.debug) {
			this.points.add(nextPoint);
			this.rads.add(this.currentRadius);
		}
		
		return nextPoint;
		
	}
	
	public void drawDebug() {
		if(this.debug) {
			PVector drawPoint;
			float drawRadius;
			for(int i = 0; i < this.points.size(); i++) {
				drawPoint = this.points.get(i);
				drawRadius = this.rads.get(i);
				this.applet.ellipse(drawPoint.x, drawPoint.y, drawRadius, drawRadius);
			}
		}
	}
	
	public EditableObject getClosestObject() {
		/*
		 * loop through scene editable objects and determine which is closest
		 * to the current point
		 */
		float current;
		EditableObject closestObject = scene.objects.get(0);
		float closest = PVector.dist(this.currentPoint, closestObject.pos); 
		
		for(EditableObject obj : scene.objects) {
			current = PVector.dist(this.currentPoint, obj.pos);
			if(current < closest)
				closest = current;
				closestObject = obj;
		}
		return closestObject;
	}
}
