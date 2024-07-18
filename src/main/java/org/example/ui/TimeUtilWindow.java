package org.example.ui;

import com.intellij.openapi.util.Pair;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeUtilWindow extends JPanel {
    private final JTextField textField10;
    private final JTextField textField13;
    private final JTextField textField16;
    private final JTextField timestampField10ToStr;
    private final JTextField stringToTimestampField;
    private final JTextField beijingTimeField;
    private final JTextField utcTimeField;

    public TimeUtilWindow() {
        // 使用GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = JBUI.insets(5); // 设置组件间的间距

        // 创建标签和文本框
        JLabel label10 = new JLabel("10位时间戳（秒）:");
        textField10 = new JTextField(15);
        textField10.setEditable(false);

        JLabel label13 = new JLabel("13位时间戳（毫秒）:");
        textField13 = new JTextField(15);
        textField13.setEditable(false);

        JLabel label16 = new JLabel("16位时间戳（微秒）:");
        textField16 = new JTextField(15);
        textField16.setEditable(false);

        // 创建按钮
        JButton refreshButton = new JButton("刷新");

        // 添加组件到面板
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(label10, gbc);

        gbc.gridx = 1;
        add(textField10, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(label13, gbc);

        gbc.gridx = 1;
        add(textField13, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(label16, gbc);

        gbc.gridx = 1;
        add(textField16, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(refreshButton, gbc);

        // 添加横线
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(new JSeparator(), gbc);

        // 重置网格宽度和填充
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        // 创建10位时间戳转字符串组件
        JLabel timestampLabel10ToStr = new JLabel("10位时间戳（秒）:");
        timestampField10ToStr = new JTextField(15);
        JButton convertToStringButton = new JButton("时间戳转字符串");

        // 创建字符串转10位时间戳组件
        JLabel stringToTimestampLabel = new JLabel("时间字符串:");
        stringToTimestampField = new JTextField(15);
        JButton convertToTimeStampButton = new JButton("字符串转时间戳");

        // 添加10位时间戳转字符串组件到面板
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(timestampLabel10ToStr, gbc);

        gbc.gridx = 1;
        add(timestampField10ToStr, gbc);

        gbc.gridx = 2;
        add(convertToStringButton, gbc);

        // 添加字符串转10位时间戳组件到面板
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(stringToTimestampLabel, gbc);

        gbc.gridx = 1;
        add(stringToTimestampField, gbc);

        gbc.gridx = 2;
        add(convertToTimeStampButton, gbc);

        // 创建结果显示组件
        JLabel beijingTimeLabel = new JLabel("北京时间:");
        beijingTimeField = new JTextField(15);
        JLabel utcTimeLabel = new JLabel("UTC时间:");
        utcTimeField = new JTextField(15);

        // 添加结果显示组件到面板
        gbc.gridx = 0;
        gbc.gridy = 7;
        add(beijingTimeLabel, gbc);

        gbc.gridx = 1;
        add(beijingTimeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        add(utcTimeLabel, gbc);

        gbc.gridx = 1;
        add(utcTimeField, gbc);

        // 设置按钮的点击事件
        refreshButton.addActionListener(e -> refreshTimeStamps());

        convertToStringButton.addActionListener(e -> convertTimestampToString());

        convertToTimeStampButton.addActionListener(e -> convertStringToTimestamp());

        // 初始化时间戳
        refreshTimeStamps();
    }

    private void refreshTimeStamps() {
        // 获取当前时间的13位时间戳（毫秒）
        long currentTimeMillis = System.currentTimeMillis();
        String timeStamp13 = String.valueOf(currentTimeMillis);

        // 获取当前时间的10位时间戳（秒）
        long currentTimeSeconds = currentTimeMillis / 1000;
        String timeStamp10 = String.valueOf(currentTimeSeconds);

        // 获取当前时间的16位时间戳（微秒）
        long currentTimeMicros = ChronoUnit.MICROS.between(Instant.EPOCH, Instant.now());
        String timeStamp16 = String.valueOf(currentTimeMicros);

        // 设置文本框的值
        textField10.setText(timeStamp10);
        textField13.setText(timeStamp13);
        textField16.setText(timeStamp16);

        Pair<String, String> timeStr = getTimeStr(timeStamp10);
        timestampField10ToStr.setText(timeStamp10);
        beijingTimeField.setText(timeStr.getFirst());
        utcTimeField.setText(timeStr.getSecond());
    }

    private Pair<String, String> getTimeStr(String origin10BitTsStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        long timeStamp = Long.parseLong(origin10BitTsStr);
        Instant instant = Instant.ofEpochSecond(timeStamp);
        LocalDateTime beijingTime = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Shanghai"));
        String beijingTimeStr = beijingTime.format(formatter);
        String utcTimeStr = DateTimeFormatter.ISO_INSTANT.format(instant);

        return new Pair<>(beijingTimeStr, utcTimeStr);
    }

    private void convertTimestampToString() {
        try {
            long timeStamp = Long.parseLong(timestampField10ToStr.getText().trim());
            Instant instant = Instant.ofEpochSecond(timeStamp);
            LocalDateTime beijingTime = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Shanghai"));
            String utcTime = DateTimeFormatter.ISO_INSTANT.format(instant);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            beijingTimeField.setText(beijingTime.format(formatter));
            utcTimeField.setText(utcTime);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的时间戳", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void convertStringToTimestamp() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime beijingTime = LocalDateTime.parse(stringToTimestampField.getText().trim(), formatter);
            Instant instant = beijingTime.atZone(ZoneId.of("Asia/Shanghai")).toInstant();
            timestampField10ToStr.setText(String.valueOf(instant.getEpochSecond()));

            // 同步 UTC 时间
            LocalDateTime utcTime = beijingTime.atZone(ZoneId.of("Asia/Shanghai")).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
            utcTimeField.setText(utcTime.format(formatter));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "请输入有效的时间字符串", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}