/*
 * This Raycaster object uses marching rays to determine distance from caster to a hit object
 */

package project_16x16.components;
import processing.core.PVector;
import project_16x16.objects.EditableObject;
import project_16x16.scene.GameplayScene;;

public class RayCaster {
	
	private GameplayScene scene;
	private PVector hitPoint;
	private PVector currentPoint;
	private float currentRadius;
	
	public RayCaster(GameplayScene scene) {
		this.scene = scene;
	}
	
	public float Cast(EditableObject castObject, float angle) {
		currentPoint = castObject.pos;
		currentPoint = getNextValidPoint(angle);
		
		return .5f;
	}
	
	public PVector getNextValidPoint(float angle) {
		/*
		 * determine where the next safe location to set
		 * a point is along the path. point can not be inside a object. 
		 */
		float closestDist = getClosestObjectDistance();
		float nextStep = closestDist/2;
		
	}
	
	public float getClosestObjectDistance() {
		/*
		 * loop through scene editable objects and determine which is closest
		 * to the current point
		 */
		float current;
		float closest = PVector.dist(this.currentPoint, scene.objects.get(0).pos); 
		for(EditableObject obj : scene.objects) {
			current = PVector.dist(this.currentPoint, obj.pos);
			if(current < closest)
				closest = current;
		}
		return closest;
	}
}
