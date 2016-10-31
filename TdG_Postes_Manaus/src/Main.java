import java.util.ArrayList;
import  java.util.Scanner;
class Main
{
    private static Scanner entrada = new Scanner(System.in);
    public static void main(String[] args)
    {
        String dicionario, codigo;
        ArrayList<No> Nos = new ArrayList<>();
        dicionario = entrada.nextLine();
        char letra;
        int quantidade;
        //System.out.println(dicionario);
        dicionario = dicionario.replace("{", " ");   //remove abertura de chaves
        dicionario = dicionario.replace("}", "");   //remove fechamento de chaves
        //System.out.println(dicionario);
        String[] values = dicionario.split(",");    //divide o dicionario por virgulas
        for (String value : values) //para cada letra e sua quantidade
        {
            letra = value.charAt(2);
            quantidade = Integer.valueOf(value.substring(6));
            No no = new No(letra, quantidade);  //cria um novo no
            //System.out.println("<--" + letra + "," + quantidade + "-->");
            Nos.add(no);    //inserindo o no na lista de nos
        }
        //Nos.sort((No o1, No o2) -> (o1.quantidade > o2.quantidade)? 1 : 0); //ordenando as letras de modo decrescente
        FilaPrioridadeMinima fila = new FilaPrioridadeMinima(Nos.size());   //criacao da fila de prioridade 
        for(No no : Nos)
        {
            //System.out.println("inseriu o " + no.letra);
            fila.Inserir(no);   //inserindo o no
            //System.out.println("menor:"+fila.getMinimo());
        }
        while(fila.getQuantidade() > 1) //enquanto a fila nao tiver so 1 no
        {
            No no1 = fila.ExtrairMinimo();  //retira o primeiro menor
            No no2 = fila.ExtrairMinimo();  //retira o segundo menor
            No newNo = new No('*', no1.quantidade+no2.quantidade);  //cria um novo no com a soma dos 2 menores
            newNo.esq = no1;    //adiciona o primeiro menor como filho a equerda
            newNo.dir = no2;    //adiciona o segundo menor como filho a direita
            //System.out.println("menor 1:" + no1.toString());
            //System.out.println("menor 2:" + no2.toString());
            fila.Inserir(newNo);    //insere o novo no na fila
            //System.out.println("inseriu" + newNo.toString());
        }
        //return;
        codigo = entrada.nextLine();
        //System.out.println("codigo: " + codigo);
        int i = 0;
        No raiz, buscador;
        raiz = fila.ExtrairMinimo();    //extrai o no raiz da arvore 
        buscador = raiz;
        /*
        System.out.println("{arvore:");
        System.out.println(buscador.toString());
        System.out.println(buscador.esq.toString());
        System.out.println(buscador.dir.toString());
        System.out.println(buscador.dir.esq.toString());
        System.out.println(buscador.dir.dir.toString());
        System.out.println("}");
        //*/
        while(i < codigo.length())
        {
            //System.out.println("codigo: " +codigo.charAt(i));
            if(codigo.charAt(i) == '1')
            {
                buscador = buscador.esq;
            }
            else
            {
                buscador = buscador.dir;
            }
            i++;
            if(buscador.letra != '*')   //se for uma letra
            {
                System.out.print(buscador.letra);
                buscador = raiz;
            }
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
                    if(nos[Esq(i)].quantidade < nos[minimo].quantidade)
                    {
                        minimo = Esq(i);
                    }
                }     
                if(Dir(i) <= tamanho)
                {
                    if(nos[Dir(i)].quantidade < nos[minimo].quantidade)
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
                while(nos[Pai(i)].quantidade > nos[i].quantidade) 
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
        private char letra;
        private int quantidade;
        private No esq, dir;
        
        @Override
        public String toString()
        {
            String ret = "[letra:"+letra + ", quantidade:" + quantidade + ", esq:" ;
            
            if(esq != null)
                ret += esq.toString();
            else
                ret += "null";
            
            ret+= ", dir:";
            if(dir != null)
                ret += dir.toString();
            else
                ret += "null";
            
            ret += "]";
            return ret;
        }
        public No getCopia()
        {
            No no = new No(letra, quantidade);
            
            if(this.dir != null) 
            {
                no.dir = this.esq.getCopia();
            }
            if(this.esq != null) 
            {
                no.esq = this.esq.getCopia();
            }
            return no;
        }
        public void inserirEsq(No noEsq)
        {
            this.esq = noEsq;
        }
        public void inserirDir(No noDir)
        {
            this.dir = noDir;
        }
        public No()
        {
            iniciarFilhos();
        }
        public No(char letra, int quantidade)
        {
            this.letra = letra;
            this.quantidade = quantidade;
            iniciarFilhos();
        }
        private void iniciarFilhos()
        {
            esq = null;
            dir = null;
        }
    }
}
 