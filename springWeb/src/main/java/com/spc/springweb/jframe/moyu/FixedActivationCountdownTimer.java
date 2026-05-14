package com.spc.springweb.jframe.moyu;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * @Author caiJH
 * @Date 2026/1/19 11:16 AM
 * @Version 1.0
 */
public class FixedActivationCountdownTimer extends JFrame {

    private JTable timerTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JTextField nameField;
    private JSpinner hourSpinner;
    private JSpinner minuteSpinner;
    private JSpinner secondSpinner;
    private JLabel activeLabel;
    private JLabel sortInfoLabel;
    private JLabel activationLabel;
    private JCheckBox autoSortCheckBox;
    private JButton activationButton;

    private List<TimerTask> timerTasks;
    private Timer updateTimer;
    private boolean autoSortEnabled = true;
    private int lastSelectedRow = -1;

    // 激活相关变量
    private boolean isActivated = false;
    private int remainingDays = 0;
    private String activationCode = "";
    private Date expirationDate = null;

    private String count = "";

    private JPanel topPanel;
    private JPanel bottomPanel;

    // 存储已经显示过完成提示的任务ID
    private Map<Integer, Boolean> completedTasksShown = new HashMap<>();
    // 存储完成对话框的引用
    private Map<Integer, JDialog> completionDialogs = new HashMap<>();
    // 存储每行的按钮组件
    private Map<Integer, JButton> startButtonMap = new HashMap<>();
    private Map<Integer, JButton> resetButtonMap = new HashMap<>();
    private Map<Integer, JButton> editButtonMap = new HashMap<>();
    private Map<Integer, JButton> deleteButtonMap = new HashMap<>();

    // 配置文件
    private static final String CONFIG_FILE = "activation_config.dat";
    private static final String ACTIVATION_RECORDS_FILE = "activation_records.dat";

    // 加密相关常量
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String SECRET_KEY_STRING = "FixedTimerKey2026!"; // 16字节密钥

    private static final byte[] SECRET_KEY_BYTES;

    static {
        // 使用MD5哈希确保密钥为16字节
        byte[] keyBytes = getAESKey(SECRET_KEY_STRING);
        SECRET_KEY_BYTES = keyBytes;
    }

    // 表头 - 增加了编辑和删除列
    private final String[] COLUMN_NAMES = {"任务名称", "总时间", "剩余时间", "状态", "操作", "重置", "编辑", "删除"};

    // 颜色定义
    private final Color NORMAL_COLOR = Color.WHITE;
    private final Color WARNING_COLOR = Color.YELLOW;  // <10秒
    private final Color CRITICAL_COLOR = Color.RED;    // <5秒
    private final Color RUNNING_COLOR = new Color(220, 255, 220); // 运行中的任务背景
    private final Color FINISHED_COLOR = new Color(255, 182, 193); // 完成
    private final Color PAUSED_COLOR = new Color(240, 240, 240);  // 暂停
    private final Color SELECTED_COLOR = new Color(180, 220, 255); // 选中行背景
    private final Color NOT_ACTIVATED_COLOR = new Color(255, 200, 200); // 未激活背景

    // 按钮颜色
    private final Color START_BUTTON_COLOR = new Color(34, 139, 34); // 开始按钮颜色
    private final Color PAUSE_BUTTON_COLOR = new Color(218, 165, 32); // 暂停按钮颜色
    private final Color RESET_BUTTON_COLOR = new Color(70, 130, 180); // 重置按钮颜色
    private final Color RESTART_BUTTON_COLOR = new Color(138, 43, 226); // 重新开始按钮颜色
    private final Color ACTIVATION_BUTTON_COLOR = new Color(0, 100, 0); // 激活按钮颜色
    private final Color EDIT_BUTTON_COLOR = new Color(255, 165, 0); // 编辑按钮颜色 - 橙色
    private final Color DELETE_BUTTON_COLOR = new Color(178, 34, 34); // 删除按钮颜色 - 红色

    // 设备标识（用于唯一识别设备）
    private String deviceId = null;

    public FixedActivationCountdownTimer() {
        setTitle("激活版倒计时管理器 - 寒山版 (一机一码)");
        setSize(1200, 650); // 增加宽度以适应新列
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        timerTasks = new ArrayList<>();

        // 生成设备ID
        deviceId = generateDeviceId();

        // 先加载激活配置，设置isActivated状态
        loadActivationConfig();

        // 初始化组件，此时会根据isActivated状态设置组件
        initComponents();

        // 初始化后，如果未激活，则显示激活对话框
        if (!isActivated) {
            showActivationDialog();
        } else {
            // 如果已激活，检查是否即将过期
            checkActivationExpiration();
        }

        setupUpdateTimer();

        setVisible(true);

        addTasks();
    }

    /**
     * 生成设备唯一标识
     */
    private String generateDeviceId() {
        try {
            StringBuilder deviceInfo = new StringBuilder();

            // 获取MAC地址
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            if (mac != null) {
                for (int i = 0; i < mac.length; i++) {
                    deviceInfo.append(String.format("%02X", mac[i]));
                }
            }

            // 获取计算机名
            String computerName = System.getProperty("user.name");
            deviceInfo.append(computerName);

            // 获取操作系统信息
            String osName = System.getProperty("os.name");
            String osArch = System.getProperty("os.arch");
            deviceInfo.append(osName).append(osArch);

            // 生成MD5哈希作为设备ID
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(deviceInfo.toString().getBytes(StandardCharsets.UTF_8));

            StringBuilder deviceIdBuilder = new StringBuilder();
            for (byte b : digest) {
                deviceIdBuilder.append(String.format("%02x", b & 0xff));
            }

            // 取前16位作为设备ID
            return deviceIdBuilder.toString().substring(0, 16).toUpperCase();

        } catch (Exception e) {
            // 如果获取失败，生成一个随机设备ID
            System.err.println("生成设备ID失败，使用随机ID: " + e.getMessage());
            return "RAND" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
        }
    }

    /**
     * 记录激活信息
     */
    private void recordActivation(String code, String deviceId, Date expirationDate) {
        try {
            // 读取现有记录
            Map<String, ActivationRecord> records = loadActivationRecords();

            // 创建新记录
            ActivationRecord record = new ActivationRecord();
            record.setActivationCode(code);
            record.setDeviceId(deviceId);
            record.setActivationDate(new Date());
            record.setExpirationDate(expirationDate);

            // 添加到记录中
            records.put(code, record);

            // 保存记录
            saveActivationRecords(records);

            System.err.println("激活记录已保存: " + code + " - " + deviceId);

        } catch (Exception e) {
            System.err.println("保存激活记录失败: " + e.getMessage());
        }
    }

