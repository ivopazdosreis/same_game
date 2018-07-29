package SameGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.LinkedList;

public class SameGame extends JPanel implements MouseListener {
   
    public boolean heuristic0 = false;
    public boolean heuristic = false;
    public boolean heuristic2 = false;
    public boolean heuristic3 = false;
    public boolean force_empty = false;
    
    Node dls;
    Stack openDls = new Stack();
    boolean random = true;
    boolean dls_finished = false;
    private int LINHAS = 10,  COLUNAS = 10,  CORES = 3; // altura, largura da matriz, número de cores (Valores Máximos de tamanho: 20x20 e cores 12)
    private int SIZE_X = 500,  SIZE_Y = 300; // Tamanho inicial do tabuleiro
    public int[][] matriz;
    public int[][] matriz2;
    public Stack<int[][]> undo,  redo;
    public Stack<Integer> points_undo,  points_redo;
    private ArrayList<Coordenada> selectedBubbles;
    public Integer pontuacao = 0;
    private int parcela_x,  parcela_y;
    public boolean empty = true;
    public int IA = 0;
    int sugest_x = 0, sugest_y = 0;
    boolean desafiar = false;
    boolean delay = false;
    int n;    
    int mais_comum=-1;
        
    public int MIN_DEPTH=0;
    public int MAX_DEPTH=10000;
    
    
    
    /**************************************************************************/
    /****************** Métodos de Pesquisa / Heurísticas *********************/
    
    
    private void iddfs(Node startNode) {
        
        int depth = MIN_DEPTH;
        

        while (dls_finished == false) {            
            dls = null;
            dls(startNode, depth++);
        }
    }

    private void dls(Node startNode, int depth) {

        openDls.push(startNode);

        while (!openDls.empty() && !dls_finished) {

            
            Node new_node = (Node) openDls.pop();
            
            if (new_node.get_depth() < MAX_DEPTH) {

                selectedBubbles.clear();
                backup();

                for (Coordenada i : new_node.get_coordenadas()) 
                    jogada(matriz, i.get_x(), i.get_y());                

                if (gameOver(matriz)) {
                    
                    dls_finished = true;
                    dls = new_node;

                    if (empty) {
                        new_node.set_value(new_node.get_value() * 2);
                    }


                } else {
                    
                    LinkedList<ArrayList<Coordenada>> ordered_spots;
                    LinkedList<ArrayList<Coordenada>> spots = get_nodes();
                                        
                    if (!heuristic2&&!heuristic&&!heuristic3) {

                    Collections.reverse(spots);
                    ordered_spots = (LinkedList<ArrayList<Coordenada>>) spots.clone();
                    
                } else {
                    
                    ordered_spots = spot_sorter(spots);
                }


                    for (ArrayList<Coordenada> i : ordered_spots) {

                        ArrayList<Coordenada> new_coords = new ArrayList<Coordenada>();
                        for (Coordenada j : new_node.get_coordenadas()) {
                            new_coords.add(j);
                        }

                        Node child = new Node(new_node.get_value() + get_points(i.size()), new_coords);
                        child.set_depth(new_node.get_depth() + 1);                        

                        child.add_coordenada(new Coordenada(i.get(0).get_x(), i.get(0).get_y()));

                        openDls.push(child);
                    }                
                }
            }
        }
    }

