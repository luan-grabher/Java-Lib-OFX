package OFX;

import Auxiliar.LctoTemplate;
import Auxiliar.Valor;
import fileManager.FileManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OFX {

    private static String valorLinha = "";

    public static List<LctoTemplate> getListaLctos(File arquivo) {
        List<LctoTemplate> lctos = new ArrayList<>();
        try {
            //Ler arquivo
            String textoArquivo = FileManager.getText(arquivo.getAbsolutePath()).replaceAll("\r", "");
            String[] linhas = textoArquivo.split("\n");

            String data = "";
            String historico = "";
            String valor = "";

            //Percorre todas linhas para ir acumulando os valores
            for (String linha : linhas) {
                if (!linha.equals("")) {
                    //Adiciona lcto
                    if (linha.contains("</STMTTRN>") && !data.equals("") && !historico.equals("") && !valor.equals("")) {
                        lctos.add(new LctoTemplate(data, "", historico, new Valor(valor)));
                        data = "";
                        historico = "";
                        valor = "";

                        //Data
                    } else if (!getValorParametro("<DTPOSTED>", linha).equals("")) {
                        data = valorLinha.substring(6, 8) + "/" + valorLinha.substring(4, 6) + "/" + valorLinha.substring(0, 4);

                        //Valor
                    } else if (!getValorParametro("<TRNAMT>", linha).equals("")) {
                        valor = valorLinha;

                        //Historico
                    } else if (!getValorParametro("<MEMO>", linha).equals("")) {
                        historico = valorLinha;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lctos;
    }

    private static String getValorParametro(String nomeParametro, String linha) {
        if (linha.contains(nomeParametro)) {
            valorLinha = linha.replaceAll(nomeParametro, "").replaceAll(nomeParametro.replaceAll("<", "</"), "").trim();
        } else {
            valorLinha = "";
        }

        return valorLinha;
    }
}