    /**
     * 检查激活码是否已被使用
     */
    private boolean isActivationCodeUsed(String code) {
        try {
            Map<String, ActivationRecord> records = loadActivationRecords();

            // 检查激活码是否已存在
            if (records.containsKey(code)) {
                ActivationRecord record = records.get(code);

                // 检查是否当前设备
                if (record.getDeviceId().equals(deviceId)) {
                    System.err.println("激活码已在当前设备上使用");
                    return true;
                } else {
                    System.err.println("激活码已在其他设备上使用，设备ID: " + record.getDeviceId());
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            System.err.println("检查激活记录失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 加载激活记录
     */
    private Map<String, ActivationRecord> loadActivationRecords() {
        Map<String, ActivationRecord> records = new HashMap<>();

        File recordsFile = new File(ACTIVATION_RECORDS_FILE);
        if (!recordsFile.exists()) {
            return records;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(recordsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String decryptedLine = decrypt(line);
                if (decryptedLine != null) {
                    ActivationRecord record = ActivationRecord.fromString(decryptedLine);
                    if (record != null) {
                        records.put(record.getActivationCode(), record);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("加载激活记录失败: " + e.getMessage());
        }

        return records;
    }

    /**
     * 保存激活记录
     */
    private void saveActivationRecords(Map<String, ActivationRecord> records) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACTIVATION_RECORDS_FILE))) {
            for (ActivationRecord record : records.values()) {
                String recordString = record.toString();
                String encryptedRecord = encrypt(recordString);
                if (encryptedRecord != null) {
                    writer.write(encryptedRecord);
                    writer.newLine();
                }
            }
        } catch (Exception e) {
            System.err.println("保存激活记录失败: " + e.getMessage());
        }
    }

    /**
     * 激活记录类
     */
    static class ActivationRecord {
        private String activationCode;
        private String deviceId;
        private Date activationDate;
        private Date expirationDate;

        public ActivationRecord() {
        }

        public String getActivationCode() {
            return activationCode;
        }

        public void setActivationCode(String activationCode) {
            this.activationCode = activationCode;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public Date getActivationDate() {
            return activationDate;
        }

        public void setActivationDate(Date activationDate) {
            this.activationDate = activationDate;
        }

        public Date getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(Date expirationDate) {
            this.expirationDate = expirationDate;
        }

        @Override
        public String toString() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "ACTIVATION_CODE=" + activationCode + "|" +
                    "DEVICE_ID=" + deviceId + "|" +
                    "ACTIVATION_DATE=" + sdf.format(activationDate) + "|" +
                    "EXPIRATION_DATE=" + sdf.format(expirationDate);
        }

        public static ActivationRecord fromString(String str) {
            try {
                String[] parts = str.split("\\|");
                ActivationRecord record = new ActivationRecord();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                for (String part : parts) {
                    if (part.startsWith("ACTIVATION_CODE=")) {
                        record.setActivationCode(part.substring("ACTIVATION_CODE=".length()));
                    } else if (part.startsWith("DEVICE_ID=")) {
                        record.setDeviceId(part.substring("DEVICE_ID=".length()));
                    } else if (part.startsWith("ACTIVATION_DATE=")) {
                        record.setActivationDate(sdf.parse(part.substring("ACTIVATION_DATE=".length())));
                    } else if (part.startsWith("EXPIRATION_DATE=")) {
                        record.setExpirationDate(sdf.parse(part.substring("EXPIRATION_DATE=".length())));
                    }
                }

                return record;
            } catch (Exception e) {
                System.err.println("解析激活记录失败: " + e.getMessage());
                return null;
            }
        }
    }

    private void checkActivation() {
        // 尝试从配置文件加载激活信息
        loadActivationConfig();

        if (!isActivated) {
            // 显示激活对话框
            showActivationDialog();
        } else {
            // 验证激活是否过期
            checkActivationExpiration();
        }
    }

    private void loadActivationConfig() {
        System.err.println("初始化页面");
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            StringBuilder encryptedContent = new StringBuilder();
            String line;

            // 读取整个文件内容
            while ((line = reader.readLine()) != null) {
                encryptedContent.append(line);
            }

            String fileContent = encryptedContent.toString();

            // 尝试解密（新版本加密格式）
            String decryptedContent = decrypt(fileContent);

            if (decryptedContent != null && !decryptedContent.isEmpty()) {
                // 解密成功，处理解密后的内容
                processDecryptedContent(decryptedContent);
            } else {
                // 解密失败，尝试以旧版本明文格式读取
                System.err.println("尝试以旧版本格式读取配置文件...");
                try (BufferedReader plainReader = new BufferedReader(new FileReader(configFile))) {
                    processPlainContent(plainReader);
                }
            }
        } catch (IOException e) {
            System.err.println("读取激活配置文件失败: " + e.getMessage());
            isActivated = false;
            expirationDate = null;
        } catch (Exception e) {
            System.err.println("配置文件处理异常: " + e.getMessage());
            isActivated = false;
            expirationDate = null;
        }
    }

    /**
     * 处理解密后的内容
     */
    private void processDecryptedContent(String decryptedContent) {
        String[] lines = decryptedContent.split("\n");
        String loadedCode = null;
        Date loadedExpiration = null;
        String loadedCount = "0";
        String loadedDeviceId = null;

        for (String line : lines) {
            if (line.startsWith("ACTIVATION_CODE=")) {
                loadedCode = line.substring("ACTIVATION_CODE=".length());
            } else if (line.startsWith("EXPIRATION_DATE=")) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    loadedExpiration = sdf.parse(line.substring("EXPIRATION_DATE=".length()));
                } catch (Exception e) {
                    System.err.println("激活日期格式错误: " + e.getMessage());
                    loadedExpiration = null;
                }
            } else if (line.startsWith("COUNT=")) {
                loadedCount = line.substring("COUNT=".length());
            } else if (line.startsWith("DEVICE_ID=")) {
                loadedDeviceId = line.substring("DEVICE_ID=".length());
            } else if (line.startsWith("ENCRYPTION_VERSION=")) {
                // 加密版本标记，可以用于后续版本升级
                System.err.println("加密版本: " + line.substring("ENCRYPTION_VERSION=".length()));
            }
        }

        count = loadedCount;

        if (loadedCode != null && !loadedCode.isEmpty() && loadedExpiration != null && loadedDeviceId != null) {
            // 验证激活码和设备ID匹配
            if (loadedDeviceId.equals(deviceId) && validateActivationCode(loadedCode, false)) {
                activationCode = loadedCode;
                expirationDate = loadedExpiration;
                isActivated = true;
                calculateRemainingDays();
                System.err.println("激活剩余天数: " + remainingDays);

                if (remainingDays <= 0) {
                    isActivated = false;
                    expirationDate = null;
                    activationCode = "";
                    System.err.println("激活已过期");
                }
            } else {
                isActivated = false;
                expirationDate = null;
                activationCode = "";
                if (!loadedDeviceId.equals(deviceId)) {
                    System.err.println("激活信息与当前设备不匹配");
                }
            }
        } else {
            isActivated = false;
            expirationDate = null;
            activationCode = "";
            System.err.println("激活配置文件不完整");
        }
    }

    /**
     * 处理明文内容（旧版本兼容）
     */
    private void processPlainContent(BufferedReader reader) throws IOException {
        String line;
        String loadedCode = null;
        Date loadedExpiration = null;
        String loadedCount = "0";
        String loadedDeviceId = null;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("ACTIVATION_CODE=")) {
                loadedCode = line.substring("ACTIVATION_CODE=".length());
            } else if (line.startsWith("EXPIRATION_DATE=")) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    loadedExpiration = sdf.parse(line.substring("EXPIRATION_DATE=".length()));
                } catch (Exception e) {
                    System.err.println("激活日期格式错误: " + e.getMessage());
                    loadedExpiration = null;
                }
            } else if (line.startsWith("COUNT=")) {
                loadedCount = line.substring("COUNT=".length());
            } else if (line.startsWith("DEVICE_ID=")) {
                loadedDeviceId = line.substring("DEVICE_ID=".length());
            }
        }

        count = loadedCount;

        if (loadedCode != null && !loadedCode.isEmpty() && loadedExpiration != null && loadedDeviceId != null) {
            // 验证激活码和设备ID匹配
            if (loadedDeviceId.equals(deviceId) && validateActivationCode(loadedCode, false)) {
                activationCode = loadedCode;
                expirationDate = loadedExpiration;
                isActivated = true;
                calculateRemainingDays();
                System.err.println("激活剩余天数: " + remainingDays);

                if (remainingDays <= 0) {
                    isActivated = false;
                    expirationDate = null;
                    activationCode = "";
                    System.err.println("激活已过期");
                }
            } else {
                isActivated = false;
                expirationDate = null;
                activationCode = "";
                if (!loadedDeviceId.equals(deviceId)) {
                    System.err.println("激活信息与当前设备不匹配");
                }
            }
        } else {
            isActivated = false;
            expirationDate = null;
            activationCode = "";
            System.err.println("激活配置文件不完整");
        }
    }

    /**
     * 保存配置文件（使用AES加密）
     */
    private void saveActivationConfig() {
        try {
            // 构建配置文件内容
            StringBuilder configContent = new StringBuilder();
            configContent.append("ENCRYPTION_VERSION=1.0\n");  // 加密版本标记
            configContent.append("ACTIVATION_CODE=").append(activationCode).append("\n");

            if (expirationDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                configContent.append("EXPIRATION_DATE=").append(sdf.format(expirationDate)).append("\n");
            }

            configContent.append("COUNT=").append(count).append("\n");
            configContent.append("DEVICE_ID=").append(deviceId).append("\n");  // 保存设备ID

            // 加密内容
            String encryptedContent = encrypt(configContent.toString());

            if (encryptedContent != null) {
                // 保存加密后的内容
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
                    writer.write(encryptedContent);
                }
                System.err.println("配置文件已加密保存");
            } else {
                System.err.println("加密失败，使用明文保存");
                // 加密失败，使用明文保存（兼容性）
                savePlainConfig();
            }

        } catch (Exception e) {
            System.err.println("保存配置文件失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 明文保存配置文件（兼容性）
     */
    private void savePlainConfig() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            writer.write("ACTIVATION_CODE=" + activationCode);
            writer.newLine();
            if (expirationDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                writer.write("EXPIRATION_DATE=" + sdf.format(expirationDate));
                writer.newLine();
            }
            writer.write("COUNT=" + count);
            writer.newLine();
            writer.write("DEVICE_ID=" + deviceId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateActivationCode(String code, boolean isNewActivation) {
        if (code == null || code.isEmpty()) {
            return false;
        }

        // 处理试用激活码（允许重复使用）
        if (code.startsWith("TRIAL-")) {
            String[] parts = code.split("-");
            if (parts.length != 4 || !parts[3].equals("DAYS")) {
                return false;
            }

            try {
                int days = Integer.parseInt(parts[2]);
                if (days <= 0) {
                    return false;
                }

                if (isNewActivation) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, days);
                    expirationDate = calendar.getTime();
                }
                return true;
            } catch (NumberFormatException e) {
                System.err.println("试用激活码格式错误: " + e.getMessage());
                expirationDate = null;
                return false;
            }
        }

        // 处理正式激活码
        if (!code.startsWith("ACT-") || code.length() != 23) {
            return false;
        }

        String[] parts = code.split("-");
        if (parts.length != 5) {
            return false;
        }

        try {
            // 验证激活码格式
            for (int i = 1; i < parts.length; i++) {
                if (parts[i].length() != 4) {
                    return false;
                }
                Integer.parseInt(parts[i]);
            }

            // 如果是新激活，检查激活码是否已被使用
            if (isNewActivation) {
                if (isActivationCodeUsed(code)) {
                    return false; // 激活码已被使用
                }

                // 计算有效期
                int dayCount = 0;
                for (int i = 1; i < parts.length; i++) {
                    dayCount += Integer.parseInt(parts[i]);
                }
                dayCount = (dayCount % 30) + 1;

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, dayCount);
                expirationDate = calendar.getTime();
            }

            return true;

        } catch (NumberFormatException e) {
            System.err.println("激活码格式错误: " + e.getMessage());
            expirationDate = null;
            return false;
        }
    }

    private boolean checkActivationConfig() {
        // 检查激活状态是否完整有效
        if (!isActivated) {
            return false;
        }

        if (expirationDate == null || activationCode == null || activationCode.isEmpty()) {
            // 激活信息不完整，重置为未激活状态
            isActivated = false;
            expirationDate = null;
            activationCode = "";
            return false;
        }

        // 检查激活码是否过期
        calculateRemainingDays();
        if (remainingDays <= 0) {
            isActivated = false;
            System.err.println("激活已过期");
            return false;
        }

        return true;
    }

    private void calculateRemainingDays() {
        if (expirationDate == null) {
            remainingDays = 0;
            return;
        }

        Date currentDate = new Date();
        long diff = expirationDate.getTime() - currentDate.getTime();
        remainingDays = (int) (diff / (1000 * 60 * 60 * 24));

        if (remainingDays < 0) {
            remainingDays = 0;
            isActivated = false;
        }
    }

    private void checkActivationExpiration() {
        calculateRemainingDays();

        if (remainingDays <= 0) {
            isActivated = false;
            showExpirationDialog();
        } else if (remainingDays <= 3) {
            showWarningDialog("激活即将到期",
                    "您的激活码将在 " + remainingDays + " 天后到期，请及时续期！");
        }
    }

    private void showActivationDialog() {
        JDialog activationDialog = new JDialog(this, "软件激活", true);
        activationDialog.setSize(700, 450); // 增加对话框大小
        activationDialog.setLocationRelativeTo(this);
        activationDialog.setLayout(new BorderLayout(10, 10));

        // 标题面板
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("软件激活 (一机一码)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 100, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        activationDialog.add(titlePanel, BorderLayout.NORTH);

        // 主内容面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // 左侧说明面板
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("激活说明"));

        JTextArea infoText = new JTextArea(
                "欢迎使用倒计时管理器！\n\n" +
                        "本软件采用一机一码激活机制。\n\n" +
                        "重要提醒：\n" +
                        "1. 每个激活码只能在一台设备上使用\n" +
                        "2. 请妥善保管您的激活码\n" +
                        "3. 试用码可以重复使用\n" +
                        "4. 正式激活码使用后无法在其他设备激活\n\n" +
                        "设备ID: " + deviceId + "\n" +
                        "（请在联系客服时提供此ID）"
        );
        infoText.setEditable(false);
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);
        infoText.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        infoText.setBackground(infoPanel.getBackground());

        JScrollPane infoScrollPane = new JScrollPane(infoText);
        infoScrollPane.setPreferredSize(new Dimension(280, 250));
        infoPanel.add(infoScrollPane);

        // 右侧输入面板
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("激活输入"));

        // 输入字段
        JPanel fieldPanel = new JPanel(new GridBagLayout());
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 激活码标签
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel codeLabel = new JLabel("激活码:");
        codeLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        fieldPanel.add(codeLabel, gbc);

        // 激活码输入框
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField codeField = new JTextField();
        codeField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        codeField.setPreferredSize(new Dimension(300, 35));
        codeField.setMinimumSize(new Dimension(300, 35));
        codeField.setToolTipText("请输入激活码，格式：ACT-XXXX-XXXX-XXXX-XXXX 或 TRIAL-0015-DAYS");
        codeField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        fieldPanel.add(codeField, gbc);

        // 设备ID显示
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel deviceLabel = new JLabel("设备ID:");
        deviceLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        fieldPanel.add(deviceLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JLabel deviceIdLabel = new JLabel(deviceId);
        deviceIdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        deviceIdLabel.setForeground(Color.BLUE);
        fieldPanel.add(deviceIdLabel, gbc);

        // 激活状态显示
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel statusLabel = new JLabel("请输入激活码...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        statusLabel.setForeground(Color.BLUE);
        fieldPanel.add(statusLabel, gbc);

        inputPanel.add(fieldPanel, BorderLayout.CENTER);

        // 将左右面板添加到主面板
        mainPanel.add(infoPanel, BorderLayout.WEST);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        activationDialog.add(mainPanel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JButton activateButton = new JButton("激活软件");
        activateButton.setBackground(ACTIVATION_BUTTON_COLOR);
        activateButton.setForeground(Color.black);
        activateButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        activateButton.setPreferredSize(new Dimension(150, 40));
        activateButton.addActionListener(e -> {
            String inputCode = codeField.getText().trim();
            if (inputCode.isEmpty()) {
                statusLabel.setText("请输入激活码！");
                statusLabel.setForeground(Color.RED);
                codeField.requestFocus();
                return;
            }

            if (validateActivationCode(inputCode, true)) {
                // 检查激活码是否已被使用（双重检查）
                if (inputCode.startsWith("ACT-") && isActivationCodeUsed(inputCode)) {
                    statusLabel.setText("激活码已被使用，请联系客服！");
                    statusLabel.setForeground(Color.RED);
                    return;
                }

                activationCode = inputCode;
                isActivated = true;

                // 记录激活信息
                recordActivation(inputCode, deviceId, expirationDate);

                if(null == count || count.equals("") || count.equals("0")){
                    count = "0";
                } else {
                    count = "1";
                }

                saveActivationConfig();
                calculateRemainingDays();
                updateActivationLabel();

                // 显示成功消息
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                String message;
                if (inputCode.startsWith("TRIAL-")) {
                    message = "试用激活成功！\n\n" +
                            "软件可使用至：" + sdf.format(expirationDate) + "\n" +
                            "剩余天数：" + remainingDays + "天\n\n" +
                            "试用码可以重复使用。";
                } else {
                    message = "正式激活成功！\n\n" +
                            "软件可使用至：" + sdf.format(expirationDate) + "\n" +
                            "剩余天数：" + remainingDays + "天\n\n" +
                            "此激活码已绑定到当前设备。\n" +
                            "设备ID：" + deviceId;
                }

                JOptionPane.showMessageDialog(activationDialog,
                        message,
                        "激活成功", JOptionPane.INFORMATION_MESSAGE);

                activationDialog.dispose();
                enableAllComponents();
            } else {
                // 检查是否是激活码已被使用的情况
                if (inputCode.startsWith("ACT-") && isActivationCodeUsed(inputCode)) {
                    statusLabel.setText("激活码已被使用，请联系客服！");
                    statusLabel.setForeground(Color.RED);

                    // 显示详细错误信息
                    Map<String, ActivationRecord> records = loadActivationRecords();
                    ActivationRecord record = records.get(inputCode);
                    if (record != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        JOptionPane.showMessageDialog(activationDialog,
                                "此激活码已被以下设备使用：\n\n" +
                                        "设备ID：" + record.getDeviceId() + "\n" +
                                        "激活时间：" + sdf.format(record.getActivationDate()) + "\n" +
                                        "到期时间：" + sdf.format(record.getExpirationDate()) + "\n\n" +
                                        "如需帮助，请联系客服并提供设备ID。",
                                "激活码已被使用", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    statusLabel.setText("激活码无效或格式错误！");
                    statusLabel.setForeground(Color.RED);
                }

                codeField.selectAll();
                codeField.requestFocus();
            }
        });
        buttonPanel.add(activateButton);

        // 查看激活记录按钮
        JButton viewRecordsButton = new JButton("查看激活记录");
        viewRecordsButton.setBackground(new Color(70, 130, 180));
        viewRecordsButton.setForeground(Color.black);
        viewRecordsButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        viewRecordsButton.setPreferredSize(new Dimension(180, 40));
        viewRecordsButton.addActionListener(e -> showActivationRecords());
        buttonPanel.add(viewRecordsButton);

        JButton exitButton = new JButton("退出软件");
        exitButton.setBackground(new Color(178, 34, 34));
        exitButton.setForeground(Color.black);
        exitButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        exitButton.setPreferredSize(new Dimension(150, 40));
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);

        activationDialog.add(buttonPanel, BorderLayout.SOUTH);

        // 设置对话框可见性
        activationDialog.setVisible(true);
    }

    /**
     * 显示激活记录对话框
     */
    private void showActivationRecords() {
        JDialog recordsDialog = new JDialog(this, "激活记录", true);
        recordsDialog.setSize(800, 400);
        recordsDialog.setLocationRelativeTo(this);
        recordsDialog.setLayout(new BorderLayout(10, 10));

        // 标题面板
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("激活记录", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 100, 0));
        titlePanel.add(titleLabel);
        recordsDialog.add(titlePanel, BorderLayout.NORTH);

        // 当前设备信息
        JPanel devicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        devicePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel deviceInfoLabel = new JLabel("当前设备ID: " + deviceId);
        deviceInfoLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        deviceInfoLabel.setForeground(Color.BLUE);
        devicePanel.add(deviceInfoLabel);
        recordsDialog.add(devicePanel, BorderLayout.NORTH);

        // 记录表格
        String[] columnNames = {"激活码", "设备ID", "激活时间", "到期时间", "状态"};
        DefaultTableModel recordsModel = new DefaultTableModel(columnNames, 0);
        JTable recordsTable = new JTable(recordsModel);
        recordsTable.setRowHeight(30);
        recordsTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 12));
        recordsTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        // 加载并显示激活记录
        Map<String, ActivationRecord> records = loadActivationRecords();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();

        for (ActivationRecord record : records.values()) {
            String status;
            if (record.getExpirationDate().after(currentDate)) {
                status = "有效";
            } else {
                status = "已过期";
            }

            // 如果是当前设备，高亮显示
            if (record.getDeviceId().equals(deviceId)) {
                status += " (当前设备)";
            }

            Object[] rowData = {
                    record.getActivationCode(),
                    record.getDeviceId(),
                    sdf.format(record.getActivationDate()),
                    sdf.format(record.getExpirationDate()),
                    status
            };
            recordsModel.addRow(rowData);
        }

        JScrollPane scrollPane = new JScrollPane(recordsTable);
        recordsDialog.add(scrollPane, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton refreshButton = new JButton("刷新记录");
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> {
            // 重新加载并刷新表格
            recordsModel.setRowCount(0);
            Map<String, ActivationRecord> refreshedRecords = loadActivationRecords();
            for (ActivationRecord record : refreshedRecords.values()) {
                String status;
                if (record.getExpirationDate().after(currentDate)) {
                    status = "有效";
                } else {
                    status = "已过期";
                }

                if (record.getDeviceId().equals(deviceId)) {
                    status += " (当前设备)";
                }

                Object[] rowData = {
                        record.getActivationCode(),
                        record.getDeviceId(),
                        sdf.format(record.getActivationDate()),
                        sdf.format(record.getExpirationDate()),
                        status
                };
                recordsModel.addRow(rowData);
            }
        });
        buttonPanel.add(refreshButton);

        JButton closeButton = new JButton("关闭");
        closeButton.setBackground(Color.GRAY);
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> recordsDialog.dispose());
        buttonPanel.add(closeButton);

        recordsDialog.add(buttonPanel, BorderLayout.SOUTH);
        recordsDialog.setVisible(true);
    }

    private void enableAllComponents() {
        // 启用所有组件
        addButton.setEnabled(true);
        nameField.setEnabled(true);
        hourSpinner.setEnabled(true);
        minuteSpinner.setEnabled(true);
        secondSpinner.setEnabled(true);
        autoSortCheckBox.setEnabled(true);
        activationButton.setEnabled(true);
        activationButton.setText("续期/查看激活信息");

        // 更新激活标签
        updateActivationLabel();

        // 启用手动排序按钮
        Component[] components = ((JPanel) ((JPanel) topPanel.getComponent(1)).getComponent(1)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().equals("手动排序") ||
                        button.getText().equals("全部开始") ||
                        button.getText().equals("全部重置") ||
                        button.getText().equals("关闭所有提示")) {
                    button.setEnabled(true);
                }
            } else if (comp instanceof JCheckBox) {
                ((JCheckBox) comp).setEnabled(true);
            }
        }

        // 启用删除所有任务按钮
        Component[] bottomComponents = ((JPanel) bottomPanel.getComponent(1)).getComponents();
        for (Component comp : bottomComponents) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().equals("删除所有任务")) {
                    button.setEnabled(true);
                }
            }
        }

        // 刷新表格
        refreshTable();

        // 重新验证和重绘界面
        revalidate();
        repaint();
    }

    private void showExpirationDialog() {
        JOptionPane.showMessageDialog(this,
                "软件激活已过期！\n" +
                        "请重新激活以继续使用完整功能。",
                "激活过期", JOptionPane.WARNING_MESSAGE);

        showActivationDialog();
    }

    private void showWarningDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }

    // 添加任务逻辑
    private static java.util.List<TimerTaskDate> timerTaskDates = new ArrayList<>();
    private static java.util.List<String> timerTaskNames = new ArrayList<>();

    static {
        timerTaskDates.add(new TimerTaskDate("树心28级别BOSS", 0, 15, 0, null));
        timerTaskDates.add(new TimerTaskDate("树心29级别BOSS", 0, 25, 0, null));
        timerTaskDates.add(new TimerTaskDate("树心30级别BOSS", 0, 40, 0, "精品"));

        timerTaskDates.add(new TimerTaskDate("树心31级别BOSS", 0, 15, 0, null));
        timerTaskDates.add(new TimerTaskDate("树心32级别BOSS", 0, 25, 0, null));
        timerTaskDates.add(new TimerTaskDate("树心33级别BOSS", 0, 40, 0, "精品"));
        timerTaskNames.add("树心30级别BOSS");
        timerTaskDates.add(new TimerTaskDate("戈壁36级别BOSS", 0, 15, 0, null));
        timerTaskDates.add(new TimerTaskDate("戈壁37级别BOSS", 0, 25, 0, null));
        timerTaskDates.add(new TimerTaskDate("戈壁38级别BOSS", 0, 40, 0, "精品"));
        timerTaskNames.add("戈壁38级别BOSS");
        timerTaskDates.add(new TimerTaskDate("戈壁41级别BOSS", 0, 15, 0, null));
        timerTaskDates.add(new TimerTaskDate("戈壁42级别BOSS", 0, 25, 0, null));
        timerTaskDates.add(new TimerTaskDate("戈壁43级别BOSS", 0, 40, 0, "精品"));

        timerTaskDates.add(new TimerTaskDate("戈壁46级别BOSS", 0, 15, 0, null));
        timerTaskDates.add(new TimerTaskDate("戈壁47级别BOSS", 0, 25, 0, null));
        timerTaskDates.add(new TimerTaskDate("戈壁48级别BOSS", 0, 40, 0, "精品"));
        timerTaskNames.add("戈壁48级别BOSS");
        timerTaskDates.add(new TimerTaskDate("戈壁51级别骑士BOSS", 0, 15, 0, null));
        timerTaskDates.add(new TimerTaskDate("戈壁52级别骑士BOSS", 0, 25, 0, null));
        timerTaskDates.add(new TimerTaskDate("戈壁53级别骑士BOSS", 0, 40, 0, "精品"));

        timerTaskDates.add(new TimerTaskDate("戈壁53级别龙BOSS", 0, 15, 0, null));
        timerTaskDates.add(new TimerTaskDate("戈壁54级别龙BOSS", 0, 25, 0, null));
        timerTaskDates.add(new TimerTaskDate("戈壁55级别龙BOSS", 0, 40, 0, "精品"));
        timerTaskNames.add("戈壁55级别BOSS");
        timerTaskDates.add(new TimerTaskDate("沼泽56级别BOSS", 0, 15, 0, null));
        timerTaskDates.add(new TimerTaskDate("沼泽57级别BOSS", 0, 25, 0, null));
        timerTaskDates.add(new TimerTaskDate("沼泽58级别BOSS", 0, 40, 0, "精品"));

        timerTaskDates.add(new TimerTaskDate("沼泽61级别BOSS", 0, 15, 0, null));
        timerTaskDates.add(new TimerTaskDate("沼泽62级别BOSS", 0, 25, 0, null));
        timerTaskDates.add(new TimerTaskDate("沼泽63级别BOSS", 0, 40, 0, "精品"));

        timerTaskDates.add(new TimerTaskDate("沼泽66级别BOSS", 0, 15, 0, null));
        timerTaskDates.add(new TimerTaskDate("沼泽67级别BOSS", 0, 25, 0, null));
        timerTaskDates.add(new TimerTaskDate("沼泽68级别BOSS", 0, 40, 0, "精品"));

        timerTaskDates.add(new TimerTaskDate("沼泽71级别BOSS", 0, 15, 0, null));
        timerTaskDates.add(new TimerTaskDate("沼泽72级别BOSS", 0, 25, 0, null));
        timerTaskDates.add(new TimerTaskDate("沼泽73级别BOSS", 0, 40, 0, "精品"));
//        timerTaskNames.add("沼泽73级别BOSS");
//        timerTaskDates.add(new TimerTaskDate("海岛71级别BOSS", 0, 15, 0, null));
//        timerTaskDates.add(new TimerTaskDate("海岛72级别BOSS", 0, 25, 0, null));
//        timerTaskDates.add(new TimerTaskDate("海岛73级别BOSS", 0, 40, 0, "精品"));
//
//        timerTaskDates.add(new TimerTaskDate("海岛75级别BOSS", 0, 15, 0, null));
//        timerTaskDates.add(new TimerTaskDate("海岛76级别BOSS", 0, 25, 0, null));
//        timerTaskDates.add(new TimerTaskDate("海岛77级别BOSS", 0, 40, 0, "精品"));
//
//        timerTaskDates.add(new TimerTaskDate("海岛81级别BOSS", 0, 15, 0, null));
//        timerTaskDates.add(new TimerTaskDate("海岛82级别BOSS", 0, 25, 0, null));
//        timerTaskDates.add(new TimerTaskDate("海岛83级别BOSS", 0, 40, 0, "精品"));
//        timerTaskNames.add("海岛83级别BOSS");
//        timerTaskDates.add(new TimerTaskDate("冰原75级别BOSS", 0, 15, 0, null));
//        timerTaskDates.add(new TimerTaskDate("冰原76级别BOSS", 0, 25, 0, null));
//        timerTaskDates.add(new TimerTaskDate("冰原77级别BOSS", 0, 40, 0, "精品"));
//
//        timerTaskDates.add(new TimerTaskDate("冰原81级别BOSS", 0, 15, 0, null));
//        timerTaskDates.add(new TimerTaskDate("冰原83级别BOSS", 0, 25, 0, null));
//        timerTaskDates.add(new TimerTaskDate("冰原87级别BOSS", 0, 40, 0, "精品"));
//
//        timerTaskDates.add(new TimerTaskDate("冰原88级别BOSS", 0, 15, 0, null));
//        timerTaskDates.add(new TimerTaskDate("冰原89级别BOSS", 0, 25, 0, null));
//        timerTaskDates.add(new TimerTaskDate("冰原90级别BOSS", 0, 40, 0, "精品"));
//
//        timerTaskDates.add(new TimerTaskDate("冰原91级别BOSS", 0, 15, 0, null));
//        timerTaskDates.add(new TimerTaskDate("冰原92级别BOSS", 0, 25, 0, null));
//        timerTaskDates.add(new TimerTaskDate("冰原93级别BOSS", 0, 40, 0, "精品"));
//        timerTaskNames.add("冰原93级别BOSS");
//        timerTaskDates.add(new TimerTaskDate("火山86级别BOSS", 0, 15, 0, null));
//        timerTaskDates.add(new TimerTaskDate("火山87级别BOSS", 0, 25, 0, null));
//        timerTaskDates.add(new TimerTaskDate("火山88级别BOSS", 0, 40, 0, "精品"));
//
//        timerTaskDates.add(new TimerTaskDate("火山91级别BOSS", 0, 15, 0, null));
//        timerTaskDates.add(new TimerTaskDate("火山92级别BOSS", 0, 25, 0, null));
//        timerTaskDates.add(new TimerTaskDate("火山93级别BOSS", 0, 40, 0, "精品"));
//
//        timerTaskDates.add(new TimerTaskDate("火山96级别BOSS", 0, 15, 0, null));
//        timerTaskDates.add(new TimerTaskDate("火山97级别BOSS", 0, 25, 0, null));
//        timerTaskDates.add(new TimerTaskDate("火山98级别BOSS", 0, 40, 0, "精品"));

        timerTaskDates.add(new TimerTaskDate("雷鸣蜘蛛精boss", 0, 60, 0, "精品"));
        timerTaskDates.add(new TimerTaskDate("树心蜘蛛精boss", 0, 60, 0, "精品"));
        timerTaskDates.add(new TimerTaskDate("戈壁蜘蛛精boss", 0, 60, 0, "精品"));
        timerTaskDates.add(new TimerTaskDate("沼泽蜘蛛精boss", 0, 60, 0, "精品"));
        timerTaskDates.add(new TimerTaskDate("沼泽定点蜘蛛精boss", 0, 60, 0, "精品"));
        timerTaskDates.add(new TimerTaskDate("海岛蜘蛛精boss", 0, 60, 0, "精品"));
        timerTaskDates.add(new TimerTaskDate("海岛定点蜘蛛精boss", 0, 60, 0, "精品"));
        timerTaskDates.add(new TimerTaskDate("火山蜘蛛精boss", 0, 60, 0, "精品"));
        timerTaskDates.add(new TimerTaskDate("冰原蜘蛛精boss", 0, 60, 0, "精品"));
        timerTaskDates.add(new TimerTaskDate("花雨蜘蛛精boss", 0, 60, 0, "精品"));
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

    private void addTasks() {
        // 添加任务
        timerTaskDates.forEach(timerTaskDate -> {
            addTimerTask(2, timerTaskDate.getName(), timerTaskDate.getHours(), timerTaskDate.getMinutes(), timerTaskDate.getSeconds(), timerTaskDate.getColorMark());
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // 顶部面板 - 激活信息和添加新任务
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // 激活信息面板
        JPanel activationPanel = new JPanel(new BorderLayout());
        activationPanel.setBorder(BorderFactory.createTitledBorder("激活信息"));
        activationPanel.setBackground(isActivated ? new Color(220, 255, 220) : NOT_ACTIVATED_COLOR);

        activationLabel = new JLabel();
        updateActivationLabel();
        activationLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        activationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        activationPanel.add(activationLabel, BorderLayout.CENTER);

        activationButton = new JButton("激活/续期");
        activationButton.setBackground(ACTIVATION_BUTTON_COLOR);
        activationButton.setForeground(Color.black);
        activationButton.addActionListener(e -> showActivationDialog());
        activationPanel.add(activationButton, BorderLayout.EAST);

        topPanel.add(activationPanel);

        // 添加新任务面板
        JPanel addTaskPanel = new JPanel();
        addTaskPanel.setLayout(new BoxLayout(addTaskPanel, BoxLayout.Y_AXIS));
        addTaskPanel.setBorder(BorderFactory.createTitledBorder("添加新任务"));

        // 输入面板
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        inputPanel.add(new JLabel("任务名称:"));
        nameField = new JTextField("任务1", 15);
        nameField.setEnabled(isActivated);
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("时:"));
        hourSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        hourSpinner.setPreferredSize(new Dimension(60, 25));
        hourSpinner.setEnabled(isActivated);
        inputPanel.add(hourSpinner);

        inputPanel.add(new JLabel("分:"));
        minuteSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 59, 1));
        minuteSpinner.setPreferredSize(new Dimension(60, 25));
        minuteSpinner.setEnabled(isActivated);
        inputPanel.add(minuteSpinner);

        inputPanel.add(new JLabel("秒:"));
        secondSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        secondSpinner.setPreferredSize(new Dimension(60, 25));
        secondSpinner.setEnabled(isActivated);
        inputPanel.add(secondSpinner);

        addButton = new JButton("添加任务");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.black);
        addButton.setEnabled(isActivated); // 未激活时禁用添加功能
        inputPanel.add(addButton);

        addTaskPanel.add(inputPanel);

        // 控制面板
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        autoSortCheckBox = new JCheckBox("自动排序运行中任务", true);
        autoSortCheckBox.setEnabled(isActivated);
        autoSortCheckBox.addActionListener(e -> {
            autoSortEnabled = autoSortCheckBox.isSelected();
            sortInfoLabel.setText("自动排序: " + (autoSortEnabled ? "已开启" : "已关闭"));
        });
        controlPanel.add(autoSortCheckBox);

        JButton manualSortButton = new JButton("手动排序");
        manualSortButton.setEnabled(isActivated);
        manualSortButton.addActionListener(e -> {
            sortTasks();
            refreshTable();
        });
        controlPanel.add(manualSortButton);

        // 添加批量操作按钮
        JButton startAllButton = new JButton("全部开始");
        startAllButton.setBackground(new Color(34, 139, 34));
        startAllButton.setForeground(Color.black);
        startAllButton.setEnabled(isActivated);
        startAllButton.addActionListener(e -> startAllTasks());
        controlPanel.add(startAllButton);

        JButton resetAllButton = new JButton("全部重置");
        resetAllButton.setBackground(new Color(70, 130, 180));
        resetAllButton.setForeground(Color.black);
        resetAllButton.setEnabled(isActivated);
        resetAllButton.addActionListener(e -> resetAllTasks());
        controlPanel.add(resetAllButton);

        JButton closeAllDialogsButton = new JButton("关闭所有提示");
        closeAllDialogsButton.setBackground(new Color(138, 43, 226));
        closeAllDialogsButton.setForeground(Color.black);
        closeAllDialogsButton.setEnabled(isActivated);
        closeAllDialogsButton.addActionListener(e -> closeAllCompletionDialogs());
        controlPanel.add(closeAllDialogsButton);

        addTaskPanel.add(controlPanel);
        topPanel.add(addTaskPanel);

        add(topPanel, BorderLayout.NORTH);

        // 中央面板 - 任务表格
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // 操作列、重置列、编辑列和删除列是可编辑的（用于按钮）
                return (column == 4 || column == 5 || column == 6 || column == 7) && isActivated;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4 || columnIndex == 5 || columnIndex == 6 || columnIndex == 7) {
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
        timerTable.getColumnModel().getColumn(4).setPreferredWidth(90);  // 操作列
        timerTable.getColumnModel().getColumn(5).setPreferredWidth(70);   // 重置列
        timerTable.getColumnModel().getColumn(6).setPreferredWidth(70);   // 编辑列
        timerTable.getColumnModel().getColumn(7).setPreferredWidth(70);   // 删除列

        // 设置按钮列的渲染器和编辑器
        timerTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonCellRenderer());
        timerTable.getColumnModel().getColumn(4).setCellEditor(new ButtonCellEditor());
        timerTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonCellRenderer());
        timerTable.getColumnModel().getColumn(5).setCellEditor(new ButtonCellEditor());
        timerTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonCellRenderer());
        timerTable.getColumnModel().getColumn(6).setCellEditor(new ButtonCellEditor());
        timerTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonCellRenderer());
        timerTable.getColumnModel().getColumn(7).setCellEditor(new ButtonCellEditor());

        // 设置其他列的自定义渲染器
        timerTable.setDefaultRenderer(Object.class, new ActivationCellRenderer());

        // 添加选择监听器
        ListSelectionModel selectionModel = timerTable.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                lastSelectedRow = timerTable.getSelectedRow();
            }
        });

        // 添加右键菜单
        timerTable.setComponentPopupMenu(createTablePopupMenu());

        JScrollPane scrollPane = new JScrollPane(timerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("倒计时任务列表 (右键点击行可查看更多操作)"));
        add(scrollPane, BorderLayout.CENTER);

        // 底部面板 - 信息和按钮
        bottomPanel = new JPanel(new BorderLayout());

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
        legendPanel.add(createLegendItem("<10秒", WARNING_COLOR));
        legendPanel.add(createLegendItem("<5秒", CRITICAL_COLOR));
        legendPanel.add(createLegendItem("已暂停", PAUSED_COLOR));
        legendPanel.add(createLegendItem("已完成", FINISHED_COLOR));

        leftPanel.add(legendPanel);
        bottomPanel.add(leftPanel, BorderLayout.WEST);

        // 右下方 - 批量操作按钮
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton removeAllButton = new JButton("删除所有任务");
        removeAllButton.setBackground(new Color(178, 34, 34));
        removeAllButton.setForeground(Color.black);
        removeAllButton.setEnabled(isActivated);
        removeAllButton.addActionListener(e -> removeAllTasks());
        rightPanel.add(removeAllButton);

        bottomPanel.add(rightPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // 添加事件监听器
        addButton.addActionListener(e -> addTimerTask(1, null, 0, 0, 0, null));
    }

    /**
     * 创建表格右键菜单
     */
    private JPopupMenu createTablePopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem editItem = new JMenuItem("编辑任务");
        editItem.addActionListener(e -> editSelectedTask());
        popupMenu.add(editItem);

        JMenuItem deleteItem = new JMenuItem("删除任务");
        deleteItem.addActionListener(e -> deleteSelectedTask());
        popupMenu.add(deleteItem);

        popupMenu.addSeparator();

        JMenuItem duplicateItem = new JMenuItem("复制任务");
        duplicateItem.addActionListener(e -> duplicateSelectedTask());
        popupMenu.add(duplicateItem);

        // 添加鼠标监听器，显示右键菜单
        timerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = timerTable.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < timerTable.getRowCount()) {
                        timerTable.setRowSelectionInterval(row, row);
                        popupMenu.show(timerTable, e.getX(), e.getY());
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = timerTable.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < timerTable.getRowCount()) {
                        timerTable.setRowSelectionInterval(row, row);
                        popupMenu.show(timerTable, e.getX(), e.getY());
                    }
                }
            }
        });

        return popupMenu;
    }

    /**
     * 编辑选中的任务
     */
    private void editSelectedTask() {
        int selectedRow = timerTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择一个任务", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TimerTask task = getTaskAtTableRow(selectedRow);
        if (task == null) return;

        showEditTaskDialog(selectedRow, task);
    }

    /**
     * 显示编辑任务对话框
     */
    private void showEditTaskDialog(int rowIndex, TimerTask task) {
        JDialog editDialog = new JDialog(this, "编辑任务", true);
        editDialog.setSize(600, 350); // 增加宽度以显示更长的任务名称
        editDialog.setLocationRelativeTo(this);
        editDialog.setLayout(new BorderLayout(10, 10));

        // 标题面板
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("编辑任务", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(EDIT_BUTTON_COLOR);
        titlePanel.add(titleLabel);
        editDialog.add(titlePanel, BorderLayout.NORTH);

        // 主内容面板
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // 任务名称面板
        JPanel namePanel = new JPanel(new BorderLayout(10, 5));
        JLabel nameLabel = new JLabel("任务名称:");
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        namePanel.add(nameLabel, BorderLayout.WEST);

        // 使用JTextArea并放在JScrollPane中，支持更长的文本
        JTextArea nameTextArea = new JTextArea(task.getName(), 2, 40); // 2行，40列
        nameTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        nameTextArea.setLineWrap(true);
        nameTextArea.setWrapStyleWord(true);
        JScrollPane nameScrollPane = new JScrollPane(nameTextArea);
        nameScrollPane.setPreferredSize(new Dimension(400, 60));
        namePanel.add(nameScrollPane, BorderLayout.CENTER);

        contentPanel.add(namePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 时间设置面板
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        int totalSeconds = task.getTotalSeconds();
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        // 小时
        JPanel hourPanel = new JPanel(new BorderLayout(5, 0));
        JLabel hourLabel = new JLabel("时:");
        hourLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        hourPanel.add(hourLabel, BorderLayout.WEST);
        JSpinner hourSpinner = new JSpinner(new SpinnerNumberModel(hours, 0, 23, 1));
        hourSpinner.setPreferredSize(new Dimension(80, 30));
        hourSpinner.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        hourPanel.add(hourSpinner, BorderLayout.CENTER);
        timePanel.add(hourPanel);

        // 分钟
        JPanel minutePanel = new JPanel(new BorderLayout(5, 0));
        JLabel minuteLabel = new JLabel("分:");
        minuteLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        minutePanel.add(minuteLabel, BorderLayout.WEST);
        JSpinner minuteSpinner = new JSpinner(new SpinnerNumberModel(minutes, 0, 59, 1));
        minuteSpinner.setPreferredSize(new Dimension(80, 30));
        minuteSpinner.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        minutePanel.add(minuteSpinner, BorderLayout.CENTER);
        timePanel.add(minutePanel);

        // 秒
        JPanel secondPanel = new JPanel(new BorderLayout(5, 0));
        JLabel secondLabel = new JLabel("秒:");
        secondLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        secondPanel.add(secondLabel, BorderLayout.WEST);
        JSpinner secondSpinner = new JSpinner(new SpinnerNumberModel(seconds, 0, 59, 1));
        secondSpinner.setPreferredSize(new Dimension(80, 30));
        secondSpinner.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        secondPanel.add(secondSpinner, BorderLayout.CENTER);
        timePanel.add(secondPanel);

        contentPanel.add(timePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 状态信息面板
        JPanel statusPanel = new JPanel();
        JLabel statusLabel = new JLabel("当前状态: " + task.getStatus() + " | 剩余时间: " + formatTime(task.getRemainingSeconds()));
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        statusLabel.setForeground(Color.BLUE);
        statusPanel.add(statusLabel);
        contentPanel.add(statusPanel);

        editDialog.add(contentPanel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton saveButton = new JButton("保存修改");
        saveButton.setBackground(EDIT_BUTTON_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        saveButton.setPreferredSize(new Dimension(120, 35));
        saveButton.addActionListener(e -> {
            String newName = nameTextArea.getText().trim();
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(editDialog, "任务名称不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int newHours = (int) hourSpinner.getValue();
            int newMinutes = (int) minuteSpinner.getValue();
            int newSeconds = (int) secondSpinner.getValue();
            int newTotalSeconds = newHours * 3600 + newMinutes * 60 + newSeconds;

            if (newTotalSeconds <= 0) {
                JOptionPane.showMessageDialog(editDialog, "总时间必须大于0秒", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 更新任务
            task.setName(newName);
            task.setTotalSeconds(newTotalSeconds);

            // 如果任务未完成且不在运行中，重置剩余时间
            if (!task.isCompleted() && !task.isRunning()) {
                task.setRemainingSeconds(newTotalSeconds);
            }

            // 刷新表格
            if (autoSortEnabled) {
                sortTasks();
            }
            refreshTable();

            JOptionPane.showMessageDialog(editDialog, "任务修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            editDialog.dispose();
        });
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("取消");
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        cancelButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.addActionListener(e -> editDialog.dispose());
        buttonPanel.add(cancelButton);

        editDialog.add(buttonPanel, BorderLayout.SOUTH);

        editDialog.setVisible(true);
    }

    /**
     * 删除选中的任务
     */
    private void deleteSelectedTask() {
        int selectedRow = timerTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择一个任务", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TimerTask task = getTaskAtTableRow(selectedRow);
        if (task == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要删除任务 '" + task.getName() + "' 吗？",
                "确认删除",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // 删除任务
            timerTasks.remove(selectedRow);

            // 清理相关数据
            completedTasksShown.remove(task.getId());
            if (completionDialogs.containsKey(task.getId())) {
                completionDialogs.get(task.getId()).dispose();
                completionDialogs.remove(task.getId());
            }

            // 重新索引按钮映射
            startButtonMap.clear();
            resetButtonMap.clear();
            editButtonMap.clear();
            deleteButtonMap.clear();

            // 刷新表格
            refreshTable();

            JOptionPane.showMessageDialog(this, "任务删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * 复制选中的任务
     */
    private void duplicateSelectedTask() {
        int selectedRow = timerTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择一个任务", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TimerTask task = getTaskAtTableRow(selectedRow);
        if (task == null) return;

        // 创建新任务（复制原任务）
        TimerTask newTask = new TimerTask(task.getName() + " (副本)", task.getTotalSeconds());
        timerTasks.add(newTask);

        if (autoSortEnabled) {
            sortTasks();
        }
        refreshTable();

        JOptionPane.showMessageDialog(this, "任务复制成功", "成功", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateActivationLabel() {
        if (isActivated) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            String expiryInfo;

            if (expirationDate != null) {
                calculateRemainingDays(); // 确保剩余天数已计算
                expiryInfo = " | 到期时间: " + sdf.format(expirationDate) +
                        " | 剩余天数: " + remainingDays + "天" +
                        " | 设备ID: " + deviceId.substring(0, 8) + "...";
            } else {
                expiryInfo = " | 有效期信息加载中...";
            }

            activationLabel.setText("软件已激活" + expiryInfo);
        } else {
            activationLabel.setText("软件未激活 | 请点击右侧按钮激活");
        }
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
                if (!isActivated) return;

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
        if (!isActivated) return;

        new Thread(() -> playCompletionSound()).start();
        SwingUtilities.invokeLater(() -> createNonModalCompletionDialog(task));
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
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> {
            dialog.dispose();
            completionDialogs.remove(task.getId());
        });
        buttonPanel.add(okButton);

        JButton autoCloseButton = new JButton("5秒后自动关闭");
        autoCloseButton.setBackground(new Color(34, 139, 34));
        autoCloseButton.setForeground(Color.WHITE);
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
        if (!isActivated) return;
        timerTasks.sort(new TimerTaskComparator());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        startButtonMap.clear();
        resetButtonMap.clear();
        editButtonMap.clear();
        deleteButtonMap.clear();

        for (int i = 0; i < timerTasks.size(); i++) {
            TimerTask task = timerTasks.get(i);

            JButton startButton = createStartButton(task, i);
            JButton resetButton = createResetButton(task, i);
            JButton editButton = createEditButton(task, i);
            JButton deleteButton = createDeleteButton(task, i);

            startButtonMap.put(i, startButton);
            resetButtonMap.put(i, resetButton);
            editButtonMap.put(i, editButton);
            deleteButtonMap.put(i, deleteButton);

            Object[] rowData = {
                    task.getName(),
                    formatTime(task.getTotalSeconds()),
                    formatTime(task.getRemainingSeconds()),
                    task.getStatus(),
                    startButton,
                    resetButton,
                    editButton,
                    deleteButton,
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
            buttonText = "重新开始";
            buttonColor = RESTART_BUTTON_COLOR;
        } else if (task.isRunning()) {
            buttonText = "暂停";
            buttonColor = PAUSE_BUTTON_COLOR;
        } else {
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
        button.setEnabled(isActivated);

        button.addActionListener(e -> handleStartAction(task, row));
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
        button.setEnabled(isActivated);

        button.addActionListener(e -> handleResetAction(task, row));
        return button;
    }

    /**
     * 创建编辑按钮
     */
    private JButton createEditButton(TimerTask task, int row) {
        JButton button = new JButton("编辑");
        button.setOpaque(true);
        button.setBackground(EDIT_BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setEnabled(isActivated);

        button.addActionListener(e -> {
            if (!isActivated) {
                showActivationRequiredDialog();
                return;
            }
            showEditTaskDialog(row, task);
        });
        return button;
    }

    /**
     * 创建删除按钮
     */
    private JButton createDeleteButton(TimerTask task, int row) {
        JButton button = new JButton("删除");
        button.setOpaque(true);
        button.setBackground(DELETE_BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setEnabled(isActivated);

        button.addActionListener(e -> {
            if (!isActivated) {
                showActivationRequiredDialog();
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "确定要删除任务 '" + task.getName() + "' 吗？",
                    "确认删除",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // 删除任务
                timerTasks.remove(row);

                // 清理相关数据
                completedTasksShown.remove(task.getId());
                if (completionDialogs.containsKey(task.getId())) {
                    completionDialogs.get(task.getId()).dispose();
                    completionDialogs.remove(task.getId());
                }

                // 重新索引按钮映射
                startButtonMap.clear();
                resetButtonMap.clear();
                editButtonMap.clear();
                deleteButtonMap.clear();

                // 刷新表格
                refreshTable();

                JOptionPane.showMessageDialog(this, "任务删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return button;
    }

    private void handleStartAction(TimerTask task, int row) {
        if (!isActivated) {
            showActivationRequiredDialog();
            return;
        }

        if (task.isCompleted()) {
            task.reset();
            task.start();
            completedTasksShown.remove(task.getId());
        } else if (task.isRunning()) {
            task.pause();
        } else {
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
        if (!isActivated) {
            showActivationRequiredDialog();
            return;
        }

        task.reset();
        completedTasksShown.remove(task.getId());

        if (autoSortEnabled) {
            sortTasks();
        }
        updateTableData();
        timerTable.repaint();
    }

    private void showActivationRequiredDialog() {
        JOptionPane.showMessageDialog(this,
                "此功能需要激活才能使用！\n" +
                        "请先激活软件。",
                "需要激活", JOptionPane.WARNING_MESSAGE);

        showActivationDialog();
    }

    private void updateTableData() {
        startButtonMap.clear();
        resetButtonMap.clear();
        editButtonMap.clear();
        deleteButtonMap.clear();

        for (int i = 0; i < timerTasks.size(); i++) {
            TimerTask task = timerTasks.get(i);
            if (i < tableModel.getRowCount()) {
                tableModel.setValueAt(task.getName(), i, 0);
                tableModel.setValueAt(formatTime(task.getTotalSeconds()), i, 1);
                tableModel.setValueAt(formatTime(task.getRemainingSeconds()), i, 2);
                tableModel.setValueAt(task.getStatus(), i, 3);

                JButton startButton = createStartButton(task, i);
                JButton resetButton = createResetButton(task, i);
                JButton editButton = createEditButton(task, i);
                JButton deleteButton = createDeleteButton(task, i);

                startButtonMap.put(i, startButton);
                resetButtonMap.put(i, resetButton);
                editButtonMap.put(i, editButton);
                deleteButtonMap.put(i, deleteButton);

                tableModel.setValueAt(startButton, i, 4);
                tableModel.setValueAt(resetButton, i, 5);
                tableModel.setValueAt(editButton, i, 6);
                tableModel.setValueAt(deleteButton, i, 7);
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
        if (!isActivated) {
            showActivationRequiredDialog();
            return;
        }

        String name = "";
        int hours, minutes, seconds;
        String colorMark;
        if (type == 1) {
            name = nameField.getText().trim();
            hours = (int) hourSpinner.getValue();
            minutes = (int) minuteSpinner.getValue();
            seconds = (int) secondSpinner.getValue();
            colorMark = _colorMark;
        } else {
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
        if (!isActivated) {
            showActivationRequiredDialog();
            return;
        }

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
        if (!isActivated) {
            showActivationRequiredDialog();
            return;
        }

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
        if (!isActivated) {
            showActivationRequiredDialog();
            return;
        }

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
            editButtonMap.clear();
            deleteButtonMap.clear();
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
        if (!isActivated) return;

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

            if (column == 4) {
                setText("操作");
                setBackground(Color.LIGHT_GRAY);
            } else if (column == 5) {
                setText("重置");
                setBackground(RESET_BUTTON_COLOR);
            } else if (column == 6) {
                setText("编辑");
                setBackground(EDIT_BUTTON_COLOR);
            } else if (column == 7) {
                setText("删除");
                setBackground(DELETE_BUTTON_COLOR);
            }

            setForeground(Color.WHITE);
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

            if (column == 6) {
                button.setText("编辑");
                button.setBackground(EDIT_BUTTON_COLOR);
            } else if (column == 7) {
                button.setText("删除");
                button.setBackground(DELETE_BUTTON_COLOR);
            } else {
                button.setText("按钮");
                button.setBackground(Color.LIGHT_GRAY);
            }

            button.setForeground(Color.WHITE);
            button.setOpaque(true);

            return button;
        }
    }

    // 自定义表格渲染器
    class ActivationCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            // 如果是按钮列（操作、重置、编辑、删除），直接返回按钮组件
            if (column == 4 || column == 5 || column == 6 || column == 7) {
                if (value instanceof JButton) {
                    JButton button = (JButton) value;
                    button.setOpaque(true);
                    return button;
                }

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
            if (!isActivated) {
                return NOT_ACTIVATED_COLOR;
            } else if (task.isCompleted()) {
                return FINISHED_COLOR;
            } else if (task.isRunning()) {
                int remaining = task.getRemainingSeconds();
                if (remaining <= 0) {
                    return FINISHED_COLOR;
                } else if (remaining <= 5) {
                    return CRITICAL_COLOR;
                } else if (remaining <= 10) {
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

    // 倒计时任务类 - 增加了setter方法用于编辑
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

        // Getter 方法
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

        // Setter 方法（用于编辑功能）
        public void setName(String name) {
            this.name = name;
        }

        public void setTotalSeconds(int totalSeconds) {
            this.totalSeconds = totalSeconds;
        }

        public void setRemainingSeconds(int remainingSeconds) {
            this.remainingSeconds = remainingSeconds;
        }
    }

    // 加密方法
    private String encrypt(String data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY_BYTES, ENCRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            System.err.println("加密失败: " + e.getMessage());
            return null;
        }
    }

    // 解密方法
    private String decrypt(String encryptedData) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY_BYTES, ENCRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("解密失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 测试加密解密功能的方法（可选）
     */
    private void testEncryption() {
        String testData = "TEST-ACTIVATION-CODE";
        System.err.println("原始数据: " + testData);

        String encrypted = encrypt(testData);
        System.err.println("加密后: " + encrypted);

        String decrypted = decrypt(encrypted);
        System.err.println("解密后: " + decrypted);

        System.err.println("加解密测试结果: " + testData.equals(decrypted));
    }

    /**
     * 生成16字节的AES密钥
     */
    private static byte[] getAESKey(String keyString) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(keyString.getBytes(StandardCharsets.UTF_8));
            return digest; // MD5生成16字节的哈希值
        } catch (NoSuchAlgorithmException e) {
            // 如果MD5不可用，使用简单的方法确保16字节
            byte[] bytes = keyString.getBytes(StandardCharsets.UTF_8);
            if (bytes.length >= 16) {
                return Arrays.copyOf(bytes, 16);
            } else {
                // 填充到16字节
                byte[] result = new byte[16];
                System.arraycopy(bytes, 0, result, 0, bytes.length);
                return result;
            }
        }
    }

    /**
     * ACT-1234-5678-9012-3456 (约7天)
     * ACT-1111-2222-3333-4444 (约15天)
     * ACT-5555-6666-7777-8888 (约30天)
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FixedActivationCountdownTimer fixedActivationCountdownTimer = new FixedActivationCountdownTimer();
            // 可选：测试加密功能
            fixedActivationCountdownTimer.testEncryption();
        });
    }
}