    private Node dfs(Node startNode) {

        Stack openList = new Stack();
        openList.push(startNode);

        while (!openList.isEmpty()) {

            Node node = (Node) openList.pop();

            selectedBubbles.clear();
            backup();

            for (Coordenada i : node.get_coordenadas()) {
              
                jogada(matriz, i.get_x(), i.get_y());                
            }

            if (gameOver(matriz)) {

                backup();

                if (empty || (!empty && !force_empty)) {
                    node.set_value(node.get_value() * 2);
                    return node;
                }                                

                //return node;
            } else {
               
                LinkedList<ArrayList<Coordenada>> ordered_spots;
                LinkedList<ArrayList<Coordenada>> spots = get_nodes();

                if (!heuristic2&&!heuristic&&!heuristic3) {

                    Collections.reverse(spots);
                    ordered_spots = (LinkedList<ArrayList<Coordenada>>) spots.clone();
                } else {
                    
                    ordered_spots = spot_sorter(spots);
                }

                for (ArrayList<Coordenada> i : ordered_spots) {

                    ArrayList<Coordenada> new_coords = new ArrayList<Coordenada>();
                    for (Coordenada j : node.get_coordenadas()) {
                        new_coords.add(j);
                    }

                    Node child = new Node(node.get_value() + get_points(i.size()), new_coords);

                    child.add_coordenada(new Coordenada(i.get(0).get_x(), i.get(0).get_y()));

                    openList.push(child);
                }
            }
        }

        return null;

    }

    public void set_mindepth(int spots) {
        
        MIN_DEPTH = spots/2;
    }
    
    public void set_maxdepth(int spots) {
        
        MAX_DEPTH = (spots*spots)/2;
        
    }
    private Node bfs(Node startNode) {

        LinkedList openList = new LinkedList();
        openList.add(startNode);

        while (!openList.isEmpty()) {

            Node node = (Node) openList.removeFirst();

            selectedBubbles.clear();
            backup();
            
            for (Coordenada i : node.get_coordenadas())                 
                jogada(matriz, i.get_x(), i.get_y());                                          

            if (gameOver(matriz)) {

                backup();
                node.set_value(node.get_value() * 2);
                return node;
            } else {
                
                LinkedList<ArrayList<Coordenada>> ordered_spots;
                LinkedList<ArrayList<Coordenada>> spots = get_nodes();

                if (!heuristic2&&!heuristic&&!heuristic3) {
                    
                    ordered_spots = (LinkedList<ArrayList<Coordenada>>) spots.clone();
                } else {
                    
                    ordered_spots = spot_sorter(spots);
                }
                    
                for (ArrayList<Coordenada> i : ordered_spots) {
                    ArrayList<Coordenada> new_coords = new ArrayList<Coordenada>();
                    for (Coordenada j : node.get_coordenadas()) {
                            new_coords.add(new Coordenada(j.get_x(), j.get_y()));
                        }
                    Node child = new Node(node.get_value() + get_points(i.size()), new_coords);
                    child.add_coordenada(new Coordenada(i.get(0).get_x(), i.get(0).get_y()));
                    openList.addLast(child);
                }                
            }
        }

        return null;
    }
    

    public LinkedList<ArrayList<Coordenada>> spot_sorter(LinkedList<ArrayList<Coordenada>> spots) {

        LinkedList<ArrayList<Coordenada>> ordered_spots = new LinkedList<ArrayList<Coordenada>>();        
        
        int[] points = new int[spots.size()];
        ArrayList<Coordenada>[] unordered = new ArrayList[spots.size()];
        for(int i = 0 ; i < spots.size() ; i++)
            unordered[i] = new ArrayList<Coordenada>();
        
        int c=0;
        
        for (ArrayList<Coordenada> j : spots) {
            int pts=0;
            
            if(heuristic0)
                pts = get_points(j.size());
            
            else 
                if(heuristic) {
                    pts= heuristic1(j.size(), matriz[j.get(0).get_x()][j.get(0).get_y()]);
                }
                else
                    if(heuristic2 || heuristic3){
                        pts = heuristic2(j.size(), j);
                    }
            
            points[c] = pts;
            unordered[c++] = j;                                            
        }
        
        quicksort(points, unordered, 0, points.length-1);                                
        
        for(ArrayList<Coordenada> j: unordered)
            ordered_spots.add(j);                       
       
        return ordered_spots;
    }
    
    public int heuristic1(int spot_size, int cor) {

        return get_points(spot_size) - loose_bubbles(cor);
    }

