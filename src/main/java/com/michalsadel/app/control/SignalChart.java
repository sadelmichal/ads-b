package com.michalsadel.app.control;

import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;


public class SignalChart extends StackPane {
    private final Path path = new Path();
    private final Axes axes = new Axes();

    public SignalChart() {
        setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
        setPrefSize(450, 350);
        setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
        setBackground(new Background(new BackgroundFill(Color.rgb(35, 39, 50), CornerRadii.EMPTY, Insets.EMPTY)));
        path.setStroke(Color.ORANGE.deriveColor(0, 1, 1, 0.6));
        path.setStrokeWidth(2);
        path.setManaged(false);
        getChildren().addAll(axes, path);
        setOnScroll(event -> {
            int modifier = event.getDeltaY() > 0 ? -10 : 10;
            setPrefWidth(this.getPrefWidth() + modifier);
            setPrefHeight(this.getPrefHeight() + modifier);
        });
        setOnMouseClicked(event -> {
            if (event.getButton().name().equals("PRIMARY")) {
                //Translate t = new Translate(0, axes.getHeight() - 22);
                //final Point2D transform = t.transform(26, 11);
                path.getElements().add(new MoveTo(mapX(0), mapY(11)));
                path.getElements().add(new VLineTo(mapY(100)));
            }
        });
    }

    private double mapX(double x) {
        double tx = axes.getPrefWidth() / 2;
        double sx = axes.getPrefWidth() / (axes.getX().getUpperBound() - axes.getX().getLowerBound());
        return x * sx + tx;
    }

    private double mapY(double y) {
        double ty = axes.getPrefHeight() / 2;
        double sy = axes.getPrefHeight() / (axes.getY().getUpperBound() - axes.getY().getLowerBound());
        return -y * sy + ty;
    }
}
