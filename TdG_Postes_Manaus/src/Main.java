import java.util.ArrayList;
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
            ArrayList<No> F = new ArrayList<>();
            for(v = 1 ; v <= vertices ; v++)    //inicializando os custos e os caminhos
            {
                No no = new No(v);  //criando um no
                custo[v] = Integer.MAX_VALUE;   //adicionando custo 'infinito'
                anterior[v] = -1;   //marcando flag vazia
                Q.Inserir(no);  //adicionando na fila
            }
            while(Q.tamanho > 0)    //enquanto a fila nao estiver vazia
            {
                No vertice = Q.ExtrairMinimo(); //vertice de menor custo
                u = vertice.verticeID;
                for(v = 1 ; v <= vertices ; v++)   //para cada vertice do grafo
                {
                    peso = getAresta(u, v);
                    if(peso > 0) // se tem adjacencia
                    {
                        if(Q.acharNo(v))    //se v esta na fila
                        {
                            if(peso < custo[v]) // se o custo de v for menor que o anterior
                            {
                                anterior[v] = u;
                                custo[v] = peso;
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
        private No[] nos; 
        private int tamanho;
        public FilaPrioridadeMinima(int quantidade)
        {
            tamanho = 0;
            nos = new No[quantidade+1];
        }
        
        public boolean acharNo(int ID)
        {
            for(int i = 1 ; i <= tamanho ; i++)
            {
                if(nos[i].verticeID == ID)
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
            return nos[1];
        }
        public No ExtrairMinimo()
        {
            No menor = nos[1];
            nos[1] = nos[tamanho];  //substitui a raiz pelo ultimo elemento
            int i = 1, minimo = 0;
            tamanho--;
            
            while(true)
            {
                minimo = i;
                if(Esq(i) <= tamanho)   //se nao passar o tamanho do heap
                {
                    if(nos[Esq(i)].verticeID < nos[minimo].verticeID)
                    {
                        minimo = Esq(i);
                    }
                }     
                if(Dir(i) <= tamanho)
                {
                    if(nos[Dir(i)].verticeID < nos[minimo].verticeID)
                    {
                        minimo = Dir(i);
                    }
                }
                if(minimo != i)
                {
                    No aux = nos[minimo];
                    nos[minimo] = nos[i];   //troca o menor filho pelo pai
                    nos[i] = aux;   //troca o pai pelo filho da esquerda
                    i = minimo;
                }
                else
                {
                    break;
                }
            }
            return menor;
        }
        public void Inserir(No no)
        {
            tamanho++;
            nos[tamanho] = no;   //adiciona o no no fim do heap
            
            if(tamanho > 1)    //se nao for o no raiz
            {
                int i = tamanho;
                
                //aux.letra = nos[Pai(tamanho)].letra;
                //aux.quantidade = nos[Pai(tamanho)].quantidade;
                while(nos[Pai(i)].verticeID > nos[i].verticeID) 
                {
                    //nos[Pai(tamanho)].letra = nos[i].letra;
                    //nos[Pai(tamanho)].quantidade = nos[i].quantidade;
                    No aux = nos[Pai(i)];
                    nos[Pai(i)] = nos[i];
                    nos[i] = aux;
                    i = Pai(i);
                    if(i < 2)
                    {
                        break;
                    }
                }
            }
        }
        
       
        private int Pai(int i)
        {
            return Math.floorDiv(i, 2);
            //return i >> 1;
        }
        private int Esq(int i)
        {
            return i * 2;
            //return i << 1;
        }
        private int Dir(int i)
        {
            return i * 2 + 1;
            //return i << 1 | 1;
        }
    }

    private static class No
    {
        private int verticeID;  //numero do vertice
        
        public No(int ID)
        {
            this.verticeID = ID;
        }
    }
}