    public int heuristic2(int spot_size, ArrayList<Coordenada> spot) {

        int x = spot.get(0).get_x(), y = spot.get(0).get_y(); // coordenadas da primeira bubble (mais perto da origem)        
        int cor = get_cor(x, y);
        
        if((cor == mais_comum) && (heuristic3==true)) {
            return -100;
        }
                        
        int[] points = new int[spot_size * 2];
        int count = 0;
        int p = 0;
        int selected = 2;
        boolean search = true;
        int a = 0, b = 0;

        x = -1;

        int cor_cima = -1;
        int cor_baixo = -1;

        for (int i = 0; i < spot.size(); i++) {

            int xp = spot.get(i).get_x();
            y = spot.get(i).get_y();

            if (xp > x) {

                if (bubbleValida(xp, y - 1)) {

                    cor_cima = get_cor(xp, y - 1);

                    while (bubbleValida(xp, ++y)) {

                        if (get_cor(xp, y) != cor) {

                            cor_baixo = get_cor(xp, y);

                            continue;
                        }
                    }

                    if (cor_cima == cor_baixo) {

                        if (search) {
                            collectBubbles(xp, y - 1, cor_cima);
                            a = selectedBubbles.size();
                            selectedBubbles.clear();

                            collectBubbles(xp, y - 1, cor_cima);
                            b = selectedBubbles.size();
                            selectedBubbles.clear();
                            selected = a + b;
                        }

                        if (count >= 1) {

                            if (points[count - 1] == cor_cima) {
                                points[count] += selected;
                            } else {
                                points[count++] = cor_cima;
                                points[count++] = 2;
                            }
                        } else {
                            points[count++] = cor_cima;
                            points[count++] = 2;
                        }
                    } else {
                        search = true;
                        a = 0;
                        b = 0;
                    }
                }

                if (a > 1 && b > 1) {
                    search = false;
                    selected = 2;
                }

                x = xp;
            }
        }

        for (int i = 1; i < points.length; i += 2) {

            if ((Integer) points[i] != null) {
                p *= get_points(points[i]);
            }
        }

        x = spot.get(spot.size()-1).get_x();
        y = spot.get(spot.size()-1).get_y();
        
        
        return p+x+y + (get_points(spot_size)) - loose_bubbles(cor);
        }
        
    public void heuristic3() {
               
        mais_comum = get_maior_ind(ocorrenciaCores());   
        System.out.println(mais_comum);
    }
    
    public int loose_bubbles(int cor) {
                
        int num = 0;        
            
            for (int i = 0; i < LINHAS; i++) {
                for (int j = 0; j < COLUNAS; j++) {
                    selectedBubbles.clear();
                    if (bubbleValida(i, j)) {
                        
                        if (matriz[i][j] == cor) {
                            collectBubbles(i, j, get_cor(i, j));
                        if (selectedBubbles.isEmpty()) 
                            num++;                            
                        }                                                
                    }
                }
            }                        
           
        selectedBubbles.clear();
            
        return num;
    }
    
    void quicksort (int[] a, ArrayList<Coordenada>[] spots, int lo, int hi)
{
//  lo is the lower index, hi is the upper index
//  of the region of array a that is to be sorted
    int i=lo, j=hi, h;
    int x=a[(lo+hi)/2];

    //  partition
    do
    {    
        while (a[i]<x) i++; 
        while (a[j]>x) j--;
        if (i<=j)
        {
            h=a[i]; a[i]=a[j]; a[j]=h;
            ArrayList<Coordenada> temp = spots[i];
            spots[i] = spots[j];
            spots[j] = temp;
            i++; j--;
        }
    } while (i<=j);

    //  recursion
    if (lo<j) quicksort(a, spots, lo, j);
    if (i<hi) quicksort(a, spots, i, hi);
}

    public void backup() {

        for (int i = 0; i < LINHAS; i++) {
            for (int j = 0; j < COLUNAS; j++) {
                
                matriz[i][j] = matriz2[i][j];
            }

        }                
    }



