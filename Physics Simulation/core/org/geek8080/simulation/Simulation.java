package org.geek8080.simulation;

import java.util.Random;

import org.geek8080.physics2d.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Simulation extends Application {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	public static final Random RAND = new Random();

	public static GraphicsContext gc;
	public static final ImpulseScene impulse = new ImpulseScene(ImpulseMath.DT, 10);

	public static Timeline tl;

	@Override
	public void start(Stage stage) throws Exception {

		Body b = null;

		// make bounds
		b = impulse.add(new Polygon(800.0f, 2.0f), 0, 0);
		b.setStatic();
		b.setOrient(0);
		b.restitution = 1.0f;
		b.staticFriction = 0;
		b.dynamicFriction = 0;

		b = impulse.add(new Polygon(2.0f, 600.0f), 0, 0);
		b.setStatic();
		b.setOrient(0);
		b.restitution = 1.0f;
		b.staticFriction = 0;
		b.dynamicFriction = 0;

		b = impulse.add(new Polygon(800.0f, 2.0f), 0, 600);
		b.setStatic();
		b.setOrient(0);
		b.restitution = 1.0f;
		b.staticFriction = 0;
		b.dynamicFriction = 0;

		b = impulse.add(new Polygon(2.0f, 600.0f), 800, 0);
		b.setStatic();
		b.setOrient(0);
		b.restitution = 1.0f;
		b.staticFriction = 0;
		b.dynamicFriction = 0;

		for (int i = 0; i < 10; i++) {
			addPolygon(ImpulseMath.random(0, 800), ImpulseMath.random(0, 600));
		}

		for (int i = 0; i < 10; i++) {
			addCircle(ImpulseMath.random(0, 800), ImpulseMath.random(0, 600));
		}

		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		stage.setScene(new Scene(new StackPane(canvas)));
		gc = canvas.getGraphicsContext2D();
		tl = new Timeline(new KeyFrame(Duration.millis(16), e -> {
			update();
		}));
		tl.setCycleCount(Timeline.INDEFINITE);
		tl.play();
		stage.getScene().setOnMouseClicked(e -> {
			MouseButton code = e.getButton();
			if (code.equals(MouseButton.PRIMARY)) {
				addPolygon(e.getX(), e.getY());
			}else if(code.equals(MouseButton.SECONDARY)){
				addCircle(e.getX(), e.getY());
			}
		});
		stage.show();

	}

	private void addCircle(double x, double y) {
		float r = ImpulseMath.random(10.0f, 30.0f);

		Circle circle = new Circle(r);
		impulse.add(circle, (int)x, (int)y);
		circle.body.velocity.set(new Random().nextInt(600) - 300, new Random().nextInt(600) - 300);
	}

	private void addPolygon(double x, double y) {
		Body b;
		float r = ImpulseMath.random(10.0f, 50.0f);
		int vertCount = ImpulseMath.random(3, Polygon.MAX_POLY_VERTEX_COUNT);

		Vector2D[] verts = Vector2D.arrayOf(vertCount);
		for (int j = 0; j < vertCount; j++) {
			verts[j].set(ImpulseMath.random(-r, r), ImpulseMath.random(-r, r));
		}

		Shape polygon = new Polygon(verts);
		b = impulse.add(polygon, (int)x, (int)y);
		b.setOrient(ImpulseMath.random(-ImpulseMath.PI, ImpulseMath.PI));
		b.velocity.set(RAND.nextInt(600) - 300, RAND.nextInt(600) - 300);
	}

	private void update() {
		impulse.step();
		render();
	}

	private void render() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		for (Body body : impulse.bodies) {
			if (body.shape instanceof Circle) {
				Circle c = (Circle) body.shape;
				float rx = (float) StrictMath.cos(body.orient) * c.radius;
				float ry = (float) StrictMath.sin(body.orient) * c.radius;

				gc.setFill(Color.rgb(0, 255, 221));
				gc.fillOval(body.position.x - c.radius, body.position.y - c.radius, c.radius * 2, c.radius * 2);

				gc.setStroke(Color.rgb(35, 74, 107));
				gc.strokeLine(body.position.x, body.position.y, body.position.x + rx, body.position.y + ry);
			} else if (body.shape instanceof Polygon) {
				Polygon p = (Polygon) body.shape;

				for (int i = 0; i < p.vertexCount; i++) {
					Vector2D v = new Vector2D(p.vertices[i]);
					body.shape.u.muli(v);
					v.addi(body.position);

					if (i == 0) {
						gc.beginPath();
						gc.moveTo(v.x, v.y);
					} else {
						gc.lineTo(v.x, v.y);
					}
				}

				gc.closePath();
				gc.setFill(Color.rgb(221, 37, 245));
				gc.fill();
				//gc.stroke();
		
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}