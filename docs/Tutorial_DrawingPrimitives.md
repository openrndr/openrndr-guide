# Drawing Primitives #

FIX ME: Add a little sentence here. Question: should Relevant APIs be on top, or be under the other topics, or explain how to use the APIs?? 

## Relevant APIs ##

```java

Drawer.circle(double x, double y, double radius)
Drawer.rectangle(double x, double y, double width, double height)
Drawer.line(double x0, double y0, double x1, double y1)

Drawer.fill(ColorRGBa fillColor)
Drawer.stroke(ColorRGBa strokeColor)
Drawer.strokeWeight(double weight)

```

## Drawing lines ##

```java
void draw() {
    drawer.background(ColorRGBa.WHITE)

    // -- setup line appearance
    drawer.stroke(ColorRGBa.BLACK)
    drawer.strokeWeight(5.0)

    // -- draw a line from (10,10) to (120, 140)
    drawer.line(10,10, 120, 140)
}
```

## Drawing rectangles ##

```java
void draw() {
    background(ColorRGBa.WHITE);

    // -- setup rectangle appearance
    drawer.fill(ColorRGBa.RED);
    drawer.stroke(ColorRGBa.BLACK);
    drawer.strokeWeight(1.0);

    // -- draw a rectangle at (10,10) with width 120 and height 140
    drawer.rectangle(10,10, 120, 140)
}
```

## Drawing circles ##

```java
void draw() {
    background(ColorRGBa.WHITE);

    // -- setup rectangle appearance
    drawer.fill(ColorRGBa.RED);
    drawer.stroke(ColorRGBa.BLACK);
    drawer.strokeWeight(1.0);

    // -- draw a circle with its center at (140,140) with radius 130
    drawer.rectangle(140, 140, 130)
}
```

## Drawing many primitives at once ##

```java
void draw() {
    background(ColorRGBa.WHITE);

    // -- setup rectangle appearance
    drawer.fill(ColorRGBa.RED);
    drawer.stroke(ColorRGBa.BLACK);
    drawer.strokeWeight(1.0);

    List<Rectangle> rectangles = new ArrayList<>();
    for (int i = 0; i < 500; ++i) {
         rectangles.add(new Rectangle(Math.random() * 400, Math.random() * 400, Math.random() * 400, Math.random() * 400);
    }

    drawer.rectangles(rectangles);
}
```