    // Get's, Set's '
    public int get_L() {
        return LINHAS;
    }

    public int get_C() {
        return COLUNAS;
    }

    public int get_CORES() {
        return CORES;
    }

    private int get_cor(int x, int y) {

        if (matriz[x][y] != -1) 
            return matriz[x][y];        

        return -1;
    }

    private int get_ind_cor(int x, int y) {
        return matriz[x][y];
    }

    public void set_L(int L) {
        this.LINHAS = L;
    }

    public void set_C(int C) {
        this.COLUNAS = C;
    }

    public void set_CORES(int CORES) {
        this.CORES = CORES;
    }

    private void set_slices() {
        parcela_x = SIZE_X / LINHAS;
        parcela_y = SIZE_Y / COLUNAS;
    }

    private int get_points(int n) {
        return n * (n - 1);
    }

    public void clear_pontuacao() {
        pontuacao = 0;
    }

    private int geraCor() { // Gera um inteiro entre '0 e CORES'
        return (int) (Math.random() * CORES);
    }

    private boolean arrayVazio(int[] m) {
        return (m[m.length - 1] == -1);
    }

    private void msgOver() {
        JOptionPane.showMessageDialog(this, "Fim do jogo! \nPontuação Final: " + pontuacao);
    }
    
    private void msgNoSolution() {
        JOptionPane.showMessageDialog(this, "Solução não encontrada!");
    }
    
    private void msgStats(long time, int jogadas) {
        JOptionPane.showMessageDialog(this, "Tempo decorrido: " + time + "msecs\nJogadas efectuadas: " + jogadas);
    }
    
    private void msgDes(long time, int jogadas) {
        JOptionPane.showMessageDialog(this, "Obtive " + pontuacao + " pontos em " +  time + "msecs e " + jogadas + " jogadas!\nAgora é a tua vez...");
    }
    private void msgOverBonus() {
        JOptionPane.showMessageDialog(this, "Parabéns!A sua pontuação será duplicada! \nPontuação Final: " + pontuacao);
    }

    private void msgSug(int linha, int coluna) {
        JOptionPane.showMessageDialog(this, "Sugestão: Linha " + (linha + 1) + ", Coluna " + (coluna + 1));
    }

    public void show_pontuacao(int p) {
        Janela.labelPontuacao.setText("                                       Pontuação: " + String.valueOf(p));
    }

    public SameGame() {
        set_slices();
        if (random) {
            geraMatriz(LINHAS, COLUNAS);
        } else {
            set_matriz_inicial(LINHAS, COLUNAS);
        }
        addMouseListener(this);
    }


    /* Verifica se a coordenada passada por argumento corresponde
    a uma posição válida da matriz de jogo. */
    private boolean bubbleValida(int linha, int coluna) {
        if (linha < 0 || coluna < 0 || linha > LINHAS - 1 || coluna > COLUNAS - 1 || matriz[linha][coluna] == -1) {
            return false;
        }
        return true;
    }

    /* Método auxiliar do procedimento collectBubbles
    Percorre o arraylist selectedBubbles, e verifica se a coordenada
    passada por argumento já foi calculada ou não.*/
    private boolean contemBubble(int x, int y) {
        int i;
        for (i = 0; i < selectedBubbles.size(); i++) {
            if (selectedBubbles.get(i).get_x() == x && selectedBubbles.get(i).get_y() == y) {
                return true;
            }
        }
        return false;
    }

    /* Método que aleatóriamente gera as bolhas que constituem o
    tabuleiro de jogo, à custa de um inteiro pseudo-aleatório, que
    determina (por relação "int - Color") a côr de cada bolha. 
    Inicializa as pilhas e arraylist's */
    private void geraMatriz(int L, int C) {

        int i, j, cor;
        matriz = new int[L][C];
        matriz2 = new int[L][C];
        undo = new Stack<int[][]>();
        redo = new Stack<int[][]>();
        points_undo = new Stack<Integer>();
        points_redo = new Stack<Integer>();
        selectedBubbles = new ArrayList<Coordenada>();


        for (i = 0; i < L; i++) {
            for (j = 0; j < C; j++) {
                cor = geraCor();
                
                matriz[i][j] = cor;
                matriz2[i][j] = cor;
            }
        }
    }
    int[][] matriz_jogadas = {{0, 1, 0, 1, 1},
        {0, 1, 0, 2, 1},
        {1, 0, 0, 1, 0},
        {0, 1, 0, 2, 0},
        {0, 1, 0, 2, 0}};

