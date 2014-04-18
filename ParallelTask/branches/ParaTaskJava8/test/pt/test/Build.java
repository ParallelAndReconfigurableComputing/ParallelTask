package pt.test;//####[1]####
//####[1]####
import java.awt.BorderLayout;//####[3]####
import java.awt.Color;//####[4]####
import java.awt.Dimension;//####[5]####
import java.awt.Font;//####[6]####
import java.awt.GridLayout;//####[7]####
import java.awt.Toolkit;//####[8]####
import java.awt.event.ActionEvent;//####[9]####
import java.awt.event.ActionListener;//####[10]####
import javax.swing.ButtonGroup;//####[12]####
import javax.swing.JButton;//####[13]####
import javax.swing.JColorChooser;//####[14]####
import javax.swing.JFrame;//####[15]####
import javax.swing.JLabel;//####[16]####
import javax.swing.JMenu;//####[17]####
import javax.swing.JMenuBar;//####[18]####
import javax.swing.JPanel;//####[19]####
import javax.swing.JRadioButtonMenuItem;//####[20]####
//####[20]####
//-- ParaTask related imports//####[20]####
import pt.runtime.*;//####[20]####
import java.util.concurrent.ExecutionException;//####[20]####
import java.util.concurrent.locks.*;//####[20]####
import java.lang.reflect.*;//####[20]####
import pt.runtime.GuiThread;//####[20]####
import java.util.concurrent.BlockingQueue;//####[20]####
import java.util.ArrayList;//####[20]####
import java.util.List;//####[20]####
//####[20]####
public class Build extends JFrame {//####[22]####
    static{ParaTask.init();}//####[22]####
    /*  ParaTask helper method to access private/protected slots *///####[22]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[22]####
        if (m.getParameterTypes().length == 0)//####[22]####
            m.invoke(instance);//####[22]####
        else if ((m.getParameterTypes().length == 1))//####[22]####
            m.invoke(instance, arg);//####[22]####
        else //####[22]####
            m.invoke(instance, arg, interResult);//####[22]####
    }//####[22]####
//####[24]####
    private String switchToParallel = "Uh oh! This is sequential (click to change)";//####[24]####
//####[25]####
    private String switchToSequential = "Great! This is parallel (click to change)";//####[25]####
//####[27]####
    private JButton btnBuild;//####[27]####
//####[28]####
    private JButton btnCancel;//####[28]####
//####[29]####
    private JButton btnParSeq;//####[29]####
//####[30]####
    private boolean isParallel = false;//####[30]####
//####[32]####
    private JRadioButtonMenuItem level_1 = new JRadioButtonMenuItem("Low");//####[32]####
//####[33]####
    private JRadioButtonMenuItem level_2 = new JRadioButtonMenuItem("Medium");//####[33]####
//####[34]####
    private JRadioButtonMenuItem level_3 = new JRadioButtonMenuItem("High");//####[34]####
//####[36]####
    private JButton btnRoof;//####[36]####
//####[37]####
    private JButton btnWalls;//####[37]####
//####[38]####
    private Color colorRoof = Color.BLUE;//####[38]####
//####[39]####
    private Color colorWalls = Color.YELLOW;//####[39]####
//####[41]####
    private House houseApplet;//####[41]####
//####[43]####
    public Build(String name) {//####[43]####
        super(name);//####[44]####
        createGUI();//####[45]####
    }//####[46]####
//####[48]####
    private void finishedBuilding() {//####[48]####
        btnBuild.setEnabled(true);//####[49]####
    }//####[50]####
