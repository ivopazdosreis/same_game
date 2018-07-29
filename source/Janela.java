package SameGame;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class Janela {
    
    int WINDOWSIZE_X = 507, WINDOWSIZE_Y = 355;
    
    SameGame BB;
    
    JFrame frame, opcoes;
    
    JMenuBar menuBar;
    
    JMenu menu1, menu2, menu3, menu4;
    
    JMenuItem menuNovoJogoItem, menuResetItem, menuOpcoesItem, menuUndoItem, menuRedoItem, menuSugerirItem, 
            menuSairItem, menuBFS, menuDFS,menuDelay, menuDLS, menuIDDFS, menuH1, menuH2, menuH3, menuH4,menuHC, menuDes, menuEmpty;
    
    JLabel labelLinhas, labelColunas, labelCores, labelMin, labelMax, labelDelay;
    
    JTextField fieldLinhas, fieldColunas, fieldCores, fieldMin, fieldMax, fieldDelay;
    
    JButton button_ok, button_cancel;
    
    public static JLabel labelPontuacao;
    
    public Janela(SameGame BB) {
        
        this.BB = BB;
        frame = new JFrame("SameGame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(WINDOWSIZE_X, WINDOWSIZE_Y));
        frame.getContentPane().add(BB);
        frame.setResizable(false);
        addWidgets(frame.getContentPane());
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }
    
    private void addWidgets(Container cont) {
        
        // Criação dos widgets
        opcoes = new JFrame("Opções");
        labelColunas = new JLabel("Linhas"); //É necessário trocar as linhas pelas colunas visto que na realidade a matriz é criada desse modo
        labelLinhas = new JLabel("Colunas");
        labelCores = new JLabel("Cores");
        labelMin = new JLabel("Min Depth");
        labelMax = new JLabel("Max Depth");        
        labelPontuacao = new JLabel("                                                 Pontuação: 0");
        fieldLinhas = new JTextField(String.valueOf(BB.get_L()));
        fieldColunas = new JTextField(String.valueOf(BB.get_C()));
        fieldCores = new JTextField(String.valueOf(BB.get_CORES()));
        fieldMin = new JTextField(String.valueOf(BB.MIN_DEPTH));        
        fieldMax = new JTextField(String.valueOf(BB.MAX_DEPTH));        
        button_ok = new JButton("OK");
        button_cancel = new JButton("Cancelar");
        menuBar = new JMenuBar();
        menu1 = new JMenu("Jogo");
        menu2 = new JMenu("Jogadas");
        menu3 = new JMenu("Pesquisa");
        menu4 = new JMenu("Nível");
        menuBFS = new JMenuItem("BFS");
        menuDFS = new JMenuItem("DFS");
        menuDLS = new JMenuItem("DLS");
        menuIDDFS = new JMenuItem("IDDFS");
        menuH1 = new JMenuItem("Fácil");
        menuH2 = new JMenuItem("Médio");
        menuH3 = new JMenuItem("Díficil");
        menuH4 = new JMenuItem("Nightmare!");
        menuHC = new JMenuItem("Desactiva IA");
        menuDes = new JMenuItem("Desafiar");
        menuEmpty = new JMenuItem("Empty");
        menuDelay = new JMenuItem("Delay");
        
        menuNovoJogoItem = new JMenuItem("Novo Jogo");
        menuOpcoesItem = new JMenuItem("Opções");
        menuUndoItem = new JMenuItem("Undo");
        menuRedoItem = new JMenuItem("Redo");
        menuResetItem = new JMenuItem("Reset");
        menuSugerirItem = new JMenuItem("Sugestão");
        menuSairItem = new JMenuItem("Sair");
        
        // Mnemónicas
        menuNovoJogoItem.setMnemonic('n');
        menuOpcoesItem.setMnemonic('o');
        menuSairItem.setMnemonic('s');
        menuUndoItem.setMnemonic('u');
        menuRedoItem.setMnemonic('r');
        menuSugerirItem.setMnemonic('s');
        button_ok.setMnemonic('o');
        button_cancel.setMnemonic('c');
        
        // Associacão de Event Listeners
        menuNovoJogoItem.addActionListener(new novojogoListener());
        menuOpcoesItem.addActionListener(new opcoesListener());
        menuUndoItem.addActionListener(new undoListener());
        menuRedoItem.addActionListener(new redoListener());
        menuResetItem.addActionListener(new resetListener());
        menuSugerirItem.addActionListener(new sugerirListener());
        menuSairItem.addActionListener(new sairListener());
        button_ok.addActionListener(new okListener());
        button_cancel.addActionListener(new cancelListener());
        menuBFS.addActionListener(new bfsListener());
        menuDFS.addActionListener(new dfsListener());
        menuDLS.addActionListener(new dlsListener());
        menuIDDFS.addActionListener(new iddfsListener());
        menuH1.addActionListener(new h1Listener());
        menuH2.addActionListener(new h2Listener());
        menuH3.addActionListener(new h3Listener());
        menuH4.addActionListener(new h4Listener());
        menuHC.addActionListener(new hcancelListener());
        menuDes.addActionListener(new desListener());
        menuEmpty.addActionListener(new emptyListener());
        menuDelay.addActionListener(new delayListener());
        
        // Adicionar os widgets ao menu
        menu1.add(menuNovoJogoItem);
        menu1.addSeparator();
        menu1.add(menuOpcoesItem);
        menu1.addSeparator();
        menu1.add(menuDes);
        menu1.addSeparator();
        menu1.add(menuSairItem);
        menu2.add(menuUndoItem);        
        menu2.addSeparator();
        menu2.add(menuRedoItem);
        menu2.addSeparator();
        menu2.add(menuResetItem);
        menu2.addSeparator();
        menu2.add(menuSugerirItem);
        menu2.addSeparator();
        menu2.add(menuDelay);
        menu3.add(menuBFS);
        menu3.add(menuDFS);
        menu3.add(menuDLS);
        menu3.add(menuIDDFS);
        menu3.add(menuEmpty);
        
        menu4.add(menuH1);
        menu4.add(menuH2);
        menu4.add(menuH3);
        menu4.add(menuH4);
        menu4.add(menuHC);
        
        menuBar.add(menu1);
        menuBar.add(menu2);
        menuBar.add(menu3);
        menuBar.add(menu4);
        menuBar.add(labelPontuacao);
        menuEmpty.setForeground(Color.LIGHT_GRAY);
        menuDelay.setForeground(Color.LIGHT_GRAY);
    }
    
    class opcoesListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
            opcoes.setPreferredSize(new Dimension(250, 150));
            opcoes.getContentPane().setLayout(new GridLayout(6, 2));
            opcoes.getContentPane().add(labelColunas);
            opcoes.getContentPane().add(fieldColunas);
            opcoes.getContentPane().add(labelLinhas);
            opcoes.getContentPane().add(fieldLinhas);
            opcoes.getContentPane().add(labelCores);
            opcoes.getContentPane().add(fieldCores);
            opcoes.getContentPane().add(labelMin);
            opcoes.getContentPane().add(fieldMin);
            opcoes.getContentPane().add(labelMax);
            opcoes.getContentPane().add(fieldMax);            
            opcoes.getContentPane().add(button_ok);
            opcoes.getContentPane().add(button_cancel);
            opcoes.setResizable(false);
            opcoes.pack();
            opcoes.setVisible(true);
        }
    }
    class desListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
            BB.desafiar();
        }
    }
    class novojogoListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
            BB.novoJogo();
        }
    }
    
    class emptyListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
            BB.force_empty = !BB.force_empty;
            
            if(menuEmpty.getForeground() == Color.LIGHT_GRAY)
                menuEmpty.setForeground(Color.BLACK);
            else
                menuEmpty.setForeground(Color.LIGHT_GRAY);
        }
    }
    class delayListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
            BB.delay = !BB.delay;
            
            if(menuDelay.getForeground() == Color.LIGHT_GRAY)
                menuDelay.setForeground(Color.BLACK);
            else
                menuDelay.setForeground(Color.LIGHT_GRAY);
        }
    }
    class undoListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
            BB.undo();
        }
    }
    
    class resetListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            BB.show_pontuacao(0);
            BB.clear_pontuacao();
        
            BB.points_undo.clear();
            BB.points_redo.clear();
            BB.undo.clear();
            BB.redo.clear();
            BB.backup();
            BB.repaint();
            
        }
    }
    
    class redoListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
            BB.redo();
        }
    }
    
    class dfsListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
            BB.IA=1;
            BB.sugerirJogada();
        }
    }
    class dlsListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
            BB.IA=2;
            BB.sugerirJogada();
        }
    }
    class iddfsListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
            BB.IA=3;
            BB.sugerirJogada();
        }
    }
    class bfsListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
            BB.IA=0;
            BB.sugerirJogada();
        }
    }
    
    class h1Listener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
            BB.heuristic0=true;
            BB.heuristic=false;
            BB.heuristic2=false;
            BB.heuristic3=false;
        }
    }
    
    class h2Listener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
           BB.heuristic0=false; 
           BB.heuristic=true;
           BB.heuristic2=false;
           BB.heuristic3=false;
           
        }
    }
    
    class h3Listener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
           BB.heuristic0=false; 
           BB.heuristic=false;
           BB.heuristic2=true;
           BB.heuristic3=false;
           
            
        }
    }
    class h4Listener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
           BB.heuristic0=false; 
           BB.heuristic=false;
           BB.heuristic2=false;
           BB.heuristic3=true;
           
            
        }
    }
    
    class hcancelListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
           BB.heuristic0=false; 
           BB.heuristic=false;
           BB.heuristic2=false;
           BB.heuristic3=false;
           
            
        }
    }
    
    
    class sugerirListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
                           
                BB.greedy();
        }
    }
    
    class sairListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
            frame.setVisible(false);
            System.exit(0);
        }
    }
    
    class okListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
            int linhas, colunas, cores, minD, maxD;
            
            try {
                
                linhas = Integer.parseInt(fieldLinhas.getText());
                colunas = Integer.parseInt(fieldColunas.getText());
                cores = Integer.parseInt(fieldCores.getText());
                minD = Integer.parseInt(fieldMin.getText());
                maxD = Integer.parseInt(fieldMax.getText());
                
                if(minD >= 0 && maxD >=0) {
                    
                    BB.MIN_DEPTH = minD;
                    BB.MAX_DEPTH = maxD;
                }
                else
                    JOptionPane.showMessageDialog(frame,
                            "Valores inválidos");
                    
                
                if (linhas < 2 || linhas > 300)
                    JOptionPane.showMessageDialog(frame,
                            "Gama incorrecta! (2 - 300)");
                else
                    BB.set_L(linhas);
                
                if (colunas < 2 || colunas > 300)
                    JOptionPane.showMessageDialog(frame,
                            "Gama incorrecta! (2 - 300)");
                else
                    BB.set_C(colunas);
                
                if (cores < 2 || cores > 6)
                    
                    JOptionPane.showMessageDialog(frame,
                            "Gama incorrecta! (2 - 6)");
                else
                    BB.set_CORES(cores);
            }
            
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Formato inválido");
                return;
            }
            
            BB.novoJogo();
            opcoes.setVisible(false);
            
        }
    }
    
    class cancelListener implements ActionListener {
        
        public void actionPerformed(ActionEvent arg0) {
            
            opcoes.setVisible(false);
        }
    }
    }