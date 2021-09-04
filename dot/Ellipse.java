import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;

public class Ellipse extends Ellipse2D.Double implements Data{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4776055373411902406L;
	
	private Color color=Color.BLACK;
	Ellipse(){
		super();
	}
	Ellipse(double x,double y,double w,double h){
		super(x,y,w,h);
	}
	Ellipse(Color color,double x,double y,double w,double h){
		this(x,y,w,h);
		this.color=color;
	}
	public Color getCouleur() {
		return color;
	}
	public Ellipse setCouleur(Color color) {
		this.color=color;
		return this;
	}
	public Ellipse setX(double x) {
		this.x=x;
		return this;
	}
	public Ellipse setY(double y) {
		this.y=y;
		return this;
	}
	public Ellipse setWidth(double width) {
		this.width=width;
		return this;
	}
	public Ellipse setHeight(double height) {
		this.height=height;
		return this;
	}
	public double distanceToCenterOf(Shape s) {
		double x=(s.getBounds().getCenterX()-this.getCenterX())*(s.getBounds().getCenterX()-this.getCenterX());
		double y=(s.getBounds().getCenterY()-this.getCenterY())*(s.getBounds().getCenterY()-this.getCenterY());
		return Math.sqrt(x+y);
	}
	public String toString() {
		return this.getMinX()+" "+this.getMinY()+" "+this.getMaxX()+" "+this.getMaxY();
	}
	@Override
	public RectangularShape getRectangle() {
		return this;
	}
	
}
