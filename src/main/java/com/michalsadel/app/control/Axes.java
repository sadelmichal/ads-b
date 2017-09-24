package com.michalsadel.app.control;

import javafx.geometry.*;
import javafx.scene.chart.*;
import javafx.scene.layout.*;

class Axes extends Pane {
    private static final int padding = 10;
    private final NumberAxis x = new NumberAxis();
    private final NumberAxis y = new NumberAxis();

    NumberAxis getX() {
        return x;
    }

    NumberAxis getY() {
        return y;
    }

    public Axes() {
        initX();
        initY();
        getChildren().addAll(x, y);
    }

    @Override
    protected void layoutChildren() {
        final double parentWidth = getLayoutBounds().getWidth();
        final double parentHeight = getLayoutBounds().getHeight();
        final Bounds boundsInParent = getBoundsInParent();
        final Bounds boundsInLocal = getBoundsInLocal();
        y.setPrefHeight(parentHeight - 2 * padding);
        x.setPrefWidth(parentWidth - 3 * padding);
        x.setLayoutY(parentHeight - padding);
        super.layoutChildren();
    }

    private void initY() {
        y.setLowerBound(0);
        y.setUpperBound(10);
        y.setTickUnit(1);
        y.setAutoRanging(false);
        y.setLayoutY(padding);
        y.setSide(Side.LEFT);
        y.setTickLabelRotation(-90);
    }

    private void initX() {
        x.setLayoutX(2 * padding + 1);
        x.setSide(Side.BOTTOM);
        x.setTickMarkVisible(false);
        x.setTickLabelsVisible(false);
    }
}