    public void set_matriz_inicial(int L, int C) {

        matriz = new int[L][C];
        matriz2 = new int[L][C];
        undo = new Stack<int[][]>();
        redo = new Stack<int[][]>();
        points_undo = new Stack<Integer>();
        points_redo = new Stack<Integer>();
        selectedBubbles = new ArrayList<Coordenada>();

        for (int i = 0; i < L; i++) {
            for (int j = 0; j < C; j++) {                
                matriz[j][i] = matriz_jogadas[i][j];
                matriz2[j][i] = matriz_jogadas[i][j];
            }
        }
    }

    /* Método que cria um clone da matriz corrente *
    e a armazena na matriz substituta 'undo'.
    Este Método é invocado em todas as jogadas válidas */
    private void backupMatriz(boolean undo_redo) {

        int[][] backup = new int[LINHAS][COLUNAS];
        int  i;
        for (i = 0; i < matriz.length; i++)
            backup[i] = matriz[i].clone();


        if (undo_redo) {
        undo.push(backup);
        } else {
        redo.push(backup);
        }
    }

    /* Itera sobre os elementos da matriz, e invoca o método drawBubble,
    que imprime directamente na frame principal, cada uma das bolhas.
    Utiliza primitivas gráficas, setColor e fillOval */
    @Override
    public void paintComponent(Graphics g) {        
        
        if(delay)
        try {
        Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }  

        super.paintComponent(g); // limpa fundo ...

        for (int i = 0; i < LINHAS; i++) {
            for (int j = 0; j < COLUNAS; j++) {
                if (bubbleValida(i, j)) {
                    drawBubble(g, i * parcela_x, j * parcela_y, get_cor(i, j));
                }
            }
        }
    }

    /* Desenha directamente uma circunferência representativa de uma bolha, *
     * partindo das coordenadas (x,y), calculadas à custa da dimensão       *
     * do painel, e das dimensões do array de bolhas (linhas, colunas)      */
    public void drawBubble(Graphics g, int x, int y, int c) {

        Color cor = null;

        switch (c) {

            case 0:
                cor = Color.BLACK;
                break;
            case 1:
                cor = Color.RED;
                break;
            case 2:
                cor = Color.GREEN;
                break;
            case 3:
                cor = Color.BLUE;
                break;
            case 4:
                cor = Color.ORANGE;
                break;
            case 5:
                cor = Color.MAGENTA;
                break;
            case 6:
                cor = Color.GRAY;
                break;
        }

        g.setColor(cor);
        g.fillOval(x, y, parcela_x, parcela_y);
    }

    /* Invoca o procedimento collectBubbles, e de seguida
    e de seguida verifica o tamanho de selectedBubbles.
    Se for maior que zero, é porque a jogada é valida, e portanto,
    são executados os Métodos necessários à manutenção do jogo. */
    private void jogada(int x, int y) {

        collectBubbles(x, y, get_cor(x, y));
        if (selectedBubbles.size() > 0) {

            backupMatriz(true);
            n++;
            deleteBubbles();
            fallingBubbles();
            pushColumns();
            
            redo.clear();
            points_undo.push(pontuacao);
            pontuacao += get_points(selectedBubbles.size());
            if (gameOver(matriz) && empty) {
                pontuacao += pontuacao;
            }

            repaint();
            show_pontuacao(pontuacao);
            
            if(gameOver(matriz) && !desafiar) {
                if (gameOver(matriz) && empty) {
                    msgOverBonus();
                }
                if (gameOver(matriz) && !empty) {
                    msgOver();
                }
            }
        }
        selectedBubbles.clear();
    }

