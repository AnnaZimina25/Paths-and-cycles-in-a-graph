package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import static com.company.Main.jPanel;
import static com.company.Main.mainFrame;

// Меню программы
public class ProgramMenu extends JMenu {
    private JMenuItem showGraph1;
    private JMenuItem showGraph2;
    private JMenuItem getVertex;

    private static Graph graph1, graph2;
    private static int[][] graphMatrixOnOk;
    private static boolean isData1Ok, isData2Ok;
    private static DrawGraph1 drawGraph1;
    private static DrawGraph2 drawGraph2;
    public static int vertexCountOnOk, start, end;

    // Конструктор меню
    public ProgramMenu() {

        // Задаем параметры окна
        super("Пути и циклы в графе", true);

        // Обрабатываем меню программы:
        // - добавили главные каталоги
        JMenuBar jMenuBar = new JMenuBar();
        JMenu program = new JMenu("Программа");
        JMenu inputData = new JMenu("Данные");
        JMenu result = new JMenu("Поиск пути");

        jMenuBar.add(program);
        jMenuBar.add(inputData);
        jMenuBar.add(result);

        // - добавили подгаталоги во вкладку Программа:
        JMenuItem aboutProgram = program.add(new JMenuItem("О программе"));
        program.addSeparator();
        JMenuItem exit = program.add(new JMenuItem("Выход"));

        // - добавили подгаталоги во вкладку Данные:
        JMenuItem getData = inputData.add(new JMenuItem("Задать параметры"));
        showGraph1 = inputData.add(new JMenuItem("Показать исходный граф"));
        showGraph1.setEnabled(false); // сделали невидимым (неактивным)

        // - добавили подгаталоги во вкладку Поиск пути:
        getVertex = result.add(new JMenuItem("Задать вершины пути"));
        showGraph2 = result.add(new JMenuItem("Вывести в графическом формате"));
        getVertex.setEnabled(false);
        showGraph2.setEnabled(false);
        result.add(showGraph2);

        mainFrame.setJMenuBar(jMenuBar);
        mainFrame.revalidate(); // для корректного отображения

        // Прописываем работу подкаталогов:
        // - вкладка выход
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        aboutProgram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(new JPanel(), "Данная программа визуализирует граф с заданным количеством вершин и списком смежности.\r\n" +
                        "Для отображения графа задайте количество вершин и список смежности.\r\n" +
                        "Обратите внимание, что вершины нумеруются с 0.\r\n" +
                        "Пример ввода списка смежности: 0 1,0 2,0 3,3 1,2 4...\r\n" +
                        "После ввода данных графа можно посмотреть его изображение.\r\n" +
                        "Так же программа ищет центр графа и может найти путь между вершинами непроходящий через центр.\r\n" +
                        "Для поиска пути задайте вершины начала и конца пути. После этого можно визуализировать решение.");

            }
        });

        // - вкладка задать параметры
        getData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getDataMethod();
            }
        });

        // - вкладка показать исходный граф
        showGraph1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGraph1Method();
            }
        });

        // - вкладка задать вершины пути
        getVertex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getVertexMethod();
            }
        });

        // - показать резульат
        showGraph2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGraph2Method();
            }
        });
    }

    // Прописываем дйствия методов:
    // - получить данные графа
    private void getDataMethod () {
        // synchronized (graph1) {

        // Открываем окно диалога
        GetGraphDialog dialog = new GetGraphDialog();
        dialog.pack();
        dialog.setVisible(true);

        // Проверяем получившийся граф и выводим в консоль данные:
        if (isData1Ok) { // если граф задан корректно

            graph1 = new Graph(vertexCountOnOk, graphMatrixOnOk);
            graph1.setCenter();

            // Выводим результаты диалога в консоль
            System.out.println("Количество вершин: " + graph1.getVertexCount());
            System.out.println("Матрица cмежности:");
            for (int i = 0; i < graph1.getVertexCount(); i++) {
                for (int j = 0; j < graph1.getVertexCount(); j++) {
                    System.out.print(graph1.getMatrix()[i][j] + " ");
                }
                System.out.println();
            }
            graph1.setCenter();
            System.out.println(graph1.getCenterStr());
            drawGraph1 = new DrawGraph1();

            showGraph1.setEnabled(true); // активировали вкладку визуализации
            getVertex.setEnabled(true); // и задания вершин для поиска пути
            showGraph2.setEnabled(false); // дезактивировали вкладку второй визуализации (на случай повторного расчета)


        } else {
            JOptionPane.showMessageDialog(new JPanel(), "Данные введены некорректо!\r\n" +
                    "Задайте параметры графа еще раз.");
        }
    }

    // - вывести изображение исходного графа
    private void showGraph1Method () {

        Main.container1.removeAll();
        Main.container1.add(new JPanel().add(drawGraph1));
        Main.container1.revalidate();

    }

    // - получить вершины для поиска пути
    private void getVertexMethod () {
        // Открываем окно диалога
        VertexPathDialog dialog = new VertexPathDialog();
        dialog.pack();
        dialog.setVisible(true);

        if (isData2Ok) { // если точки заданы корректно

            // Выводим результаты диалога в консоль
            System.out.println("Начало пути: " + start);
            System.out.println("Конец пути: " + end);
            int[][] matrix2 = graph1.removeCenterMatrix(graph1.getCenter());
            graph2 = new Graph(vertexCountOnOk, matrix2);
            ArrayList<Integer> path = new ArrayList<>();
            path = graph2.getPathDFS(start, end, path);
            System.out.println(graph2.getPathStr());

            drawGraph2 = new DrawGraph2();
            showGraph2.setEnabled(true); // активировали вкладку визуализации
            showGraph1.setEnabled(false); // дезактивировали вкладку визуализации первого графа
        } else {
            JOptionPane.showMessageDialog(new JPanel(), "Вершины начала и конца пути заданы некорректо!\r\n" +
                    "Укажите вершины пути еще раз.");
        }
    }

    // - вывести изображние результата
    private void showGraph2Method () {
        Main.container2.removeAll();
        Main.container2.add(new JPanel().add(drawGraph2));
        Main.container2.revalidate();

    }

    private class DrawGraph1 extends JComponent {

        @Override
        protected void paintComponent (Graphics g) {

            int n = vertexCountOnOk;
            int[][] matrix = graphMatrixOnOk;
           // graph1 = new Graph(vertexCountOnOk, graphMatrixOnOk);

            // Выводим результаты диалога на экран меню
            Font font = new Font("Bitstream Charter", Font.BOLD, 20); // настройки шрифта
            Graphics2D g2 = (Graphics2D) g; // преобразовываем обьект класса Graphics в объект класса Graphics2D (дает дополнительные функции)
            g2.setFont(font); // установили шрифт
            g2.drawString("Визуализация заданного графа.", 20, 20);
            g2.drawString(graph1.getCenterStr(), 20, 380);

            // Рисуем граф по точкам:
            for (int i = 0; i < n; i++) {

                double x1 = 350 + 150 * Math.cos(2 * Math.PI * i / n); // координаты первой вершины
                double y1 = 200 + 150 * Math.sin(2 * Math.PI * i / n);
                g2.setColor(Color.BLUE);
                g2.drawString(i + "", (int) x1, (int) y1); // рисуем номер вершины в указанных координатах
                Ellipse2D ell = new Ellipse2D.Double(x1 - 23, y1 - 23, 46, 46); // кружок вершины
                g2.draw(ell); // рисуем кружок вершины

                for (int j = 0; j < n; j++) {
                    g2.setColor(Color.BLACK);
                    double x2 = 350 + 150 * Math.cos(2 * Math.PI * j / n); // координаты второй точки
                    double y2 = 200 + 150 * Math.sin(2 * Math.PI * j / n);

                    Line2D line = new Line2D.Double(x1, y1, x2, y2); // параметры линии от и до

                    if (matrix[i][j] == 1) { // если есть ребро, то рисуем линию между точками
                        g2.draw(line); // рисуем линию
                    }
                }
            }

        }
    }


    private class DrawGraph2 extends JComponent {
        protected void paintComponent (Graphics g) {
            synchronized (Graph.class) {

                int n = vertexCountOnOk;
                ArrayList<Integer> path = graph2.getPathList();

                Font font = new Font("Bitstream Charter", Font.BOLD, 20); // настройки шрифта
                Graphics2D g2 = (Graphics2D) g; // преобразовываем обьект класса Graphics в объект класса Graphics2D (дает дополнительные функции)
                g2.setFont(font); // установили шрифт
                g2.drawString("Визуализация решения (граф с удаленным центром):", 20, 20);
                g2.drawString(graph2.getPathStr(), 20, 380);

                // Рисуем граф по точкам:
                for (int i = 0; i < n; i++) {

                    double x1 = 350 + 150 * Math.cos(2 * Math.PI * i / n); // координаты первой вершины
                    double y1 = 200 + 150 * Math.sin(2 * Math.PI * i / n);
                    g2.setColor(Color.BLUE);
                    g2.drawString(i + "", (int) x1, (int) y1); // рисуем номер вершины в указанных координатах
                    Ellipse2D ell = new Ellipse2D.Double(x1 - 23, y1 - 23, 46, 46); // кружок вершины
                    g2.draw(ell); // рисуем кружок вершины

                    for (int j = 0; j < n; j++) {
                        g2.setColor(Color.BLACK);

                        if (path != null) {
                            if (path.contains(i) && path.contains(j)) {
                                g2.setColor(Color.RED); // g.setPaint(..)
                            }

                            double x2 = 350 + 150 * Math.cos(2 * Math.PI * j / n); // координаты второй вершины
                            double y2 = 200 + 150 * Math.sin(2 * Math.PI * j / n);

                            Line2D line = new Line2D.Double(x1, y1, x2, y2); // параметры линии от и до

                            if (graph2.getMatrix()[i][j] == 1) { // если есть ребро, то рисуем линию между точками
                                g2.draw(line); // рисуем линию
                            }
                        }

                    }
                }
            }
        }
    }

    // Диалоговое окно для задания параметров графа
    private class GetGraphDialog extends JDialog {
        private JPanel contentPane = jPanel;
        private JButton buttonOK;
        private JButton buttonCancel;
        private JTextField vertexTxt;
        private JTextField matrixInput;

        // Конструктор диалога
        public GetGraphDialog() {

            // Задаем параметры окна
            super(mainFrame, "Ввод параметров графа", true);
            setBounds(400, 400, 400, 150); // Задаём размеры окна
            getRootPane().setDefaultButton(buttonOK);

            // Определяем положение кнопок и надписей в окне
            setLayout(new GridBagLayout()); // табличное расположение
            GridBagConstraints gbc1 = new GridBagConstraints(); // положение подписи кол-во вершин
            gbc1.anchor = GridBagConstraints.EAST;
            gbc1.weightx = 0; // возможность растягивания
            gbc1.weighty = 0;
            gbc1.gridx = 0;   // с какой клетки рисуем
            gbc1.gridy = 0;
            gbc1.gridwidth = 3; // сколько столбцов таблицы занимает (длина)
            gbc1.gridheight = 4; // сколько строк таблицы занимает (высота)

            GridBagConstraints gbc2 = new GridBagConstraints(); // положение подписи список пар вершин
            gbc2.anchor = GridBagConstraints.EAST;
            gbc2.weightx = 0; // возможность растягивания
            gbc2.weighty = 0;
            gbc2.gridx = 0;   // с какой клетки рисуем
            gbc2.gridy = 7;
            gbc2.gridwidth = 4; // сколько столбцов таблицы занимает (длина)
            gbc2.gridheight = 4; // сколько строк таблицы занимает (высота)

            GridBagConstraints gbc3 = new GridBagConstraints(); // положение поля кол-ва вершин
            gbc3.fill = GridBagConstraints.BOTH;
            //gbc3.anchor = GridBagConstraints.EAST;
            gbc3.weightx = 0.2f; // возможность растягивания
            gbc3.weighty = 0;
            gbc3.gridx = 3;   // с какой клетки рисуем
            gbc3.gridy = 0;
            gbc3.gridwidth = 7; // сколько столбцов таблицы занимает (длина)
            gbc3.gridheight = 4; // сколько строк таблицы занимает (высота)

            GridBagConstraints gbc4 = new GridBagConstraints(); // положение поля списка вершин
            gbc4.fill = GridBagConstraints.BOTH;
            gbc4.weightx = 1.0f; // возможность растягивания
            gbc4.weighty = 0;
            gbc4.gridx = 3;   // с какой клетки рисуем
            gbc4.gridy = 7;
            gbc4.gridwidth = 10; // сколько столбцов таблицы занимает (длина)
            gbc4.gridheight = 4; // сколько строк таблицы занимает (высота)

            GridBagConstraints gbc5 = new GridBagConstraints(); // положение кнопки принять
            gbc5.weightx = 0; // возможность растягивания
            gbc5.weighty = 0;
            gbc5.gridx = 0;   // с какой клетки рисуем
            gbc5.gridy = 14;
            gbc5.gridwidth = 2; // сколько столбцов таблицы занимает (длина)
            gbc5.gridheight = 2; // сколько строк таблицы занимает (высота)

            GridBagConstraints gbc6 = new GridBagConstraints(); // положение кнопки отмена
            gbc6.weightx = 0; // возможность растягивания
            gbc6.weighty = 0;
            gbc6.gridx = 3;   // с какой клетки рисуем
            gbc6.gridy = 14;
            gbc6.gridwidth = 2; // сколько столбцов таблицы занимает (длина)
            gbc6.gridheight = 2; // сколько строк таблицы занимает (высота)

            // Инициализиуем поля
            this.vertexTxt = new JTextField(10); // кол-во вершин (стр)
            this.matrixInput = new JTextField(50); // строка с парами вершин
            this.buttonOK = new JButton("Принять");
            this.buttonCancel = new JButton("Отмена");

            // Добавляем кнопки
            add(new JLabel("Количество вершин: "), gbc1);
            add(new JLabel("Списо смежности: "), gbc2);
            add(vertexTxt, gbc3);
            add(matrixInput, gbc4);
            add(buttonOK, gbc5);
            add(buttonCancel, gbc6);

            // Прописываем действия кнопок
            buttonOK.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onOK();
                }
            });

            buttonCancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onCancel();
                }
            });

            // call onCancel() when cross is clicked
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    onCancel();
                }
            });

            // call onCancel() on ESCAPE
            contentPane.registerKeyboardAction(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onCancel();
                }
            }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        }

        // Прописываем методы для действий
        // Метод срабатывает при нажатии принять:
        private void onOK() {
            // add your code here
            synchronized (Graph.class) {
                isData1Ok = false;
                String vertexStr = "";
                String matrixTxt = "";

                try {
                    vertexStr = vertexTxt.getText(); // считываем строку кол-ва вершин
                    matrixTxt = matrixInput.getText(); // считываем строку с множеством пар вершин
                    vertexCountOnOk = Integer.parseInt(vertexStr); // строку с числом приводим к формату int

                    // Проверяем введенные данные:
                    if (vertexCountOnOk <= 0) { // кол-во вершин должно быть >0
                        JOptionPane.showMessageDialog(new JPanel(), "Количество вершин должно быть больше 0!\r\nПопробуйте снова.");
                        dispose();
                        isData1Ok = false;

                    } else {
                        // Обрабатываем строку смежности (пытаемся получить матрицу смежности):
                        int[][] resultMatrix = new int[vertexCountOnOk][vertexCountOnOk]; // пустая матрица смежности
                        String[] matrixStr = matrixTxt.split(","); // убираем , символ и получаем массив из пар и пробелов

                        for (String pairLine : matrixStr) { // для каждой пары из массива выполняем
                            pairLine.trim(); // удаляем пробелы в начале и конце строки, чтобы остался пробел только между числами
                            String[] pairStr = pairLine.split(" "); // делим на подмассив убирая пробелы внутри строки

                            if (pairStr.length == 2) { // если в массиве не 2 числа, значит ошибка ввода данных
                                try {
                                    int i = Integer.parseInt(pairStr[0]);
                                    int j = Integer.parseInt(pairStr[1]);

                                    if (i < vertexCountOnOk && j < vertexCountOnOk) {
                                        resultMatrix[i][j] = 1;
                                        resultMatrix[j][i] = 1;
                                    } else {
                                        JOptionPane.showMessageDialog(new JPanel(), "Вершины нумеруются с 0!\r\n" +
                                                "Пример ввода строки смежности: 1 2,3 5... \r\nНомер вершины не может превышать количество вершин. " +
                                                "\r\nПроверьте строку смежности и попробуйте снова.");
                                        isData1Ok = false;
                                        dispose(); // если нарушаем условие, то закрываем окно
                                        break; // и выхожим из цикла
                                    }
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(new JPanel(), "Неверный формат введенных данных!\r\n" +
                                            "Пример ввода строки смежности: 1 2,3 5... \r\nНомера вершин должен быть целыми числами." +
                                            "\r\nПроверьте строку смежности и попробуйте снова.");
                                    isData1Ok = false;
                                    dispose();
                                    break;
                                }

                            } else {
                                JOptionPane.showMessageDialog(new JPanel(), "Неверный формат введенных данных!\r\n" +
                                        "Пример ввода строки смежности: 1 2,3 5... \r\nВершины нумеруются c 0." +
                                        "Проверьте строку смежности и попробуйте снова.");
                                isData1Ok = false;
                                dispose();
                                break;
                            }
                        }
                        graphMatrixOnOk = resultMatrix;
                        //graph1 = new Graph(vertexCountOnOk, graphMatrixOnOk);
                        isData1Ok = true;
                    }

                } catch (NullPointerException ex) { // ловим исключение, если была введена пустая строка
                    JOptionPane.showMessageDialog(new JPanel(), "Данные введены не полностью!\r\n" +
                            " Пожалуйста, заполните все поля."); // выводим всплывающее окно с предупреждением
                    isData1Ok = false;
                    dispose();

                } catch (NumberFormatException ex) { // ловим исключение, если формат неверный (невозможно перевести в int)
                    JOptionPane.showMessageDialog(new JPanel(), "Неверный формат введенных данных!\r\n" +
                            "Проверьте количество вершин, оно должно быть целым числом.\r\n" +
                            "Вершины нумеруются с 0.");
                    isData1Ok = false;
                    dispose();
                }
            }
            dispose(); // закрываем окно если все данные введены верно
        }

        // Метод срабатывает при нажатии отмена:
        private void onCancel () {
            // add your code here if necessary
            dispose();
        }
    }

    // Диалоговое окно для задания начальной и конечной вершин пути
    private class VertexPathDialog extends JDialog{
        private JPanel contentPane = jPanel;
        private JButton buttonOK;
        private JButton buttonCancel;
        private JTextField startInput;
        private JTextField endInput;

        // Конструктор диалога
        public VertexPathDialog() {

            // Задаем наполнение формы диалога
            super(mainFrame, "Ввод начальной и конечной вершин пути", true);
            setBounds(400, 400, 200, 150); // Задаём размеры окна
            getRootPane().setDefaultButton(buttonOK);

            // Определяем положение кнопок и надписей в окне
            setLayout(new GridBagLayout()); // табличное расположение
            GridBagConstraints gbc1 = new GridBagConstraints(); // положение подписи start
            gbc1.anchor = GridBagConstraints.EAST;
            gbc1.weightx = 0; // возможность растягивания
            gbc1.weighty = 0;
            gbc1.gridx = 0;   // с какой клетки рисуем
            gbc1.gridy = 0;
            gbc1.gridwidth = 2; // сколько столбцов таблицы занимает (длина)
            gbc1.gridheight = 2; // сколько строк таблицы занимает (высота)

            GridBagConstraints gbc2 = new GridBagConstraints(); // положение подписи end
            gbc2.anchor = GridBagConstraints.EAST;
            gbc2.weightx = 0; // возможность растягивания
            gbc2.weighty = 0;
            gbc2.gridx = 0;   // с какой клетки рисуем
            gbc2.gridy = 2;
            gbc2.gridwidth = 2; // сколько столбцов таблицы занимает (длина)
            gbc2.gridheight = 2; // сколько строк таблицы занимает (высота)

            GridBagConstraints gbc3 = new GridBagConstraints(); // положение поля start
            gbc3.fill = GridBagConstraints.HORIZONTAL;
            gbc3.weightx = 0; // возможность растягивания
            gbc3.weighty = 0;
            gbc3.gridx = 3;   // с какой клетки рисуем
            gbc3.gridy = 0;
            gbc3.gridwidth = 2; // сколько столбцов таблицы занимает (длина)
            gbc3.gridheight = 2; // сколько строк таблицы занимает (высота)

            GridBagConstraints gbc4 = new GridBagConstraints(); // положение поля end
            gbc4.fill = GridBagConstraints.HORIZONTAL;
            gbc4.weightx = 0; // возможность растягивания
            gbc4.weighty = 0;
            gbc4.gridx = 3;   // с какой клетки рисуем
            gbc4.gridy = 2;
            gbc4.gridwidth = 2; // сколько столбцов таблицы занимает (длина)
            gbc4.gridheight = 2; // сколько строк таблицы занимает (высота)

            GridBagConstraints gbc5 = new GridBagConstraints(); // положение кнопки принять
            gbc5.weightx = 0; // возможность растягивания
            gbc5.weighty = 0;
            gbc5.gridx = 0;   // с какой клетки рисуем
            gbc5.gridy = 6;
            gbc5.gridwidth = 2; // сколько столбцов таблицы занимает (длина)
            gbc5.gridheight = 2; // сколько строк таблицы занимает (высота)

            GridBagConstraints gbc6 = new GridBagConstraints(); // положение кнопки отмена
            gbc6.weightx = 0; // возможность растягивания
            gbc6.weighty = 0;
            gbc6.gridx = 3;   // с какой клетки рисуем
            gbc6.gridy = 6;
            gbc6.gridwidth = 2; // сколько столбцов таблицы занимает (длина)
            gbc6.gridheight = 2; // сколько строк таблицы занимает (высота)

            // Инициализируем поля:
            this.startInput = new JTextField(40);
            this.endInput = new JTextField(40);
            this.buttonOK = new JButton("Принять");
            this.buttonCancel = new JButton("Отмена");

            // Добавляем кнопки
            add(new JLabel("Начальная вершина: "), gbc1);
            add(new JLabel("Конечная вершина: "), gbc2);
            add(startInput, gbc3);
            add(endInput, gbc4);
            add(buttonOK, gbc5);
            add(buttonCancel, gbc6);

            // Прописываем действия кнопок
            buttonOK.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onOK();
                }
            });

            buttonCancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onCancel();
                }
            });

            // call onCancel() when cross is clicked
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    onCancel();
                }
            });

            // call onCancel() on ESCAPE
            contentPane.registerKeyboardAction(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onCancel();
                }
            }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        }

        // Метод срабатывает при нажатии принять:
        private void onOK() {

            isData2Ok = false;
            try {
                String startStr = startInput.getText(); // считываем строку start вершины
                String endStr = endInput.getText(); // считываем строку end вершины
                int startDialog = Integer.parseInt(startStr); // строку с числом приводим к формату int
                int endDialog = Integer.parseInt(endStr); // строку с числом приводим к формату int
                System.out.println(graph1.getVertexCount());

                // Проверяем введенные данные:
                // закрываем окно если все данные введены верно
                if (startDialog < 0 || startDialog >= vertexCountOnOk||
                        endDialog < 0 || endDialog >= vertexCountOnOk) { // если вершины не существуют
                    JOptionPane.showMessageDialog(new JPanel(), "Одна или обе вершины не существуют." +
                            "\r\nПопробуйте снова.");
                    isData2Ok = false;
                    dispose();

                } else {
                    start = startDialog;
                    end = endDialog;
                    isData2Ok = true;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(new JPanel(), "Неверный формат введенных данных!\r\n" +
                        "\r\nНомера вершин должен быть целыми числами. " +
                        "\r\nВершины нумеруются с 0.");
                isData2Ok = false;
                dispose();
            }

            isData2Ok = true;
            dispose(); // закрываем окно если все данные введены верно
        }

        // Метод срабатывает при нажатии отмена:
        private void onCancel() {
            // add your code here if necessary
            dispose();
        }
    }

}


