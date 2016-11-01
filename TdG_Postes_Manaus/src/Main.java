import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import  java.util.Scanner;
class Main
{
    private static Scanner entrada = new Scanner(System.in);
    
    public static void main(String[] args)
    {
        int ordem, i, j;
        Grafo g;
        // leitura da ordem-1 da matriz
        ordem = entrada.nextInt();
        g = new Grafo(ordem);
        // leitura da matriz de adjacencias
        for(i = 1 ; i <= ordem ; i++)
        {
            for(j = 1 ; j <= ordem ; j++)
            {
                g.AddAresta(i, j, entrada.nextInt());
            }
        }
        System.out.println(g.MinimumSpanningTree());
        //g.MinimumSpanninTree();
    }
    
    private static class Grafo
    {
        private int adj[][];
        private int vertices = 0;
        private int custo[];
        private int anterior[];
        public Grafo(int quantidade)
        {
            vertices = quantidade;
            quantidade++;
            adj = new int [quantidade][quantidade];
            custo = new int[quantidade];
            anterior = new int [quantidade];
        }
        public int getAresta(int origem, int destino)
        {
            return adj[origem][destino];    //vertice de para
        }
        public void AddAresta(int linha, int coluna, int value)
        {
            adj[linha][coluna] = value; //valor de para
        }
        public int MinimumSpanningTree()
        {
            int v, peso, u, custoTotal = 0;
            FilaPrioridadeMinima Q = new FilaPrioridadeMinima(vertices);    //inicializando a fila
            for(v = 1 ; v <= vertices ; v++)    //inicializando os custos e os caminhos
            {
                No no = new No(v);  //criando um no
                custo[v] = Integer.MAX_VALUE;   //adicionando custo 'infinito'
                anterior[v] = -1;   //marcando flag vazia
                no.setCusto(Integer.MAX_VALUE);
                Q.Inserir(no);  //adicionando na fila
            }
            while(Q.tamanho > 0)    //enquanto a fila nao estiver vazia
            {
                No vertice = Q.ExtrairMinimo(); //vertice de menor custo
                u = vertice.verticeID;
                //System.out.println("extraiu "+u);
                for(v = 1 ; v <= vertices ; v++)   //para cada vertice do grafo
                {
                    peso = getAresta(u, v);
                    if(peso > 0) // se tem adjacencia
                    {
                        if(Q.acharNo(v))    //se v esta na fila
                        {
                            if(peso < custo[v]) // se o custo de v for menor que o anterior
                            {
                                //anterior[v] = u;
                                custo[v] = peso;
                                Q.increasePriority(v, peso);
                            }
                        }
                    }
                }
            }
            for(v = 1 ; v <= vertices ; v++)
            {
                if(custo[v] != Integer.MAX_VALUE)
                {
                    custoTotal += custo[v];
                }
            }
            return custoTotal;
        }
    }
    
    private static class FilaPrioridadeMinima
    {
        private No[] fila; 
        private int tamanho;
        public FilaPrioridadeMinima(int quantidade)
        {
            tamanho = 0;
            fila = new No[quantidade+1];
        }
        private boolean decreaseKey(int i, int key)
        {
            if(key > fila[i].getCusto())
            {
                return false;
            }
            fila[i].setCusto(key);
            while(i > 1 && fila[Pai(i)].getCusto() > fila[i].getCusto())
            {
                No aux = fila[i];
                fila[i] = fila[Pai(i)];
                fila[Pai(i)] = aux;
                i = Pai(i);
            }
            return true;
        }
        public void increasePriority(int value, int key)
        {
            for(int i = 1 ; i <= tamanho ; i++)
            {
                if(value == fila[i].getVerticeID())
                {
                    if(key > fila[i].getCusto())
                    {
                        return;
                    }
                    fila[i].setCusto(key);
                    while(i > 1 && fila[Pai(i)].getCusto() > fila[i].getCusto())
                    {
                        No aux = fila[i];
                        fila[i] = fila[Pai(i)];
                        fila[Pai(i)] = aux;
                        i = Pai(i);
                    }
                    return;
                }
            }
        }
        private void minHeapify(int i)
        {
            int l, r, smallest;
            No aux;
            l = Esq(i);
            r = Dir(i);
            if(l <= tamanho && fila[i].getCusto() > fila[l].getCusto())
            {
                smallest = l;
            }
            else
            {
                smallest = i;
            }
            if(r <= tamanho && fila[r].getCusto() < fila[smallest].getCusto())
            {
                smallest = r;
            }
            if(smallest != i)
            {
                aux = fila[i];
                fila[i] = fila[smallest];
                fila[smallest] = aux;
                minHeapify(smallest);
            }
        }
        public boolean acharNo(int ID)
        {
            for(int i = 1 ; i <= tamanho ; i++)
            {
                if(fila[i].verticeID == ID)
                {
                    return true;
                }
            }
            return false;
        }
        public int getQuantidade()
        {
            return this.tamanho;
        }
        
        public No getMinimo()
        {
            return fila[1];
        }
        public No ExtrairMinimo()
        {
            if(tamanho < 1)
            {
                return null;
            }
            No min = fila[1];
            fila[1] = fila[tamanho];
            tamanho--;
            minHeapify(1);
            return min;
        }
        public void Inserir(No no)
        {
            tamanho++;
            fila[tamanho] = no;
            decreaseKey(tamanho, no.getCusto());
            minHeapify(tamanho);
        }

        private int Pai(int i)
        {
            return Math.floorDiv(i, 2);
        }
        private int Esq(int i)
        {
            return i * 2;
        }
        private int Dir(int i)
        {
            return i * 2 + 1;
        }
    }

    private static class No
    {
        private int verticeID;  //numero do vertice
        private int custo;
        public No(int ID)
        {
            this.verticeID = ID;
        }
        public int getVerticeID()
        {
            return verticeID;
        }
        public int getCusto()
        {
            return this.custo;
        }
        public void setCusto(int value)
        {
            this.custo = value;
        }
    }
}