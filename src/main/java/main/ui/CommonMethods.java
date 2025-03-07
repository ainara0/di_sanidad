package main.ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalTime;

public class CommonMethods {
    public static BufferedImage getBufferedImage(String url) {
        try {
            return ImageIO.read(new File(url));
        } catch (IOException e) {
            System.out.println("Could not load image: " + url);
        }
        return null;
    }
    public static ImageIcon getImageIcon(String url) {
        BufferedImage bufferedImage = getBufferedImage(url);
        if (bufferedImage == null) {
            return null;
        }
        return new ImageIcon(bufferedImage);
    }

    public static ImageIcon getImageIcon(String url, int width, int height) {
        BufferedImage bufferedImage = getBufferedImage(url);
        if (bufferedImage == null) {
            return null;
        }
        return new ImageIcon(bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
    public static JLabel getImageLabel(String url, int width, int height) {
        ImageIcon image = getImageIcon(url, width, height);
        if (image == null) {
            return new JLabel("Image not found");
        }
        return new JLabel(image);
    }
    public static ImageIcon getSVGImage(String url, int width, int height) {
        FlatSVGIcon flatSVGIcon = new FlatSVGIcon(url, width, height);
        if (flatSVGIcon == null) {
            return null;
        }
        return new ImageIcon(flatSVGIcon.getImage());
    }
    public static JLabel getSVGLabel(String url, int width, int height) {
        return new JLabel(getSVGImage(url, width, height));
    }
    public static Font getFontFromPath(String path) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File(path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void setDimension(JComponent component, int width, int height) {
        Dimension buttonDimension = new Dimension(width, height);
        component.setPreferredSize(buttonDimension);
        component.setMinimumSize(buttonDimension);
        component.setMaximumSize(buttonDimension);
    }

    public static String hash(String input) {
        try {
            MessageDigest digest  = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (Exception e) {
            System.out.println("Could not hash password");
        }
        return null;
    }
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String getDateString(LocalDate appointmentDate) {
        String day = appointmentDate.getDayOfMonth() < 10 ? "0" + appointmentDate.getDayOfMonth() : "" + appointmentDate.getDayOfMonth();
        String month = appointmentDate.getMonthValue() < 10 ? "0" + appointmentDate.getMonthValue() : "" + appointmentDate.getMonthValue();
        String year = "" + appointmentDate.getYear();
        return day + "/" +  month + "/" + year;
    }

    public static String getTimeString12H(LocalTime appointmentTime) {
        String timeString;
        int hourNumber = appointmentTime.getHour();
        String minute = appointmentTime.getMinute() < 10 ? "0" + appointmentTime.getMinute() : "" + appointmentTime.getMinute();
        String frame;
        if (hourNumber < 12) {
            frame = "AM";
        } else {
            hourNumber -= 12;
            frame = "PM";
        }
        if (hourNumber == 0) {
            hourNumber = 12;
        }
        String hour = hourNumber < 10 ? "0" + hourNumber : "" + hourNumber;
        timeString = hour + ":" + minute + " " + frame;
        System.out.println(timeString);
        return timeString;
    }

    public static String getTimeString24H(LocalTime appointmentTime) {
        String timeString = "";
        String hour = appointmentTime.getHour() < 10 ? "0" + appointmentTime.getHour() : "" + appointmentTime.getHour();
        String minute = appointmentTime.getMinute() < 10 ? "0" + appointmentTime.getMinute() : "" + appointmentTime.getMinute();
        timeString = hour + ":" + minute;
        return timeString;
    }

    public static JLabel getLogoLabel(int width, int height) {
        return CommonMethods.getImageLabel("src/main/resources/icons/logo.png",200,200);
    }

}
