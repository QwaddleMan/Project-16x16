/*
 * This Raycaster object uses marching rays to determine distance from caster to a hit object
 */

package project_16x16.components;
import processing.core.PVector;
import processing.core.PApplet;
import project_16x16.PClass;
import project_16x16.objects.MagicSourceObject;
import project_16x16.SideScroller;
import project_16x16.objects.EditableObject;
import project_16x16.scene.GameplayScene;
import processing.core.*;
import java.util.ArrayList;


public class RayCaster{
	
	private GameplayScene scene;
	private PVector hitPoint = new PVector();
	private PVector currentPoint = new PVector();
	private float currentRadius;
	private ArrayList<PVector> points = new ArrayList<PVector>();
	private ArrayList<Float> rads = new ArrayList<Float>();
	private boolean debug = true;
    private EditableObject castObject;
	private PVector hit = new PVector();
	private boolean isHit;
	public RayCaster(GameplayScene scene) {
		this.scene = scene;
	}
	
	//returns distance
	public float Cast(EditableObject castObject, float angle) {
		//reset everything because bugs.. oh dear god... the bugs... they are everywhere
		this.isHit = false;
        this.castObject = castObject;
        this.hitPoint = new PVector();
        this.currentPoint = new PVector();
        this.currentRadius = 9999.9f;
        this.points = new ArrayList<PVector>();
        this.rads = new ArrayList<Float>();
        this.hit = new PVector();
        
                
		currentPoint = castObject.pos;
		currentPoint = getNextValidPoint(angle);
		float rayDistance = PVector.dist(currentPoint, castObject.pos);
		
		
		//keep on marching until the current radius is less that .5
		while(currentRadius > 0.5f) {
			System.out.println("looping...");
			currentPoint = getNextValidPoint(angle);
			rayDistance = PVector.dist(currentPoint, castObject.pos);
			System.out.println(rayDistance);
			if(rayDistance > 1000) {
				return rayDistance;
			}
		}
		
		//hit
		isHit = true;
		hit = currentPoint;
		
		float hitDistance = PVector.dist(castObject.pos, hit);
		System.out.println("woopwoop: " + hitDistance);
		return hitDistance;
	}
	
	public PVector getNextValidPoint(float angle) {
		/*
		 * determine where the next safe location to set
		 * a point is along the path. point can not be inside a object. 
		 */
		EditableObject closestObject = getClosestObject();
		float closestDist = PVector.dist(currentPoint, closestObject.pos) - ((closestObject.width + closestObject.height)/4);

		float nextStep = closestDist/2;
		PVector nextPoint = new PVector();
		nextPoint.x = this.currentPoint.x + (nextStep * PApplet.cos(angle));
		nextPoint.y = this.currentPoint.y + (nextStep * PApplet.sin(angle));
		
		this.currentRadius = PVector.dist(nextPoint, closestObject.pos) - ((closestObject.width + closestObject.height)/4);
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
                this.scene.applet.noFill();
				this.scene.applet.stroke(255,0,0);
                if(i > 0){
                    this.scene.applet.line(drawPoint.x, drawPoint.y, this.points.get(i-1).x, this.points.get(i-1).y);
                }


				this.scene.applet.ellipse(drawPoint.x, drawPoint.y, drawRadius, drawRadius);
			}
		}
	}
	
	public EditableObject getClosestObject() {
		/*
		 * loop through scene editable objects and determine which is closest
		 * to the current point
		 */
		float current;
		EditableObject closestObject = scene.objects.get(12);
		float closest = PVector.dist(this.currentPoint, closestObject.pos)- ((closestObject.width + closestObject.height)/4); 
		
		for(int i = 0; i < scene.objects.size(); i++) {
			current = PVector.dist(this.currentPoint, scene.objects.get(i).pos)- ((closestObject.width + closestObject.height)/4);
			
			if(current < closest)
				closest = current;
				closestObject = scene.objects.get(i);
		}
		return closestObject;
	}
}
