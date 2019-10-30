
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author patel
 */
public class TypingTestScreen extends javax.swing.JFrame {

    private int backspaceCount = 0;
    private int time_duration = 30;
    Timer timer;
    boolean isTestStarted = false;
    String text_to_show = "";
    int index_text_to_show = 0;
    String previewString = "";
    int fontSize;
    List<String> rightWordsList, wrongWordsList;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    /**
     * Creates new form TypingTestScreen
     *
     * @param ExcerciseTitle
     * @param preview_string
     * @param time_duration
     */
    public TypingTestScreen(String ExcerciseTitle, String preview_string, int time_duration) {
        setTitle("Typing Test");
        previewString = preview_string;

        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        disableKeys(typingTextArea.getInputMap());

        /*
        Dimension dmnsn = Toolkit.getDefaultToolkit().getScreenSize();

        setBounds(0, 0, dmnsn.width, dmnsn.height);
        setExtendedState(MAXIMIZED_BOTH);
        MainContainerPanel.setBounds(getX(), getY(),getWidth(),getHeight()-30);
        LeftPanel.setSize(MainContainerPanel.getWidth() - RightPanel.getWidth() - 30, MainContainerPanel.getHeight() - 50);
        LeftPanel.setBounds(getX()+10, getY()+30, MainContainerPanel.getWidth() - RightPanel.getWidth() - 30, MainContainerPanel.getHeight() - 50);
        PreviewScrollPane.setBounds(LeftPanel.getX(),LeftPanel.getY(), LeftPanel.getWidth(), LeftPanel.getHeight() / 2 - 5);
        previewEditorPane.setBounds(LeftPanel.getX(),LeftPanel.getY(), LeftPanel.getWidth(), LeftPanel.getHeight() / 2 - 5);
        TypingScrollPane.setBounds(LeftPanel.getX(),LeftPanel.getY()+ PreviewScrollPane.getHeight()+5, LeftPanel.getWidth(), LeftPanel.getHeight() / 2 - 5);
        TypingEditorPane.setBounds(LeftPanel.getX(),LeftPanel.getY()+ PreviewScrollPane.getHeight()+5, LeftPanel.getWidth(), LeftPanel.getHeight() / 2 - 5);

        //pack();
         */
        titleLabel.setText(ExcerciseTitle);
        ResultPanel.setEnabled(false);
        ResultPanel.setVisible(false);

        previewTextArea.setText(previewString);
        previewTextArea.setCaretPosition(0);

        Font font = previewTextArea.getFont();
        fontSize = font.getSize();
        //typingTextArea.setText(font.getName());

        KeyAdapter keyAdapter = new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                typingTextArea.append(evt.getKeyChar() + "");
                if (isTestStarted) {
                    if (evt.getKeyCode() == 8 && !typingTextArea.getText().isEmpty()) {
                        String s = typingTextArea.getText();
                        if (!s.substring(
                                s.length() - 1)
                                .equals(" ")) {
                            backspaceCount++;
                            typedTextSize--;
                            try {
                                typingTextArea.setText(typingTextArea.getText(0, typingTextArea.getText().length() - 1));
                            } catch (BadLocationException ex) {
                                Logger.getLogger(TypingTestScreen.class.getName()).log(Level.SEVERE, null, ex);
                                JOptionPane.showMessageDialog(TypingTestScreen.this, "Some problem occured!");
                            }
                        }

                    }
                }
                evt.consume();
            }

