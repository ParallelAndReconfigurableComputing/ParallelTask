package annotatedHouse;


public class Build extends javax.swing.JFrame implements java.awt.event.ActionListener {
    private static final long serialVersionUID = 1L;

    private java.lang.String sequential = "Uh oh! this is sequential (click to change)";

    private java.lang.String concurrent = "Better... This is concurrent (click to change)";

    private java.lang.String parallel = "Great! This is parallel (click to change)";

    private java.lang.String currentJob;

    private boolean isParallel = false;

    private long startTime;

    private javax.swing.JButton btnBuild;

    private javax.swing.JButton btnCancel;

    private javax.swing.JButton btnParSeq;

    private javax.swing.JRadioButtonMenuItem level_1 = new javax.swing.JRadioButtonMenuItem("Low");

    private javax.swing.JRadioButtonMenuItem level_2 = new javax.swing.JRadioButtonMenuItem("Medium");

    private javax.swing.JRadioButtonMenuItem level_3 = new javax.swing.JRadioButtonMenuItem("High");

    private javax.swing.JButton btnRoof;

    private javax.swing.JButton btnWalls;

    private java.awt.Color colorRoof = java.awt.Color.BLUE;

    private java.awt.Color colorWalls = java.awt.Color.YELLOW;

    private annotatedHouse.House houseApplet;

    public Build(java.lang.String name) {
        super(name);
        createGUI();
    }

    private void finishedBuilding() {
        btnBuild.setEnabled(true);
        long time = (java.lang.System.currentTimeMillis()) - (startTime);
        java.lang.System.out.println(((((currentJob) + " time: ") + time) + " ms"));
    }

    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (level_1.isSelected())
            houseApplet.setCompletionLevel(10);
        else if (level_2.isSelected())
            houseApplet.setCompletionLevel(28);
        else if (level_3.isSelected())
            houseApplet.setCompletionLevel(50);
        