    private int[][] jogada(int[][] board, int x, int y) {

        collectBubbles(x, y, get_cor(x, y));

        if (selectedBubbles.size() > 0) {
            backupMatriz(false);            
            deleteBubbles();
            fallingBubbles();
            pushColumns();

            selectedBubbles.clear();
        }

        return board;
    }
    /* Devolve um ArrayList com os vizinhos de uma bolha colocada
    na posição (x,y) da matriz. Um vizinho é aquele que contém a mesma cor
    que a bolha em questão. */

    private ArrayList<Coordenada> vizinhos(int x, int y, int cor) {
        

        ArrayList<Coordenada> v = new ArrayList<Coordenada>();
        
        int k = 0;

        // Cima
        if (bubbleValida(x, y - 1) && get_cor(x, y - 1) == cor) {
            v.add(new Coordenada(x, y - 1));
        }        

        // Direita
        if (bubbleValida(x + 1, y) && get_cor(x + 1, y) == cor) {
            v.add(new Coordenada(x + 1, y));
        }        

        // Esquerda
        if (bubbleValida(x - 1, y) && get_cor(x - 1, y) == cor) {
            v.add(new Coordenada(x - 1, y));
        }        

        // Baixo
        if (bubbleValida(x, y + 1) && get_cor(x, y + 1) == cor) {
            v.add(new Coordenada(x, y + 1));
        }        

        return v;
    }

    /* Método devidamente explicado no relatório.
    Sucintamente, recolhe as bolhas a eliminar no decorrer
    de uma jogada. */
    private void collectBubbles(int lin, int col, int cor) {

        int i, x, y;

        ArrayList<Coordenada> v = vizinhos(lin, col, cor);

        if (v.size() == 0) 
            return;
        


        selectedBubbles.add(new Coordenada(lin, col));

        for (i = 0; i < v.size(); i++) {

            x = v.get(i).get_x();
            y = v.get(i).get_y();

            if (!contemBubble(x, y)) {
                collectBubbles(x, y, cor);
            }
        }

        v.clear();
    }

    /* Método que, a cada jogada válida, percorre 
    o ArrayList selectedBubbles, e para cada coordenada,
    elimina o conteúdo na matriz para a posição correspondente. */
    private void deleteBubbles() {

        int i, x, y;

        for (i = 0; i < selectedBubbles.size(); i++) {

            x = selectedBubbles.get(i).get_x();
            y = selectedBubbles.get(i).get_y();
            matriz[x][y] = -1;
        }       
    }

    /* Método que reajusta as bolhas nos arrays, devido aos espa�os *
     * que se formam em cada jogada válida.                         *
     * Percorre sempre todos os sub-arrays da matriz, e sempre que  *
     * encontra um índice 'null', itera sobre as bolhas precedentes,*
     * aumentando-lhes o seu índice de forma unitária.              *
     * Provoca o efeito de queda das bolhas no painel de jogo.      */
    private void fallingBubbles() {
        int j, aux;

        for (int[] i : matriz) {
            for (j = 0; j < i.length; j++) {
                if (i[j] == -1) {
                    for (aux = j; aux > 0; aux--) {
                        i[aux] = i[aux - 1];
                    }
                    if (i[0] != -1) {
                        i[0] = -1;
                    }
                }
            }
        }
      
    }

