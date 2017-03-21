package annotatedHouse;


public class BuildingMaterial {
    private int x;

    private int y;

    private int width;

    private int height;

    private java.util.concurrent.atomic.AtomicBoolean isVisible;

    public BuildingMaterial(int x ,int y ,int width ,int height ,boolean isVisible) {
        annotatedHouse.BuildingMaterial.this.x = x;
        annotatedHouse.BuildingMaterial.this.y = y;
        annotatedHouse.BuildingMaterial.this.width = width;
        annotatedHouse.BuildingMaterial.this.height = height;
        annotatedHouse.BuildingMaterial.this.isVisible = new java.util.concurrent.atomic.AtomicBoolean(isVisible);
    }

    public int getX() {
        return annotatedHouse.BuildingMaterial.this.x;
    }

    public int getY() {
        return annotatedHouse.BuildingMaterial.this.y;
    }

    public int getWidth() {
        return annotatedHouse.BuildingMaterial.this.width;
    }

    public int getHeight() {
        return annotatedHouse.BuildingMaterial.this.height;
    }

    public boolean isVisible() {
        return annotatedHouse.BuildingMaterial.this.isVisible.get();
    }

    public boolean getAndSetVisible(boolean isVisilbe) {
        return annotatedHouse.BuildingMaterial.this.isVisible.getAndSet(isVisilbe);
    }
}

