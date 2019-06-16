package br.com.iresult.gmfmobile.ui.main.tarefas.itinerary.reading;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;

import br.com.iresult.gmfmobile.BuildConfig;
import br.com.iresult.gmfmobile.model.Visita;
import br.com.iresult.gmfmobile.model.bean.Faixa;
import br.com.iresult.gmfmobile.model.bean.Ligacao;
import br.com.iresult.gmfmobile.model.bean.Ocorrencia;
import br.com.iresult.gmfmobile.model.database.AppDataBase;
import br.com.iresult.gmfmobile.print.RW420PrintManager;
import br.com.iresult.gmfmobile.utils.NumberUtils;
import br.com.iresult.gmfmobile.utils.Utils;

class ImpressaoConta {
    private RW420PrintManager pm = new RW420PrintManager();

    private static final int TAM_MAX_MSG = 70;

    private static final int TAM_MAX_ENDERECO = 55;

    private Ligacao ligacao = null;

    private String[] mensagem = new String[4];

    // Barcode
    private String[] barcode = new String[4];
    private String[] digitoBarcode = new String[4];

    // Variáveis utilizadas para impressão do gráfico de consumos
    private String[] consumos;

    private String totalPagar;



    boolean imprimirConta(Ligacao ligacao, Visita visita, AppDataBase dataBase) throws Exception {

        this.ligacao = ligacao;
        boolean staImpresso = true;

        DecimalFormat df = new DecimalFormat("###,##0.00");

        totalPagar =  df.format(visita.getValorTotal());
        if (pm.isPortOpen()){
            int[] linha = {0, 140, 230, 300, 410, 450, 560, 710, 790, 1049, 1314};
            int[] slip = {1539, 1606, 1659, 1679};


            pm.setPageConfig(340, 1);
            pm.setTone(-15);
            pm.setSpeed(0);

            pm.setPageWidth(110);

            /* Estes dois métodos fazem funcionar o black mark */
            pm.setLabel();
            pm.setBarSense();

            String condicaoLeitura = "";
            Ocorrencia ocorrencia = null;


            String categoria = "";
            String mensagemIsento = "";
            StringBuilder msgMininoNLancado = new StringBuilder();
            int corte = 0;

            // Configura algumas variáveis de impressão
            this.setImpressao(visita); // TODO <<<<<
            if (visita.getQuantidadeEmissao() > 0){
                    pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 730,
                            linha[0] + 50, "2a.Imp.");
            }
            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 730,
                    linha[0] + 15, BuildConfig.VERSION_NAME);

            /* ### 1º Linha ###**/

