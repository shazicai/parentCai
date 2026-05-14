package com.spc.springweb.jframe.moyu;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author caiJH
 * @Date 2026/1/19 9:41 AM
 * @Version 1.0
 */
public class TaskResetCountdownTimer extends JFrame {
    private JTable timerTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JTextField nameField;
    private JSpinner hourSpinner;
    private JSpinner minuteSpinner;
    private JSpinner secondSpinner;
    private JLabel activeLabel;
    private JLabel sortInfoLabel;
    private JCheckBox autoSortCheckBox;
    private JButton closeAllDialogsButton;

    private java.util.List<TimerTask> timerTasks;
    private Timer updateTimer;
    private boolean autoSortEnabled = true;
    private int lastSelectedRow = -1;

    // 存储已经显示过完成提示的任务ID
    private Map<Integer, Boolean> completedTasksShown = new HashMap<>();
    // 存储完成对话框的引用
    private Map<Integer, JDialog> completionDialogs = new HashMap<>();
    // 存储每行的按钮组件
    private Map<Integer, JButton> startButtonMap = new HashMap<>();
    private Map<Integer, JButton> resetButtonMap = new HashMap<>();

    // 表头
    private final String[] COLUMN_NAMES = {"任务名称", "总时间", "剩余时间", "状态", "操作", "重置", "剩余(秒)"};

    // 颜色定义
    private final Color NORMAL_COLOR = Color.WHITE;
    private final Color WARNING_COLOR = Color.YELLOW;  // <60秒
    private final Color CRITICAL_COLOR = Color.RED;    // <10秒
    private final Color RUNNING_COLOR = new Color(220, 255, 220); // 运行中的任务背景
    private final Color FINISHED_COLOR = new Color(255, 182, 193); // 完成
    private final Color PAUSED_COLOR = new Color(240, 240, 240);  // 暂停
    private final Color SELECTED_COLOR = new Color(180, 220, 255); // 选中行背景

    // 按钮颜色
    private final Color START_BUTTON_COLOR = new Color(34, 139, 34); // 开始按钮颜色
    private final Color PAUSE_BUTTON_COLOR = new Color(218, 165, 32); // 暂停按钮颜色
    private final Color RESET_BUTTON_COLOR = new Color(70, 130, 180); // 重置按钮颜色
    private final Color RESTART_BUTTON_COLOR = new Color(138, 43, 226); // 重新开始按钮颜色
    private final Color DELETE_BUTTON_COLOR = new Color(178, 34, 34); // 删除按钮颜色

    public TaskResetCountdownTimer() {
        setTitle("倒计时管理器 - 独立重置按钮");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        timerTasks = new ArrayList<>();
        initComponents();
        setupUpdateTimer();

        setVisible(true);
        addTasks();
    }

