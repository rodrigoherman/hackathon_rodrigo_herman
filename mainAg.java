import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Collections;

public class mainAg {
	
	static ArrayList<String> palavrasArq = new ArrayList<String>();
	static HashSet<String> proibidos = new HashSet<String>();
	static HashSet<String> listan = new HashSet<String>();
	static String anok = "";
	static int repet = 0;
	
	public static void main(String[] args) {
		
		String op = "0";
		
		ArrayList<String> palavras = new ArrayList<String>();
		
		try
		{
			
			FileReader arq = new FileReader("C:\\Users\\Rodrigo\\eclipse-workspace\\Anagramas_Hack\\src\\palavras.txt");
			BufferedReader lerArq = new BufferedReader(arq);
			
			String linha = lerArq.readLine();
			
			while (linha != null)
			{
				//System.out.println(linha);
				
				palavrasArq.add(linha);
				linha = lerArq.readLine();
			}
			

			while (op.intern() != "1")
			{
				System.out.println("Insira a expressão desejada:");
				System.out.println("");
			
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				op = in.readLine();
				
				op = op.toUpperCase().replaceAll("\\s","");
				
				if (op.intern() == "1") 
					break;
				
				// verificar se a entrada foi valida conforme as regras
				if (!validarEntrada(op))
					throw new Exception("Caracteres inválidos encontrados.");
				
			
				// reinicia variáveis
				proibidos = new HashSet<String>();
				listan = new HashSet<String>();
				anok = "";
				repet = 0;
				
				// copia as palavras lidas do arquivo
				palavras.addAll(palavrasArq);
				
				//descarta palavras que não são anagramas
				otimizarPalavras(palavras, op);
				
			
				System.out.println("Expressão: " + op);
				System.out.println("");
				System.out.println("----------------------------------------------");
				
				while (repet < 30000)
				{				
					processarAnagramas(palavras, op, "", 0);
				}
				
				
				//System.out.println(listan.toString());
				
				System.out.println("----------------------------------------------");
				System.out.println("");
				
			}
			
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		
		System.out.println("Até logo!");
		System.out.println("");		
	}
	
	public static void otimizarPalavras(ArrayList palavras, String exp)
	{
		exp = exp.trim();
		
		// lista todas as palavras lidas do arquivo
		for (Object anagrama : palavrasArq) {
			
			String an = ((String) anagrama).trim();
			
			if (an.length() <= exp.length() && !procurarAnagrama(an, exp)
					|| an.length() > exp.length())
			{
				// remove palavra da cópia
				palavras.remove(an);
			}
		}
		
	}
	
	public static boolean validarEntrada(String entrada)
	{
		String validos = "ABCDEFGHIJKLMNOPQRSTUVXZ";
		
		for (int i=0;i<entrada.length();i++)
		{
			if (validos.indexOf(entrada.substring(i, i+1))==-1)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean procurarAnagrama(String anagrama, String exp)
	{
		String achados = "";
		
		for (int i=0; i<anagrama.length();i++)
		{
			if (exp.contains(anagrama.substring(i, i+1)))
			{
				achados = achados + anagrama.substring(i, i+1);
				exp = exp.replace(anagrama.substring(i, i+1), "");
			}
		}	
		
		return isAnagrama(anagrama, achados);
	}

	
	public static boolean isAnagrama(String a, String b)
	{
		char[] c = a.toCharArray();
		char[] d = b.toCharArray();
		
		Arrays.sort(c);
		Arrays.sort(d);
		
		if (Arrays.equals(c, d))
			return true;
		else
			return false;	
	}
	
	public static String newAnagrama(String orig, String exp)
	{
		char[] e = exp.toCharArray();
		
		
		for (int i=0; i<exp.length(); i++)
		{
			orig = orig.replaceFirst(Character.toString(e[i]),"");			
		}

		return orig; 
	}
	
	public static String processarAnagramas(ArrayList palavras, String exp, String ultimo, int nivel)
	{
		
		exp = exp.trim();
		
		String novoAnagrama = exp;
		
		for (Object anagrama : palavras) {
			
			String an = ((String) anagrama).trim();
			
							
			if (an.length() <= exp.length() &&
					procurarAnagrama(an, exp) &&
					!proibidos.contains(anok + an + " "))
			{
				// forma o novo anagrama com as letras restantes
				novoAnagrama = newAnagrama(exp, an);
				
				// anota o anagrama encontrado
				anok = anok + an + " ";
				
				//System.out.print(an + " ");
				
				novoAnagrama = processarAnagramas(palavras, novoAnagrama, an, nivel+1);
				
				break;
			}
		}
		
		// se estiver no primeiro nível, irá fazer o teste se foi bem sucedido
		if (nivel == 0)
		{

			// se não foi bem sucessedido, anota que esta combinação não é anagrama
			if (novoAnagrama.length()>0) {
				proibidos.add(anok);
				
				// limpa anagrama da vez
				anok = "";
				
				// número de repetições sem achar nada
				repet++;
			}
			// se for bem sucedido, anota que esta combinação é anagrama e para não repeti-la.
			else {
				
				if (!listan.contains(anok))
				{
					System.out.println(anok);
					listan.add(anok);
					proibidos.add(anok);
				}
				
				// limpa anagrama da vez
				anok = "";
			}
			
			//System.out.println("");
		}	
		
		return novoAnagrama;		
	}
}