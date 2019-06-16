package br.com.iresult.gmfmobile.ui.main.impressao

import br.com.iresult.gmfmobile.model.bean.Leitura
import br.com.iresult.gmfmobile.model.database.AppDataBase
import br.com.iresult.gmfmobile.print.RW420PrintManager
import br.com.iresult.gmfmobile.print.RW420PrintManager.ARIAL_06
import br.com.iresult.gmfmobile.utils.Utils
import org.apache.commons.lang3.StringUtils


class Impressao{

    private val pm = RW420PrintManager()

    private var dataBase: AppDataBase? = null

    constructor(dataBase: AppDataBase){
        this.dataBase = dataBase
    }

    fun imprimir50(coletor: String): Boolean{
        if (pm.isPortOpen){
            val leituras: List<Leitura> = dataBase?.leituraDao()?.getLeituras50()?.blockingFirst()!!
            if (!leituras.isEmpty()){

                Thread(Runnable {
                      imprimir(leituras, coletor)

                }).start()
                return true
            }
        }

        return false
    }

    fun imprimirBackUp(coletor: String): Boolean{

        if (pm.isPortOpen){
            val leituras: List<Leitura> = dataBase?.leituraDao()?.getLeiturasBackup()?.blockingFirst()!!
            if (!leituras.isEmpty()){

                Thread(Runnable {
                    imprimir(leituras, coletor)

                }).start()
                return true
            }
        }

        return false
    }

    private fun imprimir(leituras: List<Leitura>, coletor: String){

        var dCA: Int
        var dCB: Int
        var dCE: Int
        var dCD: Int = 0
        var dCC: Int
        var dCF: Int

        pm.setPageConfig(340, 1)
        pm.setLabel()
        pm.setContrast(0)
        pm.setTone(0)
        pm.setSpeed(5)
        pm.setPageWidth(110)
        pm.setBarSense()

        var y = 100 // coordenada y (linha de impressão) //550
        var nCont = 0
       leituras.forEach{ leitura ->


          // var nLinhas = 0 // Quantidade de Ligações impressas



           if (nCont == 0) {

               y = 132

               // 10
               pm.printText(ARIAL_06,
                       RW420PrintManager.FONT_SIZE_0, 40, y,
                       "Coletor: " + coletor)

               y = y + 32

               // 10
               pm.printText(ARIAL_06,
                       RW420PrintManager.FONT_SIZE_0, 30, y,
                       "         CA")

               // 173
               pm.printText(ARIAL_06,
                       RW420PrintManager.FONT_SIZE_0, 193, y,
                       "       CB")

               // 313
               pm.printText(ARIAL_06,
                       RW420PrintManager.FONT_SIZE_0, 333, y,
                       "  CE")

               // 390
               pm.printText(ARIAL_06,
                       RW420PrintManager.FONT_SIZE_0, 410, y,
                       "  CD")

               // 470
               pm.printText(ARIAL_06,
                       RW420PrintManager.FONT_SIZE_0, 490, y,
                       "   CC")

               // 640
               pm.printText(ARIAL_06,
                       RW420PrintManager.FONT_SIZE_0, 660, y,
                       "   CF")


               y = y + 16 // pulando 1 linha

               pm.printText(ARIAL_06,
                       RW420PrintManager.FONT_SIZE_0, 30, y,
                       "-----------")

               pm.printText(ARIAL_06,
                       RW420PrintManager.FONT_SIZE_0, 193, y,
                       "---------")

               pm.printText(ARIAL_06,
                       RW420PrintManager.FONT_SIZE_0, 333, y,
                       "----")

               pm.printText(ARIAL_06,
                       RW420PrintManager.FONT_SIZE_0, 410, y,
                       "----")

               pm.printText(ARIAL_06,
                       RW420PrintManager.FONT_SIZE_0, 490, y,
                       "-----")

               pm.printText(ARIAL_06,
                       RW420PrintManager.FONT_SIZE_0, 570, y,
                       "-----")

               pm.printText(ARIAL_06,
                       RW420PrintManager.FONT_SIZE_0, 660, y,
                       "-----")

               pm.printText(ARIAL_06,
                       RW420PrintManager.FONT_SIZE_0, 760,
                       y, "--")

               y = y + 16 // pulando 1 linha

           }

           nCont++

           dCA = leitura.numeroLigacao.toInt() + 9875
           dCB = leitura.leituraAtual + 245

           var value = 0
           if (StringUtils.isNotBlank(leitura.descMotivoEntrega)){
               value = leitura.descMotivoEntrega?.substring(0,2 )?.toInt()!!
           }
           dCE = value * 7

           if (StringUtils.isNotBlank(leitura.tipoEntrega)){
               value = leitura.tipoEntrega?.toInt()!!
               dCD = value  + 18
           }
           dCC = leitura.codLeitura * 9
           dCF = leitura.numReg.toInt() + 199

           pm.printText(ARIAL_06, RW420PrintManager.FONT_SIZE_0,
                   30, y, Utils.Companion.setField(dCA, 11))

           pm.printText(ARIAL_06, RW420PrintManager.FONT_SIZE_0,
                   193, y, Utils.Companion.setField(dCB, 9))

           pm.printText(ARIAL_06, RW420PrintManager.FONT_SIZE_0,
                   333, y, Utils.Companion.setField(dCE, 4))

           pm.printText(ARIAL_06, RW420PrintManager.FONT_SIZE_0,
                   410, y, Utils.Companion.setField(dCD, 4))

           pm.printText(ARIAL_06, RW420PrintManager.FONT_SIZE_0,
                   490, y, Utils.Companion.setField(dCC, 5))

           pm.printText(ARIAL_06, RW420PrintManager.FONT_SIZE_0,
                   660, y, Utils.Companion.setField(dCF, 5))

           y = y + 32


       }

        pm.setPageFinalize()
        pm.print()

        pm.setPageConfig(340, 1)
        pm.setLabel()
        pm.setContrast(0)
        pm.setTone(0)
        pm.setSpeed(5)
        pm.setPageWidth(110)
        pm.setBarSense()
    }
}