    /* Método idêntico ao anterior, que é responsável por 
    encostar as colunas de bolhas da esquerda para a direita,
    sempre que uma coluna fica vazia. Itera sobre as colunas, e quando
    encontra uma vazia move os subarrays da matriz uma casa para a direita. */
    private void pushColumns() {
        int i, aux;

        for (i = 0; i < matriz.length; i++) {
            if (arrayVazio(matriz[i])) {
                for (aux = i; aux > 0; aux--) {
                    matriz[aux] = matriz[aux - 1].clone();
                }
                if (!arrayVazio(matriz[0])) // Só é executado da primeira vez,
                {
                    matriz[0] = new int[COLUNAS];
                    for(int k = 0 ; k < COLUNAS ; k++)
                        matriz[0][k] = -1;
                }
            // para garantir que a primeira 
            // coluna é eliminada      
            }
        }
        //return board;
    }
    
    
    public void desafiar() {
        
       // novoJogo();
        desafiar = true;
        sugerirJogada();                
    }

    public void undo() {

        if (undo.size() > 0) {
            backupMatriz(false); // cria backup da jogada corrente, e armazena em 'redo'
            points_redo.push(pontuacao); // armazena pontuação corrente na pilha de pontuacoes de 'redo'
            pontuacao = points_undo.pop(); // pontuação aquando da jogada anterior
            show_pontuacao(pontuacao);
            matriz = undo.pop(); // matriz fica a apontar para a jogada anterior
            repaint();
        }
    }

    public void redo() {

        if (redo.size() > 0) {
            backupMatriz(true); // cria backup da jogada corrente, e armazena em 'undo'
            points_undo.push(pontuacao); // armazena pontuação corrente na pilha de pontuações de 'redo'
            pontuacao = points_redo.pop(); // pontuação aquando da jogada posterior
            show_pontuacao(pontuacao);
            matriz = redo.pop(); // matriz fica a apontar para jogada posterior
            repaint();
        }
    }

