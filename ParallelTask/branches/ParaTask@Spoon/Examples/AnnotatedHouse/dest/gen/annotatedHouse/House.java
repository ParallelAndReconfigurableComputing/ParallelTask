package annotatedHouse;


public class House extends java.applet.Applet {
    private static final long serialVersionUID = 1L;

    private int width = 500;

    private int height = 320;

    private int N = 20;

    private boolean clearScreen = true;

    private java.util.concurrent.CopyOnWriteArrayList<annotatedHouse.BuildingMaterial> foundation = null;

    private java.util.concurrent.CopyOnWriteArrayList<annotatedHouse.BuildingMaterial> wallSiding = null;

    private java.util.concurrent.CopyOnWriteArrayList<annotatedHouse.BuildingMaterial> roofTiles = null;

    private java.util.concurrent.CopyOnWriteArrayList<annotatedHouse.BuildingMaterial> windows = null;

    private annotatedHouse.BuildingMaterial door = null;

    private annotatedHouse.BuildingMaterial forSaleSign = null;

    private java.awt.Color colorRoof;

    private java.awt.Color colorWalls;

    public House(annotatedHouse.Build builder) {
        setPreferredSize(new java.awt.Dimension(width , height));
        initialiseMaterial();
    }

    public java.lang.Void buildSingleTask(java.awt.Color colorWalls, java.awt.Color colorRoof) {
        build(colorWalls, colorRoof);
        return null;
    }

    public void build(java.awt.Color colorWalls, java.awt.Color colorRoof) {
        annotatedHouse.House.this.colorWalls = colorWalls;
        annotatedHouse.House.this.colorRoof = colorRoof;
        initialiseMaterial();
        buildAll(foundation);
        buildAll(wallSiding);
        buildAll(roofTiles);
        buildItem(door);
        buildAll(windows);
        buildItem(forSaleSign);
    }

