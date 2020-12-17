package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main {

    static JFrame mainFrame = getMenuJFrame();
    static Container container1 = mainFrame.getContentPane();
    static Container container2 = mainFrame.getContentPane();
    static JPanel jPanel = new JPanel();

    // Метод для создания окна для графического отображения меню программы
    static JFrame getMenuJFrame() {

        JFrame jFrame = new JFrame(); // пустое окно
        jFrame.setVisible(true); // сделали выдимым
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // возможность закрывать окно
        Toolkit toolkit = Toolkit.getDefaultToolkit(); // набор инструментов для работы с размерами окна
        Dimension dimension = toolkit.getScreenSize(); // подстраиваем размеры окна под размеры экрана
        jFrame.setBounds(dimension.width / 2 - 500, dimension.width / 2 - 600, 800, 500); // устанавивает размеры и положение окна
        jFrame.setTitle("Пути и циклы в графе"); // заголовок окна

        return jFrame;
    }

    public static void main(String[] args) {

        mainFrame.add(jPanel);
        jPanel.add(new ProgramMenu());

    }
}