    // 添加任务逻辑
    private static java.util.List<TimerTaskDate> timerTaskDates = new ArrayList<>();
    private static java.util.List<String> timerTaskNames = new ArrayList<>();
    static {
        timerTaskDates.add(new TimerTaskDate("树心28级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("树心29级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("树心30级别BOSS", 0, 40, 0,"精品"));

        timerTaskDates.add(new TimerTaskDate("树心31级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("树心32级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("树心33级别BOSS", 0, 40, 0,"精品"));
        timerTaskNames.add("树心30级别BOSS");
        timerTaskDates.add(new TimerTaskDate("戈壁36级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("戈壁37级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("戈壁38级别BOSS", 0, 40, 0,"精品"));
        timerTaskNames.add("戈壁38级别BOSS");
        timerTaskDates.add(new TimerTaskDate("戈壁41级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("戈壁42级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("戈壁43级别BOSS", 0, 40, 0,"精品"));

        timerTaskDates.add(new TimerTaskDate("戈壁46级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("戈壁47级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("戈壁48级别BOSS", 0, 40, 0,"精品"));
        timerTaskNames.add("戈壁48级别BOSS");
        timerTaskDates.add(new TimerTaskDate("戈壁51级别骑士BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("戈壁52级别骑士BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("戈壁53级别骑士BOSS", 0, 40, 0,"精品"));

        timerTaskDates.add(new TimerTaskDate("戈壁53级别龙BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("戈壁54级别龙BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("戈壁55级别龙BOSS", 0, 40, 0,"精品"));
        timerTaskNames.add("戈壁55级别BOSS");
        timerTaskDates.add(new TimerTaskDate("沼泽56级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("沼泽57级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("沼泽58级别BOSS", 0, 40, 0,"精品"));

        timerTaskDates.add(new TimerTaskDate("沼泽61级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("沼泽62级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("沼泽63级别BOSS", 0, 40, 0,"精品"));

        timerTaskDates.add(new TimerTaskDate("沼泽66级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("沼泽67级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("沼泽68级别BOSS", 0, 40, 0,"精品"));

        timerTaskDates.add(new TimerTaskDate("沼泽71级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("沼泽72级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("沼泽73级别BOSS", 0, 40, 0,"精品"));
        timerTaskNames.add("沼泽73级别BOSS");
        timerTaskDates.add(new TimerTaskDate("海岛71级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("海岛72级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("海岛73级别BOSS", 0, 40, 0,"精品"));

        timerTaskDates.add(new TimerTaskDate("海岛75级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("海岛76级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("海岛77级别BOSS", 0, 40, 0,"精品"));

        timerTaskDates.add(new TimerTaskDate("海岛81级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("海岛82级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("海岛83级别BOSS", 0, 40, 0,"精品"));
        timerTaskNames.add("海岛83级别BOSS");
        timerTaskDates.add(new TimerTaskDate("冰原75级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("冰原76级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("冰原77级别BOSS", 0, 40, 0,"精品"));

        timerTaskDates.add(new TimerTaskDate("冰原81级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("冰原83级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("冰原87级别BOSS", 0, 40, 0,"精品"));

        timerTaskDates.add(new TimerTaskDate("冰原88级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("冰原89级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("冰原90级别BOSS", 0, 40, 0,"精品"));

        timerTaskDates.add(new TimerTaskDate("冰原91级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("冰原92级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("冰原93级别BOSS", 0, 40, 0,"精品"));
        timerTaskNames.add("冰原93级别BOSS");
        timerTaskDates.add(new TimerTaskDate("火山86级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("火山87级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("火山88级别BOSS", 0, 40, 0,"精品"));

        timerTaskDates.add(new TimerTaskDate("火山91级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("火山92级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("火山93级别BOSS", 0, 40, 0,"精品"));

        timerTaskDates.add(new TimerTaskDate("火山96级别BOSS", 0, 15, 0,null));
        timerTaskDates.add(new TimerTaskDate("火山97级别BOSS", 0, 25, 0,null));
        timerTaskDates.add(new TimerTaskDate("火山98级别BOSS", 0, 40, 0,"精品"));

        timerTaskDates.add(new TimerTaskDate("雷鸣蜘蛛精boss", 0, 60, 0,"精品"));
        timerTaskDates.add(new TimerTaskDate("树心蜘蛛精boss", 0, 60, 0,"精品"));
        timerTaskDates.add(new TimerTaskDate("戈壁蜘蛛精boss", 0, 60, 0,"精品"));
        timerTaskDates.add(new TimerTaskDate("沼泽蜘蛛精boss", 0, 60, 0,"精品"));
        timerTaskDates.add(new TimerTaskDate("海岛蜘蛛精boss", 0, 60, 0,"精品"));
        timerTaskDates.add(new TimerTaskDate("火山蜘蛛精boss", 0, 60, 0,"精品"));
        timerTaskDates.add(new TimerTaskDate("冰原蜘蛛精boss", 0, 60, 0,"精品"));
        timerTaskDates.add(new TimerTaskDate("花雨蜘蛛精boss", 0, 60, 0,"精品"));
    }

    static class TimerTaskDate {
        private String name;
        private int hours;
        private int minutes;
        private int seconds;
        // 表示超级
        private String colorMark;

        public TimerTaskDate(String name, int hours, int minutes, int seconds, String color) {
            this.name = name;
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
            this.colorMark = color;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }

        public String getColorMark() {
            return colorMark;
        }

        public void setColorMark(String colorMark) {
            this.colorMark = colorMark;
        }
    }

    private void addTasks(){
        // 添加任务
        timerTaskDates.forEach(timerTaskDate -> {
            addTimerTask(2, timerTaskDate.getName(), timerTaskDate.getHours(), timerTaskDate.getMinutes(), timerTaskDate.getSeconds(), timerTaskDate.getColorMark());
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // 顶部面板 - 添加新任务
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createTitledBorder("添加新任务"));

        // 输入面板
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        inputPanel.add(new JLabel("任务名称:"));
        nameField = new JTextField("任务1", 15);
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("时:"));
        hourSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        hourSpinner.setPreferredSize(new Dimension(60, 25));
        inputPanel.add(hourSpinner);

        inputPanel.add(new JLabel("分:"));
        minuteSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 59, 1));
        minuteSpinner.setPreferredSize(new Dimension(60, 25));
        inputPanel.add(minuteSpinner);

        inputPanel.add(new JLabel("秒:"));
        secondSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        secondSpinner.setPreferredSize(new Dimension(60, 25));
        inputPanel.add(secondSpinner);

        addButton = new JButton("添加任务");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.black);
        inputPanel.add(addButton);

        topPanel.add(inputPanel);

        // 控制面板
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        autoSortCheckBox = new JCheckBox("自动排序运行中任务", true);
        autoSortCheckBox.addActionListener(e -> {
            autoSortEnabled = autoSortCheckBox.isSelected();
            sortInfoLabel.setText("自动排序: " + (autoSortEnabled ? "已开启" : "已关闭"));
        });
        controlPanel.add(autoSortCheckBox);

        JButton manualSortButton = new JButton("手动排序");
        manualSortButton.addActionListener(e -> {
            sortTasks();
            refreshTable();
        });
        controlPanel.add(manualSortButton);

        // 添加批量操作按钮
        JButton startAllButton = new JButton("全部开始");
        startAllButton.setBackground(new Color(34, 139, 34));
        startAllButton.setForeground(Color.black);
        startAllButton.addActionListener(e -> startAllTasks());
        controlPanel.add(startAllButton);

        JButton resetAllButton = new JButton("全部重置");
        resetAllButton.setBackground(new Color(70, 130, 180));
        resetAllButton.setForeground(Color.black);
        resetAllButton.addActionListener(e -> resetAllTasks());
        controlPanel.add(resetAllButton);

        closeAllDialogsButton = new JButton("关闭所有提示");
        closeAllDialogsButton.setBackground(new Color(138, 43, 226));
        closeAllDialogsButton.setForeground(Color.black);
        closeAllDialogsButton.addActionListener(e -> closeAllCompletionDialogs());
        controlPanel.add(closeAllDialogsButton);

        topPanel.add(controlPanel);
        add(topPanel, BorderLayout.NORTH);

        // 中央面板 - 任务表格
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // 操作列和重置列是可编辑的（用于按钮）
                return column == 4 || column == 5;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4 || columnIndex == 5) {
                    return JButton.class;
                }
                return String.class;
            }
        };

        timerTable = new JTable(tableModel);
        timerTable.setRowHeight(35);
        timerTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 12));
        timerTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        timerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 设置列宽
        timerTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        timerTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        timerTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        timerTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        timerTable.getColumnModel().getColumn(4).setPreferredWidth(100);  // 操作列
        timerTable.getColumnModel().getColumn(5).setPreferredWidth(80);   // 重置列
        timerTable.getColumnModel().getColumn(6).setPreferredWidth(80);   // 剩余秒数列

        // 设置操作列和重置列的渲染器和编辑器
        timerTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonCellRenderer());
        timerTable.getColumnModel().getColumn(4).setCellEditor(new ButtonCellEditor());
        timerTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonCellRenderer());
        timerTable.getColumnModel().getColumn(5).setCellEditor(new ButtonCellEditor());

        // 设置其他列的自定义渲染器
        timerTable.setDefaultRenderer(Object.class, new TaskResetCellRenderer());

        // 添加选择监听器
        ListSelectionModel selectionModel = timerTable.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                lastSelectedRow = timerTable.getSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(timerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("倒计时任务列表 (每个任务有独立的重置按钮)"));
        add(scrollPane, BorderLayout.CENTER);

        // 底部面板 - 信息和按钮
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // 左下方 - 信息和颜色图例
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        sortInfoLabel = new JLabel("自动排序: 已开启 - 运行中任务按剩余时间倒序排列");
        sortInfoLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        leftPanel.add(sortInfoLabel);

        activeLabel = new JLabel("活动任务: 0 个");
        activeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        leftPanel.add(activeLabel);

        // 颜色图例
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        legendPanel.setBorder(BorderFactory.createTitledBorder("颜色图例"));

        legendPanel.add(createLegendItem("运行中", RUNNING_COLOR));
        legendPanel.add(createLegendItem("<60秒", WARNING_COLOR));
        legendPanel.add(createLegendItem("<10秒", CRITICAL_COLOR));
        legendPanel.add(createLegendItem("已暂停", PAUSED_COLOR));
        legendPanel.add(createLegendItem("已完成", FINISHED_COLOR));

        leftPanel.add(legendPanel);
        bottomPanel.add(leftPanel, BorderLayout.WEST);

        // 右下方 - 批量操作按钮
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton removeAllButton = new JButton("删除所有任务");
        removeAllButton.setBackground(new Color(178, 34, 34));
        removeAllButton.setForeground(Color.black);
        removeAllButton.addActionListener(e -> removeAllTasks());
        rightPanel.add(removeAllButton);

        bottomPanel.add(rightPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // 添加事件监听器
        addButton.addActionListener(e -> addTimerTask(1,null,0, 0, 0,null));
    }

    private JPanel createLegendItem(String text, Color color) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel colorLabel = new JLabel("■■");
        colorLabel.setOpaque(true);
        colorLabel.setBackground(color);
        colorLabel.setForeground(Color.BLACK);
        colorLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        colorLabel.setPreferredSize(new Dimension(20, 15));
        itemPanel.add(colorLabel);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        itemPanel.add(textLabel);

        return itemPanel;
    }

    private void setupUpdateTimer() {
        updateTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean needUpdate = false;

                for (int i = 0; i < timerTasks.size(); i++) {
                    TimerTask task = timerTasks.get(i);
                    if (task.isRunning()) {
                        boolean finished = task.update();
                        needUpdate = true;

                        // 检查是否需要警告（小于10秒）
                        if (task.getRemainingSeconds() <= 10 && task.getRemainingSeconds() > 0) {
                            playWarningBeep(task.getRemainingSeconds());
                        }

                        if (finished && !completedTasksShown.containsKey(task.getId())) {
                            // 任务完成，记录已经提示过
                            completedTasksShown.put(task.getId(), true);
                            showNonBlockingCompletionMessage(task);
                        }
                    }
                }

                if (needUpdate) {
                    if (autoSortEnabled) {
                        sortTasks();
                    }
                    updateTableData();
                    updateActiveCount();
                }
            }
        });
        updateTimer.start();
    }

    private void showNonBlockingCompletionMessage(TimerTask task) {
        // 在新线程中播放完成声音
        new Thread(() -> {
            playCompletionSound();
        }).start();

        // 在EDT线程中显示非模态对话框
        SwingUtilities.invokeLater(() -> {
            createNonModalCompletionDialog(task);
        });
    }

    private void createNonModalCompletionDialog(TimerTask task) {
        JDialog dialog = new JDialog(this, "倒计时完成", false);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("任务完成!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 100, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel nameLabel = new JLabel("任务名称: " + task.getName());
        nameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messagePanel.add(nameLabel);

        JLabel timeLabel = new JLabel("总时间: " + formatTime(task.getTotalSeconds()));
        timeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messagePanel.add(timeLabel);

        messagePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel congratsLabel = new JLabel("任务已完成！");
        congratsLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        congratsLabel.setForeground(Color.BLUE);
        congratsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messagePanel.add(congratsLabel);

        panel.add(messagePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        JButton okButton = new JButton("确定");
        okButton.setBackground(new Color(70, 130, 180));
        okButton.setForeground(Color.black);
        okButton.addActionListener(e -> {
            dialog.dispose();
            completionDialogs.remove(task.getId());
        });
        buttonPanel.add(okButton);

        JButton autoCloseButton = new JButton("5秒后自动关闭");
        autoCloseButton.setBackground(new Color(34, 139, 34));
        autoCloseButton.setForeground(Color.black);
        autoCloseButton.addActionListener(e -> {
            scheduleDialogClose(dialog, task.getId(), 5000);
            autoCloseButton.setEnabled(false);
            autoCloseButton.setText("自动关闭中...");
        });
        buttonPanel.add(autoCloseButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);

        completionDialogs.put(task.getId(), dialog);

        scheduleDialogClose(dialog, task.getId(), 5000);
    }

    private void scheduleDialogClose(JDialog dialog, int taskId, int delayMillis) {
        Timer closeTimer = new Timer(delayMillis, e -> {
            dialog.dispose();
            completionDialogs.remove(taskId);
        });
        closeTimer.setRepeats(false);
        closeTimer.start();
    }

    private void closeAllCompletionDialogs() {
        for (JDialog dialog : completionDialogs.values()) {
            dialog.dispose();
        }
        completionDialogs.clear();
    }

    private void sortTasks() {
        timerTasks.sort(new TimerTaskComparator());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        startButtonMap.clear();
        resetButtonMap.clear();

        for (int i = 0; i < timerTasks.size(); i++) {
            TimerTask task = timerTasks.get(i);

            // 创建操作按钮和重置按钮
            JButton startButton = createStartButton(task, i);
            JButton resetButton = createResetButton(task, i);

            startButtonMap.put(i, startButton);
            resetButtonMap.put(i, resetButton);

            Object[] rowData = {
                    task.getName(),
                    formatTime(task.getTotalSeconds()),
                    formatTime(task.getRemainingSeconds()),
                    task.getStatus(),
                    startButton,  // 操作按钮
                    resetButton,  // 重置按钮
                    task.getRemainingSeconds()
            };
            tableModel.addRow(rowData);
        }

        if (lastSelectedRow >= 0 && lastSelectedRow < tableModel.getRowCount()) {
            timerTable.setRowSelectionInterval(lastSelectedRow, lastSelectedRow);
        }

        updateActiveCount();
        timerTable.repaint();
    }

    private JButton createStartButton(TimerTask task, int row) {
        String buttonText = "";
        Color buttonColor = START_BUTTON_COLOR;

        if (task.isCompleted()) {
            // 已完成的任务：显示"重新开始"
            buttonText = "重新开始";
            buttonColor = RESTART_BUTTON_COLOR;
        } else if (task.isRunning()) {
            // 运行中的任务：显示"暂停"
            buttonText = "暂停";
            buttonColor = PAUSE_BUTTON_COLOR;
        } else {
            // 已暂停的任务：显示"开始"
            buttonText = "开始";
            buttonColor = START_BUTTON_COLOR;
        }

        JButton button = new JButton(buttonText);
        button.setOpaque(true);
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // 为按钮添加动作监听器
        button.addActionListener(e -> {
            handleStartAction(task, row);
        });

        return button;
    }

    private JButton createResetButton(TimerTask task, int row) {
        JButton button = new JButton("重置");
        button.setOpaque(true);
        button.setBackground(RESET_BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // 为重置按钮添加动作监听器
        button.addActionListener(e -> {
            handleResetAction(task, row);
        });

        return button;
    }

    private void handleStartAction(TimerTask task, int row) {
        if (task.isCompleted()) {
            // 已完成的任务：重置并开始
            task.reset();
            task.start();
            completedTasksShown.remove(task.getId());
        } else if (task.isRunning()) {
            // 运行中的任务：暂停
            task.pause();
        } else {
            // 已暂停的任务：开始
            task.start();
            completedTasksShown.remove(task.getId());
        }

        if (autoSortEnabled) {
            sortTasks();
        }
        updateTableData();
        timerTable.repaint();
    }

    private void handleResetAction(TimerTask task, int row) {
        // 重置任务：恢复初始状态，但不开始
        task.reset();
        completedTasksShown.remove(task.getId());

        if (autoSortEnabled) {
            sortTasks();
        }
        updateTableData();
        timerTable.repaint();
    }

    private void updateTableData() {
        startButtonMap.clear();
        resetButtonMap.clear();

        for (int i = 0; i < timerTasks.size(); i++) {
            TimerTask task = timerTasks.get(i);
            if (i < tableModel.getRowCount()) {
                tableModel.setValueAt(task.getName(), i, 0);
                tableModel.setValueAt(formatTime(task.getTotalSeconds()), i, 1);
                tableModel.setValueAt(formatTime(task.getRemainingSeconds()), i, 2);
                tableModel.setValueAt(task.getStatus(), i, 3);

                // 创建新的按钮
                JButton startButton = createStartButton(task, i);
                JButton resetButton = createResetButton(task, i);

                startButtonMap.put(i, startButton);
                resetButtonMap.put(i, resetButton);

                tableModel.setValueAt(startButton, i, 4);
                tableModel.setValueAt(resetButton, i, 5);
                tableModel.setValueAt(task.getRemainingSeconds(), i, 6);
            }
        }

        if (timerTasks.size() != tableModel.getRowCount()) {
            refreshTable();
        }

        if (lastSelectedRow >= 0 && lastSelectedRow < timerTable.getRowCount()) {
            timerTable.setRowSelectionInterval(lastSelectedRow, lastSelectedRow);
        }

        timerTable.repaint();
    }

    private void addTimerTask(Integer type, String _name, int _hours, int _minutes, int _seconds, String _colorMark) {
        String name = "";
        int hours, minutes, seconds;
        String colorMark;
        if(type == 1){
            name = nameField.getText().trim();
            hours = (int) hourSpinner.getValue();
            minutes = (int) minuteSpinner.getValue();
            seconds = (int) secondSpinner.getValue();
            colorMark = _colorMark;
        }else {
            name = _name;
            hours = _hours;
            minutes = _minutes;
            seconds = _seconds;
            colorMark = _colorMark;
        }

        if (name.isEmpty()) {
            name = "未命名任务";
        }

        int totalSeconds = hours * 3600 + minutes * 60 + seconds;

        if (totalSeconds <= 0) {
            JOptionPane.showMessageDialog(this,
                    "总时间必须大于0秒！",
                    "输入错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        TimerTask task = new TimerTask(name, totalSeconds);
        timerTasks.add(task);

        if (autoSortEnabled) {
            sortTasks();
        }
        refreshTable();

        nameField.setText("任务" + (timerTasks.size() + 1));
    }

    private void startAllTasks() {
        for (TimerTask task : timerTasks) {
            if (!task.isRunning() && !task.isCompleted()) {
                task.start();
                completedTasksShown.remove(task.getId());
            }
        }

        if (autoSortEnabled) {
            sortTasks();
        }
        updateTableData();
        timerTable.repaint();
    }

    private void resetAllTasks() {
        for (TimerTask task : timerTasks) {
            task.reset();
            completedTasksShown.remove(task.getId());
        }

        if (autoSortEnabled) {
            sortTasks();
        }
        updateTableData();
        timerTable.repaint();
    }

    private void removeAllTasks() {
        if (timerTasks.isEmpty()) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要删除所有任务吗？",
                "确认删除",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            timerTasks.clear();
            lastSelectedRow = -1;
            completedTasksShown.clear();
            startButtonMap.clear();
            resetButtonMap.clear();
            closeAllCompletionDialogs();
            refreshTable();
        }
    }

    private void updateActiveCount() {
        int activeCount = 0;
        for (TimerTask task : timerTasks) {
            if (task.isRunning()) {
                activeCount++;
            }
        }
        activeLabel.setText("活动任务: " + activeCount + " 个");
    }

    private String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    private void playWarningBeep(int secondsLeft) {
        try {
            Toolkit.getDefaultToolkit().beep();

            if (secondsLeft <= 5) {
                new Thread(() -> {
                    try {
                        Thread.sleep(100);
                        Toolkit.getDefaultToolkit().beep();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playCompletionSound() {
        try {
            for (int i = 0; i < 3; i++) {
                Toolkit.getDefaultToolkit().beep();
                Thread.sleep(300);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 自定义排序器
    class TimerTaskComparator implements Comparator<TimerTask> {
        @Override
        public int compare(TimerTask t1, TimerTask t2) {
            if (t1.isRunning() && !t2.isRunning()) {
                return -1;
            } else if (!t1.isRunning() && t2.isRunning()) {
                return 1;
            } else if (t1.isRunning() && t2.isRunning()) {
                return Integer.compare(t1.getRemainingSeconds(), t2.getRemainingSeconds());
            } else if (t1.isCompleted() && !t2.isCompleted()) {
                return 1;
            } else if (!t1.isCompleted() && t2.isCompleted()) {
                return -1;
            } else {
                return Integer.compare(t1.getId(), t2.getId());
            }
        }
    }

    // 按钮单元格渲染器
    class ButtonCellRenderer extends JButton implements TableCellRenderer {
        public ButtonCellRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JButton) {
                JButton button = (JButton) value;
                button.setOpaque(true);
                button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return button;
            }

            // 如果没有按钮对象，创建一个默认的
            if (column == 4) {
                setText("操作");
                setBackground(Color.LIGHT_GRAY);
            } else if (column == 5) {
                setText("重置");
                setBackground(RESET_BUTTON_COLOR);
            }

            setForeground(Color.BLACK);
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            return this;
        }
    }

    // 按钮单元格编辑器
    class ButtonCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JButton button;

        public ButtonCellEditor() {
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Object getCellEditorValue() {
            return button;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (value instanceof JButton) {
                button = (JButton) value;
                button.setOpaque(true);
                return button;
            }

            button.setText("按钮");
            button.setBackground(Color.LIGHT_GRAY);
            button.setForeground(Color.BLACK);
            button.setOpaque(true);

            return button;
        }
    }

    // 自定义表格渲染器
    class TaskResetCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            // 如果是操作列或重置列（按钮列），直接返回按钮组件
            if (column == 4 || column == 5) {
                if (value instanceof JButton) {
                    JButton button = (JButton) value;
                    button.setOpaque(true);
                    return button;
                }

                // 如果没有按钮，创建一个默认的
                JLabel label = new JLabel("按钮", SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBackground(Color.LIGHT_GRAY);
                label.setForeground(Color.BLACK);
                return label;
            }

            Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            TimerTask task = getTaskAtTableRow(row);

            if (task != null) {
                if (isSelected) {
                    c.setBackground(SELECTED_COLOR);
                    c.setForeground(Color.BLACK);
                } else {
                    Color backgroundColor = getBackgroundColorForTask(task);
                    Color textColor = getTextColorForBackground(backgroundColor);

                    c.setBackground(backgroundColor);
                    c.setForeground(textColor);
                }

                if (task.isRunning()) {
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }

                if (task.isRunning() && !task.isCompleted() && task.getRemainingSeconds() <= 10) {
                    if (task.getRemainingSeconds() <= 5) {
                        c.setFont(c.getFont().deriveFont(Font.BOLD, 14));
                    } else {
                        c.setFont(c.getFont().deriveFont(Font.BOLD, 12));
                    }
                }
            }

            setHorizontalAlignment(SwingConstants.CENTER);

            return c;
        }

        private Color getBackgroundColorForTask(TimerTask task) {
            if (task.isCompleted()) {
                return FINISHED_COLOR;
            } else if (task.isRunning()) {
                int remaining = task.getRemainingSeconds();
                if (remaining <= 0) {
                    return FINISHED_COLOR;
                } else if (remaining <= 10) {
                    return CRITICAL_COLOR;
                } else if (remaining <= 60) {
                    return WARNING_COLOR;
                } else {
                    return RUNNING_COLOR;
                }
            } else {
                return PAUSED_COLOR;
            }
        }

        private Color getTextColorForBackground(Color backgroundColor) {
            double luminance = (0.299 * backgroundColor.getRed() +
                    0.587 * backgroundColor.getGreen() +
                    0.114 * backgroundColor.getBlue()) / 255;
            return luminance > 0.6 ? Color.BLACK : Color.WHITE;
        }
    }

    private TimerTask getTaskAtTableRow(int tableRow) {
        if (tableRow >= 0 && tableRow < timerTasks.size()) {
            return timerTasks.get(tableRow);
        }
        return null;
    }

    // 倒计时任务类
    static class TimerTask {
        private static int nextId = 1;

        private int id;
        private String name;
        private int totalSeconds;
        private int remainingSeconds;
        private boolean running;
        private boolean completed;

        public TimerTask(String name, int totalSeconds) {
            this.id = nextId++;
            this.name = name;
            this.totalSeconds = totalSeconds;
            this.remainingSeconds = totalSeconds;
            this.running = false;
            this.completed = false;
        }

        public void start() {
            if (remainingSeconds > 0 && !completed) {
                running = true;
                completed = false;
            } else if (completed) {
                // 如果是已完成的任务重新开始
                remainingSeconds = totalSeconds;
                running = true;
                completed = false;
            }
        }

        public void pause() {
            running = false;
        }

        public void reset() {
            remainingSeconds = totalSeconds;
            running = false;
            completed = false;
        }

        public boolean update() {
            if (running && remainingSeconds > 0) {
                remainingSeconds--;
                if (remainingSeconds <= 0) {
                    remainingSeconds = 0;
                    running = false;
                    completed = true;
                    return true;
                }
            }
            return false;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getTotalSeconds() {
            return totalSeconds;
        }

        public int getRemainingSeconds() {
            return remainingSeconds;
        }

        public boolean isRunning() {
            return running;
        }

        public boolean isCompleted() {
            return completed;
        }

        public String getStatus() {
            if (completed) {
                return "已完成";
            } else if (running) {
                return "进行中";
            } else {
                return "已暂停";
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TaskResetCountdownTimer();
        });
    }
}