        btnBuild.setEnabled(false);
        houseApplet.reset();
        startTime = java.lang.System.currentTimeMillis();
        if (btnParSeq.getText().equals(sequential)) {
            currentJob = "Sequential";
            houseApplet.build(colorWalls, colorRoof);
            finishedBuilding();
        } else if (btnParSeq.getText().equals(concurrent)) {
            currentJob = "Concurrent";
            pt.runtime.TaskInfoTwoArgs<Void, java.awt.Color, java.awt.Color> __taskPtTask__ = ((pt.runtime.TaskInfoTwoArgs<Void, java.awt.Color, java.awt.Color>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, 
			(pt.functionalInterfaces.FunctorTwoArgsNoReturn<java.awt.Color, java.awt.Color>)(__colorRoofPtNonLambdaArg__, __colorWallsPtNonLambdaArg__) -> houseApplet.buildSingleTask(__colorWallsPtNonLambdaArg__, __colorRoofPtNonLambdaArg__))));
            pt.runtime.ParaTask.registerSlotToNotify(__taskPtTask__, ()->finishedBuilding());
            pt.runtime.TaskID<Void> __taskPtTaskID__ = __taskPtTask__.start(colorRoof, colorWalls);
        } else {
            currentJob = "Parallel";
            pt.runtime.ParaTask.setSchedulingType(pt.runtime.ParaTask.PTSchedulingType.WorkSharing);
            pt.runtime.TaskInfoTwoArgs<Void, java.awt.Color, java.awt.Color> __taskPtTask__ = ((pt.runtime.TaskInfoTwoArgs<Void, java.awt.Color, java.awt.Color>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, 
			(pt.functionalInterfaces.FunctorTwoArgsNoReturn<java.awt.Color, java.awt.Color>)(__colorRoofPtNonLambdaArg__, __colorWallsPtNonLambdaArg__) -> houseApplet.buildTask(__colorWallsPtNonLambdaArg__, __colorRoofPtNonLambdaArg__))));
            pt.runtime.ParaTask.registerSlotToNotify(__taskPtTask__, ()->finishedBuilding());
            pt.runtime.TaskID<Void> __taskPtTaskID__ = __taskPtTask__.start(colorRoof, colorWalls);
        }
    }

    private void createGUI() {
        javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu menu = new javax.swing.JMenu("Computation Level");
        javax.swing.ButtonGroup levelGroup = new javax.swing.ButtonGroup();
        level_2.setSelected(true);
        levelGroup.add(level_1);
        levelGroup.add(level_2);
        levelGroup.add(level_3);
        menu.add(level_1);
        menu.add(level_2);
        menu.add(level_3);
        menuBar.add(menu);
        setJMenuBar(menuBar);
        int width = 200;
        int height = 80;
        btnRoof = new javax.swing.JButton("Select...");
        btnRoof.addActionListener((java.awt.event.ActionEvent e) -> {
            java.awt.Color c = javax.swing.JColorChooser.showDialog(annotatedHouse.Build.this, "Select a colour for the roof", colorRoof);
            if (c != null) {
                colorRoof = c;
                btnRoof.setBackground(colorRoof);
            } 
        });
        btnWalls = new javax.swing.JButton("Select...");
        btnWalls.addActionListener((java.awt.event.ActionEvent e) -> {
            java.awt.Color c = javax.swing.JColorChooser.showDialog(annotatedHouse.Build.this, "Select a colour for the walls", colorWalls);
            if (c != null) {
                colorWalls = c;
                btnWalls.setBackground(colorWalls);
            } 
        });
        btnBuild = new javax.swing.JButton("Build!");
        btnBuild.setPreferredSize(new java.awt.Dimension(width , height));
        btnBuild.addActionListener(annotatedHouse.Build.this);
        btnCancel = new javax.swing.JButton("Clear");
        btnCancel.setPreferredSize(new java.awt.Dimension(width , height));
        btnCancel.addActionListener((java.awt.event.ActionEvent e) -> houseApplet.reset());
        javax.swing.JPanel panelRoof = new javax.swing.JPanel(new java.awt.GridLayout(1 , 2));
        panelRoof.add(new javax.swing.JLabel("Roof color:" , javax.swing.JLabel.CENTER));
        btnRoof.setBackground(colorRoof);
        panelRoof.add(btnRoof);
        panelRoof.setPreferredSize(new java.awt.Dimension(width , height));
        javax.swing.JPanel panelWalls = new javax.swing.JPanel(new java.awt.GridLayout(1 , 2));
        panelWalls.add(new javax.swing.JLabel("Wall color:" , javax.swing.JLabel.CENTER));
        btnWalls.setBackground(colorWalls);
        panelWalls.add(btnWalls);
        panelWalls.setPreferredSize(new java.awt.Dimension(width , height));
        javax.swing.JPanel panelLeft = new javax.swing.JPanel();
        panelLeft.setLayout(new java.awt.GridLayout(4 , 1));
        panelLeft.add(panelRoof);
        panelLeft.add(panelWalls);
        panelLeft.add(btnBuild);
        panelLeft.add(btnCancel);
        getContentPane().add(panelLeft, java.awt.BorderLayout.WEST);
        if (isParallel) {
            btnParSeq = new javax.swing.JButton("switchToSequential");
            btnParSeq.setBackground(new java.awt.Color(0 , 153 , 0));
        } else {
            btnParSeq = new javax.swing.JButton(sequential);
            btnParSeq.setBackground(java.awt.Color.RED);
        }
        java.awt.Font f = btnParSeq.getFont();
        btnParSeq.setFont(new java.awt.Font(f.getName() , java.awt.Font.BOLD , 25));
        btnParSeq.addActionListener((java.awt.event.ActionEvent e) -> {
            isParallel = !(isParallel);
            if (btnParSeq.getText().equals(sequential)) {
                btnParSeq.setText(concurrent);
                btnParSeq.setBackground(java.awt.Color.ORANGE);
            } else if (btnParSeq.getText().equals(concurrent)) {
                btnParSeq.setText(parallel);
                btnParSeq.setBackground(java.awt.Color.GREEN);
            } else {
                btnParSeq.setText(sequential);
                btnParSeq.setBackground(java.awt.Color.RED);
            }
        });
        btnParSeq.setPreferredSize(new java.awt.Dimension(width , height));
        getContentPane().add(btnParSeq, java.awt.BorderLayout.SOUTH);
        java.awt.Toolkit tk = java.awt.Toolkit.getDefaultToolkit();
        java.awt.Dimension screenSize = tk.getScreenSize();
        int screenWidth = ((int)(screenSize.getWidth()));
        int screenHeight = ((int)(screenSize.getHeight()));
        setLocation((screenWidth / 4), (screenHeight / 4));
        houseApplet = new annotatedHouse.House(annotatedHouse.Build.this);
        getContentPane().add(houseApplet, java.awt.BorderLayout.CENTER);
    }

    public static void main(java.lang.String[] args) {
        pt.runtime.ParaTask.init();
        annotatedHouse.Build app = new annotatedHouse.Build("Build a house");
        app.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        app.setResizable(false);
        app.pack();
        app.setVisible(true);
    }
}