//####[52]####
    private void createGUI() {//####[52]####
        JMenuBar menuBar = new JMenuBar();//####[55]####
        JMenu menu = new JMenu("Computation Level");//####[56]####
        ButtonGroup levelGroup = new ButtonGroup();//####[58]####
        level_1.setSelected(true);//####[59]####
        levelGroup.add(level_1);//####[60]####
        levelGroup.add(level_2);//####[61]####
        levelGroup.add(level_3);//####[62]####
        menu.add(level_1);//####[64]####
        menu.add(level_2);//####[65]####
        menu.add(level_3);//####[66]####
        menuBar.add(menu);//####[67]####
        setJMenuBar(menuBar);//####[69]####
        int width = 200;//####[72]####
        int height = 80;//####[73]####
        btnRoof = new JButton("Select...");//####[75]####
        btnRoof.addActionListener(new ActionListener() {//####[75]####
//####[78]####
            @Override//####[78]####
            public void actionPerformed(ActionEvent e) {//####[78]####
                Color c = JColorChooser.showDialog(Build.this, "Select a colour for the roof", colorRoof);//####[79]####
                if (c != null) //####[80]####
                {//####[80]####
                    colorRoof = c;//####[81]####
                    btnRoof.setBackground(c);//####[82]####
                }//####[83]####
            }//####[84]####
        });//####[84]####
        btnWalls = new JButton("Select...");//####[86]####
        btnWalls.addActionListener(new ActionListener() {//####[86]####
//####[89]####
            @Override//####[89]####
            public void actionPerformed(ActionEvent e) {//####[89]####
                Color c = JColorChooser.showDialog(Build.this, "Select a colour for the walls", colorWalls);//####[90]####
                if (c != null) //####[91]####
                {//####[91]####
                    colorWalls = c;//####[92]####
                    btnWalls.setBackground(c);//####[93]####
                }//####[94]####
            }//####[95]####
        });//####[95]####
        btnBuild = new JButton("Build!");//####[97]####
        btnBuild.setPreferredSize(new Dimension(width, height));//####[98]####
        btnBuild.addActionListener(new ActionListener() {//####[98]####
//####[101]####
            @Override//####[101]####
            public void actionPerformed(ActionEvent arg0) {//####[101]####
                if (level_1.isSelected()) //####[102]####
                houseApplet.setComputationLevel(10); else if (level_2.isSelected()) //####[104]####
                houseApplet.setComputationLevel(25); else if (level_3.isSelected()) //####[106]####
                houseApplet.setComputationLevel(50);//####[107]####
                btnBuild.setEnabled(false);//####[109]####
                houseApplet.reset();//####[110]####
                if (isParallel) //####[114]####
                {//####[114]####
                    TaskInfo __pt__id = new TaskInfo();//####[115]####
//####[115]####
                    boolean isEDT = GuiThread.isEventDispatchThread();//####[115]####
//####[115]####
//####[115]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[115]####
                    try {//####[115]####
                        Method __pt__id_slot_0 = null;//####[115]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(Build.this.getClass(), "finishedBuilding", new Class[] {});//####[115]####
                        if (false) Build.this.finishedBuilding(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[115]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, Build.this, false));//####[115]####
//####[115]####
                    } catch(Exception __pt__e) { //####[115]####
                        System.err.println("Problem registering method in clause:");//####[115]####
                        __pt__e.printStackTrace();//####[115]####
                    }//####[115]####
                    TaskID id = houseApplet.buildTask(colorWalls, colorRoof, __pt__id);//####[115]####
                } else {//####[116]####
                    houseApplet.build(colorWalls, colorRoof);//####[117]####
                    Build.this.finishedBuilding();//####[118]####
                }//####[119]####
            }//####[120]####
        });//####[120]####
        btnCancel = new JButton("Clear");//####[122]####
        btnCancel.setPreferredSize(new Dimension(width, height));//####[123]####
        btnCancel.addActionListener(new ActionListener() {//####[123]####
//####[126]####
            @Override//####[126]####
            public void actionPerformed(ActionEvent arg0) {//####[126]####
                houseApplet.reset();//####[127]####
            }//####[128]####
        });//####[128]####
        JPanel panelRoof = new JPanel(new GridLayout(1, 2));//####[131]####
        panelRoof.add(new JLabel("Roof color:", JLabel.CENTER));//####[132]####
        btnRoof.setBackground(colorRoof);//####[133]####
        panelRoof.add(btnRoof);//####[134]####
        panelRoof.setPreferredSize(new Dimension(width, height));//####[135]####
        JPanel panelWalls = new JPanel(new GridLayout(1, 2));//####[137]####
        panelWalls.add(new JLabel("Wall color:", JLabel.CENTER));//####[138]####
        btnWalls.setBackground(colorWalls);//####[139]####
        panelWalls.add(btnWalls);//####[140]####
        panelWalls.setPreferredSize(new Dimension(width, height));//####[141]####
        JPanel panelLeft = new JPanel();//####[143]####
        panelLeft.setLayout(new GridLayout(4, 1));//####[144]####
        panelLeft.add(panelRoof);//####[145]####
        panelLeft.add(panelWalls);//####[146]####
        panelLeft.add(btnBuild);//####[147]####
        panelLeft.add(btnCancel);//####[148]####
        getContentPane().add(panelLeft, BorderLayout.WEST);//####[150]####
        if (isParallel) //####[153]####
        {//####[153]####
            btnParSeq = new JButton(switchToSequential);//####[154]####
            btnParSeq.setBackground(new Color(0, 153, 0));//####[155]####
        } else {//####[156]####
            btnParSeq = new JButton(switchToParallel);//####[157]####
            btnParSeq.setBackground(new Color(255, 153, 0));//####[158]####
        }//####[159]####
        Font f = btnParSeq.getFont();//####[160]####
        btnParSeq.setFont(new Font(f.getName(), Font.BOLD, 25));//####[161]####
        btnParSeq.addActionListener(new ActionListener() {//####[161]####
//####[164]####
            @Override//####[164]####
            public void actionPerformed(ActionEvent arg0) {//####[164]####
                isParallel = !isParallel;//####[165]####
                if (isParallel) //####[166]####
                {//####[166]####
                    btnParSeq.setText(switchToSequential);//####[167]####
                    btnParSeq.setBackground(new Color(0, 153, 0));//####[168]####
                } else {//####[169]####
                    btnParSeq.setText(switchToParallel);//####[170]####
                    btnParSeq.setBackground(new Color(255, 153, 0));//####[171]####
                }//####[172]####
            }//####[173]####
        });//####[173]####
        btnParSeq.setPreferredSize(new Dimension(width, height));//####[175]####
        getContentPane().add(btnParSeq, BorderLayout.SOUTH);//####[176]####
        Toolkit tk = Toolkit.getDefaultToolkit();//####[179]####
        Dimension screenSize = tk.getScreenSize();//####[180]####
        int screenHeight = screenSize.height;//####[181]####
        int screenWidth = screenSize.width;//####[182]####
        setLocation(screenWidth / 4, screenHeight / 4);//####[183]####
        houseApplet = new House(this);//####[185]####
        getContentPane().add(houseApplet, BorderLayout.CENTER);//####[186]####
    }//####[187]####
//####[189]####
    public static void main(String[] args) {//####[189]####
        ParaTask.init();//####[190]####
        Build app = new Build("Build a house");//####[191]####
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//####[192]####
        app.setResizable(false);//####[193]####
        app.pack();//####[194]####
        app.setVisible(true);//####[195]####
    }//####[196]####
}//####[196]####