    public java.lang.Void buildTask(java.awt.Color colorWalls, java.awt.Color colorRoof) {
        annotatedHouse.House.this.colorWalls = colorWalls;
        annotatedHouse.House.this.colorRoof = colorRoof;
        initialiseMaterial();
        pt.runtime.TaskInfoOneArg<Void, java.util.concurrent.CopyOnWriteArrayList<annotatedHouse.BuildingMaterial>> __foundationTaskPtTask__ = ((pt.runtime.TaskInfoOneArg<Void, java.util.concurrent.CopyOnWriteArrayList<annotatedHouse.BuildingMaterial>>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.MULTI, 
			(pt.functionalInterfaces.FunctorOneArgNoReturn<java.util.concurrent.CopyOnWriteArrayList<annotatedHouse.BuildingMaterial>>)(__foundationPtNonLambdaArg__) -> buildAll(__foundationPtNonLambdaArg__))));
        pt.runtime.TaskID<Void> __foundationTaskPtTaskID__ = __foundationTaskPtTask__.start(foundation);
        pt.runtime.TaskInfoOneArg<Void, java.util.concurrent.CopyOnWriteArrayList<annotatedHouse.BuildingMaterial>> __wallTaskPtTask__ = ((pt.runtime.TaskInfoOneArg<Void, java.util.concurrent.CopyOnWriteArrayList<annotatedHouse.BuildingMaterial>>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.MULTI, 
			(pt.functionalInterfaces.FunctorOneArgNoReturn<java.util.concurrent.CopyOnWriteArrayList<annotatedHouse.BuildingMaterial>>)(__wallSidingPtNonLambdaArg__) -> buildAll(__wallSidingPtNonLambdaArg__))));
        __wallTaskPtTask__.dependsOn(__foundationTaskPtTaskID__);
        pt.runtime.TaskID<Void> __wallTaskPtTaskID__ = __wallTaskPtTask__.start(wallSiding);
        pt.runtime.TaskInfoOneArg<Void, java.util.concurrent.CopyOnWriteArrayList<annotatedHouse.BuildingMaterial>> __roofTaskPtTask__ = ((pt.runtime.TaskInfoOneArg<Void, java.util.concurrent.CopyOnWriteArrayList<annotatedHouse.BuildingMaterial>>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.MULTI, 
			(pt.functionalInterfaces.FunctorOneArgNoReturn<java.util.concurrent.CopyOnWriteArrayList<annotatedHouse.BuildingMaterial>>)(__roofTilesPtNonLambdaArg__) -> buildAll(__roofTilesPtNonLambdaArg__))));
        __roofTaskPtTask__.dependsOn(__wallTaskPtTaskID__);
        pt.runtime.TaskID<Void> __roofTaskPtTaskID__ = __roofTaskPtTask__.start(roofTiles);
        pt.runtime.TaskInfoOneArg<Void, annotatedHouse.BuildingMaterial> __doorTaskPtTask__ = ((pt.runtime.TaskInfoOneArg<Void, annotatedHouse.BuildingMaterial>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, 
			(pt.functionalInterfaces.FunctorOneArgNoReturn<annotatedHouse.BuildingMaterial>)(__doorPtNonLambdaArg__) -> buildItem(__doorPtNonLambdaArg__))));
        __doorTaskPtTask__.dependsOn(__wallTaskPtTaskID__);
        pt.runtime.TaskID<Void> __doorTaskPtTaskID__ = __doorTaskPtTask__.start(door);
        pt.runtime.TaskInfoOneArg<Void, java.util.concurrent.CopyOnWriteArrayList<annotatedHouse.BuildingMaterial>> __windowsTaskPtTask__ = ((pt.runtime.TaskInfoOneArg<Void, java.util.concurrent.CopyOnWriteArrayList<annotatedHouse.BuildingMaterial>>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.MULTI, 
			(pt.functionalInterfaces.FunctorOneArgNoReturn<java.util.concurrent.CopyOnWriteArrayList<annotatedHouse.BuildingMaterial>>)(__windowsPtNonLambdaArg__) -> buildAll(__windowsPtNonLambdaArg__))));
        __windowsTaskPtTask__.dependsOn(__wallTaskPtTaskID__);
        pt.runtime.TaskID<Void> __windowsTaskPtTaskID__ = __windowsTaskPtTask__.start(windows);
        java.lang.Void[] signTask = new java.lang.Void[1];
        pt.runtime.TaskIDGroup<java.lang.Void> __signTaskPtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>(1);
        pt.runtime.TaskInfoOneArg<Void, annotatedHouse.BuildingMaterial> ____signTask_1__PtTask__ = ((pt.runtime.TaskInfoOneArg<Void, annotatedHouse.BuildingMaterial>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, 
			(pt.functionalInterfaces.FunctorOneArgNoReturn<annotatedHouse.BuildingMaterial>)(__forSaleSignPtNonLambdaArg__) -> buildItem(__forSaleSignPtNonLambdaArg__))));
        ____signTask_1__PtTask__.dependsOn(__roofTaskPtTaskID__, __doorTaskPtTaskID__);
        pt.runtime.TaskID<Void> ____signTask_1__PtTaskID__ = ____signTask_1__PtTask__.start(forSaleSign);
        __signTaskPtTaskIDGroup__.setInnerTask(0, ____signTask_1__PtTaskID__);
        try {
            __signTaskPtTaskIDGroup__.waitTillFinished();
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.lang.Void var = signTask[0];
        return var;
    }

    private java.lang.Void buildItem(annotatedHouse.BuildingMaterial item) {
        if (!(item.getAndSetVisible(true))) {
            simulateWork(N);
            repaint();
        } 
        return null;
    }

    private java.lang.Void buildAll(java.util.List<annotatedHouse.BuildingMaterial> items) {
        for (annotatedHouse.BuildingMaterial item : items)
            buildItem(item);
        return null;
    }

    public void update(java.awt.Graphics g) {
        paint(g);
    }

    public void paint(java.awt.Graphics g) {
        if (clearScreen) {
            g.clearRect(0, 0, width, height);
            clearScreen = false;
        } 
        for (annotatedHouse.BuildingMaterial b : foundation) {
            if (b.isVisible()) {
                g.setColor(new java.awt.Color(162 , 158 , 24));
                g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
                g.setColor(java.awt.Color.BLACK);
                g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
            } 
        }
        for (annotatedHouse.BuildingMaterial b : wallSiding) {
            if (b.isVisible()) {
                g.setColor(colorWalls);
                g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
                g.setColor(java.awt.Color.BLACK);
                g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
            } 
        }
        for (annotatedHouse.BuildingMaterial b : roofTiles) {
            if (b.isVisible()) {
                g.setColor(colorRoof);
                g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
                g.setColor(java.awt.Color.BLACK);
                g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
            } 
        }
        if (door.isVisible()) {
            g.setColor(new java.awt.Color(152 , 118 , 84));
            g.fillRect(door.getX(), door.getY(), door.getWidth(), door.getHeight());
            g.setColor(java.awt.Color.BLACK);
            g.drawRect(door.getX(), door.getY(), door.getWidth(), door.getHeight());
        } 
        for (annotatedHouse.BuildingMaterial b : windows) {
            if (b.isVisible()) {
                g.setColor(new java.awt.Color(173 , 216 , 230));
                g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
                g.setColor(java.awt.Color.BLACK);
                g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
            } 
        }
        if (forSaleSign.isVisible()) {
            g.setColor(new java.awt.Color(152 , 118 , 84));
            g.fillRect(400, 260, 15, 20);
            g.fillRect(445, 260, 15, 20);
            g.setColor(java.awt.Color.BLACK);
            g.drawRect(400, 260, 15, 20);
            g.drawRect(445, 260, 15, 20);
            g.setColor(new java.awt.Color(225 , 239 , 0));
            g.fillRect(forSaleSign.getX(), forSaleSign.getY(), forSaleSign.getWidth(), forSaleSign.getHeight());
            g.setColor(java.awt.Color.BLACK);
            g.drawRect(forSaleSign.getX(), forSaleSign.getY(), forSaleSign.getWidth(), forSaleSign.getHeight());
            java.awt.Font f = g.getFont();
            g.setFont(new java.awt.Font(f.getName() , java.awt.Font.BOLD , 21));
            g.drawString("For", ((forSaleSign.getX()) + 30), ((forSaleSign.getY()) + 30));
            g.drawString("Sale", ((forSaleSign.getX()) + 25), ((forSaleSign.getY()) + 60));
        } 
    }

    public void initialiseMaterial() {
        annotatedHouse.House.this.foundation = new java.util.concurrent.CopyOnWriteArrayList<>();
        annotatedHouse.House.this.wallSiding = new java.util.concurrent.CopyOnWriteArrayList<>();
        annotatedHouse.House.this.roofTiles = new java.util.concurrent.CopyOnWriteArrayList<>();
        annotatedHouse.House.this.windows = new java.util.concurrent.CopyOnWriteArrayList<>();
        initFoundation();
        initWallSiding();
        initRoofTiles();
        initWindowsAndDoor();
        initForSaleSign();
    }

    private void initFoundation() {
        int numCols = 31;
        int w = 15;
        int h = 10;
        int x = 20;
        int y = 280;
        for (int i = 0 ; i < numCols ; i++)
            foundation.add(new annotatedHouse.BuildingMaterial((x + (i * w)) , y , w , h , false));
        x -= 7;
        y += h;
        for (int i = 0 ; i < numCols ; i++)
            foundation.add(new annotatedHouse.BuildingMaterial((x + (i * w)) , y , w , h , false));
        x += 7;
        y += h;
        for (int i = 0 ; i < numCols ; i++)
            foundation.add(new annotatedHouse.BuildingMaterial((x + (i * w)) , y , w , h , false));
        java.util.List<annotatedHouse.BuildingMaterial> list = new java.util.ArrayList<>();
        list.addAll(foundation);
        java.util.Collections.shuffle(list);
        foundation.clear();
        foundation.addAll(list);
    }

    private void initWallSiding() {
        int numPlanks = 40;
        int totalW = 320;
        int w = totalW / numPlanks;
        int h = 150;
        int x = 40;
        int y = 130;
        for (int i = 0 ; i < numPlanks ; i++)
            wallSiding.add(new annotatedHouse.BuildingMaterial((x + (i * w)) , y , w , h , false));
        java.util.List<annotatedHouse.BuildingMaterial> list = new java.util.ArrayList<>();
        list.addAll(wallSiding);
        java.util.Collections.shuffle(list);
        wallSiding.clear();
        wallSiding.addAll(list);
    }

    private void initRoofTiles() {
        int numPlanks = 12;
        int dec = 10;
        int h = 10;
        int w = 360;
        int x = 20;
        int y = 130 - h;
        for (int i = 0 ; i < numPlanks ; i++) {
            roofTiles.add(new annotatedHouse.BuildingMaterial((x + (i * dec)) , (y - (i * h)) , w , h , false));
            w -= 2 * dec;
        }
        java.util.List<annotatedHouse.BuildingMaterial> list = new java.util.ArrayList<>();
        list.addAll(roofTiles);
        java.util.Collections.shuffle(list);
        roofTiles.clear();
        roofTiles.addAll(list);
    }

    private void initWindowsAndDoor() {
        door = new annotatedHouse.BuildingMaterial(63 , 160 , 75 , 120 , false);
        windows.add(new annotatedHouse.BuildingMaterial(160 , 160 , 80 , 60 , false));
        windows.add(new annotatedHouse.BuildingMaterial(253 , 160 , 75 , 60 , false));
    }

    private void initForSaleSign() {
        forSaleSign = new annotatedHouse.BuildingMaterial(380 , 180 , 100 , 80 , false);
    }

    public void setCompletionLevel(int N) {
        annotatedHouse.House.this.N = N;
    }

    public void reset() {
        clearScreen = true;
        for (annotatedHouse.BuildingMaterial b : wallSiding)
            b.getAndSetVisible(false);
        for (annotatedHouse.BuildingMaterial b : foundation)
            b.getAndSetVisible(false);
        for (annotatedHouse.BuildingMaterial b : roofTiles)
            b.getAndSetVisible(false);
        for (annotatedHouse.BuildingMaterial b : windows)
            b.getAndSetVisible(false);
        door.getAndSetVisible(false);
        forSaleSign.getAndSetVisible(false);
        repaint();
    }

    private void simulateWork(int N) {
        double xmin = -1.0;
        double ymin = -1.0;
        double width = 2.0;
        double height = 2.0;
        for (int i = 0 ; i < N ; i++) {
            for (int j = 0 ; j < N ; j++) {
                double x = xmin + ((i * width) / N);
                double y = ymin + ((j * height) / N);
                annotatedHouse.Complex z = new annotatedHouse.Complex(x , y);
                newton(z);
            }
        }
    }

    private java.awt.Color newton(annotatedHouse.Complex z) {
        double EPSILON = 1.0E-8;
        annotatedHouse.Complex four = new annotatedHouse.Complex(4 , 0);
        annotatedHouse.Complex one = new annotatedHouse.Complex(1 , 0);
        annotatedHouse.Complex root1 = new annotatedHouse.Complex(1 , 0);
        annotatedHouse.Complex root2 = new annotatedHouse.Complex((-1) , 0);
        annotatedHouse.Complex root3 = new annotatedHouse.Complex(0 , 1);
        annotatedHouse.Complex root4 = new annotatedHouse.Complex(0 , (-1));
        for (int i = 0 ; i < 100 ; i++) {
            annotatedHouse.Complex f = z.times(z).times(z).times(z).minus(one);
            annotatedHouse.Complex fp = four.times(z).times(z).times(z);
            z = z.minus(f.divides(fp));
            if ((z.minus(root1).abs()) <= EPSILON)
                return java.awt.Color.WHITE;
            
            if ((z.minus(root2).abs()) <= EPSILON)
                return java.awt.Color.RED;
            
            if ((z.minus(root3).abs()) <= EPSILON)
                return java.awt.Color.GREEN;
            
            if ((z.minus(root4).abs()) <= EPSILON)
                return java.awt.Color.BLUE;
            
        }
        return java.awt.Color.BLACK;
    }
}

