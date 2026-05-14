package com.spc.springweb.jframe.moyu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Author caiJH
 * @Date 2026/1/14 2:40 PM
 * @Version 1.0
 */
public class CountdownTimer extends JFrame {
    // 组件声明
    private JLabel timeLabel;
    private JTextField minuteField;
    private JButton startButton;
    private JButton resetButton;
    private JButton pauseButton;
    private JLabel statusLabel;

    // 计时器相关变量
    private Timer timer;
    private int totalSeconds;
    private int remainingSeconds;
    private boolean isRunning = false;

    public CountdownTimer() {
        setTitle("倒计时器");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示
        setLayout(new BorderLayout());

        initComponents();
        setupTimer();

        setVisible(true);
    }

    private void initComponents() {
        // 顶部面板 - 输入区域
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("设置分钟数:"));

        minuteField = new JTextField("5", 10);
        topPanel.add(minuteField);

        startButton = new JButton("开始");
        topPanel.add(startButton);

        pauseButton = new JButton("暂停");
        pauseButton.setEnabled(false);
        topPanel.add(pauseButton);

        resetButton = new JButton("重置");
        topPanel.add(resetButton);

        add(topPanel, BorderLayout.NORTH);

        // 中间面板 - 时间显示
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.BLACK);

        timeLabel = new JLabel("05:00");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 60));
        timeLabel.setForeground(Color.GREEN);
        centerPanel.add(timeLabel);

        add(centerPanel, BorderLayout.CENTER);

        // 底部面板 - 状态信息
        JPanel bottomPanel = new JPanel();
        statusLabel = new JLabel("就绪 - 请输入分钟数并点击开始");
        bottomPanel.add(statusLabel);

        add(bottomPanel, BorderLayout.SOUTH);

        // 按钮事件监听
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTimer();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseTimer();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTimer();
            }
        });
    }

    private void setupTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingSeconds--;
                updateDisplay();

                if (remainingSeconds <= 0) {
                    timer.stop();
                    isRunning = false;
                    timeLabel.setForeground(Color.RED);
                    statusLabel.setText("时间到！");
                    startButton.setEnabled(true);
                    pauseButton.setEnabled(false);

                    // 播放提示音（可选）
                    Toolkit.getDefaultToolkit().beep();

                    // 显示对话框提示
                    JOptionPane.showMessageDialog(CountdownTimer.this,
                            "时间到！",
                            "倒计时结束",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private void startTimer() {
        try {
            if (!isRunning) {
                // 解析分钟数
                int minutes = Integer.parseInt(minuteField.getText().trim());
                if (minutes <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "请输入大于0的分钟数",
                            "输入错误",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                totalSeconds = minutes * 60;
                remainingSeconds = totalSeconds;

                // 禁用输入框和开始按钮，启用暂停按钮
                minuteField.setEnabled(false);
                startButton.setEnabled(false);
                pauseButton.setEnabled(true);
                timeLabel.setForeground(Color.GREEN);

                isRunning = true;
                statusLabel.setText("倒计时进行中...");

                timer.start();
                updateDisplay();
            } else {
                // 从暂停状态恢复
                timer.start();
                pauseButton.setText("暂停");
                statusLabel.setText("倒计时进行中...");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "请输入有效的数字",
                    "输入错误",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pauseTimer() {
        if (timer.isRunning()) {
            timer.stop();
            pauseButton.setText("继续");
            statusLabel.setText("已暂停");
        } else {
            timer.start();
            pauseButton.setText("暂停");
            statusLabel.setText("倒计时进行中...");
        }
    }

    private void resetTimer() {
        timer.stop();
        isRunning = false;

        // 启用输入框和开始按钮，禁用暂停按钮
        minuteField.setEnabled(true);
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        pauseButton.setText("暂停");

        // 重置显示
        try {
            int minutes = Integer.parseInt(minuteField.getText().trim());
            if (minutes > 0) {
                totalSeconds = minutes * 60;
                remainingSeconds = totalSeconds;
                updateDisplay();
            }
        } catch (NumberFormatException ex) {
            // 如果输入无效，显示默认值
            timeLabel.setText("05:00");
        }

        timeLabel.setForeground(Color.GREEN);
        statusLabel.setText("已重置 - 请输入分钟数并点击开始");
    }

    private void updateDisplay() {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;

        // 格式化显示：MM:SS
        String timeStr = String.format("%02d:%02d", minutes, seconds);
        timeLabel.setText(timeStr);

        // 根据剩余时间改变颜色
        if (remainingSeconds <= 30 && remainingSeconds > 0) {
            timeLabel.setForeground(Color.YELLOW);
        } else if (remainingSeconds <= 0) {
            timeLabel.setForeground(Color.RED);
        } else {
            timeLabel.setForeground(Color.GREEN);
        }
    }

    public static void main(String[] args) {
        // 使用SwingUtilities确保线程安全
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CountdownTimer();
            }
        });
    }
}