            // ----> Ligação
            String ligacaoDigito =  Objects.requireNonNull(ligacao.getLigacaoDigito()).trim();
            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 40,
                    linha[1], ligacaoDigito);

            // ----> Referência
            String mesMedicao =   StringUtils.leftPad(String.valueOf(Objects.requireNonNull(visita.getTarefa()).getMesMedicao()), 2, "0");
            String anoReferencia =  String.valueOf(visita.getTarefa().getAnoMedicao());
            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 240, linha[1],
                    (StringUtils.leftPad(mesMedicao, 2,   "0") + "/" +
                     StringUtils.leftPad(anoReferencia, 2,   "0")));

            // ----> Vencimento
            String dataVencimento =  ligacao.getDataVencimento();

            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 410,
                    linha[1], dataVencimento);


            // ----> Total a pagar
            pm.printText(RW420PrintManager.ARIAL_09, RW420PrintManager.FONT_SIZE_0, 600,
                    linha[1], StringUtils.leftPad(this.totalPagar, 12, ' '));

            /* ### 2º Linha ###**/
            // ----> Nome
            String nomeCliente =  ligacao.getNomeCliente();
            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 40, linha[2],
                    nomeCliente);

            /* ### 3º Linha ###**/

            // ----> Endereço
            String endereco =  ligacao.getEnderecoLigacao();
            int tamanhoEndereco = 54; // ligacao.getEnderecoLigacao().length()
            // pula uma linha de tamanho normal
            int pulaLinha = 16;

            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 40,
                    linha[3], endereco);


            /* ### 4º Linha ###* */
            // ----> RECEITA
            String receita = visita.getReceita();
            if (receita == null){
                receita = "2 Agua e esgoto";
            }
            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 150,
                    linha[4], receita);

            // ----> Economias
            int economias = 1; //ligacao.getTotalEconomias() ;
            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 550,
                    linha[4], String.valueOf(economias));


            /**### 5º Linha ###**/
            // ----> Nº Hidrômetro
            String numeroMedidor =   ligacao.getNumeroMedidor();
            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 150,
                    linha[5], numeroMedidor);

            // ----> Categoria
            if (StringUtils.isNoneBlank(ligacao.getCodigoCategoria())){
                if (ligacao.getCodigoCategoria().substring(0, 1).equals("R")) {
                    categoria = "RESIDENCIAL";
                } else if (ligacao.getCodigoCategoria().substring(0, 1).equals("C")) {
                    categoria = "COMERCIAL";
                } else if (ligacao.getCodigoCategoria().substring(0, 1).equals("I")) {
                    categoria = "INDUSTRIAL";
                } else if (ligacao.getCodigoCategoria().substring(0, 1).equals("P")) {
                    categoria = "PUBLICO";
                } else {
                    categoria = "OUTROS";
                }
            } else {
                categoria = "OUTROS";
            }

            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 550,
                    linha[5], categoria);


            /**### 6º Linha ###**/
            // ----> Faixa de consumos

            for (int i = 0; i < 3; i++) {
                int meio = (int)consumos[i].length()/2;
                String colunaEsq = consumos[i].substring(0, meio);
                String colunaDir = consumos[i].substring(meio);

                pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 40, linha[6],	colunaEsq);
                pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 415, linha[6], colunaDir);
                linha[6] += pulaLinha;
            }



            /**### 7º Linha ###**/
            // ----> Data da Leitura
            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 30,
                    linha[7], Utils.Companion.getDataDDMMYYYY(visita.getDataLeitura()) );

            // ----> Condição Leitura Faturamento
            if (visita.getCodigoLeituraInterno() == visita.getCodigoLeitura()) {
                condicaoLeitura = ocorrencia !=null ? ocorrencia.getMensagemLeitura() : "";
            } else if (visita.getCriterioFaturamento() == 10) {
                condicaoLeitura = "Normal";
            } else if (visita.getCriterioFaturamento() != 10) {
                condicaoLeitura = "Pelo sistema";
            }

            condicaoLeitura = Utils.Companion.setField(condicaoLeitura, 50,1);

            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 175,
                    linha[7], condicaoLeitura.substring(0, 11));


            // ----> Leitura Atual
            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 295, linha[7],
                    Utils.Companion.setField(String.valueOf(visita.getLeituraAtual()), 7, 0));


            // ----> Consumo Medido/m³
            if (ligacao.getCodigoFaturamento() == 3) {
                pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 375, linha[7],
                        Utils.Companion.setField(String.valueOf(visita.getConsumoMedidoEsgoto()), 7, 0));
            } else {
                pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 375, linha[7],
                        Utils.Companion.setField(String.valueOf(visita.getConsumoMedido()), 7, 0));
            }

            // ----> Consumo Faturado/m³
            if (ligacao.getCodigoFaturamento() == 3) {
                pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 470, linha[7],
                         Utils.Companion.setField(String.valueOf(visita.getConsumoFaturadoEsgoto()), 7, 0));
            } else {
                pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 470, linha[7],
                        Utils.Companion.setField(String.valueOf(visita.getConsumoFaturadoAgua()), 7, 0));
            }

            // ----> Dias de Consumo
            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 590,
                    linha[7], String.valueOf(visita.getQuantidadeDiasConsumo()));



            // ----> Próxima Leitura
            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 680, linha[7],
                    ligacao.getDataProximaLeitura().substring(7,8) + "/" +
                            ligacao.getDataProximaLeitura().substring(5,6) + "/" +
                            ligacao.getDataProximaLeitura().substring(1,4) );



            /**### 8º Linha ###**/

            if (visita.getCascata() != null && visita.getCascata().getFaixa().length > 0) {
                for (int i = 0; i <= 11; i++) {
                    if (visita.getCascata().getFaixa()[i] != null && StringUtils.isNotBlank(visita.getCascata().getFaixa()[i])) {
                        String casc = visita.getCascata().getFaixa()[i].substring(35, 53);
                        if (StringUtils.isNotBlank(casc)){
                            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 30,
                                    linha[8], visita.getCascata().getFaixa()[i].substring(35, 53));
                            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 650,
                                    linha[8], visita.getCascata().getFaixa()[i].substring(53, 70));

                            linha[8] += pulaLinha;
                        }
                    }
                }
            }


            /**### 9º Linha ###**/
           for (int i = 0; i < visita.getFaixas().size(); i++) {

                Faixa faixa = visita.getFaixas().get(i);

                pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 53,
                        linha[9], faixa.getDesc());
                pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 239,
                        linha[9], NumberUtils.Companion.toString(faixa.getTarifaAgua(), 4));
                pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 363,
                        linha[9], NumberUtils.Companion.toString(faixa.getTarifaEsgoto(), 4));
                pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 485,
                        linha[9], NumberUtils.Companion.toString(faixa.getTarifaTratamento(), 4));
                pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 603,
                         linha[9], StringUtils.rightPad(NumberUtils.Companion.toString(faixa.getTotal(), 4), 7, ' '));


                if (i == visita.getQuantidadeFaixas() -1 ){//Última faixa consumo
                    int marca = (linha[9]-2);
                    //MARCAR LINHA

                    pm.appendNewLine("IL 42 "+marca+" 698 "+marca+" 23");
                }

                linha[9] += 24;
            }



            pm.appendNewLine("BOX 40 996 700 1288 1");
            pm.appendNewLine("T 5 0 518 1001 " + categoria);
            pm.appendNewLine("T Ari06Bpt.cpf 0 614 1025 TOTAL");
            pm.appendNewLine("T Ari06Bpt.cpf 0 44 1000 CALCULO DO SEU CONSUMO");
            pm.appendNewLine("T Ari06Bpt.cpf 0 250 1025 AGUA");
            pm.appendNewLine("T Ari06Bpt.cpf 0 362 1025 ESGOTO");
            pm.appendNewLine("T Ari06Bpt.cpf 0 464 1025 TRATAMENTO");
            pm.appendNewLine("BOX 40 1118 700 1144 1");
            pm.appendNewLine("LINE 335 622 337 624 1");
            pm.appendNewLine("LINE 88 272 90 274 1");
            pm.appendNewLine("T Ari06Bpt.cpf 0 88 1024 FAIXA");
            pm.appendNewLine("LINE 207 1023 207 1289 1");
            pm.appendNewLine("LINE 330 1023 330 1287 1");
            pm.appendNewLine("LINE 576 1023 576 1289 1");
            pm.appendNewLine("LINE 453 1023 453 1289 1");
            pm.appendNewLine("BOX 40 1022 700 1047 1");
            pm.appendNewLine("BOX 40 1046 700 1071 1");
            pm.appendNewLine("IL 41 997 698 997 48");
            pm.appendNewLine("BOX 40 1263 700 1288 1");
            pm.appendNewLine("BOX 40 1094 700 1119 1");
            pm.appendNewLine("BOX 40 1143 700 1168 1");
            pm.appendNewLine("BOX 40 1167 700 1192 1");
            pm.appendNewLine("BOX 40 1191 700 1216 1");
            pm.appendNewLine("BOX 40 1215 700 1240 1");
            pm.appendNewLine("BOX 40 1239 700 1264 1");


            /**### 10º Linha ###**/


            String msg =  Utils.Companion.setField(visita.getTarefa().getMensagem(), 208, 1);

            this.mensagem[0] = msg.substring(0, 63).trim();
            this.mensagem[1] = msg.substring(63, 130).trim();
            this.mensagem[2] = msg.substring(133, 196).trim();
            // ----> Mensagens
            if ((ligacao.getStatusOcorrencia().equals("S"))
                    && (ocorrencia.getStatusAceita().equals("N"))
                    && (ligacao.getMensagemDebito().equals(""))
                    && ligacao.getStatusImpedimento().equals("S")) {
                this.mensagem[3] = ligacao.getMensagemImpedimento();
            } else {
                this.mensagem[3] = ligacao.getMensagemDebito();
            }
            linha[10] += pulaLinha;


            if (ligacao.getMensagemLeiFederal() == null || ligacao.getMensagemLeiFederal().trim().equals("")) {
                for (int x = 0; x < this.mensagem.length; x++) {


                    if (this.mensagem[x].length() < TAM_MAX_MSG) {

                        pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 45,
                                linha[10], this.mensagem[x]);

                    } else {

                        corte = getNextSpaceIndex(this.mensagem[x],
                                TAM_MAX_MSG);

                        pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 45,
                                linha[10], this.mensagem[x].substring(0, corte));

                        linha[10] += pulaLinha;

                        pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 45,
                                linha[10], this.mensagem[x].substring(corte));

                    }

                    linha[10] += pulaLinha;

                }
            } else { //Impressão da Mensagem da Declaração de Quitação (Lei Federal)

                Vector mensagem = stringToList(ligacao.getMensagemLeiFederal(), "|"); //||

                for (int i = 0; i < mensagem.size(); i++) {

                    pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 45,
                            linha[10], (String) mensagem.get(i) );
                    linha[10] += pulaLinha;

                }

            }



            /**
             * **************** Slip da Conta ****************
             */

            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0,
                    40, (slip[0]-68), BuildConfig.VERSION_NAME);

            /**### 0º Linha ###**/
            // ----> Ligação
            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 40,
                    slip[0], ligacao.getLigacaoDigito().trim());


            // ----> Referência
            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0, 240, slip[0],
                    mesMedicao + "/" + anoReferencia);

            // Vencimento
            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0,
                    410, slip[0], dataVencimento);


            // ----> Valor do documento
            pm.printText(RW420PrintManager.ARIAL_09, RW420PrintManager.FONT_SIZE_0,
                    600, slip[0], StringUtils.leftPad(this.totalPagar, 12, ' '));


            /**### 1º Linha ###**/
            // ----> Data do Documento
            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0,
                    300, slip[1], new SimpleDateFormat("dd/MM/yyyy").format(visita.getDataLeitura()));


            /**### 2 e 3º Linha ###**/
            // * Espaço para impressão do código de barras
            if ((visita.getValorTotal() > 0)
                    && !(ligacao.getTipoLancamento().equals("A") && !(ligacao.getTipoLancamento()
                    .equals("E")))) {

                pm.printText(RW420PrintManager.FONT_TYPE_MONACO, RW420PrintManager.FONT_SIZE_0, 70,
                        slip[2], this.barcode[0] + "-"
                                + this.digitoBarcode[0] + " "
                                + this.barcode[1] + "-"
                                + this.digitoBarcode[1] + " "
                                + this.barcode[2] + "-"
                                + this.digitoBarcode[2] + " "
                                + this.barcode[3] + "-"
                                + this.digitoBarcode[3] );

                // ----> Código de barras (1,2,100,10,1650)
                pm.printBarCode("I2OF5", 1, 2, 110, 50, slip[3], this.barcode[0]
                        + this.barcode[1] + this.barcode[2]
                        + this.barcode[3]);


            }




            if (visita.getValorTotal() == 0) {

                if (ligacao.getSituacaoLigacao().equals("C")) {
                    mensagemIsento = "I S E N T O - L I G A C A O  C O R T A D A";

                    //Conta zerada devido serviço maior que o valor da fatura.
                } else{
                    mensagemIsento = "                 ISENTO                   ";
                }

                //Conta com valor inferior ao mínimo, lançar o valor em próximo fatura.
                if (visita.getValorMinNaoLancado() > 0) {

                    msgMininoNLancado.append("\n");
                    msgMininoNLancado.append("PREZADO CLIENTE.");
                    msgMininoNLancado.append("\n");
                    msgMininoNLancado.append("   A PRESENTE  FATURA NAO ATINGIU O VALOR MINIMO,");
                    msgMininoNLancado.append("\n");
                    msgMininoNLancado.append("   O VALOR DE R$ ");
                    msgMininoNLancado.append(Utils.Companion.setField(NumberUtils.Companion.zeroPad(NumberUtils.Companion.toString(visita.getValorMinNaoLancado(), 4),  9), 9, 0) );
                    msgMininoNLancado.append(" SERA INCLUSO EM CONTAS FUTURAS.");

                    pm.printMultiLineText(pulaLinha, RW420PrintManager.ARIAL_06,
                            RW420PrintManager.FONT_SIZE_0, 40, slip[3],
                            msgMininoNLancado.toString());
                }

            } else if (ligacao.getTipoLancamento().equals("A")
                    || ligacao.getTipoLancamento().equals("E")) {
                mensagemIsento = "DEBITO AUTOMATICO - NAO RECEBER";
            }

            pm.printText(RW420PrintManager.ARIAL_06, RW420PrintManager.FONT_SIZE_0,
                    40, slip[2], mensagemIsento);




            ///////////////////////////


            pm.setPageFinalize();

            pm.print();

            pm.disconnect();

            return true;

        }  else {
            // TODO THROW SAIDAS
        }
        return staImpresso;
    }

    private static int getNextSpaceIndex(String text, int limit) {

        int index = 0;

        if (text != null && text.length() > limit) {

            index = text.indexOf(" ", limit+1);
            index = (index<0?limit:(index+1));
        }

        return index;

    }
    private static Vector stringToList(String sourceString, String separatorToken) {
        Vector destinationList = new Vector();

        if (sourceString != null) {
            int index = -1;

            while (true) {
                int oldIndex = index + 1;
                index = sourceString.indexOf(separatorToken, oldIndex);

                if (index != -1) {
                    destinationList.addElement(sourceString.substring(oldIndex,
                            index));
                } else {
                    destinationList.addElement(sourceString.substring(oldIndex,
                            sourceString.length()));
                    break;
                }
            }
        }

        return destinationList;
    }

    protected void montaBarCode(String valorConta, double valorTotal) throws Exception{

        int virgula;
        String inteiros;
        String centavos;
        String valorSomadoString;
        String valorTotalFatura;

        virgula = valorConta.indexOf(",");
        inteiros = valorConta.substring(0, virgula).replace(".", "");
        centavos = valorConta.substring((virgula + 1), valorConta.length());
        // isso forma uma String, não é soma!
        valorSomadoString = inteiros.replace(".", "") + centavos;

        //Formata o valor total da fatura com as 10 casas decimais a esquerda.
        valorTotalFatura = StringUtils.leftPad(valorSomadoString, 11,  "0");


        //Recupera o numero do código de barra.
        String codigoBarra = ligacao.getCodigoBarras();


        //Adiciona o valor da fatura no codigo de barra.
        if (codigoBarra !=null &&  codigoBarra.length() > 0){

            codigoBarra = codigoBarra.substring(0,4).concat(valorTotalFatura).concat(codigoBarra.substring(15,codigoBarra.length()));

            //Monta a linha de números do código de barra.
            this.formataCodDigCon(codigoBarra);

            //Verificação do código de barras
            double total = (Double.parseDouble(valorSomadoString) / 100);

            //Aqui sim é uma soma
            if ((Double.parseDouble(inteiros + centavos) / 100) != total) {
                Log.e("ImpressaoConta", "ERRO NO CODIGO DE BARRAS");
                return;
            }

            if (Integer.parseInt(valorSomadoString) != Integer.parseInt(codigoBarra.substring(5,15))) {
                Log.e("ImpressaoConta", "ERRO NO CODIGO DE BARRAS");
                return;
            }
        }

    }

    protected void formataCodDigCon(String sCod) {

        sCod = calculoModulo10QuartoDig(sCod);

        if (sCod.length() != 44)
            return;

        this.barcode[0] = sCod.substring(0, 11);
        this.barcode[1] = sCod.substring(11, 22);
        this.barcode[2] = sCod.substring(22, 33);
        this.barcode[3] = sCod.substring(33, 44);

        this.digitoBarcode[0] = String.valueOf(Utils.Companion.calcularDV(this.barcode[0])) ;

        this.digitoBarcode[1] = String.valueOf(Utils.Companion.calcularDV(this.barcode[1]));

        this.digitoBarcode[2] = String.valueOf(Utils.Companion.calcularDV(this.barcode[2]));

        this.digitoBarcode[3] = String.valueOf(Utils.Companion.calcularDV(this.barcode[3]));

    }

    private String calculoModulo10QuartoDig(String sCod) {
        String sb44 = sCod.substring(0, 3) + sCod.substring(4, 44);
        String sDv44 = String.valueOf(Utils.Companion.calcularDV(sb44));
        sCod = sCod.substring(0, 3) + sDv44 + sCod.substring(4, 44);
        return sCod;
    }

    /**
     * Configura os parâmetros utilizados para impressão
     *
     * @param visita
     */
    private void setImpressao(Visita visita) throws Exception {
        // TODO VER Receita
        /*
         * Busca os consumos da ligação para a montagem do gráfico
         */
        this.buscaConsumos();

      //  buscarUltFaixaFat();


        /*
         * Monta a linha do código de barras
         */
        montaBarCode(this.totalPagar, visita.getValorTotal());

    }

    private void buscarUltFaixaFat(){
/*
        if (visita.quantidadeFaixas == 0) {

            Faixa faixa = new Faixa();
            faixa.codigoCategoria = ligacao.codigoCategoria.trim();
            faixa.codigoFaturamento = ligacao.codigoFaturamento;
            faixa.tipoLigacao = ligacao.tipoLigacao;
            double faturamento = visita.consumoFaturadoAgua;

            visita.quantidadeFaixas = FaixaDAO.getInstance().qtdFaixa(faixa,
                    faturamento);

        }*/
    }
    /**
     * Carrega as informações necessárias a montagem do gráfico de consumo
     */
    private void buscaConsumos() throws Exception {

        this.consumos = new String[3];

        this.consumos[0] = "";
        this.consumos[1] = "";
        this.consumos[2] = "";

        String[] seisMeses = Utils.Companion.splitInParts(ligacao.getHistoricoConsumo(), 28);

        for (int k = 0; k < seisMeses.length; k++) {
            String mes = seisMeses[k];

            String ref = mes.substring(0, 6);
            String lei = mes.substring(6, 13);
            String con = mes.substring(13, 20);
            String dat = mes.length() > 20 ? mes.substring(20, 28) : "";


            ref = ref.trim().length() > 0 ? ref.substring(0, 2) + "/" + ref.substring(2, 6) : StringUtils.leftPad("",  7, " ");
            lei = lei.trim().length() > 0 ? StringUtils.leftPad((new Integer(lei)).toString(), 7, ' ') : StringUtils.leftPad("",  7, " ");
            con = con.trim().length() > 0 ? StringUtils.leftPad((new Integer(con)).toString(), 7, ' ') : StringUtils.leftPad("",  7, " ");
            dat = dat.trim().length() > 0 ? dat  : StringUtils.leftPad("",  10, " ");

            if (StringUtils.isNoneBlank(dat)){
                SimpleDateFormat sdf =  new SimpleDateFormat("ddMMyyyy");
                Date data = sdf.parse(dat);
                sdf.applyPattern("dd/MM/yyyy");
                dat = sdf.format(data);
            }
            mes = ref + StringUtils.leftPad("",  1, " ") +
                        lei +
                        StringUtils.leftPad("",  1, " ") +
                        con + StringUtils.leftPad("",  2, " ") +
                        dat;


            if(k == 0 || k == 1)
                consumos[0] += consumos[0].length() > 0 ? StringUtils.leftPad("",  3, " ") + mes : mes;

            if(k == 2 || k == 3)
                consumos[1] += consumos[1].length() > 0 ? StringUtils.leftPad("",  3, " ") + mes : mes;

            if(k == 4 || k == 5)
                consumos[2] += consumos[2].length() > 0 ? StringUtils.leftPad("",  3, " ") + mes : mes;

        }
        if (consumos[0].length() > 0 && consumos[0].length() <= 33){
            consumos[0] = consumos[0] + StringUtils.rightPad("", 33, " ");
        }
        if (consumos[1].length() > 0 && consumos[1].length() <= 33){
            consumos[1] = consumos[1] + StringUtils.rightPad("", 33, " ");
        }
        if (consumos[2].length() > 0 && consumos[2].length() <= 33){
            consumos[2] = consumos[2] + StringUtils.rightPad("", 33, " ");
        }
    }
 

}