            public void keyReleased(java.awt.event.KeyEvent evt) {
                evt.consume();
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                evt.consume();
            }
        };

        previewTextArea.addKeyListener(keyAdapter);
        MainContainerPanel.addKeyListener(keyAdapter);
        addKeyListener(keyAdapter);

        this.time_duration = time_duration;

        if (time_duration < 10) {
            timerMinuteLabel.setText("0" + time_duration);
        } else {
            timerMinuteLabel.setText("" + time_duration);
        }
        timerSecondLabel.setText("00");

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (Integer.parseInt(timerMinuteLabel.getText()) == 0
                        && Integer.parseInt(timerSecondLabel.getText()) == 0) {

                    //JOptionPane.showMessageDialog(TypingTestScreen.this, "Your "+time_duration+" minutes are ended now. Return with a new Excercise.", "Times Up!", JOptionPane.WARNING_MESSAGE);
                    submitTest();
                } else if (Integer.parseInt(timerSecondLabel.getText()) == 0) {
                    if (Integer.parseInt(timerMinuteLabel.getText()) <= 10) {
                        timerMinuteLabel.setText("0" + (Integer.parseInt(timerMinuteLabel.getText()) - 1));
                    } else {
                        timerMinuteLabel.setText("" + (Integer.parseInt(timerMinuteLabel.getText()) - 1));
                    }
                    timerSecondLabel.setText("" + 59);
                } else if (Integer.parseInt(timerSecondLabel.getText()) <= 10) {
                    timerSecondLabel.setText("0" + (Integer.parseInt(timerSecondLabel.getText()) - 1));
                } else {
                    timerSecondLabel.setText("" + (Integer.parseInt(timerSecondLabel.getText()) - 1));
                }
            }
        });

    }

    private void submitTest() {
        timer.stop();

        int remaining_time_minute = Integer.parseInt(timerMinuteLabel.getText());

        typingTextArea.setEditable(false);
        ResultPanel.setEnabled(true);
        ResultPanel.setVisible(true);
        SubmitTestButton.setEnabled(false);
        SubmitTestButton.setSize(20, 20);

        // Enable it to use origional word count
        /*
        String totalWordsArray[] = new String[previewString.split(" ").length];
        int indexTotalWordsArray = 0;
        for (String s : previewString.split(" ")) {
            if (!s.isEmpty()) {
                totalWordsArray[indexTotalWordsArray++] = s;
            }
        }

        int totalWords = indexTotalWordsArray;
        totalWordsLabel.setText(Integer.toString(totalWords));

        String typedWordsArray[] = new String[TypingEditorPane.getText().split(" ").length];
        int indexTypedStringArray = 0;
        for (String s : TypingEditorPane.getText().split(" ")) {
            if (!s.isEmpty()) {
                typedWordsArray[indexTypedStringArray++] = s;
            }
        }
         */
        previewTextArea.setCaretPosition(0);

        String[] totalWordsArray = previewString.split(" ");
        String[] typedWordsArray;
        typedWordsArray = typingTextArea.getText().split(" ");

        int typedWords = typedWordsArray.length;
        int totalWords = totalWordsArray.length;

        totalWordsLabel.setText(Integer.toString(totalWords));
        typedWordsLabel.setText("" + typedWords);

        int remainingWords = totalWords - typedWords;
        RemainingWordsLabel.setText(Integer.toString(remainingWords));

        backspaceLabel.setText("" + backspaceCount);

        rightWordsList = new ArrayList<>();
        wrongWordsList = new ArrayList<>();
        int wrong_words = 0;
        int right_words = 0;
        int smallWordSet;
        if (totalWords <= typedWords) {
            smallWordSet = totalWords;
        } else {
            smallWordSet = typedWords;
        }
        for (int i = 0; i < smallWordSet; i++) {
            if (totalWordsArray[i].trim().equals(typedWordsArray[i].trim())) {

                right_words++;
            } else {
                rightWordsList.add(totalWordsArray[i]);
                wrongWordsList.add(typedWordsArray[i]);
                wrong_words++;
            }
        }

        rightWordsLabel.setText("" + right_words);
        wrongWordsLabel.setText("" + wrong_words);

        int accuracy = right_words * 100 / totalWords;
        accuracyLabel.setText(accuracy + "%");

        int speed = typedWords / time_duration - remaining_time_minute;
        speedLabel.setText("" + speed);

        ShowList showList = new ShowList(rightWordsList, wrongWordsList);
        showList.setVisible(true);
        showList.setAlwaysOnTop(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainContainerPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        LeftPanel = new javax.swing.JPanel();
        TypingScrollPaneNew = new javax.swing.JScrollPane();
        typingTextArea = new javax.swing.JTextArea();
        PreviewScrollPaneNew = new javax.swing.JScrollPane();
        previewTextArea = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        timerSecondLabel = new javax.swing.JLabel();
        timerMinuteLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        DecreaseTextSizeButton = new javax.swing.JButton();
        IncreaseTextSizeButton = new javax.swing.JButton();
        ResultPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        totalWordsLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        RemainingWordsLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        backspaceLabel = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        wrongWordsLabel = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        rightWordsLabel = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        accuracyLabel = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        speedLabel = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        typedWordsLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        SubmitTestButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);

        MainContainerPanel.setBackground(new java.awt.Color(0, 0, 255));
        MainContainerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 51, 255)));
        MainContainerPanel.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        MainContainerPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                MainContainerPanelMouseDragged(evt);
            }
        });
        MainContainerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                MainContainerPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                MainContainerPanelMouseReleased(evt);
            }
        });

        titleLabel.setBackground(new java.awt.Color(255, 255, 255));
        titleLabel.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(102, 102, 102));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Excercise Title");
        titleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        titleLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        titleLabel.setOpaque(true);

        typingTextArea.setColumns(20);
        typingTextArea.setFont(new java.awt.Font("Arial Unicode MS", 0, 22)); // NOI18N
        typingTextArea.setLineWrap(true);
        typingTextArea.setRows(5);
        typingTextArea.setWrapStyleWord(true);
        typingTextArea.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                typingTextAreaCaretUpdate(evt);
            }
        });
        typingTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                typingTextAreaKeyPressed(evt);
            }
        });
        TypingScrollPaneNew.setViewportView(typingTextArea);

        previewTextArea.setBackground(new java.awt.Color(255, 255, 204));
        previewTextArea.setColumns(20);
        previewTextArea.setFont(new java.awt.Font("Arial Unicode MS", 0, 22)); // NOI18N
        previewTextArea.setForeground(new java.awt.Color(102, 102, 0));
        previewTextArea.setLineWrap(true);
        previewTextArea.setRows(5);
        previewTextArea.setWrapStyleWord(true);
        PreviewScrollPaneNew.setViewportView(previewTextArea);

        javax.swing.GroupLayout LeftPanelLayout = new javax.swing.GroupLayout(LeftPanel);
        LeftPanel.setLayout(LeftPanelLayout);
        LeftPanelLayout.setHorizontalGroup(
            LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TypingScrollPaneNew, javax.swing.GroupLayout.DEFAULT_SIZE, 1142, Short.MAX_VALUE)
            .addComponent(PreviewScrollPaneNew)
        );
        LeftPanelLayout.setVerticalGroup(
            LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LeftPanelLayout.createSequentialGroup()
                .addComponent(PreviewScrollPaneNew, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(TypingScrollPaneNew, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102))
        );

        jPanel4.setOpaque(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 241, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 608, Short.MAX_VALUE)
        );

        jPanel2.setOpaque(false);

        jLabel8.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 51, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText(":");

        timerSecondLabel.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        timerSecondLabel.setForeground(new java.awt.Color(255, 51, 0));
        timerSecondLabel.setText("00");

        timerMinuteLabel.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        timerMinuteLabel.setForeground(new java.awt.Color(255, 51, 0));
        timerMinuteLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        timerMinuteLabel.setText("10");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(timerMinuteLabel)
                .addGap(0, 0, 0)
                .addComponent(jLabel8)
                .addGap(0, 0, 0)
                .addComponent(timerSecondLabel))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(timerSecondLabel)
                    .addComponent(timerMinuteLabel))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1.setOpaque(false);

        DecreaseTextSizeButton.setBackground(new java.awt.Color(255, 204, 204));
        DecreaseTextSizeButton.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        DecreaseTextSizeButton.setForeground(new java.awt.Color(204, 0, 0));
        DecreaseTextSizeButton.setText("A-");
        DecreaseTextSizeButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        DecreaseTextSizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DecreaseTextSizeButtonActionPerformed(evt);
            }
        });

        IncreaseTextSizeButton.setBackground(new java.awt.Color(255, 204, 204));
        IncreaseTextSizeButton.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        IncreaseTextSizeButton.setForeground(new java.awt.Color(204, 0, 0));
        IncreaseTextSizeButton.setText("A+");
        IncreaseTextSizeButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        IncreaseTextSizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IncreaseTextSizeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(IncreaseTextSizeButton)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(DecreaseTextSizeButton))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DecreaseTextSizeButton)
                    .addComponent(IncreaseTextSizeButton)))
        );

        ResultPanel.setBackground(new java.awt.Color(204, 255, 255));
        ResultPanel.setForeground(new java.awt.Color(0, 0, 204));
        ResultPanel.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabel5.setText("Total Words:");

        totalWordsLabel.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        totalWordsLabel.setText("30");

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabel7.setText("Remaining Words:");

        RemainingWordsLabel.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        RemainingWordsLabel.setText("30");

        jLabel9.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabel9.setText("Backspace:");

        backspaceLabel.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        backspaceLabel.setText("0");

        jLabel11.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabel11.setText("Wrong Words:");

        wrongWordsLabel.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        wrongWordsLabel.setText("0");

        jLabel13.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabel13.setText("Right Words");

        rightWordsLabel.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        rightWordsLabel.setText("0");

        jLabel15.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabel15.setText("Accuracy:");

        accuracyLabel.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        accuracyLabel.setText("100%");

        jLabel17.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabel17.setText("Speed:");

        speedLabel.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        speedLabel.setText("0");

        jLabel19.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabel19.setText("wpm");

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabel6.setText("Words Typed:");

        typedWordsLabel.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        typedWordsLabel.setText("0");

        jLabel1.setFont(new java.awt.Font("Calibri", 2, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setText("Show wrong words list.");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ResultPanelLayout = new javax.swing.GroupLayout(ResultPanel);
        ResultPanel.setLayout(ResultPanelLayout);
        ResultPanelLayout.setHorizontalGroup(
            ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ResultPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ResultPanelLayout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(speedLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19))
                    .addGroup(ResultPanelLayout.createSequentialGroup()
                        .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(ResultPanelLayout.createSequentialGroup()
                                .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(accuracyLabel)
                                    .addComponent(typedWordsLabel)
                                    .addComponent(totalWordsLabel)
                                    .addComponent(rightWordsLabel)
                                    .addComponent(wrongWordsLabel)
                                    .addComponent(backspaceLabel)
                                    .addComponent(RemainingWordsLabel))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        ResultPanelLayout.setVerticalGroup(
            ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ResultPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(totalWordsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(typedWordsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(RemainingWordsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(backspaceLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(wrongWordsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(rightWordsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(accuracyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(speedLabel)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(0, 0, 255));

        SubmitTestButton.setBackground(new java.awt.Color(255, 0, 0));
        SubmitTestButton.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        SubmitTestButton.setForeground(new java.awt.Color(255, 255, 255));
        SubmitTestButton.setText("Submit");
        SubmitTestButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SubmitTestButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        SubmitTestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubmitTestButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SubmitTestButton, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(SubmitTestButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout MainContainerPanelLayout = new javax.swing.GroupLayout(MainContainerPanel);
        MainContainerPanel.setLayout(MainContainerPanelLayout);
        MainContainerPanelLayout.setHorizontalGroup(
            MainContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainContainerPanelLayout.createSequentialGroup()
                .addGroup(MainContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(MainContainerPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(LeftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(MainContainerPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(113, 113, 113)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addGroup(MainContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainContainerPanelLayout.createSequentialGroup()
                        .addGap(304, 304, 304)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ResultPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        MainContainerPanelLayout.setVerticalGroup(
            MainContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainContainerPanelLayout.createSequentialGroup()
                .addGroup(MainContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainContainerPanelLayout.createSequentialGroup()
                        .addGroup(MainContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(MainContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, 0)
                        .addComponent(LeftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(MainContainerPanelLayout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(ResultPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(MainContainerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(MainContainerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void SubmitTestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubmitTestButtonActionPerformed
        submitTest();
    }//GEN-LAST:event_SubmitTestButtonActionPerformed
    private void disableKeys(InputMap inputMap) {
        String[] keys = {"UP", "DOWN", "LEFT", "DOWN", "TAB"};
        for (String key : keys) {
            inputMap.put(KeyStroke.getKeyStroke(key), "none");
        }
    }

    private void IncreaseTextSizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IncreaseTextSizeButtonActionPerformed

        if (fontSize < 36) {
            fontSize = fontSize + 2;
            previewTextArea.setFont(new java.awt.Font("Arial Unicode MS", Font.PLAIN, fontSize));
            typingTextArea.setFont(new java.awt.Font("Arial Unicode MS", Font.PLAIN, fontSize));
        }
    }//GEN-LAST:event_IncreaseTextSizeButtonActionPerformed

    private void DecreaseTextSizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DecreaseTextSizeButtonActionPerformed
        if (fontSize > 14) {
            fontSize = fontSize - 2;
            previewTextArea.setFont(new java.awt.Font("Arial Unicode MS", Font.PLAIN, fontSize));
            typingTextArea.setFont(new java.awt.Font("Arial Unicode MS", Font.PLAIN, fontSize));
        }
    }//GEN-LAST:event_DecreaseTextSizeButtonActionPerformed

    int caretPosition = 0;
    boolean lockMouse = false;
    int mouseDraggedX, mouseDraggedY;
    private void MainContainerPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MainContainerPanelMouseDragged
        if (lockMouse) {
            setBounds(evt.getXOnScreen() - mouseDraggedX, evt.getYOnScreen() - mouseDraggedY, getWidth(), getHeight());
        }
    }//GEN-LAST:event_MainContainerPanelMouseDragged

    private void MainContainerPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MainContainerPanelMousePressed
        mouseDraggedX = evt.getX();
        mouseDraggedY = evt.getY();
        lockMouse = true;
    }//GEN-LAST:event_MainContainerPanelMousePressed

    private void MainContainerPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MainContainerPanelMouseReleased
        lockMouse = false;
    }//GEN-LAST:event_MainContainerPanelMouseReleased

    private int typedTextSize = 0;
    private void typingTextAreaCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_typingTextAreaCaretUpdate

        if (evt.getDot() >= typedTextSize) {
            typedTextSize = evt.getDot();
        } else {
            typingTextArea.setCaretPosition(typedTextSize);
        }

        if (!isTestStarted && typingTextArea.getCaretPosition() > 0) {
            timer.start();
            isTestStarted = true;
        }
        previewTextArea.setCaretPosition(typingTextArea.getCaretPosition());
    }//GEN-LAST:event_typingTextAreaCaretUpdate

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked

        ShowList showList = new ShowList(rightWordsList, wrongWordsList);
        showList.setVisible(true);
        showList.setAlwaysOnTop(true);
    }//GEN-LAST:event_jLabel1MouseClicked

    private void typingTextAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_typingTextAreaKeyPressed

        if (isTestStarted) {
            if (evt.getKeyCode() == 8) {
                String s = typingTextArea.getText();
                if (s.substring(
                        s.length() - 1)
                        .equals(" ")) {
                    evt.consume();
                } else {
                    backspaceCount++;
                    typedTextSize--;
                }

            }
        }
    }//GEN-LAST:event_typingTextAreaKeyPressed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DecreaseTextSizeButton;
    private javax.swing.JButton IncreaseTextSizeButton;
    private javax.swing.JPanel LeftPanel;
    public javax.swing.JPanel MainContainerPanel;
    private javax.swing.JScrollPane PreviewScrollPaneNew;
    private javax.swing.JLabel RemainingWordsLabel;
    private javax.swing.JPanel ResultPanel;
    private javax.swing.JButton SubmitTestButton;
    private javax.swing.JScrollPane TypingScrollPaneNew;
    private javax.swing.JLabel accuracyLabel;
    private javax.swing.JLabel backspaceLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextArea previewTextArea;
    private javax.swing.JLabel rightWordsLabel;
    private javax.swing.JLabel speedLabel;
    private javax.swing.JLabel timerMinuteLabel;
    private javax.swing.JLabel timerSecondLabel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel totalWordsLabel;
    private javax.swing.JLabel typedWordsLabel;
    private javax.swing.JTextArea typingTextArea;
    private javax.swing.JLabel wrongWordsLabel;
    // End of variables declaration//GEN-END:variables

}