    /*Verifica se ainda há alguma jogada poss�vel.
    Itera sobre as bolhas da matriz, e calcula os vizinhos de cada uma.
    Um vizinho é uma bolha que está imediatamente acima, baixo, esquerda ou direita, 
    e que contém a mesma cor da bolha em quest�o. Devolve false assim que encontra
    um ArrayList de tamanho superior a zero. */
    private boolean gameOver(int[][] board) {

        int i, j;
        empty = true;
        ArrayList<Coordenada> v;        

        for (i = 0; i < board.length; i++) {
            for (j = 0; j < board[i].length; j++) {
                if (bubbleValida(i, j)) {
                    empty = false;
                    v = vizinhos(i, j, get_cor(i, j));
                    if (v.size() > 0) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    
    public void novoJogo() {

        n = 0;
        show_pontuacao(0);
        clear_pontuacao();
        undo.clear();
        redo.clear();
        points_undo.clear();
        points_redo.clear();

        set_slices();
        if (random) {
            geraMatriz(LINHAS, COLUNAS);
        } else {
            set_matriz_inicial(LINHAS, COLUNAS);
        }
        // para garantir que é criado um jogo com jogadas possiveis
        if (!gameOver(matriz)) {
            int s = get_nodes().size();
            repaint();
        } else {
            novoJogo();
        }
    }

    /* Percorre a matriz de jogo e recolhe o número de ocorrências
    de cada cor. Os índices do array de inteiros estão em conformidade
    com a ordem de cor presentes no construtor da classe Bubble.
    Assim, por exemplo, o indice 0 do array guarda as ocorrencias 
    da cor preto, pois preto toma o valor zero no construtor de Bubble.*/
    public int[] ocorrenciaCores() {

        int i, j;
        int[] ocorrencias = new int[CORES];

        for (i = 0; i < LINHAS; i++) {
            for (j = 0; j < COLUNAS; j++) {
                if (bubbleValida(i, j)) {
                    ocorrencias[get_ind_cor(i, j)]++;
                }
            }
        }

        return ocorrencias;
    }

    public void sugerirJogada() {
        
        if (gameOver(matriz)) {
            if (gameOver(matriz) && empty) {
                msgOverBonus();
            }
            if (gameOver(matriz) && !empty) {
                msgOver();
            }
            return;
        }

        int t = 0;
        Node root = new Node();
        root.set_depth(0);
        Node solution = new Node();
        dls = null;
        openDls.clear();
        
        
        long start = System.currentTimeMillis();     
        
        if(heuristic3) {
            heuristic3();
        }
        
        if(desafiar) {
            
            t = IA;
            IA = 1;
        }
            
        
        switch (IA) {
            case 0:
                solution = bfs(root);
                break;

            case 1:
                solution = dfs(root);
                break;

            case 2:
                
                dls(root, MAX_DEPTH);
                if(!dls_finished)
                solution = null;
                else {dls_finished = false;solution=dls;}
                break;

            case 3:
                iddfs(root);
                if(!dls_finished)
                solution = null;
                else {dls_finished = false;solution=dls;}
                break;

        }
        long time = System.currentTimeMillis() - start;
                
        backup();
        mais_comum = -1;                
        
        if (solution != null) {
            
            for (Coordenada i : solution.get_coordenadas()) {
                               
            jogada(i.get_x(), i.get_y());               
            paintComponent(this.getGraphics());
            }
            
            if(desafiar) {
                
                msgDes(time, solution.get_coordenadas().size());
                show_pontuacao(0);
                clear_pontuacao();

                points_undo.clear();
                points_redo.clear();
                undo.clear();
                redo.clear();
                backup();
                repaint();
            }
            
            else msgStats(time, solution.get_coordenadas().size());
        } else {
            if(desafiar)
                desafiar();
            else msgNoSolution();
        }
        
        if(desafiar) {
            
            IA = t;
            desafiar = false;
        }

        selectedBubbles.clear();       
    }

    public LinkedList<ArrayList<Coordenada>> get_nodes() {

        LinkedList<ArrayList<Coordenada>> nodes = new LinkedList<ArrayList<Coordenada>>();
        for (int i = 0; i < LINHAS; i++) {
            for (int j = 0; j < COLUNAS; j++) {
                if (!visited(nodes, i, j) && bubbleValida(i, j)) {

                    selectedBubbles.clear();

                    collectBubbles(i, j, get_cor(i, j));

                    if (selectedBubbles.size() > 0) {

                        ArrayList<Coordenada> temp = new ArrayList<Coordenada>();
                        for (Coordenada k : selectedBubbles) {
                            temp.add(k);
                        }

                        nodes.add(temp);
                    }
                }
            }
        }

        return nodes;
    }

    public boolean visited(LinkedList<ArrayList<Coordenada>> nodes, int x, int y) {

        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.get(i).size(); j++) {
                if (nodes.get(i).get(j).get_x() == x && nodes.get(i).get(j).get_y() == y) {                    
                    return true;
                }
            }
        }

        return false;
    }
    
    
    

    public void greedy() {
        int melhor_jogada = 0, jogada_actual = 0;

        for (int i = 0; i <
                LINHAS; i++) {
            for (int j = 0; j <
                    COLUNAS; j++) {
                selectedBubbles.clear();
                if (bubbleValida(i, j)) {
                    collectBubbles(i, j, get_cor(i, j));
                    jogada_actual = get_points(selectedBubbles.size());
                    if (jogada_actual > melhor_jogada) {
                        sugest_x = i;
                        sugest_y = j;
                        melhor_jogada = jogada_actual;
                    }

                }
            }
        }
        
        msgSug(sugest_y, sugest_x);
    }

    public int get_maior_ind(int[] cores) {
        int i, cor = 0;
        for (i = 1; i < CORES; i++) {
            if (cores[i] > cores[cor]) 
                cor = i;
            

        }
        return cor;
    }

    /* Calcula a posição na matriz directamente a partir
    das coordenadas (x,y) onde o utilizador clickou. A posição (x,y) na matriz
    é dada pelo quociente entre as coordenadas no ecrâ, e as respectivas parcelas*/
    public void mouseClicked(MouseEvent e) {

        int x = (int) Math.floor(e.getX() / parcela_x);
        int y = (int) Math.floor(e.getY() / parcela_y);

        if (bubbleValida(x, y)) 
            jogada(x, y);
        

    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
